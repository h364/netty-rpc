package com.hh.netty.nettydemo.dubborpc.netty;

import com.hh.netty.nettydemo.dubborpc.ioc.ClassPathXmlApplicationContext;
import com.hh.netty.nettydemo.dubborpc.protocol.MessageProtocol;
import com.hh.netty.nettydemo.dubborpc.publicinterface.HelloService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    public static ClassPathXmlApplicationContext context;

    static {
        try {
            context = new ClassPathXmlApplicationContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        byte[] content = messageProtocol.getContent();
        System.out.println("服务端收到消息：" + new String(content));
        HelloService service = (HelloService) context.getBean("helloServiceImpl");

        String result = service.hello(new String(content));
        System.out.println(result);
        MessageProtocol msg = new MessageProtocol();

        msg.setContent(result.getBytes(StandardCharsets.UTF_8));
        msg.setLen(result.getBytes(StandardCharsets.UTF_8).length);
        channelHandlerContext.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
