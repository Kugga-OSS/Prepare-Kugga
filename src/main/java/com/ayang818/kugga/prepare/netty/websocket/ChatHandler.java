package com.ayang818.kugga.prepare.netty.websocket;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 杨丰畅
 * @description 自定义的助手类，在WebSocket中Frame是消息的载体
 * @date 2020/1/10 20:39
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatHandler.class);

    // 用于记录和管理所有客户端的Channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame message) throws Exception {
        String content = message.text();
        String channelId = context.channel().id().asShortText();
        LOGGER.info("accept message: {}", content);
        for (Channel channel : clients) {
            channel.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now()+" 来自"+channelId+": "+content));
        }
    }

    /**
     * @param ctx
     * @desc 将每次连接的Channel添加到ChannelGroup中
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        //System.out.println("连接池中channelid为");
        //clients.stream().forEach(channel -> System.out.print(channel.id().asShortText() + ", \n"));
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        LOGGER.info("channel {} has been closed",ctx.channel().id().asShortText());
    }
}
