package com.inforbus.gjk.dataCenter.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.dataCenter.service.FileService;

@Service("fileService")
public class FileServiceImpl implements FileService {
	@Value("${git.local.path}")
	private String localBasePath;
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/**
	 * @Title: uploadLocalFile
	 * @Desc 多文件上传
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 保存的本地文件路劲
	 * @param localFile 要保存的文件列表
	 * @return
	 * @throws IOException
	 */
	@Override
	public Boolean uploadLocalFile(String localPath, List<InputStream> localFile) throws IOException {
		if (localFile.isEmpty()) {
			return false;
		}
		// 创建文件
		UploadFilesUtils.createFile(localBasePath + File.separator + localPath);
		localFile.stream().forEach(is -> {
		});
		return false;
	}

	/**
	 * @Title: downloadFile
	 * @Desc 下载文件
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件/文件夹路径 相对路径
	 * @param localPath  保存文件/文件夹根路径 相对路径
	 * @param fileName   文件名字
	 */
	@Override
	public Boolean downloadFile(String sourcePath, String localPath, String fileName) {
		File file = new File(localBasePath + File.separator + sourcePath);
		// 判断是不是目录
		try {
			if (file.isDirectory()) {
				FileUtil.copyFile(file.getPath(), localBasePath + File.separator + localPath);
				logger.info("下载文件夹 {} 中文件到 {} 中", sourcePath, localPath);
			} else {
				FileUtil.copyFile(file.getPath(), localBasePath + File.separator + localPath, fileName);
				logger.info("下载文件夹 {} 中文件到 {} 中 ；名字为{}", sourcePath, localPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件路径
	 * @return
	 */
	@Override
	public Boolean delAllFile(String sourcePath) {
		String allPath = localBasePath + File.separator + sourcePath;
		// TODO Auto-generated method stub
		return UploadFilesUtils.delAllFile(allPath);
	}

	/**
	 * @Title: delFolder
	 * @Description: 删除文件夹
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param folderPath 文件夹路径
	 */
	@Override
	public Boolean delFolder(String folderPath) {
		String allPath = localBasePath + File.separator + folderPath;
		//// 删除完里面所有内容
		if (delAllFile(allPath)) {
			String filePath = allPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			return myFilePath.delete(); // 删除空文件夹
		} else {
			return false;
		}

	}

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
	@Override
	public void copylocalFile(String source, String destin) throws Exception {
		UploadFilesUtils.NioToCopyFile(source, destin);
	}

	/**
	 * @Title: readFile
	 * @Desc 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 文件路径
	 * @param charset   编码格式
	 * @return
	 * @throws Exception
	 */
	@Override
	public String readFile(String localPath, String charset) throws Exception {
		String filepath = localBasePath + File.separator + localPath;
		if (localPath.endsWith(".doc") && localPath.endsWith(".docx")) {
			readWord(filepath);
		} else if (localPath.endsWith(".doc") && localPath.endsWith(".docx")) {

		} else {
			readTxt(filepath, charset);
		}
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @Title: writeFile
	 * @Desc 编辑文件 更改文件内容
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param localPath   文件路径
	 * @param charset     编码格式
	 * @param textContext 文件内容
	 * @return
	 */
	@Override
	public Boolean writeFile(String localPath, String charset, String textContext) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @Description: POI 读取 word
	 * 
	 * @create: 2019-07-27 9:48
	 * 
	 * @update logs
	 * 
	 * @throws Exception
	 * 
	 */

	private static List<String> readWord(String filePath) throws Exception {
		List<String> linList = new ArrayList<String>();
		String buffer = "";
		try {
			if (filePath.endsWith(".doc")) {
				InputStream is = new FileInputStream(new File(filePath));
				WordExtractor ex = new WordExtractor(is);
				buffer = ex.getText();
				ex.close();
				if (buffer.length() > 0) {
					// 使用回车换行符分割字符串
					String[] arry = buffer.split("\\r\\n");
					for (String string : arry) {
						linList.add(string.trim());
					}
				}
			} else if (filePath.endsWith(".docx")) {
				OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
				POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
				buffer = extractor.getText();
				extractor.close();
				if (buffer.length() > 0) {
					// 使用换行符分割字符串
					String[] arry = buffer.split("\\n");
					for (String string : arry) {
						linList.add(string.trim());
					}
				}
			} else {
				return null;
			}
			return linList;
		} catch (Exception e) {
			System.out.print("error---->" + filePath);
			e.printStackTrace();
			return null;
		}

	}

	// 判断编码格式方法
	private static String getFilecharset(File sourceFile) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						// 双字节 (0xC0 - 0xDF)
						if (0x80 <= read && read <= 0xBF)
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	/**
	 * 读取txt文件
	 */
	@SuppressWarnings("resource")
	public static void readTxt(String localPath, String charset) {
		try {
			// 定义缓冲区对象
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			// 通过文件输入流获取文件通道流对象
			FileChannel inFc = new FileInputStream(localPath).getChannel();
			// 读取数据
			buffer.clear();
			int length = inFc.read(buffer);
//			System.out.println(new String(buffer.array(), 0, length, "GBK"));
			System.out.println(new String(buffer.array(), 0, length, charset));
			inFc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写入txt 文件
	 */
	@SuppressWarnings("resource")
	public static void writeTxt(String localPath, String charset) {
		try {
			FileChannel outFc = new FileOutputStream(localPath, true).getChannel();
//			ByteBuffer byteBuffer = ByteBuffer.wrap("\r\n李白乘舟将欲行".getBytes("GBK"));
			ByteBuffer byteBuffer = ByteBuffer.wrap("\r\n李白乘舟将欲行".getBytes(charset));
			outFc.write(byteBuffer);
			outFc.close();
		} catch (Exception e) {
		}
	}
}
