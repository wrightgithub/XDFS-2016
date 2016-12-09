/**
 * 
 */
package com.xdfs.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.xdfs.common.util.RpcUtil;
import com.xdfs.daemon.ChunkReport;
import com.xdfs.meta.Chunk;
import com.xdfs.meta.DataNode;
import com.xdfs.meta.DefaultParam;
import com.xdfs.service.ChunkServer;
import com.xdfs.service.impl.ChunkServerImpl;
import org.springframework.util.CollectionUtils;

public class BootstrapDataNode {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("please input port");
            System.exit(1);
        }

        // 暴露服务
        Long port = Long.valueOf(args[0]);
        DataNode dataNode = new DataNode(port);
        loadChunkList(dataNode);
        ChunkServer chunkServer = new ChunkServerImpl(dataNode);
        RpcUtil.providService(ChunkServer.class, chunkServer, Integer.valueOf(args[0]));

        // chunkreport
        ChunkReport.startChunkReport(dataNode);

        System.out.println("entry any key to quit:");
        System.in.read();
    }

    public static void loadChunkList(DataNode dataNode) {
        List<Chunk> chunkList = dataNode.getChunkList();
        File file = new File(DefaultParam.BASIC_PATH);
        File[] fileList = file.listFiles();
        for (File chunkFile : fileList) {
            if (chunkFile.isDirectory()) {
                File[] chunkFileList = chunkFile.listFiles();
                String filename = null;
                String path = null;
                if (chunkFileList!=null&&chunkFileList.length>0) {
                    filename = chunkFileList[0].getName();
                    path = chunkFileList[0].getPath();
                }
                chunkList.add(new Chunk(Long.valueOf(chunkFile.getName()), filename, path));
            }
        }

    }

    // 1. 加载nodeinfo,chunklist
    // 2. 启动后台的线程(心跳,blockreport)
    // 3. 发布rpc服务
}
