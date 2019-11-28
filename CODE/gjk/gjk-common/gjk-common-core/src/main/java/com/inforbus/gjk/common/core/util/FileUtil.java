package com.inforbus.gjk.common.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Set;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

	/**
	 * 将文件夹下所有子文件及子文件夹拷贝到指定文件夹下
	 *
	 * @param filePath       需要拷贝的文件夹
	 * @param targetFilePath 指定文件夹目录
	 * @return
	 * @throws IOException
	 */
	public boolean copyFile(String filePath, String targetFilePath) throws IOException {
		File file = new File(filePath);
		File targetFile = new File(targetFilePath);

		if (!file.exists()) {
			System.out.println("源文件不存在,请检查源文件");
			return false;
		}

		// 判断目标文件夹是否存在，若不存在，则创建文件夹
		if (!targetFile.exists()) {
			targetFile.mkdirs();
			System.out.println("我创建了" + targetFile.getAbsolutePath());
		}

		if (file.isFile()) {
			copyFile(filePath, targetFilePath, file.getName());
		} else {
			String[] fileListPaths = null;
			if (file.exists()) {
				// 若文件存在，则获取文件名称列表
				fileListPaths = file.list();
			} else {
				return false;
			}

			for (String fileName : fileListPaths) {
				File newFile = new File(filePath + File.separator + fileName);
				if (newFile.isDirectory()) {
					copyFile(newFile.getAbsolutePath(), targetFilePath + File.separator + fileName);
				} else {
					copyFile(newFile.getAbsolutePath(), targetFilePath);
				}
			}
		}

		return true;
	}

	/**
	 * 将文件拷贝到指定的文件夹下
	 *
	 * @param filePath       需要拷贝的文件
	 * @param targetFilePath 指定的文件夹
	 * @param targetFilePath 指定文件名
	 * @return
	 * @throws IOException
	 */
	public String copyFile(String filePath, String targetFilePath, String fileName) throws IOException {
		FileChannel in = null;
		FileChannel out = null;

		File sourcePath = new File(filePath);
		File destinPath = new File(targetFilePath);

		if (!destinPath.exists()) {
			destinPath.mkdirs();
		}

		try {
			// 取得对应文件的通道
			in = new FileInputStream(sourcePath).getChannel();
			out = new FileOutputStream(destinPath + File.separator + fileName).getChannel();
			// 连接两个通道，并从in通道读取，写入out中
			in.transferTo(0, in.size(), out);
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}

		return targetFilePath;
	}

	/**
	 * 获取在指定文件路径下所有符合条件的文件的绝对路径的集合
	 *
	 * @param returnFilePathList
	 * @param filePath
	 * @param selectFileName
	 */
	public void getSelectStrFilePathList(Set<String> returnFilePathList, String filePath, String selectFileName) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isFile()) {
				if (file.getName().endsWith(selectFileName)) {
					returnFilePathList.add(file.getAbsolutePath());
				}
			} else if (file.isDirectory()) {
				File[] childFiles = file.listFiles();
				for (File childFile : childFiles) {
					getSelectStrFilePathList(returnFilePathList, childFile.getAbsolutePath(), selectFileName);
				}
			}
		} else {
			System.out.println("传入路径错误，请联系管理员。" + filePath);
		}
	}

	/**
	 * 获取在指定文件路径下所有符合条件的文件的绝对路径的集合
	 *
	 * @param returnFilePathList
	 * @param filePath
	 * @param selectFileName
	 */
	public void getSelectStrFilePathList(Set<String> returnFilePathList, String filePath, String startWith,
			String endWith) {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isFile()) {
				if (file.getName().startsWith(startWith) && file.getName().endsWith(endWith)) {
					returnFilePathList.add(file.getAbsolutePath());
				}
			} else if (file.isDirectory()) {
				File[] childFiles = file.listFiles();
				for (File childFile : childFiles) {
					getSelectStrFilePathList(returnFilePathList, childFile.getAbsolutePath(), startWith, endWith);
				}
			}
		} else {
			System.out.println("传入路径错误，请联系管理员。");
		}
	}

	/**
	 * 获取在指定文件路径下所有符合条件的文件的绝对路径的集合
	 *
	 * @param returnFilePathList
	 * @param filePath
	 * @param selectFileNameList
	 */
	public void getSelectStrFilePathList(Set<String> returnFilePathList, String filePath,
			List<String> selectFileNameList) {
		for (String selectStr : selectFileNameList) {
			getSelectStrFilePathList(returnFilePathList, filePath, selectStr);
		}
	}

	/**
	 * 获取在指定文件路径下与搜索文件名相同的文件夹路径
	 *
	 * @param filePath
	 * @param selectFileName
	 * @return
	 */
	public String getSelectStrFilePath(String filePath, String selectFileName) {
		String returnFilePath = null;
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				String[] childFilePaths = file.list();
				for (String path : childFilePaths) {
					if (path.equals(selectFileName)) {
						returnFilePath = filePath + File.separator + path;
						break;
					} else {
						returnFilePath = getSelectStrFilePath(filePath + File.separator + path, selectFileName);
						if (returnFilePath != null) {
							break;
						}
					}
				}
			}
			return returnFilePath;
		} else {
			System.out.println("传入路径错误，请联系管理员。");
			return null;
		}
	}

}
