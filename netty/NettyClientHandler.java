package com.hh.netty.nettydemo.dubborpc.netty;

import com.hh.netty.nettydemo.dubborpc.protocol.MessageProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> implements Callable {
    private ChannelHandlerContext context;
    private String result;
    private String params;

    @Override
    public synchronized Object call() throws Exception {
        byte[] content = params.getBytes(StandardCharsets.UTF_8);
        int length = params.getBytes(StandardCharsets.UTF_8).length;

        //创建协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(content);
        messageProtocol.setLen(length);

        context.writeAndFlush(messageProtocol);
        wait();
        return result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        byte[] content = messageProtocol.getContent();
        result = new String(content, StandardCharsets.UTF_8);
        System.out.println("客户端收到消息：" + result);
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    void setParams(String para) {
        this.params = para;
    }
}
