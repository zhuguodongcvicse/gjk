package com.inforbus.gjk.common.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.jgit.JGitUtil;

/**
 * @ClassName: UploadFilesUtils
 * @Description: 文件上传工具
 * @Author xiaohe
 * @DateTime 2019年5月13日 上午10:55:53
 */
public class UploadFilesUtils {
	// 文件存储在服务器的相对地址
//	@Value("${file.local.path}")
//	private static String serverPath = JGitUtil.getLOCAL_REPO_PATH();
//	static {
//		serverPath = "D:/14S_GJK_GIT/gjk";
//	}

	/**
	 * @Title: createFile
	 * @Description: 创建文件及文件夹
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:14:51
	 * @param filePath 文件名
	 * @return File()
	 * @throws IOException 文件目录或目标文件创建失败
	 */
	public static File createFile(String filePath) throws IOException {
		// 获取文件的完整目录
//		String fileDir = "D:/upload/files";
		File file = null;
		// 判断目录是否存在，不存在就创建一个目录
//		String path = serverPath + "/" + filePath;
		if (StringUtils.isNotEmpty(filePath)) {
			file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}

	/**
	 * @Title: getUploadFilesUrl
	 * @Description: 单文件上传
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:12:45
	 * @param file
	 * @return
	 */
	public static String getUploadFilesUrl(MultipartFile file) {
		String url = new String();
		File uploadFile = null;
		try {
			// 获取上传文件名,包含后缀
			String originalFilename = file.getOriginalFilename();
			// 上传"image/"

			url = new String("gjk/upload/" + originalFilename).replace("\\", "/");
			uploadFile = createFile(url);
			// 将上传文件保存到路径
			if (uploadFile.exists()) {
				boolean ok = uploadFile.delete();
				System.out.println(ok);
			}
			file.transferTo(uploadFile);

			JGitUtil.commitAndPush(url, "");
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			url = new String(uploadFile.getPath().replace("\\", "/"));
		}
		return url;
	}

	/**
	 * @Title: getUploadImgUrl
	 * @Description: 上传图片
	 * @Author xiaohe
	 * @DateTime 2019年5月28日 下午2:32:10
	 * @param file
	 * @return
	 */
	public static String getUploadImgUrl(MultipartFile file) {
		String url = "";
		try {
			// 获取上传文件名,包含后缀
			String originalFilename = file.getOriginalFilename();
			url = new String("./gjk/image/" + originalFilename);
			// 上传
//			File uploadFile = createFile(originalFilename);
			File uploadFile = createFile(url);
			// 将上传文件保存到路径
			if (uploadFile.exists()) {
				uploadFile.delete();
			}
			file.transferTo(uploadFile);
			JGitUtil.commitAndPush(url, "上传构件图片img");
			url = new String(url).replace("\\", "/");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * @Title: getUploadFiles 多文件上传
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年7月9日 下午4:24:11
	 * @param file
	 * @return
	 */
	public static List<Map<String, String>> getUploadFilesUrl(MultipartFile[] files, Map<String, String> mapPath) {
		String url = "";
		List<Map<String, String>> listMaps = Lists.newArrayList();
		try {
			for (MultipartFile file : files) {
				Map<String, String> map = Maps.newHashMap();
				// 获取上传文件名,包含后缀
				String fileName = file.getOriginalFilename();
				url = new String(mapPath.get("path") + File.separator + fileName);
				File uploadFile = createFile(url);
				// 将上传文件保存到路径
				if (uploadFile.exists()) {
					uploadFile.delete();
				}
				file.transferTo(uploadFile);
				JGitUtil.commitAndPush(url, "多个文件上传");
				int index = fileName.lastIndexOf("/");
//				if (index == -1) {
				map.put("name", fileName);
//				} else {
//					map.put("name", fileName.substring(index+1));
//				}

				map.put("size", Long.toString(uploadFile.length()));
				map.put("relativePath", uploadFile.getPath());
				System.out.println(uploadFile.length());
//				System.out.println(file.getSize());
				System.out.println(file.getOriginalFilename());
				listMaps.add(map);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
//			url = new String(serverPath + File.separator + mapPath.get("path")).replace("\\", "/");
		}
		return listMaps;

	}

	/**
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2019年9月29日 下午5:13:56
	 * @param 文件夹路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			if (file.isFile()) {
				file.delete();
				return true;
			}
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
//				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * @Title: delFolder
	 * @Description: 删除文件夹
	 * @Author xiaohe
	 * @DateTime 2019年9月29日 下午5:14:34
	 * @param 文件夹路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: base64ToFile
	 * @Description: 将base64字节流转文件
	 * @Author xiaohe
	 * @DateTime 2019年10月14日 下午3:29:20
	 * @param file
	 * @param base64
	 * @throws IOException
	 */
	public static void base64ToFile(File file, String base64) throws IOException {
		BufferedOutputStream bos = null;
		java.io.FileOutputStream fos = null;
		byte[] bytes = Base64.getDecoder().decode(base64);
		fos = new java.io.FileOutputStream(file);
		bos = new BufferedOutputStream(fos);
		bos.write(bytes);
		if (ObjectUtils.isNotEmpty(bos)) {
			bos.close();
		}
		if (ObjectUtils.isNotEmpty(fos)) {
			fos.close();
		}
	}

	/**
	 * @Title: getBase64ByInputStream
	 * @Description: 将inputStream 转Base64字符串
	 * @Author xiaohe
	 * @DateTime 2019年10月14日 下午4:43:03
	 * @param inputStream
	 * @return
	 */
	public static String getBase64ByInputStream(InputStream inputStream) throws IOException {
		byte[] data = null;
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int index = 0;
		while ((index = inputStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, index);
		}
		data = swapStream.toByteArray();
		if (ObjectUtils.isNotEmpty(inputStream)) {
			inputStream.close();
		}
		return new String(Base64.getEncoder().encode(data));
	}

	/**
	 * @Title: encodeBase64File
	 * @Description: 将文件转BASE64
	 * @Author xiaohe
	 * @DateTime 2019年10月14日 下午8:46:20
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String fileToEncodeBase64(File file) throws IOException {
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new String(Base64.getEncoder().encode(buffer));
	}

	/**
	 * @Title: encodeBase64File
	 * @Description: 拷贝文件
	 * @Author xiaohe
	 * @DateTime 2019年11月27日 下午8:46:20
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void NioToCopyFile(String source, String destin) throws Exception {
		File sourcePath = new File(source);
		File destinPath = new File(destin);
		// 如果源文件不存在就不用赋值
		if (sourcePath.exists()) {
			// 目标目录是否存在、不存在就创建
			if (!destinPath.exists()) {
				destinPath.mkdirs();
			}
			FileChannel in = null;
			FileChannel out = null;
			// 如果是文件就拷贝文件
			if (sourcePath.isFile()) {
				try {
					in = new FileInputStream(sourcePath).getChannel();
					String xxx=destinPath + File.separator + sourcePath.getName();
					FileOutputStream outs=new FileOutputStream(destinPath + File.separator + sourcePath.getName());
					System.out.println(xxx);
					System.out.println(outs);
					out = new FileOutputStream(destinPath + File.separator + sourcePath.getName()).getChannel();
					// 连接两个通道，并从in通道读取，写入out中
					in.transferTo(0, in.size(), out);
				} catch (Exception e) {
					throw e;
				} finally {
					in.close();
					out.close();
				}
			}
			// 如果是文件夹循环
			else if (sourcePath.isDirectory()) {
				// 源文件路径下的所有文件和文件夹
				File[] items = sourcePath.listFiles();
				for (File file : items) {
					// 判断是不是文件
					if (file.isFile()) {
						try {
							in = new FileInputStream(file).getChannel();
							out = new FileOutputStream(destinPath + File.separator + file.getName()).getChannel();
							// 连接两个通道，并从in通道读取，写入out中
							in.transferTo(0, in.size(), out);
						} catch (Exception e) {
							throw e;
						} finally {
							in.close();
							out.close();
						}
					} else {
						NioToCopyFile(file.getPath(), destinPath + File.separator + file.getName());
					}
				}
			}
		} else {
			throw new NullPointerException("source is null");
		}
	}

	/**
	 * @Title: moveNioFile
	 * @Description: 移动文件
	 * @Author xiaohe
	 * @DateTime 2019年11月27日 下午8:46:20
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @throws Exception
	 */
	public static void moveNioFile(String source, String destin) throws Exception {
		NioToCopyFile(source, destin);
		delAllFile(source);
	}

	/**
	 * @Title: decompression
	 * @Description: 解压压缩包文件
	 * @Author xiaohe
	 * @DateTime 2019年12月10日 下午8:46:20
	 * @param InputStream   接收的文件流
	 * @param targetPathStr 解压目录(D:/xxx)
	 * @throws IOException
	 */
	public static void decompression(InputStream file, String targetPathStr) throws IOException {
		ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new BufferedInputStream(file));
		Files.createDirectories(Paths.get(targetPathStr));
		ZipArchiveEntry entry = null;
		while ((entry = inputStream.getNextZipEntry()) != null) {
			if (entry.isDirectory()) {
				Files.createDirectories(Paths.get(targetPathStr, entry.getName()));
			} else {
				OutputStream outputStream = null;
				try {
					outputStream = new BufferedOutputStream(
							new FileOutputStream(new File(targetPathStr, entry.getName())));
					// 输出文件路径信息
					IOUtils.copy(inputStream, outputStream);
				} finally {
					IOUtils.closeQuietly(outputStream);
				}
			}
		}
	}

	@Test
	public void moveNioFileTest() {
		try {
			moveNioFile(
					"D:\\14S_GJK_GIT\\gjk\\gjk\\component\\admin\\1219\\20191220141933\\平台文件\\构件框架库_6.0\\func4 - 副本.c",
					"D:\\14S_GJK_GIT\\gjk\\gjk\\component\\admin\\1219\\20191220141933\\平台文件\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
