package com.inforbus.gjk.dataCenter.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: FileService
 * @Desc 文件服务管理
 * @Author xiaohe
 * @DateTime 2020年4月1日
 */

public interface FileService  {

	/**
	 * @Title: uploadLocalFile
	 * @Desc 多文件上传
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 保存的本地文件路劲
	 * @param localFile 要保存的文件列表
	 * @return
	 */
	public Boolean uploadLocalFile(String localPath, List<InputStream> localFile) throws IOException;

	/**
	 * @Title: downloadFile
	 * @Desc 下载文件
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件/文件夹路径
	 * @param localPath  保存文件/文件夹根路径
	 * @param fileName   文件名字
	 */
	public Boolean downloadFile(String sourcePath, String localPath, String fileName);

	/**
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件路径
	 * @return
	 */
	public Boolean delAllFile(String sourcePath);

	/**
	 * @Title: delFolder
	 * @Description: 删除文件夹
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param folderPath 文件夹路径
	 */
	public Boolean delFolder(String folderPath);

	/**
	 * @Title: copylocalFile
	 * @Description: 拷贝文件
	 * @Author xiaohe
	 * @DateTime 2019年11月27日 下午8:46:20
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @return
	 * @throws Exception
	 */
	public void copylocalFile(String source, String destin) throws Exception;

	/**
	 * @Title: readFile
	 * @Desc 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
	 * @Author xiaohe
	 * @DateTime 2020年4月1日 
	 * @param localPath 文件路径
	 * @param charset 编码格式
	 * @return
	 */
	public String readFile(String localPath,String charset) throws Exception;

	/**
	 * @Title: writeFile
	 * @Desc 编辑文件 更改文件内容
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param localPath 文件路径
	 * @param charset 编码格式
	 * @param textContext 文件内容
	 * @return 
	 */
	public Boolean writeFile(String localPath,String charset,String textContext);
}
