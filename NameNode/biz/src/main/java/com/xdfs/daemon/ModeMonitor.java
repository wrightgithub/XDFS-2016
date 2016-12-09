package com.xdfs.daemon;

import com.xdfs.protocol.Constants;
import com.xdfs.protocol.Mode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.xdfs.common.util.Debug.debug;
import static com.xdfs.protocol.Constants.SAFEMODE_TIME;

/**
 * Created by xyy on 16-12-9.
 */
public class ModeMonitor {

    private static String name = "modemonitor";

    private Mode mode;
    private long startTime;

    public ModeMonitor(Mode mode, long startTime) {
        this.mode = mode;
        this.startTime = startTime;
    }

    public  void start() {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1, new MonitorFactory(name));
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            long temp=System.currentTimeMillis()-startTime;
            if(temp > SAFEMODE_TIME*1000&&mode==Mode.SAFEMODE){
                mode=Mode.NORMAL;
                debug("mode become NORMAL!");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
