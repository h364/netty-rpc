package com.hh.netty.nettydemo.dubborpc.publicinterface;

//消费者与生产者共用接口
public interface HelloService {

    String hello(String msg);
}
