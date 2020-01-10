package com.ayang818.kugga.prepare.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2020/1/10 20:18
 **/
public class WSServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WSServer.class);

    public static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup slaveGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitailizer());

            ChannelFuture future = server.bind(PORT).sync();
            LOGGER.info("WebSocket server is started on http://localhost:{}", PORT);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }
    }
}
