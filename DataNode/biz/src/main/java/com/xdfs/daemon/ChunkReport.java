package com.xdfs.daemon;

import com.alibaba.fastjson.JSONObject;
import com.xdfs.meta.Chunk;
import com.xdfs.util.CacheRpc;
import com.xdfs.meta.DataNode;
import com.xdfs.protocol.Constants;
import com.xdfs.service.NameServer;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.xdfs.common.util.Debug.debug;

/**
 * Created by xyy on 16-12-6.
 */
public class ChunkReport {

    private static Logger logger = Logger.getLogger(ChunkReport.class);

    public static void startChunkReport(DataNode dataNode) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            try {
                NameServer nameServer = CacheRpc.nameServer;
                List<Chunk> recList = nameServer.chunkReport(dataNode);
                if (CollectionUtils.isEmpty(recList)) {
                    return;
                }

                debug("chunkReport receive : need delete chunks:" + JSONObject.toJSONString(recList));
                // TODO: 16-12-9  need delete chunks

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }, 1, Constants.CHUNKREPORT_TIME, TimeUnit.SECONDS);
    }

}
