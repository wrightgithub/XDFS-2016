package com.xdfs.common.util;

/**
 * Created by xyy on 16-12-6.
 */
public class Debug {

    private static boolean openDebug = true;

    public static void debug(String str) {
        if (openDebug == true) {
            System.out.println(str);
        }
    }
}
