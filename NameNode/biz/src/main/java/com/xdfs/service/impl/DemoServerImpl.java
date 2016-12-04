package com.xdfs.service.impl;

import com.xdfs.service.DemoServer;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @author wanggengqi
 * @email wanggengqi@chinasofti.com
 * @date 2014年10月23日 下午1:31:47
 */
public class DemoServerImpl implements DemoServer {
	private static Logger logger = Logger.getLogger(DemoServerImpl.class);

	/**
	 * 返回添加后的语句
	 */
	public String sayHello(String str) {
		str = "Hello " + str + "  2:" + new Date();
		System.err.println("server:" + str);
		logger.info("log: info");
		logger.error("log: error");
		return str;
	}

}