package com.ayang818.kugga.prepare.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 杨丰畅
 * @description 实现客户端发送请求，服务器返回Hello Netty
 * @date 2020/1/9 21:33
 **/
public class HelloServer {
    public static final Logger LOGGER = LoggerFactory.getLogger(HelloServer.class);

    public static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup slaveGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new HelloServerInitializer());

            // 启动Server，设置端口号，设置启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            LOGGER.info("Server is start at http://localhost:{}", PORT);
            // 监听关闭的Channel，设置为同步
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }
    }
}
