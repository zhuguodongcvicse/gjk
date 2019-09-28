package com.inforbus.gjk.common.core.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
	private static String serverPath = JGitUtil.getLOCAL_REPO_PATH();
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
		String path = serverPath + "/" + filePath;
		if (StringUtils.isNotEmpty(path)) {
			file = new File(path);
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
			url = new String(serverPath + File.separator + mapPath.get("path")).replace("\\", "/");
		}
		return listMaps;

	}
}
