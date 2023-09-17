package com.hh.netty.nettydemo.dubborpc.register;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class ZkRegistry {
    private ZkConnection connection;
    private String ip;
    private int port;

    public void registerService(Class<?> serviceInterface) throws Exception {
        String rmi = "rmi://" + ip + ":" + port + "/" + serviceInterface.getName();
        String path = "/bjsxt/rpc/" + serviceInterface.getName();

        //如果zookeeper已存在节点，则删除
        List<String> children = connection.getConnection().getChildren("/bjsxt/rpc", false);
        if(children.contains(serviceInterface.getName())) {
            Stat stat = new Stat();
            connection.getConnection().getData(path, false, stat);
            connection.getConnection().delete(path, stat.getCversion());
        }

        connection.getConnection().create(path, rmi.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public boolean isExistsService(Class<?> serviceInterface) throws Exception {
        String path = "/bjsxt/rpc/" + serviceInterface.getName();

        Stat stat = connection.getConnection().exists(path, false);
        return stat == null;
    }

    public ZkConnection getConnection() {
        return connection;
    }

    public void setConnection(ZkConnection connection) {
        this.connection = connection;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
