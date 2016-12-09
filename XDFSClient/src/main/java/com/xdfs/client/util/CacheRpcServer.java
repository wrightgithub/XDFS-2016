package com.xdfs.client.util;

import com.xdfs.common.util.RpcUtil;
import com.xdfs.service.NameServer;

/**
 * Created by xyy on 16-12-8.
 */
public class CacheRpcServer {

    private static String     nameServerUrl ="127.0.0.1:10880";
    public  static NameServer nameServer    = RpcUtil.getService(NameServer.class, nameServerUrl);
}
