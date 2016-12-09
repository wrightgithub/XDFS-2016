package com.xdfs.daemon;

import com.xdfs.common.util.CommonUtil;
import com.xdfs.common.util.RpcUtil;
import com.xdfs.meta.DataNode;
import com.xdfs.protocol.Constants;
import com.xdfs.service.ChunkServer;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static com.xdfs.common.util.Debug.debug;

/**
 * Created by xyy on 16-12-6.
 */
public class HeartBeat {

    private static Logger logger = Logger.getLogger(HeartBeat.class);
    private static String name   = "heartbeat";

    public static void start(Properties prop, List<DataNode> dataNodeList) {

        // 延迟启动1s,定时发送心跳
        prop.forEach((k, v) -> {
            ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1, new MonitorFactory(name));
            scheduledThreadPool.scheduleAtFixedRate(() -> {
                try {
                    ChunkServer chunkServer = RpcUtil.getService(ChunkServer.class, (String) v);
                    boolean ret = chunkServer.heartBeat();
                    debug(v + " heartbeat is " + ret);
                    for (DataNode n : dataNodeList) {
                        if (v.equals(n.getUrl())) {
                            n.setAvailable(ret);
                            break;
                        }
                    }

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }, 1, Constants.HEARTBEAT_TIME, TimeUnit.SECONDS);
        });

    }

}
