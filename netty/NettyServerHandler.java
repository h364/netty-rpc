package com.hh.netty.nettydemo.dubborpc.netty;

import com.hh.netty.nettydemo.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg：" + msg);
        HelloServiceImpl helloService = new HelloServiceImpl();
        String result = helloService.hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
        ctx.writeAndFlush(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
