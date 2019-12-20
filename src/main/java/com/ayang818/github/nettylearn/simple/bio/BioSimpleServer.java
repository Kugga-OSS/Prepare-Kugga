package com.ayang818.github.nettylearn.simple.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2019/12/19 18:21
 **/
public class BioSimpleServer {
    public static void main(String[] args) throws IOException {
        new Thread(new ReactorTask(1080), "NIO-Server-001").start();
    }
}

class ReactorTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorTask.class);
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    public ReactorTask(Integer port) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }


    @Override
    public void run() {
        while (!stop) {
            try {
                // 休眠时间为1s
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                // 处理新接入的请求消息,创建ServerSocketChannel,注册到Selector
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                SocketChannel accept = channel.accept();
                channel.configureBlocking(false);
                serverChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                //  读取消息, 创建SocketChannel
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = channel.read(readBuffer);
                if (readBytes > 0) {
                    // 将缓冲区的limit设置为position, position设为0
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    LOGGER.info("server had received message : {}", body);
                    String responseMillis = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAS REQUEST";
                    doWrite(channel, responseMillis);
                } else if (readBytes < 0) {
                    key.cancel();
                    channel.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String responseMillis) throws IOException {
        if (responseMillis != null && responseMillis.trim().length() > 0) {
            byte[] bytes = responseMillis.getBytes();
            // 缓冲流中允许的大小
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}