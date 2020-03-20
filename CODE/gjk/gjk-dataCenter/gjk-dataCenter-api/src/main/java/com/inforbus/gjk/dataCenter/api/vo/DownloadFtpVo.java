package com.inforbus.gjk.dataCenter.api.vo;

import lombok.Data;

/**
 * @ClassName: DownloadFtpVo
 * @Description: 下载文件条件的vo
 * @Author xiaohe
 * @DateTime 2020年3月5日  
 */
@Data
public class DownloadFtpVo {

	/**
	  * @Fields remotePath :  ftp路径
	  */
	private String remotePath;
	/**
	  * @Fields isTask : 是否开启线程
	  */
	private Boolean isTask;
	/**
	  * @Fields localPath : 本地路径
	  */
	private String localPath;
	/**
	  * @Fields isDir : 是否多文件下载（包含文件夹）
	  */
	private Boolean isDir;
	/**
	  * @Fields fileName : 单文件下载时的文件名称
	  */
	private String fileName;
	
}
