package com.xdfs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xdfs.common.util.FileUtil;
import com.xdfs.common.util.RpcUtil;
import com.xdfs.meta.Chunk;
import com.xdfs.meta.DefaultParam;
import com.xdfs.meta.DataNode;
import com.xdfs.service.ChunkServer;
import com.xdfs.utils.Result;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.xdfs.common.util.Debug.debug;

public class ChunkServerImpl implements ChunkServer {

    private static Logger logger = Logger.getLogger(ChunkServerImpl.class);
    private DataNode      dataNode;

    public ChunkServerImpl(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    public Result<Boolean> writeChunk(String fileName, Long chunkId, byte[] bytes,
                                      List<String> urlList) throws IOException {
        if (dataNode.getFreeSpace() <= DefaultParam.DEFAULT_CHUNKSIZE) {
            return new Result(false, " freeSpace  is too less");
        }

        String path = localFileName(fileName, chunkId);
        FileUtil.write(path, bytes);

        List<Chunk> chunkList = dataNode.getChunkList();
        if (chunkList == null) {
            logger.error("chunkList is null");
        }
        chunkList.add(new Chunk(chunkId, fileName, path));


        // write to next datnode
        if(urlList==null){
            return new Result(Boolean.TRUE);
        }
        int i = 0;
        for (String url : urlList) {
            ++i;
            if (dataNode.getUrl().equals(url)) break;
        }
        final String nexturl = urlList.get(i - 1);
        if (i < urlList.size()) {
            new Thread(() -> {
                ChunkServer chunkServer = RpcUtil.getService(ChunkServer.class, nexturl);
                try {
                    chunkServer.writeChunk(fileName, chunkId, bytes, urlList);
                    debug("write "+fileName+" chunkId="+chunkId+" to datanode["+nexturl+"]");
                } catch (IOException e) {
                    logger.error("write to next datanode(url=" + nexturl + ") error urllist="
                                 + JSONObject.toJSONString(urlList), e);
                }
            }).start();
        }

        return new Result(Boolean.TRUE);

    }

    public Result<String> readChunk(String fileName, Long chunkId) throws IOException {
        String path = localFileName(fileName, chunkId);
        String content = FileUtil.read(path);
        return new Result(content);
    }

    @Override
    public Result<Boolean> delete(String fileName, Long chunkId) {

        String path=localFileName(fileName,chunkId);
        File file=new File(path);
        if(file.exists()){
            file.delete();
            debug("delete "+path);
            return new Result(Boolean.TRUE);
        }
        return new Result(false,"file is not exist");
    }

    public boolean heartBeat() {
        return true;
    }

    private String localFileName(String fileName, Long chunkId) {
        return DefaultParam.BASIC_PATH + chunkId + "/" + fileName;
    }

    public void setDataNode(DataNode dataNode) {
        this.dataNode = dataNode;
    }

    public static void main(String[] args) throws IOException {
        ChunkServerImpl chunkServer = new ChunkServerImpl(new DataNode(123l));
        Result<Boolean> result = chunkServer.writeChunk("xyytest.txt", 0l, "123".getBytes(),null);
        System.out.println(result.isSuccess());
        Result<String> content = chunkServer.readChunk("xyytest.txt", 0l);
        System.out.println(content.isSuccess() + "  " + content.getValue());

    }
}
