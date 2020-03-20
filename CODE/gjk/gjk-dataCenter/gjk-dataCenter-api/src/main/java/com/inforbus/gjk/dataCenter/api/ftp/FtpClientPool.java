package com.inforbus.gjk.dataCenter.api.ftp;

import org.apache.commons.net.ftp.FTPClient;

import org.apache.commons.pool2.impl.GenericObjectPool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.inforbus.gjk.dataCenter.api.utils.FtpClientUtil;

/**
 * 
 * fileName:ftpPool
 * description:FTP连接池
 * 1.可以获取池中空闲链接
 * 2.可以将链接归还到池中
 * 3.当池中空闲链接不足时，可以创建链接
 * author:luojie
 * createTime:2019-03-16 9:59
 * 
 */
@Component
public class FtpClientPool {
	private static final Logger logger = LoggerFactory.getLogger(FtpClientPool.class);
	FtpClientFactory factory;
	private final GenericObjectPool<FTPClient> internalPool;
	// 初始化连接池
	public FtpClientPool(@Autowired FtpClientFactory factory) {
		logger.info("初始化FtpClientPool连接池...............");
		this.factory = factory;
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(FtpConstants.MaxTotal);
		poolConfig.setMinIdle(FtpConstants.MinIdel);
		poolConfig.setMaxIdle(FtpConstants.MaxIdle);
		poolConfig.setMaxWaitMillis(FtpConstants.MaxWaitMillis);
		this.internalPool = new GenericObjectPool<FTPClient>(factory, poolConfig);
	}
	// 从连接池中取连接
	public FTPClient borrowObject() {
		logger.info("初始化FtpClientPool连接池中取连接...............");
		try {
			return internalPool.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 将链接归还到连接池
	public void returnObject(FTPClient ftpClient) {
		logger.info("将链接归还到FtpClientPool连接池...............");
		try {
			internalPool.returnObject(ftpClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 销毁池子
	 * 
	 */

	public void destroy() {
		try {
			internalPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
