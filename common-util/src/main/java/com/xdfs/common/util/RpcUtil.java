package com.xdfs.common.util;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * Created by xyy on 16-12-4.
 */
public class RpcUtil<T> {

    public T getService(Class<T> cls) {
        return getService(cls, null);
    }

    public T getService(Class<T> cls, String url) {

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
        // 如果点对点直连，可以用reference.setUrl()指定目标地址，设置url后将绕过注册中心
        if (url != null) {

            reference.setUrl(url);
        }
        // reference.setVersion("1.0.0");
        return reference.get();
    }
}
