package com.inforbus.gjk.dataCenter.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.dataCenter.api.entity.FileCenter;
import com.inforbus.gjk.dataCenter.service.FileService;

/**
 * @ClassName: FileController
 * @Desc
 * @Author cvics
 * @DateTime 2020年4月1日
 */
@RestController
@RequestMapping("/fileServe")
public class FileController {
	@Autowired
	private FileService fileService;
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	/**
	 * @Title: uploadLocalFile
	 * @Desc 多文件上传
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 保存的本地文件路劲
	 * @param localFile 要保存的文件列表
	 * @return
	 */
	@PostMapping("uploadFile")
	public R<Boolean> uploadLocalFile(@RequestParam("localPath") String localPath,
			@RequestParam("localFile") List<FileCenter> localFile) {
		R<Boolean> ret = new R<Boolean>();
		try {
			if (fileService.uploadLocalFile(localPath, localFile)) {
				ret.setData(true);
				ret.setMsg("上传文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("上传文件失败");
			}
		} catch (Exception e) {
			logger.error("多文件上传", e);
			return new R<Boolean>(e);
		}
		return ret;

	};

	/**
	 * @Title: downloadFile
	 * @Desc 下载文件
	 * @Author cvics
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件/文件夹路径
	 * @param localPath  保存文件/文件夹根路径
	 * @param fileName   文件名字
	 */
	@PostMapping("downloadFile")
	public R<Boolean> downloadFile(@RequestParam("sourcePath") String sourcePath,
			@RequestParam("localPath") String localPath, @RequestParam("fileName") String fileName) {
		R<Boolean> ret = new R<Boolean>();
		try {
			if (fileService.downloadFile(sourcePath, localPath, fileName)) {
				ret.setData(true);
				ret.setMsg("下载文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("下载文件失败");
			}
		} catch (Exception e) {
			logger.error("下载文件上传", e);
			return new R<Boolean>(e);
		}
		return ret;
	};

	/**
	 * @Title: downloadFile
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年4月2日
	 * @param sourcePath
	 * @param localPath
	 * @param fileName
	 * @return
	 */
	@PostMapping("downloadLocalFile")
	public R<List<FileCenter>> downloadFile(@RequestParam("sourcePath") String sourcePath) {
		R<List<FileCenter>> ret = new R<List<FileCenter>>();
		try {
			List<FileCenter> list = fileService.downloadFile(sourcePath);
			// 判断list是否为空
			if (ObjectUtils.isNotEmpty(list)) {
				ret.setData(list);
				ret.setMsg("下载文件成功");
			} else {
				ret.setCode(1);
				ret.setData(null);
				ret.setMsg("下载文件失败");
			}
		} catch (Exception e) {
			logger.error("下载文件上传", e);
			return new R<>(e);
		}
		return ret;
	};

	/**
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param sourcePath 文件路径
	 * @return
	 */
	@PostMapping("delAllFile")
	public R<Object> delAllFile(@RequestParam("sourcePath") String sourcePath) {
//		return fileService.delAllFile(sourcePath);
		R<Object> ret = new R<Object>();
		try {
			if (fileService.delAllFile(sourcePath)) {
				ret.setData(true);
				ret.setMsg("删除指定文件夹下的所有文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("删除指定文件夹下的所有文件失败");
			}
		} catch (Exception e) {
			logger.error("删除指定文件夹下的所有文件", e);
			return new R<Object>(e);
		}
		return ret;
	}

	/**
	 * @Title: delFolder
	 * @Description: 删除文件夹
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param folderPath 文件夹路径
	 */
	@PostMapping("delFolder")
	public R<Object> delFolder(@RequestParam("folderPath") String folderPath) {
		R<Object> ret = new R<Object>();
		try {
			if (fileService.delFolder(folderPath)) {
				ret.setData(true);
				ret.setMsg("删除文件夹成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("删除文件夹失败");
			}
		} catch (Exception e) {
			logger.error("删除指定文件夹", e);
			return new R<Object>(e);
		}
		return ret;
	};

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
	@PostMapping("copylocalFile")
	public R<Boolean> copylocalFile(@RequestParam("source") String source, @RequestParam("destin") String destin) {
		R<Boolean> ret = new R<Boolean>();
		try {
			fileService.copylocalFile(source, destin);
			ret.setData(true);
			ret.setMsg("拷贝文件成功");
		} catch (Exception e) {
			logger.error("删除指定文件夹", e);
			return new R<Boolean>(e);
		}
		return ret;
	};

	/**
	 * @Title: readFile
	 * @Desc 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 * @param localPath 文件路径
	 * @param charset   编码格式
	 * @return
	 */
	@PostMapping("readFile")
	public R<Map<String, String>> readFile(@RequestParam("localPath") String localPath) {
		R<Map<String, String>> ret = new R<Map<String, String>>();
		try {
			Map<String, String> data = fileService.readFile(localPath);
			if (ObjectUtils.isNotEmpty(data)) {
				ret.setData(data);
				ret.setMsg("读取文件成功");
			} else {
				ret.setCode(1);
				ret.setData(null);
				ret.setMsg("读取文件失败");
			}
		} catch (Exception e) {
			logger.error("读取文件", e);
			return new R<Map<String, String>>(e);
		}
		return ret;
	};

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
	@PostMapping("writeFile")
	public R<Boolean> writeFile(@RequestParam("localPath") String localPath, @RequestParam("charset") String charset,
			@RequestParam("textContext") String textContext) {
		R<Boolean> ret = new R<Boolean>();
		try {
			if (fileService.writeFile(localPath, charset, textContext)) {
				ret.setData(true);
				ret.setMsg("写入文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("写入文件失败");
			}
		} catch (Exception e) {
			logger.error("写入文件", e);
			return new R<Boolean>(e);
		}
		return ret;
	};

	/**
	 * @Title: uploadLocalFile
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年4月13日
	 * @param ufile
	 * @param type
	 * @return
	 */
	@PostMapping(value = "/uploadMultipartFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R uploadLocalFile(@RequestPart(value = "files") MultipartFile ufile, HttpServletResponse response) {
		// 临时变量
		MultipartFile[] ufiles = { ufile };
		R<FileEntity> ret = new R<FileEntity>();
		try {
			List<FileCenter> fileCenters = fileService.uploadLocalFile(ufiles, "aaa/bbb");
			if (fileCenters.size() > 0) {
				ret.setCode(CommonConstants.SUCCESS);
				ret.setData(null);
				ret.setMsg("上传文件成功。。。");
			} else {
				ret.setCode(CommonConstants.FAIL);
				ret.setData(null);
				ret.setMsg("上传文件失败。。。");
			}
		} catch (Exception e) {
			logger.error("上传文件失败", e);
			ret = new R<>(e);
		}
		return ret;
	}

	@PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String handleFileUpload(@RequestPart(value = "file") MultipartFile file) {
		logger.error("上传文件成功", file);
		return file.getOriginalFilename();
	}

	/**
	 * @Title: downloadFile
	 * @Desc 下载文件返回流
	 * @Author xiaohe
	 * @DateTime 2020年4月15日
	 * @param filePaths 文件全路径
	 * @param response 返回zip流
	 */
	@ResponseBody
	@PostMapping(value = "/downloadStreamFiles")
	public void downloadFile(@RequestParam("filePaths") String[] filePaths, HttpServletResponse response) {
		InputStream in = null;
		try {
			ByteArrayOutputStream zps = UploadFilesUtils.toZip(filePaths);
			OutputStream out = response.getOutputStream();
			out.write(zps.toByteArray());
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
	}

	/**
	 * @Title: downloadFile
	 * @Desc 下载文件返回流
	 * @Author cvics
	 * @DateTime 2020年4月15日
	 * @param filePath
	 * @param response 
	 */
	@ResponseBody
	@PostMapping(value = "/downloadStreamFile")
	public void downloadFile(@RequestParam("filePath") String filePath, HttpServletResponse response) {
		File file = new File(filePath);
		InputStream in = null;
		if (file.exists()) {
			try {
				OutputStream out = response.getOutputStream();
				in = new FileInputStream(file);
				byte buffer[] = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) >= 0) {
					out.write(buffer, 0, length);
				}
			} catch (IOException e) {
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
		}
	}

}
