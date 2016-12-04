package com.xdfs.maven.dubbotest;

import com.xdfs.common.util.RpcUtil;
import com.xdfs.service.DemoServer;

/**
 * Created by xyy on 16-12-4.
 */
public class P2Ptest {

    public static void main(String[] args) {
        String url="dubbo://172.16.245.1:20881";
        DemoServer demoServer =  new RpcUtil<DemoServer>().getService(DemoServer.class,url);

        System.out.println(demoServer.sayHello("6666"));
    }
}
