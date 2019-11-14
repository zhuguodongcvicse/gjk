package com.inforbus.gjk.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * 用于测试对外接口
 * 
 * @author dong
 *
 */
@RestController
@RequestMapping("/interface")
public class InferfaceTest {

	/**
	 * 通过ID查询字典信息
	 *
	 * @param id ID
	 */
	@GetMapping("/{id}")
	public String getById(@PathVariable Integer id) {
		return "hello" + id;
	}

	/**
	 * 文件上传接口
	 * 
	 * @param photo
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@HystrixCommand(fallbackMethod = "uploadFallback")
	public String upload(@RequestParam("photo") MultipartFile photo) {
		if (photo != null) { // 表示现在已经有文件上传了
			System.out.println("【*** UploadRest ***】文件名称：" + photo.getOriginalFilename() + "、文件大小：" + photo.getSize());
		}
		return "mldn-file-" + System.currentTimeMillis() + ".jpg";
	}

	public String uploadFallback(@RequestParam("photo") MultipartFile photo) {
		return "nophoto.jpg";
	}

	/**
	 * 文件（二进制数据）下载
	 * 
	 * @param fileType 文件类型
	 * @return
	 */
	@RequestMapping("/downloadFile")
	public ResponseEntity<byte[]> downloadFile(String fileType, HttpServletRequest request) {

		System.out.println(request.getParameter("fileType"));
		System.out.println("参数fileType: " + fileType);

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> entity = null;
		InputStream in = null;
		try {
			in = new FileInputStream(new File("E:/book1.xml"));

			byte[] bytes = new byte[in.available()];

			String imageName = "001.png";

			// 处理IE下载文件的中文名称乱码的问题
			String header = request.getHeader("User-Agent").toUpperCase();
			if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
				imageName = URLEncoder.encode(imageName, "utf-8");
				imageName = imageName.replace("+", "%20"); // IE下载文件名空格变+号问题
			} else {
				imageName = new String(imageName.getBytes(), "iso-8859-1");
			}

			in.read(bytes);

			headers.add("Content-Disposition", "attachment;filename=" + imageName);

			entity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return entity;
	}

}
