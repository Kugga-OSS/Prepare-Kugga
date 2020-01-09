package com.ayang818.kugga.prepare.netty.hello;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @author 杨丰畅
 * @description 自定义请求类，对于请求来说就，相当于入站InBound的一个操作
 * @date 2020/1/9 21:56
 **/
public class CustomizeHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, HttpObject httpObject) throws Exception {
        Channel channel = context.channel();
        System.out.println(channel.remoteAddress());

        // 定义发送的消息到缓冲区
        ByteBuf content = Unpooled.copiedBuffer("Hello Netty", CharsetUtil.UTF_8);

        // 构建一个http response
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(content.readableBytes()));

        context.writeAndFlush(response);
    }
}
