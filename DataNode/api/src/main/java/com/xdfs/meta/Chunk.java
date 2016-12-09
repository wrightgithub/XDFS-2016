package com.xdfs.meta;

import java.io.Serializable;

/**
 * Created by xyy on 16-12-5.
 */
public class Chunk  implements Serializable{

    private String  fileName;
    private Long    chunkId;
    private String  path;
    private boolean available = true;

    public Chunk(Long chunkId, String fileName, String path) {
        this.chunkId = chunkId;
        this.fileName = fileName;
        this.path = path;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getChunkId() {
        return chunkId;
    }

    public void setChunkId(Long chunkId) {
        this.chunkId = chunkId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
