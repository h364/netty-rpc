package com.hh.netty.nettydemo.dubborpc.register;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.io.InputStream;

import java.util.List;
import java.util.Properties;

public class ZkFactory {
    private static final Properties config = new Properties();
    private static final ZkConnection connection;
    private static final ZkRegistry registry;

    static {
        try {
            InputStream input = ZkFactory.class.getClassLoader().getResourceAsStream("rpc.properties");
            config.load(input);

            String serverIp = config.getProperty("registry.ip") == null ? "localhost" : config.getProperty("registry.ip");
            int serverPort = config.getProperty("registry.port") == null ? 9090 : Integer.parseInt(config.getProperty("registry.port"));
            String zkServer = config.getProperty("zk.server") == null ? "localhost:2181" : config.getProperty("zk.server");
            int zkSessionTimeout = config.getProperty("zk.sessionTimeout") == null ? 10000 : Integer.parseInt(config.getProperty("zk.sessionTimeout"));

            connection = new ZkConnection(zkServer, zkSessionTimeout);
            registry = new ZkRegistry();
            registry.setIp(serverIp);
            registry.setPort(serverPort);
            registry.setConnection(connection);

            //初始化zk父节点
            List<String> children = connection.getConnection().getChildren("/", false);
            if (!children.contains("bjsxt")) {
                connection.getConnection().create("/bjsxt", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            List<String> bjsxtChildren = connection.getConnection().getChildren("/bjsxt", false);
            if (!bjsxtChildren.contains("rpc")) {
                connection.getConnection().create("/bjsxt/rpc", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void registerService(Class<?> serviceInterface) throws Exception {
        registry.registerService(serviceInterface);
    }

    public static boolean isExistsService(Class<?> serviceInterface) throws Exception {
        return registry.isExistsService(serviceInterface);
    }
}
