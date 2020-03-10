package com.inforbus.gjk.dataCenter.api.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.dataCenter.api.ftp.FtpConfig;
import com.inforbus.gjk.dataCenter.api.ftp.FtpConstants;
import com.inforbus.gjk.dataCenter.api.vo.DownloadFtpVo;
import com.inforbus.gjk.dataCenter.api.ftp.FtpClientPool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * fileName:FTP工具类 description:提供文件上传和下载 author:luojie createTime:2019-03-16
 * 8:55
 */
@Component
public class FtpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(FtpClientUtil.class);

	@Autowired
	FtpClientPool ftpClientPool;
	@Autowired
	FtpConfig ftpConfig;

	ExecutorService uploadThreadPool = null;

	// *************公共方法 用于其他调用*****************//
	/**
	 * @Title: download
	 * @Description: 文件下载
	 * @Author xiaohe
	 * @DateTime 2020年2月27日
	 * @param remotePath ftp路径
	 * @param isTask     是否开启线程
	 * @param localPath  本地路径
	 * @param isDir      是否多文件下载（包含文件夹）
	 * @param fileName   单文件下载时的文件路径
	 * @return
	 */
	public R<T> download(DownloadFtpVo ftpVo) {
		uploadThreadPool = Executors.newFixedThreadPool(10);
		DownloadTask upload = null;
		FTPClient ftpClient = null;
		ftpClient = ftpClientPool.borrowObject();
		R<T> ret = new R<T>();
		try {
			// 是否开启线程下载
			if (ftpVo.getIsTask()) {
				// 多文件下载还是单文件下载
				if (ftpVo.getIsDir()) {
					upload = new DownloadTask(this, ftpClient, ftpVo.getRemotePath(), ftpVo.getLocalPath());
				} else {
					upload = new DownloadTask(this, ftpClient, ftpVo.getRemotePath(), ftpVo.getFileName(),
							ftpVo.getLocalPath());
				}
				Future<R<T>> submit = uploadThreadPool.submit(upload);
				return submit.get();
			} else {
				// 多文件下载还是 单文件下载 false====单文件 true 多文件
				if (ftpVo.getIsDir()) {
					this.downloadFile(ftpVo.getRemotePath(), ftpVo.getLocalPath());
				} else {
					this.downloadFile(ftpVo.getRemotePath(), ftpVo.getFileName(), ftpVo.getLocalPath());
				}
			}

		} catch (Exception e) {
			ret.setException(e);
		} finally {
			uploadThreadPool.shutdown();
			ftpClientPool.returnObject(ftpClient);
		}
		return ret;
	}

	/**
	 * 上传本地文件到FTP指定目录下
	 * 
	 * @param mfile      MultipartFile
	 * @param remotePath 上传服务器路径 - 应该以/结束
	 * @return R<T>
	 */
	public R<T> uploadMultipartFile(MultipartFile mfile, String remotePath, Boolean isTask) {
		uploadThreadPool = Executors.newFixedThreadPool(10);
		R<T> ret = new R<T>();
		try {
			if (isTask) {
				UploadTask upload = new UploadTask(this, mfile.getInputStream(), mfile.getOriginalFilename(),
						remotePath);
				Future<R<T>> futureTask = uploadThreadPool.submit(upload);
				ret = futureTask.get();
			} else {
				ret = this.uploadFile(mfile.getInputStream(), mfile.getOriginalFilename(), remotePath);
			}
			return ret;
		} catch (Exception e) {
			logger.error("upload file failure!", e);
			ret.setException(e);
		} finally {
			uploadThreadPool.shutdown();
		}
		return ret;
	}

	/**
	 * 上传本地文件到FTP指定目录下
	 * 
	 * @param localFile  File
	 * @param remotePath 上传服务器路径 - 应该以/结束
	 * @return R<T>
	 */
	public R<T> uploadlocalFile(File localFile, String remotePath, Boolean isTask) {
		uploadThreadPool = Executors.newFixedThreadPool(10);
		FileInputStream fileInputStream = null;
		R<T> ret = new R<T>();
		try {
			// 判断是否需要开启线程
			if (isTask) {
				fileInputStream = new FileInputStream(localFile);
				UploadTask upload = new UploadTask(this, fileInputStream, localFile.getName(), remotePath);
				Future<R<T>> submit = uploadThreadPool.submit(upload);
				ret = submit.get();
			} else {
				ret = this.uploadFile(fileInputStream, localFile.getName(), remotePath);
			}

		} catch (Exception e) {
			ret.setException(e);
		} finally {
			uploadThreadPool.shutdown();
		}
		return ret;
	}

	/**
	 * 删除FTP上的指定文件
	 * 
	 * @param remotePath FTP服务器保存目录
	 * @param fileName   要删除的文件名称
	 * @return true or false
	 */
	public R<T> deleteFile(String remotePath, String fileName) {
		FTPClient ftpClient = null;
		R<T> ret = new R<T>();
		try {
			ftpClient = ftpClientPool.borrowObject();
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(remotePath);
			boolean file = ftpClient.deleteFile(fileName);
			if (file) {
				return ret;
			} else {
				ret.setCode(CommonConstants.FAIL);
				ret.setMsg("delete file failure!");
				logger.error("delete file failure!");
				return ret;
			}

		} catch (Exception e) {
			logger.error("delete file failure!", e);
			ret.setException(e);
			return ret;
		} finally {
			ftpClientPool.returnObject(ftpClient);
		}
	}

	/**
	 * 删除指定文件夹下的所有文件
	 * 
	 * @param remotePath
	 */
	public R<T> deleteFileOnFolder(String remotePath) {
		FTPClient client = null;
		R<T> ret = new R<T>();
		try {
			client = ftpClientPool.borrowObject();
			// 切换FTP目录
			client.changeWorkingDirectory(remotePath);
			FTPFile[] files = client.listFiles();
			for (FTPFile file : files) {
				if (file.isFile()) {
					String fileName = file.getName();
					client.deleteFile(fileName);
					logger.info("delete file success,name:{}", fileName);
					ret.setMsg("delete file failure!");
				}
			}
			logger.info("delete data success,size:{}", files.length);
			logger.debug("delete file reply code:{}", remotePath);
		} catch (Exception e) {
			logger.error("delete file failure!", e);
			ret.setException(e);
		} finally {
			ftpClientPool.returnObject(client);
		}
		return ret;
	}

	/**
	 * 获取客户端，进行自定义的操作
	 **/
	public void syncDataByTableName(List<String> tables, String remotePath) {
		if (!tables.isEmpty()) {
			FTPClient client = null;
			try {
				client = ftpClientPool.borrowObject();
				// 切换FTP目录
				client.changeWorkingDirectory(remotePath);
				// DO something
			} catch (Exception e) {
				logger.error("delete file failure!", e);
			} finally {
				ftpClientPool.returnObject(client);
			}
		}
	}

	/**
	 * @Title: downloadFile
	 * @Description: 下载FTP指定文件到本地目(多文件下载 包含文件夹)
	 * @Author xiaohe
	 * @DateTime 2020年2月27日
	 * @param remotePath
	 * @param localPath
	 * @return
	 */
	protected R<T> downloadFile(String remotePath, String localPath) {
		this.doDownloadAllFile(remotePath, localPath);
		return new R<T>();
	}

	/**
	 * @Title: downloadFile
	 * @Description: 下载FTP指定文件到本地目录（单文件下载）
	 * @Author xiaohe
	 * @DateTime 2020年2月27日
	 * @param remotePath FTP服务器文件目录
	 * @param fileName   需要下载的文件名称
	 * @param localPath  下载后的文件路径
	 * @return
	 */
	public R<T> downloadFile(String remotePath, String fileName, String localPath) {
		OutputStream outputStream = null;
		FTPClient ftpClient = null;
		R<T> ret = new R<T>();
		try {
			ftpClient = ftpClientPool.borrowObject();
			// 创建目录
			Boolean makeDir = this.makeDirs(remotePath);
			if (makeDir) {
				// 将目录切换至指定路径
				ftpClient.changeWorkingDirectory(remotePath);
				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile file : ftpFiles) {
					logger.info("fileName...--{} to file.getName()----{}", fileName, file.getName());
					// 判断名字是否相同
					if (fileName.equalsIgnoreCase(file.getName())) {
						File localFile = UploadFilesUtils.createFile(localPath + File.separator + file.getName());
						outputStream = new FileOutputStream(localFile);
						// 下载文件到 localFile 中
						ftpClient.retrieveFile(file.getName(), outputStream);
					}
				}
				ftpClient.logout();
				ret.setMsg("download file succeeded!");
			} else {
				logger.info("failed to switch or create folders!");
				ret.setMsg("failed to switch or create folders!");
				ret.setCode(CommonConstants.FAIL);
			}
		} catch (Exception e) {
			logger.error("download file failure!", e);
			ret.setException(e);
		} finally {
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // 将对象放回池中
			ftpClientPool.returnObject(ftpClient);
		}
		return ret;
	}

	/**
	 * 创建目录(有则切换目录，没有则创建目录)
	 *
	 * @param dir
	 * @return
	 */
	protected boolean makeDirs(String dir) {
		FTPClient ftpClient = null;
		if (StringUtils.isEmpty(dir))
			return true;
		String d;
		try {
			ftpClient = ftpClientPool.borrowObject();
			// 目录编码，解决中文路径问题
			dir = dir.indexOf("/") == 0 ? dir.substring(1) : dir;
			dir = dir.lastIndexOf("/") == dir.length() - 1 ? dir.substring(0, dir.length() - 1) : dir;
			d = new String(dir.toString().getBytes("GBK"), FtpConstants.SERVER_CHARSET);
			// 尝试切入目录
			if (ftpClient.changeWorkingDirectory(d)) {
				return true;
			}
			String[] arr = dir.split("/");
			StringBuffer sbfDir = new StringBuffer();
			// 循环生成子目录
			for (String s : arr) {
				sbfDir.append("/");
				sbfDir.append(s);
				// 目录编码，解决中文路径问题
				d = new String(sbfDir.toString().getBytes("GBK"), FtpConstants.SERVER_CHARSET);
				// 尝试切入目录
				if (ftpClient.changeWorkingDirectory(d))
					continue;
				if (!ftpClient.makeDirectory(d)) {
					logger.error("[失败]ftp创建目录：{}", sbfDir.toString());
					return false;
				}
				logger.info("[成功]创建ftp目录：{}", sbfDir.toString());
			}
			// 将目录切换至指定路径
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ftpClientPool.returnObject(ftpClient);
		}
	}

	/**
	 * @Title: downloadFile
	 * @Description: 下载指定目录下的所有文件（ 不包含文件夹）
	 * @Author xiaohe
	 * @DateTime 2020年2月26日
	 * @param remotePath
	 * @param localPath
	 * @return
	 */
	private void doDownloadAllFile(String remotePath, String localPath) {
		OutputStream outputStream = null;
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpClientPool.borrowObject();
			// 切换FTP目录
			Boolean makeDir = this.makeDirs(remotePath);
			if (makeDir) {
				// 将目录切换至指定路径
				ftpClient.changeWorkingDirectory(remotePath);
				// 获取目录下的文件列表(包含文件和文件夹)
				String[] fileNames = ftpClient.listNames();
				for (String fname : fileNames) {
					// 如果是文件，就会返回false
					// 如果文件夹或文件名中含有中文，这里要转换一下，不然会返回false
					boolean isDir = ftpClient.changeWorkingDirectory(remotePath + "/" + fname);
					if (isDir) {
						// 递归调用
						this.doDownloadAllFile(remotePath + "/" + fname, localPath + "/" + fname);
					} else {
						// 下载单个文件
						this.downloadFile(remotePath, fname, localPath);
					}
				}
			} else {
				logger.info("failed to switch or create folders!");
			}
		} catch (Exception e) {
			logger.error("download file failure!", e);
		} finally {
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 将对象放回池中
			ftpClientPool.returnObject(ftpClient);
		}
	}

	/***
	 * 上传本地文件到FTP指定目录下
	 * 
	 * @param localFileName 文件名字
	 * @param remotePath    上传服务器路径 - 应该以/结束
	 * @return R<T>
	 */
	protected R<T> uploadFile(InputStream localFile, String localFileName, String remotePath) {
		FTPClient ftpClient = null;
		R<T> ret = new R<>();
		try (BufferedInputStream inStream = new BufferedInputStream(localFile)) {
			// 从池中获取对象
			ftpClient = ftpClientPool.borrowObject();
			// 创建文件路径并
			Boolean makeDir = this.makeDirs(remotePath);
			if (makeDir) {
				logger.info("start upload... {}", localFileName);
				// 改变工作路径
				ftpClient.changeWorkingDirectory(remotePath);
				final int retryTimes = 3;
				for (int j = 0; j <= retryTimes; j++) {
					boolean success = ftpClient.storeFile(localFileName, inStream);
					if (success) {
						ret.setMsg("upload file success!" + localFileName);
						logger.info("upload file success! {}", localFileName);
						return ret;
					}
					ret.setMsg("upload file failure! try uploading again... " + j + " times");
					logger.warn("upload file failure! try uploading again... {} times", j);
				}
			} else {
				logger.info("failed to switch or create folders!");
				ret.setMsg("failed to switch or create folders!");
				ret.setCode(CommonConstants.FAIL);
			}
		} catch (FileNotFoundException e) {
			ret.setException(e);
			ret.setMsg("send File To FTP failed, file not found!" + localFileName);
			logger.error("send File To FTP failed, file not found!{}", localFileName);
		} catch (Exception e) {
			ret.setException(e);
			ret.setMsg("send File To FTP failed, filename=" + localFileName + ", error=" + e.getCause());
			logger.error("send File To FTP failed, filename={}, error={}", localFileName, e.getCause());
		} finally {
			// 将对象放回池中
			ftpClientPool.returnObject(ftpClient);
		}
		return ret;
	}
	// *************私有方法*****************//
}

/**
 * 上传文件的线程
 * 
 * @ClassName: UploadTask
 * @Description:
 * @Author xiaohe
 * @DateTime 2020年2月26日
 */
class UploadTask implements Callable<R<T>> {
	private FtpClientUtil clientUtil;
	private InputStream inputStream;
	private String fileName;
	private String remotePath;

	public UploadTask(FtpClientUtil clientUtil, InputStream inputStream, String fileName, String remotePath) {
		super();
		this.clientUtil = clientUtil;
		this.inputStream = inputStream;
		this.fileName = fileName;
		this.remotePath = remotePath;
	}

	@Override
	public R<T> call() throws Exception {
		return clientUtil.uploadFile(inputStream, fileName, remotePath);
	}
}

/**
 * 下载文件的线程
 * 
 * @ClassName: UploadTask
 * @Description:
 * @Author xiaohe
 * @DateTime 2020年2月26日
 */
class DownloadTask implements Callable<R<T>> {
	private FtpClientUtil clientUtil;
	private FTPClient ftpClient;
	private String remotePath;
	private String fileName;
	private String localPath;
	private boolean isTask;

	/**
	 * 单文件上传
	 * 
	 * @param remotePath 服务器路径
	 * @param fileName   文件名字
	 * @param localPath  本地文件路径
	 */
	public DownloadTask(FtpClientUtil clientUtil, FTPClient ftpClient, String remotePath, String fileName,
			String localPath) {
		super();
		this.ftpClient = ftpClient;
		this.remotePath = remotePath;
		this.clientUtil = clientUtil;
		this.fileName = fileName;
		this.localPath = localPath;
		this.isTask = false;
	}

	/**
	 * 多文件上传
	 * 
	 * @param remotePath 服务器路径
	 * @param localPath  本地文件路径
	 */
	public DownloadTask(FtpClientUtil clientUtil, FTPClient ftpClient, String remotePath, String localPath) {
		super();
		this.ftpClient = ftpClient;
		this.remotePath = remotePath;
		this.clientUtil = clientUtil;
		this.localPath = localPath;
		this.isTask = true;
	}

	@Override
	public R<T> call() throws Exception {
		// 多文件下载还是单文件下载
		R<T> ret = null;
		if (isTask) {
			// 多文件下载
			ret = clientUtil.downloadFile(remotePath, localPath);
		} else {
			// 单文件上传
			ret = clientUtil.downloadFile(remotePath, fileName, localPath);
		}
		return ret;
	}
}