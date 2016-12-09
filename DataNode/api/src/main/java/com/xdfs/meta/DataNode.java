package com.xdfs.meta;

import java.io.Serializable;
import java.util.*;

/**
 * Created by xyy on 16-12-5.
 */
public class DataNode implements Serializable {

    private String      ip        = DefaultParam.DEFAULT_IP;        //
    private long        port;                                       //
    private String      nodeName  = DefaultParam.APPNAME;
    private List<Chunk> chunkList = new ArrayList<Chunk>();
    private int        freeSpace = DefaultParam.DEFAULT_ALLSPACE;
    private int        allSpace  = DefaultParam.DEFAULT_ALLSPACE;
    private int        chunkSize = DefaultParam.DEFAULT_CHUNKSIZE;
    private boolean     available = true;

    public DataNode(long port) {
        this.port = port;
    }

    public String getUrl() {
        return ip + ":" + port;
    }

    public synchronized long getAllSpace() {
        return allSpace;
    }

    public synchronized void setAllSpace(int allSpace) {
        this.allSpace = allSpace;
    }

    public synchronized long getChunkSize() {
        return chunkSize;
    }

    public synchronized void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public synchronized long getFreeSpace() {
        return freeSpace;
    }

    public synchronized void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public List<Chunk> getChunkList() {
        return chunkList;
    }

    public void setChunkList(List<Chunk> chunkList) {
        this.chunkList = chunkList;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
