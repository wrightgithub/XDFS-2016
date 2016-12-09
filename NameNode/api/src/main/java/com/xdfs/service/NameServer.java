package com.xdfs.service;

/**
 * Created by xyy on 16-12-4.
 */

import com.xdfs.meta.Chunk;
import com.xdfs.meta.DataNode;

import java.io.IOException;
import java.util.*;

/**
 * Created by xyy on 16-12-4.
 */
public interface NameServer {

    public List<Chunk> chunkReport(DataNode dataNode);

    public Map<Long, List<String>> getWritableChunks(String fileName, int chunkNum) throws IOException;


}
