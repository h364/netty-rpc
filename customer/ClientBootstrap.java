package com.hh.netty.nettydemo.dubborpc.customer;

import com.hh.netty.nettydemo.dubborpc.netty.NettyClient;
import com.hh.netty.nettydemo.dubborpc.publicinterface.HelloService;
import com.hh.netty.nettydemo.dubborpc.register.ZkFactory;

public class ClientBootstrap {

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        try {
            ZkFactory.isExistsService(HelloService.class);
        } catch (Exception e) {
            System.out.println("访问的服务不存在");
            e.printStackTrace();
        }
        HelloService service = (HelloService) nettyClient.getBean(HelloService.class);
        String result = service.hello("你好 dubbo");
        System.out.println("服务端返回的结果：" + result);
    }
}
