package com.xdfs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xdfs.common.util.RpcUtil;
import com.xdfs.common.util.Tuple;
import com.xdfs.meta.Chunk;
import com.xdfs.meta.DataNode;
import com.xdfs.meta.DefaultParam;
import com.xdfs.protocol.Constants;
import com.xdfs.protocol.Mode;
import com.xdfs.service.ChunkServer;
import com.xdfs.service.NameServer;
import com.xdfs.util.PropertiesUtil;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

import static com.xdfs.common.util.Debug.debug;

/**
 * Created by xyy on 16-12-4.
 */
public class NameServerImpl implements NameServer {

    private Mode mode;

    /**
     * 全局自增chunkId,需要持久化到
     */
    private Long chunkId = new Long(0);

    /**
     * fileName--->(chunkId1,chunkId2....)
     */
    private Map<String, List<Long>> fileMap;

    /**
     * 每个chunk 的每份拷贝所在的DataNode chunkId------->url1,url2,url3
     */
    private Map<Long, List<String>> chunksToDN;

    /**
     * 每个datanode的信息，包括每个dn 拥有的chunkIds
     */

    private List<DataNode> dataNodeList;

    public NameServerImpl(Long chunkId, Map<String, List<Long>> fileMap, List<DataNode> dataNodeList,
                          Map<Long, List<String>> chunksToDN, Mode mode) {
        this.fileMap = fileMap;
        this.dataNodeList = dataNodeList;
        this.chunksToDN = chunksToDN;
        this.mode = mode;
    }

    public Map<String, List<Long>> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, List<Long>> fileMap) {
        this.fileMap = fileMap;
    }

    public List<DataNode> getDataNodeList() {
        return dataNodeList;
    }

    public void setDataNodeList(List<DataNode> dataNodeList) {
        this.dataNodeList = dataNodeList;
    }

    public Long getChunkId() {
        return chunkId;
    }

    public void setChunkId(Long chunkId) {
        this.chunkId = chunkId;
    }

    public Map<Long, List<String>> getChunksToDN() {
        return chunksToDN;
    }

    public void setChunksToDN(Map<Long, List<String>> chunksToDN) {
        this.chunksToDN = chunksToDN;
    }

    @Override
    public List<Chunk> chunkReport(DataNode dataNode) {
        debug("receive chunkReport :" + JSONObject.toJSONString(dataNode));
        if (dataNode == null) {
            return null;
        }

        synchronized (dataNodeList) {
            for (DataNode node : dataNodeList) {
                if (node.getUrl().equals(dataNode.getUrl())) {
                    dataNodeList.remove(node);
                    break;
                }
            }
            dataNodeList.add(dataNode);

        }

        List<Chunk> retList = new ArrayList<>();
        synchronized (fileMap) {
            List<Chunk> chunklist = dataNode.getChunkList();
            if (!CollectionUtils.isEmpty(chunklist)) {
                for (Chunk chunk : chunklist) {
                    // TODO: 16-12-9 以及其他方法在safemode下不能使用，需要加代码
                    // // TODO: 16-12-9  must do it !!! safemode这样没有本质上解决问题，考虑datanode很晚加入。
                    // 此处需要考虑namenode和datanode，数据不一致的情况。 chunkIdList=null
                    // 同时还要考虑namenode重启构建filemap的情况。
                    List<Long> chunkIdList = fileMap.get(chunk.getFileName());

                    if (CollectionUtils.isEmpty(chunkIdList)) {
                        switch (mode) {
                            case SAFEMODE:
                                fileMap.put(chunk.getFileName(), new ArrayList<>(Arrays.asList(chunk.getChunkId())));
                                break;
                            case NORMAL:
                                // 返回告知datanode调用detele函数，去清理掉这些chunk
                                retList.add(chunk);
                                break;
                        }
                        continue;
                    }

                    if (!chunkIdList.contains(chunk.getChunkId())) {
                        chunkIdList.add(chunk.getChunkId());
                    }

                }

                if (!CollectionUtils.isEmpty(retList)) {
                    return retList;
                }
            }
        }

        synchronized (chunksToDN) {
            List<Chunk> chunklist = dataNode.getChunkList();
            if (CollectionUtils.isEmpty(chunklist)) {
                return null;
            }
            for (Chunk chunk : chunklist) {
                List<String> urlList = chunksToDN.get(chunk.getChunkId());
                if (CollectionUtils.isEmpty(urlList)) {
                    switch (mode) {
                        case SAFEMODE:
                            chunksToDN.put(chunk.getChunkId(), new ArrayList<>(Arrays.asList(dataNode.getUrl())));
                            break;
                        case NORMAL:
                            // 返回告知datanode调用detele函数，去清理掉这些chunk
                            retList.add(chunk);
                            break;
                    }
                    continue;
                }

                if (!urlList.contains(dataNode.getUrl())) {
                    urlList.add(dataNode.getUrl());
                }

            }

        }
        return retList;
    }

    /**
     * @param fileName
     * @param chunkNum
     * @return map<chunkId,urllist>
     * @throws IOException
     */
    @Override
    public Map<Long, List<String>> getWritableChunks(String fileName, int chunkNum) throws IOException {

        synchronized (fileMap) {
            if (fileMap.containsKey(fileName)) {
                throw new FileAlreadyExistsException(fileName);
            }
        }

        Map<Long, List<String>> retList = new HashMap<>();

        for (int i = 0; i < chunkNum; i++) {
            List<String> urlList = new ArrayList<>();
            int replNum = 0;
            for (DataNode dataNode : dataNodeList) {
                if (replNum >= Constants.REPLICATION_NUM) {
                    break;
                }
                if (!dataNode.isAvailable() || dataNode.getFreeSpace() < DefaultParam.DEFAULT_CHUNKSIZE) {
                    continue;
                }

                // 根据一定策略(如机架)分配datanode。这里直接分配
                urlList.add(dataNode.getUrl());

                // 分配chunkId
                synchronized (chunkId) {
                    chunkId++;
                    PropertiesUtil.updateProperties("chunkId", String.valueOf(chunkId));
                }

                retList.put(chunkId, urlList);
                replNum++;
            }
        }

        return retList;
    }

}
