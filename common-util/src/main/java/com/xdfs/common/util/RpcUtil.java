package com.xdfs.common.util;

import com.alibaba.dubbo.config.*;

/**
 * Created by xyy on 16-12-4.
 */
public class RpcUtil {

    public static <T> T getService(Class<T> cls) {
        return getService(cls, null);
    }

    public static<T> T getService(Class<T> cls, String url) {

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("yyy");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183");
        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

        // 引用远程服务
        ReferenceConfig<T> reference = new ReferenceConfig<T>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(application);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(cls);
        reference.setTimeout(1000);
        // 如果点对点直连，可以用reference.setUrl()指定目标地址，设置url后将绕过注册中心
        if (url != null) {

            reference.setUrl(url);
        }
        // reference.setVersion("1.0.0");
        return reference.get();
    }

    public static<T> void providService(Class<T> cls, T serviceImpl,int port) {

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("xxx");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183");
        registry.setUsername("aaa");
        registry.setPassword("bbb");
        registry.setRegister(false);// 目前不去注册，直连

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(port);
        protocol.setThreads(5);

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

        // 服务提供者暴露服务配置
        ServiceConfig<T> service = new ServiceConfig<T>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        service.setApplication(application);
        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
        service.setProtocol(protocol); // 多个协议可以用setProtocols()
        service.setInterface(cls);
        service.setRef(serviceImpl);
        // service.setVersion("1.0.0");

        // 暴露及注册服务
        service.export();
    }
}
