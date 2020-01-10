package com.ayang818.kugga.prepare.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2020/1/10 20:25
 **/
public class WSServerInitailizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        // 大数据流的读写支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对Http message进行聚合，聚合成FullHttpRequest或FullHttpResponse(常用), maxContentLength 单位byte 8B, 这里设置为8MB
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024, true));

        // ==============以上用于支持Http协议==================

        // 用于指定客户端用来访问的路由；处理其它繁重的工作比如握手，心跳检测等
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new ChatHandler());
    }
}
