package com.xdfs.protocol;

/**
 * Created by xyy on 16-12-6.
 */
public interface Constants {

    long HEARTBEAT_TIME   = 1;// 1s
    long CHUNKREPORT_TIME = 5;// 5s
    long REPLICATION_NUM  = 3;
    long SAFEMODE_TIME    = 10;// 3s 用于刚启动时构建元数据。

}
