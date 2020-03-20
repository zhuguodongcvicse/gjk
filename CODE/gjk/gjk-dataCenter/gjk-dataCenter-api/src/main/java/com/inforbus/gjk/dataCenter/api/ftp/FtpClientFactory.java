package com.inforbus.gjk.dataCenter.api.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketException;

/**
 * 连接工厂
 */
@Component

public class FtpClientFactory implements PooledObjectFactory<FTPClient> {
	@Autowired
	FtpConfig ftpConfig;
	private static final Logger logger = LoggerFactory.getLogger(FtpClientFactory.class);

	// 创建连接到池中
	@Override
	public PooledObject<FTPClient> makeObject() {
		logger.warn("创建连接到池中..........makeObject()");
		FTPClient ftpClient = new FTPClient();// 创建客户端实例
		return new DefaultPooledObject<FTPClient>(ftpClient);
	}

	// 销毁连接，当连接池空闲数量达到上限时，调用此方法销毁连接
	@Override
	public void destroyObject(PooledObject<FTPClient> pooledObject) {
		logger.warn("销毁连接..........destroyObject()");
		FTPClient ftpClient = pooledObject.getObject();
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not disconnect from server.", e);
		}
	}

	// 链接状态检查
	@Override
	public boolean validateObject(PooledObject<FTPClient> pooledObject) {
		logger.warn("链接状态检查..........validateObject()");
		FTPClient ftpClient = pooledObject.getObject();
		try {
			return ftpClient.sendNoOp();
		} catch (IOException e) {
			return false;
		}
	}

	// 初始化连接
	@Override
	public void activateObject(PooledObject<FTPClient> pooledObject) {
		logger.warn("初始化连接..........activateObject()");
		FTPClient ftpClient = pooledObject.getObject();
		try {
			// 设置ip 和端口号
			ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				logger.warn("FTPServer refused connection,replyCode:{}", replyCode);
			}
			if (!ftpClient.login(ftpConfig.getUserName(), ftpConfig.getPassWord())) {
				logger.warn("ftpClient login failed... username is {}; password: {} host is {}; port: {}",
						ftpConfig.getUserName(), ftpConfig.getPassWord(), ftpConfig.getHost(), ftpConfig.getPort());
			} else {
				logger.info("ftpClient login succeeded... username is {}; password: {} host is {}; port: {}",
						ftpConfig.getUserName(), ftpConfig.getPassWord(), ftpConfig.getHost(), ftpConfig.getPort());
			}
			// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
			String LOCAL_CHARSET = "GBK";
			if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
				LOCAL_CHARSET = "UTF-8";
			}
			ftpClient.setControlEncoding(LOCAL_CHARSET);
			logger.warn("设置每次读取文件流时缓存数组的大小.....{}" , ftpClient.getBufferSize());
			ftpClient.setBufferSize(10240000);//设置每次读取文件流时缓存数组的大小。
			logger.warn("设置每次读取文件流时缓存数组的大小.....{}" , ftpClient.getBufferSize());
			ftpClient.setConnectTimeout(FtpConstants.MaxWaitMillis);
//			ftpClient.enterLocalPassiveMode();// 开启本地被动模式
//			ftpClient.enterRemotePassiveMode();// 开启远程被动传输模式
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置上传文件类型为二进制，否则将无法打开文件
//			ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			Boolean validate = this.validateObject(pooledObject);
			logger.warn("初始化连接..........activateObject().........链接状态检查....validateObject：{}" , validate);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 钝化连接，使链接变为可用状态
	@Override
	public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {
		logger.warn("钝化连接..........passivateObject()");
		FTPClient ftpClient = pooledObject.getObject();
		try {
			ftpClient.changeWorkingDirectory(FtpConstants.root);
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not disconnect from server.", e);
		}
	}

	// 用于连接池中获取pool属性
	public FtpConfig getConfig() {
		logger.warn("用于连接池中获取pool属性..........getConfig()");
		return ftpConfig;
	}

}
