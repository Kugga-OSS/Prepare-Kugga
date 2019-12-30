package com.ayang818.kugga.prepare.simple.aio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2019/12/20 1:03
 **/
public class AioSimpleServer {
    public static void main(String[] args) {

    }
}

class AsyncServerHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncServerHandler.class);
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncServerHandler(Integer port) {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            LOGGER.info("async server start at port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        adAccept();
    }

    private void adAccept() {
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}