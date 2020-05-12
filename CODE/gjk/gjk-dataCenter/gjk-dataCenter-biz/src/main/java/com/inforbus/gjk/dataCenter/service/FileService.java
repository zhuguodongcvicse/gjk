package com.inforbus.gjk.dataCenter.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.dataCenter.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.dataCenter.api.entity.FileCenter;

/**
 * @ClassName: FileService
 * @Description 文件服务管理
 * @Author xiaohe
 * @DateTime 2020年4月1日
 */

public interface FileService {

	/**
	 * 多文件上传多文件上传
	 *
	 * @Title: uploadLocalFile
	 * @Description 多文件上传多文件上传
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 保存的本地文件路劲
	 * @param localFile 要保存的文件列表
	 * @return Boolean
	 * @throws Exception 文件流出错
	 */
	public Boolean uploadLocalFile(String localPath, List<FileCenter> localFile) throws Exception;

	/**
	 * 下载文件
	 *
	 * @Title: downloadFile
	 * @Description 下载文件
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件/文件夹路径
	 * @param localPath  保存文件/文件夹根路径
	 * @param fileName   文件名字
	 * @return Boolean
	 * @throws Exception 文件流出错
	 */
	public Boolean downloadFile(String sourcePath, String localPath, String fileName) throws Exception;

	/**
	 * 下载文件到本地
	 *
	 * @Title: downloadFile
	 * @Description 下载文件到本地
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件/文件夹路径
	 * @return List<FileCenter>
	 * @throws Exception 文件流出错
	 */
	public List<FileCenter> downloadFile(String sourcePath) throws Exception;

	/**
	 * 删除指定文件夹下的所有文件
	 *
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件路径
	 * @return Boolean
	 * @throws Exception 文件流出错
	 */
	public Boolean delAllFile(String sourcePath) throws Exception;

	/**
	 * 删除文件夹
	 *
	 * @Title: delFolder
	 * @Description: 删除文件夹
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param folderPath 文件路径集合
	 * @return Boolean
	 * @throws Exception 文件流出错
	 */
	public Boolean delFolder(String[] folderPath) throws Exception;

	/**
	 * 拷贝文件
	 *
	 * @Title: copylocalFile
	 * @Description 拷贝文件
	 * @Author xiaohe
	 * @DateTime 2020年4月7日
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @throws Exception 文件流出错
	 */
	public void copylocalFile(String source, String destin) throws Exception;

	/**
	 * 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
	 *
	 * @Title: readFile
	 * @Description 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 文件路径
	 * @param charset   编码格式
	 * @return Boolean
	 * @throws Exception
	 */
	public Map<String, String> readFile(String localPath) throws Exception;

	/**
	 * 编辑文件 更改文件内容
	 *
	 * @Title: writeFile
	 * @Description 编辑文件 更改文件内容
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param localPath   文件路径
	 * @param charset     编码格式
	 * @param textContext 文件内容
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean writeFile(String localPath,String charset,String textContext);

	/**
	 * @Author wang
	 * @Description: 解析xml文件为xmlEntity对象
	 * @Param: [localPath]
	 * @Return: boolean
	 * @Create: 2020/4/13
	 */
    XmlEntityMap analysisXmlFileToXMLEntityMap(String localPath) throws FileNotFoundException;

	/**
	 * @Author wang
	 * @Description: 早指定路径下生成xml文件
	 * @Param: xMlEntityMapVO
	 * @Return: com.inforbus.gjk.common.core.util.R
	 * @Create: 2020/4/14
	 */
    boolean createXMLFile(XMlEntityMapVO xMlEntityMapVO);

	/**
	 * @Author wang
	 * @Description: 根据据对路径删除文件
	 * @Param: [absolutePath] 文件的绝对路径
	 * @Return: com.inforbus.gjk.common.core.util.R
	 * @Create: 2020/4/15
	 */
	boolean delFile(String absolutePath);
	
	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 * 
	 * @param filePath 文件路径
	 * @param textContext 文本内容
	 */
	void saveFileContext(String filePath, String textContext) ;
	
	/**
	 * 程序文本编辑器的文件展示
	 * 
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	R fileReads(ThreeLibsFilePathDTO threeLibsFilePathDTO);

	/**
	 * @Title: getUploadFilePaths
	 * @Desc 文件上传返回路径
	 * @Author xiaohe
	 * @DateTime 2020年4月16日
	 * @param files 要上传的文件集合
	 * @param paths 文件的绝对路径（带盘符）
	 * @return 
	 * @throws IllegalStateException
	 * @throws IOException 
	 */
	Boolean getUploadFilePaths(MultipartFile[] files, String paths) throws IllegalStateException, IOException;

	/**
	 * @Title: decompression
	 * @Desc 上传文件（ZIP）并解压文件
	 * @Author xiaohe
	 * @DateTime 2020年5月8日
	 * @param file  文件流
	 * @param paths 要解压的文件路径
	 * @return
	 */
	R decompression(MultipartFile file, String paths);
	Boolean getUploadFilePaths(MultipartFile[] files, String paths) throws IllegalStateException, IOException;

	/**
	 * @Title: decompression
	 * @Desc 上传文件（ZIP）并解压文件
	 * @Author xiaohe
	 * @DateTime 2020年5月8日
	 * @param file  文件流
	 * @param paths 要解压的文件路径
	 * @return
	 */
	R decompression(MultipartFile file, String paths);

	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @auther sunchao
	 * @return
	 * @throws IOException
	 */
	Boolean judgeFileExist(String filePath);

	/**
	 * 查找App路径
	 * @param filePath
	 * @param selectFileName
	 * @auther sunchao
	 * @return
	 * @throws IOException
	 */
	String getAppPath(String filePath, String selectFileName) throws IOException;
}
