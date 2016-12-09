package com.xdfs.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xyy on 16-12-6.
 */
public class Test {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        final List<Long> l = new ArrayList<>();
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                synchronized (l) {
                    l.add(new Random().nextLong());
                    System.out.println(l.size());
                }
            }
        }, 1, 100, TimeUnit.MILLISECONDS);

        while (true)
            ;
    }
}
