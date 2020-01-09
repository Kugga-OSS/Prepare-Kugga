package com.ayang818.kugga.prepare.netty.hello;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author 杨丰畅
 * @description Channel的初始化器
 * @date 2020/1/9 21:48
 **/
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();
        // 通过管道添加Handler工具类，提供HttpServer的编解码
        pipeline.addLast("HttpServerCode", new HttpServerCodec());
        // 返回"hello netty"
        pipeline.addLast("customizeHandler", new CustomizeHandler());

    }
}
