package com.xdfs.client.action;

import com.xdfs.common.util.RpcUtil;

import java.lang.reflect.Array;
import java.util.*;
import com.xdfs.meta.DefaultParam;
import com.xdfs.service.ChunkServer;
import org.springframework.util.CollectionUtils;
import java.io.IOException;

import static com.xdfs.client.util.CacheRpcServer.nameServer;
import static com.xdfs.common.util.Debug.debug;
import static com.xdfs.meta.DefaultParam.DEFAULT_CHUNKSIZE;

/**
 * Created by xyy on 16-12-8.
 */
public class FsOperator {

    // close(String filePath)

    /**
     * 一次写到多个datanode中去 ,只请求一次nameNode写到哪里，再多次写到datanode 这样便于namenode判断文件是否已经存在
     * 异常由用户捕获
     * @param fileName 不支持文件目录。 为了模拟方便
     * @param content
     */
    public static void write(String fileName, String content) throws IOException {
        if (fileName == null) {
            throw new IOException("filePath can not be null");
        }

        // TODO: 16-12-9 后期考虑文件目录的支持
//        String[] strs = fileName.split("/");
//        String fileName = strs[strs.length - 1];


        byte[] bytes=content.getBytes();
        int temp1 = bytes.length / DEFAULT_CHUNKSIZE;
        int temp2 = bytes.length % DEFAULT_CHUNKSIZE;
        int chunkNum = (temp2 == 0 ? 0 : 1) + temp1;
        Map<Long, List<String>> retMap = nameServer.getWritableChunks(fileName, chunkNum);

        if(CollectionUtils.isEmpty(retMap)){
            throw new IOException("now can not write ,because may no datanode alive or no space to write");
        }

        int copyNum=0;
        for (Map.Entry entry: retMap.entrySet()){
            Long chunkId= (Long) entry.getKey();
            List<String> urlList= (List<String>) entry.getValue();
            if(CollectionUtils.isEmpty(urlList)){
                throw new IOException("can not write ,please retry  because has one chunk can't get at least one datanode url.");
            }
            ChunkServer chunkServer=  RpcUtil.getService(ChunkServer.class,urlList.get(0));
            // 将每一份的文件chunk的bytes写入。
            byte[] chunkBytes=new byte[DEFAULT_CHUNKSIZE];
            int neelen=(copyNum>=temp1)?temp2:DEFAULT_CHUNKSIZE;
            System.arraycopy(bytes,copyNum*DEFAULT_CHUNKSIZE,chunkBytes,0,neelen);
            chunkServer.writeChunk(fileName,chunkId,chunkBytes,urlList);
            debug("copynum="+copyNum+" to datanode:"+urlList.get(0));
            copyNum++;
        }


    }

    /**
     * 需要把多个chunk合起来
     * 
     * @param filePath
     * @return
     */
    public String read(String filePath) {

        return null;
    }
}
