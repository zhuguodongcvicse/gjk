package com.inforbus.gjk.dataCenter.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.constant.DataCenterConstants;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * 数据中心对外接口
 * 
 * @author dong
 *
 */
@RestController
@RequestMapping("/gjkInterface")
public class GjkInterface {

	/**
	 * 通过类型及具体的操作指令来进行查询操作
	 *
	 * @param command 请求命令
	 * @return 查询结果
	 */
	@GetMapping("/{command}")
	public String query(@PathVariable String command) {
		// 返回结果
		String result;

		// 根据类型进行匹配对应的方法
		switch (command) {
		case DataCenterConstants.COMPONENT:
			result = command;
			break;
		case DataCenterConstants.PROJECT:
			result = command;
			break;
		default:
			result = "无此指令11111";
		}

		return result;
	}

	/**
	 * 通过类型及具体的操作指令来进行创建操作
	 *
	 * @param command 请求命令
	 * @return
	 */
	@PostMapping("/{command}")
	public String create(@PathVariable String command) {
		// 返回结果
		String result;

		// 根据类型进行匹配对应的方法
		switch (command) {
		case DataCenterConstants.COMPONENT:
			result = command;
			break;
		case DataCenterConstants.PROJECT:
			result = command;
			break;
		default:
			result = "无此指令";
		}

		return result;
	}

	/**
	 * 通过类型及具体的操作指令来进行修改操作
	 *
	 * @param command 请求命令
	 * @return
	 */
	@PutMapping("/{command}")
	public String modify(@PathVariable String command) {
		// 返回结果
		String result;

		// 根据类型进行匹配对应的方法
		switch (command) {
		case DataCenterConstants.COMPONENT:
			result = command;
			break;
		case DataCenterConstants.PROJECT:
			result = command;
			break;
		default:
			result = "无此指令";
		}

		return result;
	}

	/**
	 * 通过类型及具体的操作指令来进行删除操作
	 *
	 * @param command 请求命令
	 * @return
	 */
	@DeleteMapping("/{command}")
	public String delete(@PathVariable String command) {
		// 返回结果
		String result;

		// 根据类型进行匹配对应的方法
		switch (command) {
		case DataCenterConstants.COMPONENT:
			result = command;
			break;
		case DataCenterConstants.PROJECT:
			result = command;
			break;
		default:
			result = "无此指令";
		}

		return result;
	}

	/**
	 * 文件上传接口
	 * 
	 * @param fileName
	 * @param 文件路径
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@HystrixCommand(fallbackMethod = "uploadFallback")
	public String upload(@RequestParam("file") MultipartFile files, String filePath) {
		if (files != null) { // 表示现在已经有文件上传了
			System.out.println(
					"【*** UploadRest ***】文件名称：" + files.getOriginalFilename() + "、文件大小：" + files.getSize());
		}
		
		String result = "0";
		//for (MultipartFile file :list) {
		// 获取文件名
		String fileName = files.getOriginalFilename();
		
		
		// 判断文件路径是否存在,不存在则创建
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		// 组装保存的路径
		String fPath = filePath+fileName;
		file = new File(fPath);
		
		// 通过流的方式控制文件上传，防止数据量大导致异常
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = files.getInputStream();
			outputStream = new FileOutputStream(file);
			
			byte[] bs = new byte[1024];
			int res = 0;
			while ((res = inputStream.read(bs)) != -1) {
				outputStream.write(bs, 0, res);
			}
		} catch (IOException e) {
			 result = "1";
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
			}
		}
		//}
		return result;
	}

	public String uploadFallback(@RequestParam("photo") MultipartFile photo) {
		return "nophoto.jpg";
	}

	/**
	 * 文件（二进制数据）下载
	 * 
	 * @param filePath 文件路径 ，可从数据接口中获取
	 * @return
	 */
	@RequestMapping("/downloadFile")
	public ResponseEntity<byte[]> downloadFile(String filePath, HttpServletRequest request) {

		System.out.println(request.getParameter("filePath"));
		System.out.println("参数filePath: " + filePath);

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
