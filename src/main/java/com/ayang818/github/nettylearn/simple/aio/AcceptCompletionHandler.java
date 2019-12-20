package com.ayang818.github.nettylearn.simple.aio;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;

/**
 * @author 杨丰畅
 * @description TODO
 * @date 2019/12/20 1:10
 **/
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServerHandler attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment, this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // result.read(byteBuffer, byteBuffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandler attachment) {

    }
}
