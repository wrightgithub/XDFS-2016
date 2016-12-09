/**
 * 
 */
package com.xdfs.main;

import java.io.*;
import java.util.*;

import com.xdfs.common.util.RpcUtil;
import com.xdfs.daemon.HeartBeat;
import com.xdfs.daemon.ModeMonitor;
import com.xdfs.meta.DataNode;
import com.xdfs.protocol.Mode;
import com.xdfs.service.NameServer;
import com.xdfs.service.impl.NameServerImpl;
import org.springframework.util.CollectionUtils;

public class BootstrapNameNode {

    public static void main(String[] args) throws IOException {

        // 全局安全模式
        Mode mode=Mode.SAFEMODE;

        // 读取datanode的IP,port
        Properties properties = loadProperties();
        // 全局对象，内存维护
        Long chunkId = Long.valueOf(properties.getProperty("chunkId"));
        List<DataNode> dataNodeList = new ArrayList<>();
        Map<String, List<Long>> fileMap = new HashMap<>();
        Map<Long, List<String>> chunksToDN = new HashMap<>();

        // 暴露rpc服务
        RpcUtil.providService(NameServer.class, new NameServerImpl(chunkId, fileMap, dataNodeList, chunksToDN,mode), 10880);

        // 启动心跳
        HeartBeat.start(properties, dataNodeList);

        // 启动监控线程
        new ModeMonitor(mode,System.currentTimeMillis()).start();
        System.out.println("按任意键退出");
        System.in.read();
    }

    private static Properties loadProperties() {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            String path = "/home/xyy/idea-pro/distributed/NameNode/biz/src/main/resources/node.properties";
            in = new BufferedInputStream(new FileInputStream(path));

            prop.load(in); /// 加载属性列表
            in.close();
            if (CollectionUtils.isEmpty(prop)) {
                System.err.println("node.properties is null !!!");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;
    }

}
