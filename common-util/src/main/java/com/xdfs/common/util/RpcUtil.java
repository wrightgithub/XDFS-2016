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

        // ��ǰӦ������
        ApplicationConfig application = new ApplicationConfig();
        application.setName("yyy");

        // ����ע����������
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183");
        // ע�⣺ReferenceConfigΪ�ض����ڲ���װ����ע�����ĵ����ӣ��Լ�������ṩ��������

        // ����Զ�̷���
        ReferenceConfig<T> reference = new ReferenceConfig<T>(); // ��ʵ�����أ���װ����ע�����ĵ������Լ����ṩ�ߵ����ӣ������л��棬�����������ڴ������й©
        reference.setApplication(application);
        reference.setRegistry(registry); // ���ע�����Ŀ�����setRegistries()
        reference.setInterface(cls);
        // �����Ե�ֱ����������reference.setUrl()ָ��Ŀ���ַ������url���ƹ�ע������
        if (url != null) {

            reference.setUrl(url);
        }
        // reference.setVersion("1.0.0");
        return reference.get();
    }
}
