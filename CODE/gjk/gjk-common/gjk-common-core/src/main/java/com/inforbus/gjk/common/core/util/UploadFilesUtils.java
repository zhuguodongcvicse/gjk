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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.jgit.JGitUtil;

/**
 * @ClassName: UploadFilesUtils
 * @Description: 鏂囦欢涓婁紶宸ュ叿
 * @Author xiaohe
 * @DateTime 2019骞�5鏈�13鏃� 涓婂崍10:55:53
 */
public class UploadFilesUtils {

	/**
	 * @Title: createFile
	 * @Description: 鍒涘缓鏂囦欢鍙婃枃浠跺す
	 * @Author xiaohe
	 * @DateTime 2019骞�5鏈�13鏃� 涓嬪崍3:14:51
	 * @param filePath 鏂囦欢鍚�
	 * @return File()
	 * @throws IOException 鏂囦欢鐩綍鎴栫洰鏍囨枃浠跺垱寤哄け璐�
	 */
	public static File createFile(String filePath) throws IOException {
		// 鑾峰彇鏂囦欢鐨勫畬鏁寸洰褰�
//		String fileDir = "D:/upload/files";
		File file = null;
		// 鍒ゆ柇鐩綍鏄惁瀛樺湪锛屼笉瀛樺湪灏卞垱寤轰竴涓洰褰�
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
	 * @Description: 鍗曟枃浠朵笂浼�
	 * @Author xiaohe
	 * @DateTime 2019骞�5鏈�13鏃� 涓嬪崍3:12:45
	 * @param file
	 * @return
	 */
	public static String getUploadFilesUrl(MultipartFile file) {
		String url = new String();
		File uploadFile = null;
		try {
			// 鑾峰彇涓婁紶鏂囦欢鍚�,鍖呭惈鍚庣紑
			String originalFilename = file.getOriginalFilename();
			// 涓婁紶"image/"

			url = new String("gjk/upload/" + originalFilename).replace("\\", "/");
			uploadFile = createFile(url);
			// 灏嗕笂浼犳枃浠朵繚瀛樺埌璺緞
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
	 * @Description: 涓婁紶鍥剧墖
	 * @Author xiaohe
	 * @DateTime 2019骞�5鏈�28鏃� 涓嬪崍2:32:10
	 * @param file
	 * @return
	 */
	public static String getUploadImgUrl(MultipartFile file) {
		String url = "";
		try {
			// 鑾峰彇涓婁紶鏂囦欢鍚�,鍖呭惈鍚庣紑
			String originalFilename = file.getOriginalFilename();
			url = new String("./gjk/image/" + originalFilename);
			// 涓婁紶
//			File uploadFile = createFile(originalFilename);
			File uploadFile = createFile(url);
			// 灏嗕笂浼犳枃浠朵繚瀛樺埌璺緞
			if (uploadFile.exists()) {
				uploadFile.delete();
			}
			file.transferTo(uploadFile);
			JGitUtil.commitAndPush(url, "涓婁紶鏋勪欢鍥剧墖img");
			url = new String(url).replace("\\", "/");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * @Title: getUploadFiles 澶氭枃浠朵笂浼�
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019骞�7鏈�9鏃� 涓嬪崍4:24:11
	 * @param file
	 * @return
	 */
	public static List<Map<String, String>> getUploadFilesUrl(MultipartFile[] files, Map<String, String> mapPath) {
		String url = "";
		List<Map<String, String>> listMaps = Lists.newArrayList();
		try {
			for (MultipartFile file : files) {
				Map<String, String> map = Maps.newHashMap();
				// 鑾峰彇涓婁紶鏂囦欢鍚�,鍖呭惈鍚庣紑
				String fileName = file.getOriginalFilename();
				url = new String(mapPath.get("path") + File.separator + fileName);
				File uploadFile = createFile(url);
				// 灏嗕笂浼犳枃浠朵繚瀛樺埌璺緞
				if (uploadFile.exists()) {
					uploadFile.delete();
				}
				file.transferTo(uploadFile);
				JGitUtil.commitAndPush(url, "澶氫釜鏂囦欢涓婁紶");
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
	 * @Description: 鍒犻櫎鎸囧畾鏂囦欢澶逛笅鐨勬墍鏈夋枃浠�
	 * @Author xiaohe
	 * @DateTime 2019骞�9鏈�29鏃� 涓嬪崍5:13:56
	 * @param 鏂囦欢澶硅矾寰�
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
//				delAllFile(path + "/" + tempList[i]);// 鍏堝垹闄ゆ枃浠跺す閲岄潰鐨勬枃浠�
				delFolder(path + "/" + tempList[i]);// 鍐嶅垹闄ょ┖鏂囦欢澶�
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * @Title: delFolder
	 * @Description: 鍒犻櫎鏂囦欢澶�
	 * @Author xiaohe
	 * @DateTime 2019骞�9鏈�29鏃� 涓嬪崍5:14:34
	 * @param 鏂囦欢澶硅矾寰�
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 鍒犻櫎瀹岄噷闈㈡墍鏈夊唴瀹�
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 鍒犻櫎绌烘枃浠跺す
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: base64ToFile
	 * @Description: 灏哹ase64瀛楄妭娴佽浆鏂囦欢
	 * @Author xiaohe
	 * @DateTime 2019骞�10鏈�14鏃� 涓嬪崍3:29:20
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
	 * @Description: 灏唅nputStream 杞珺ase64瀛楃涓�
	 * @Author xiaohe
	 * @DateTime 2019骞�10鏈�14鏃� 涓嬪崍4:43:03
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
	 * @Description: 灏嗘枃浠惰浆BASE64
	 * @Author xiaohe
	 * @DateTime 2019骞�10鏈�14鏃� 涓嬪崍8:46:20
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
	 * @Description: 鎷疯礉鏂囦欢
	 * @Author xiaohe
	 * @DateTime 2019骞�11鏈�27鏃� 涓嬪崍8:46:20
	 * @param source 婧愭枃浠惰矾寰�
	 * @param destin 鎷疯礉鏂囦欢璺緞
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void NioToCopyFile(String source, String destin) throws Exception {
		File sourcePath = new File(source);
		File destinPath = new File(destin);
		// 濡傛灉婧愭枃浠朵笉瀛樺湪灏变笉鐢ㄨ祴鍊�
		if (sourcePath.exists()) {
			// 鐩爣鐩綍鏄惁瀛樺湪銆佷笉瀛樺湪灏卞垱寤�
			if (!destinPath.exists()) {
				destinPath.mkdirs();
			}
			FileChannel in = null;
			FileChannel out = null;
			// 濡傛灉鏄枃浠跺氨鎷疯礉鏂囦欢
			if (sourcePath.isFile()) {
				try {
					in = new FileInputStream(sourcePath).getChannel();
					String xxx = destinPath + File.separator + sourcePath.getName();
					FileOutputStream outs = new FileOutputStream(destinPath + File.separator + sourcePath.getName());
					System.out.println(xxx);
					System.out.println(outs);
					out = new FileOutputStream(destinPath + File.separator + sourcePath.getName()).getChannel();
					// 杩炴帴涓や釜閫氶亾锛屽苟浠巌n閫氶亾璇诲彇锛屽啓鍏ut涓�
					in.transferTo(0, in.size(), out);
				} catch (Exception e) {
					throw e;
				} finally {
					in.close();
					out.close();
				}
			}
			// 濡傛灉鏄枃浠跺す寰幆
			else if (sourcePath.isDirectory()) {
				// 婧愭枃浠惰矾寰勪笅鐨勬墍鏈夋枃浠跺拰鏂囦欢澶�
				File[] items = sourcePath.listFiles();
				for (File file : items) {
					// 鍒ゆ柇鏄笉鏄枃浠�
					if (file.isFile()) {
						try {
							in = new FileInputStream(file).getChannel();
							out = new FileOutputStream(destinPath + File.separator + file.getName()).getChannel();
							// 杩炴帴涓や釜閫氶亾锛屽苟浠巌n閫氶亾璇诲彇锛屽啓鍏ut涓�
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
	 * @Description: 绉诲姩鏂囦欢
	 * @Author xiaohe
	 * @DateTime 2019骞�11鏈�27鏃� 涓嬪崍8:46:20
	 * @param source 婧愭枃浠惰矾寰�
	 * @param destin 鎷疯礉鏂囦欢璺緞
	 * @throws Exception
	 */
	public static void moveNioFile(String source, String destin) throws Exception {
		NioToCopyFile(source, destin);
		delAllFile(source);
	}

	/**
	 * @Title: decompression
	 * @Description: 瑙ｅ帇鍘嬬缉鍖呮枃浠�
	 * @Author xiaohe
	 * @DateTime 2019骞�12鏈�10鏃� 涓嬪崍8:46:20
	 * @param InputStream   鎺ユ敹鐨勬枃浠舵祦
	 * @param targetPathStr 瑙ｅ帇鐩綍(D:/xxx)
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
					// 杈撳嚭鏂囦欢璺緞淇℃伅
					IOUtils.copy(inputStream, outputStream);
				} finally {
					IOUtils.closeQuietly(outputStream);
				}
			}
		}
	}

	/**
	 * 2 澶氭枃浠� 鍘嬬缉鎴� ZIP
	 * 
	 * @Title: toZip
	 * @Desc
	 * @Author xiaohe
	 * @DateTime 2020骞�4鏈�15鏃�
	 * @param filePaths 鏂囦欢璺緞+鍚嶅瓧
	 * @return ByteArrayOutputStream 鏂囦欢娴�
	 * @throws Exception
	 */
	public static ByteArrayOutputStream toZip(String[] filePaths) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zipstream = new ZipOutputStream(outputStream);
		ZipEntry zipEntry = null;
		for (String path : filePaths) {
			File file = createFile(path);
			try {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				zipEntry = new ZipEntry(file.getName());
				zipstream.putNextEntry(zipEntry);
				zipstream.write(FileUtils.readFileToByteArray(file));
				IOUtils.closeQuietly(bis);
				zipstream.flush();
				zipstream.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		IOUtils.closeQuietly(zipstream);
		return outputStream;
	}

	@Test
	public void moveNioFileTest() {
		try {
			moveNioFile(
					"D:\\14S_GJK_GIT\\gjk\\gjk\\component\\admin\\1219\\20191220141933\\骞冲彴鏂囦欢\\鏋勪欢妗嗘灦搴揰6.0\\func4 - 鍓湰.c",
					"D:\\14S_GJK_GIT\\gjk\\gjk\\component\\admin\\1219\\20191220141933\\骞冲彴鏂囦欢\\");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
