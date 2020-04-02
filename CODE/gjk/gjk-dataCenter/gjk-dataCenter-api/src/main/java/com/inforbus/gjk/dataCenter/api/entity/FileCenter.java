package com.inforbus.gjk.dataCenter.api.entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName: FileCenter
 * @Desc 文件中心管理的文件实体类
 * @Author xiaohe
 * @DateTime 2020年4月1日
 */
@Data
public class FileCenter implements Serializable {
	/**
	 * @Fields serialVersionUID :
	 */

	private static final long serialVersionUID = 1L;
	/**
	  * @Fields fileName : 文件名字
	  */
	private String fileName;
	/**
	  * @Fields fileSize : 文件大小
	  */
	private Long fileSize;
	/**
	  * @Fields filePath : 文件路径
	  */
	private String filePath;
	/**
	  * @Fields absolutePath : 文件绝对路径
	  */
	private String absolutePath;
	/**
	  * @Fields inputStream : 文件输入流
	  */
	private FileInputStream inputStream;
	/**
	  * @Fields outputStream : 文件输出流
	  */
	private FileOutputStream outputStream;

	/**
	 * @param fileName 文件名字
	 * @param fileSize 文件大小
	 * @param filePath 文件路径
	 * @param absolutepath 文件绝对路径
	 * @param inputStream  文件输入流
	 */
	public FileCenter(String fileName, Long fileSize, String filePath, String absolutePath,
			FileInputStream inputStream) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.absolutePath = absolutePath;
		this.inputStream = inputStream;
	}

	/**
	 * @param fileName 文件名字
	 * @param fileSize 文件大小
	 * @param filePath 文件路径
	 * @param absolutepath 文件绝对路径
	 * @param outputStream 文件输出流
	 */
	public FileCenter(String fileName, Long fileSize, String filePath, String absolutePath,
			FileOutputStream outputStream) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.absolutePath = absolutePath;
		this.outputStream = outputStream;
	}

	public FileCenter() {
		super();
	}
}
