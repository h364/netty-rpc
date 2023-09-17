package com.hh.netty.nettydemo.dubborpc.provider;

import com.hh.netty.nettydemo.dubborpc.netty.NettyServer;

//生产者服务提供者
public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
