package com.xdfs.service;

import com.xdfs.utils.Result;
import java.util.*;
import java.io.IOException;

public interface ChunkServer {

    public Result<Boolean> writeChunk(String fileName, Long chunkId, byte[] bytes,
                                      List<String> urlList) throws IOException;

    public Result<String> readChunk(String fileName, Long chunkId) throws IOException;

    public Result<Boolean> delete(String fileName, Long chunkId);

    public boolean heartBeat();

}
