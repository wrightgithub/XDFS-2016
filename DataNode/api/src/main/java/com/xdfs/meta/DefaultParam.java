package com.xdfs.meta;

/**
 * Created by xyy on 16-12-5.
 */
public interface DefaultParam {

    String DEFAULT_IP        = "127.0.0.1";
    String APPNAME           = "DATANODE";
    String BASIC_PATH        = "/home/xyy/idea-pro/distributed/fileSys/";
    int   DEFAULT_ALLSPACE  = 50 * 1024 * 1024;                        // 50M
    int   DEFAULT_CHUNKSIZE = 1 * 1024 * 1024;                         // 1M
}
