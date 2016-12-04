package com.xdfs.service.impl;

import com.xdfs.protocol.Meta;
import com.xdfs.service.NameServer;

import java.util.List;
import java.util.Map;

/**
 * Created by xyy on 16-12-4.
 */
public class NameServerImpl implements NameServer {

    private List<Meta> blocksInDN;
    private List<Meta> dnForblocks;
    private Map<String,Boolean> SurvivalMap;


}
