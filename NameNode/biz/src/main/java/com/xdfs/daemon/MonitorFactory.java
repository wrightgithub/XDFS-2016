package com.xdfs.daemon;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xyy on 16-12-9.
 */
public class MonitorFactory implements ThreadFactory {

    private static String              BASENAME     = "monitor-thread-";
    private static final AtomicInteger poolNumber   = new AtomicInteger(1);
    private ThreadGroup                group;
    private final AtomicInteger        threadNumber = new AtomicInteger(1);
    private String                     namePrefix;

    MonitorFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = BASENAME + name + "-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        return t;
    }
}
