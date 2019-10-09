package com.inforbus.gjk.common.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
	public boolean copyDir(String filePath, String targetFilePath) throws IOException {
		File file = new File(filePath);
		File targetFile = new File(targetFilePath);
		// 判断目标文件夹是否存在，若不存在，则创建文件夹
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}

		String[] fileListPaths = null;
		if (file.exists()) {
			// 若文件存在，则获取文件名称列表
			fileListPaths = file.list();
		} else {
			return false;
		}

		for (String fileName : fileListPaths) {
			if ((new File(filePath + File.separator + fileName)).isDirectory()) {
				copyDir(filePath + File.separator + fileName, targetFilePath + File.separator + fileName);
			} else if ((new File(filePath + File.separator + fileName)).isFile()) {
				copyFile(filePath + File.separator + fileName, targetFilePath + File.separator);
			}
		}
		return true;
	}

	/**
	 * 将文件拷贝到指定的文件夹下
	 * 
	 * @param filePath       需要拷贝的文件
	 * @param targetFilePath 指定的文件夹
	 * @return
	 * @throws IOException
	 */
	public String copyFile(String filePath, String targetFilePath) throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			File file = new File(filePath);
			File targetFile = new File(targetFilePath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			targetFilePath += File.separator + file.getName();
			targetFile = new File(targetFilePath);

			if (targetFile.exists()) {
				targetFile.delete();
			}

			inputStream = new FileInputStream(file);
			outputStream = new FileOutputStream(targetFile);

			if (file.exists()) {
				byte[] buffer = new byte[2048];
				while (true) {
					int len = inputStream.read(buffer);

					if (len == -1) {
						break;
					} else {
						outputStream.write(buffer, 0, len);
					}
				}
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return targetFilePath;
	}

	/**
	 * 将文件拷贝到指定的文件夹下指定的文件
	 * 
	 * @param filePath       需要拷贝的文件
	 * @param targetFilePath 指定的文件夹
	 * @return
	 * @throws IOException
	 */
	public String copyFile(String filePath, String targetFilePath, String fileName) throws IOException {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			File file = new File(filePath);
			File targetFile = new File(targetFilePath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			targetFilePath += File.separator + fileName;
			targetFile = new File(targetFilePath);

			if (targetFile.exists()) {
				targetFile.delete();
			}

			inputStream = new FileInputStream(file);
			outputStream = new FileOutputStream(targetFile);

			if (file.exists()) {
				byte[] buffer = new byte[2048];
				while (true) {
					int len = inputStream.read(buffer);

					if (len == -1) {
						break;
					} else {
						outputStream.write(buffer, 0, len);
					}
				}
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
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
			System.out.println("传入路径错误，请联系管理员。");
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
