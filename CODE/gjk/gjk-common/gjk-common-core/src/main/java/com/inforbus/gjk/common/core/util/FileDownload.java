package com.inforbus.gjk.common.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.inforbus.gjk.common.core.entity.ZipCompressorByAnt;

public class FileDownload {
	public static void zipwordDownAction(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, String> map) throws Exception {

		// 打包文件的存放路径
		ZipCompressorByAnt zc = new ZipCompressorByAnt(map.get("downFilePath") + "/file.zip");
		// 如果原压缩包存在,则删除
		File file = new File(map.get("downFilePath") + "/file.zip");
		if (file.exists()) {
			file.delete();
		}
		// 需要打包的文件路径（需要改）
		String filePaths = "D:\\sql\\20190621";
		// 需要打包的文件路径
		zc.compress(map.get("oriFilePath"));
		String contentType = "application/octet-stream";
		try {
			// 导出压缩包
			download(request, response, "upload/file.zip", contentType,
					encodeChineseDownloadFileName(request, "file.zip"));
		} catch (Exception e) {
			request.getSession().setAttribute("msg", "暂无内容");
		}
	}

	/**
	 * 下载文件
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String storeName,
			String contentType, String realName) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

//		String ctxPath = FileUtil.class.getResource("/").getPath().split("WEB-INF")[0];
		String ctxPath = "D:\\sql\\eeee\\";
		String downLoadPath = ctxPath + storeName;

		long fileLength = new File(downLoadPath).length();

		response.setContentType(contentType);
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(realName.getBytes("utf-8"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
	 */
	public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName)
			throws UnsupportedEncodingException {
		String filename = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent) {
			if (-1 != agent.indexOf("Firefox")) {// Firefox
				filename = "=?UTF-8?B?"
						+ (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
						+ "?=";
			} else if (-1 != agent.indexOf("Chrome")) {// Chrome
				filename = new String(pFileName.getBytes(), "ISO8859-1");
			} else {// IE7+
				filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
				filename = StringUtils.replace(filename, "+", "%20");// 替换空格
			}
		} else {
			filename = pFileName;
		}
		return filename;
	}

}
