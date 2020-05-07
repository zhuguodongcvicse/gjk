package com.inforbus.gjk.dataCenter.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import org.slf4j.Logger;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.FileEntity;
import org.slf4j.LoggerFactory;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.api.dto.ThreeLibsFilePathDTO;
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
@SuppressWarnings("unchecked")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	/**
	 * @param localPath 保存的本地文件路劲
	 * @param localFile 要保存的文件列表
	 * @return
	 * @Title: uploadLocalFile
	 * @Desc 多文件上传
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
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

	}

	/**
	 * @param sourcePath 文件/文件夹路径
	 * @param localPath  保存文件/文件夹根路径
	 * @param fileName   文件名字
	 * @Title: downloadFile
	 * @Desc 下载文件
	 * @Author cvics
	 * @DateTime 2020年4月1日
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
	}

	;

	/**
	 * @param sourcePath
	 * @param localPath
	 * @param fileName
	 * @return
	 * @Title: downloadFile
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年4月2日
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
	}

	;

	/**
	 * @param sourcePath 文件路径
	 * @return
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 */
	@PostMapping("delAllFile")
	public R<Boolean> delAllFile(@RequestParam("sourcePath") String sourcePath) {
		R<Boolean> ret = new R<Boolean>();
		try {
			if (fileService.delAllFile(sourcePath)) {
				ret.setData(true);
				ret.setMsg("删除指定文件夹下的所有文件成功");
			} else {
				ret.setCode(CommonConstants.FAIL);
				ret.setData(false);
				ret.setMsg("删除指定文件夹下的所有文件失败");
			}
		} catch (Exception e) {
			logger.error("删除指定文件夹下的所有文件", e);
			ret.setCode(CommonConstants.FAIL);
			ret.setData(false);
			ret.setMsg("删除指定文件夹下的所有文件失败");
		}
		return ret;
	}

	/**
	 * @param folderPath 文件夹路径
	 * @Title: delFolder
	 * @Description: 删除文件夹
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
	 */
	@PostMapping("delFolder")
	public R<Boolean> delFolder(@RequestParam("folderPath") String[] folderPath) {
		R<Boolean> ret = new R<Boolean>();
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
			return new R<Boolean>(e);
		}
		return ret;
	}

	/**
	 * @Author wang
	 * @Description: 根据绝对路径删除文件
	 * @Param: [absolutePath] 文件的绝对路径
	 * @Return: com.inforbus.gjk.common.core.util.R
	 * @Create: 2020/4/15
	 */
	@PostMapping("/delFile")
	public R<Boolean> delFile(@RequestParam("absolutePath") String absolutePath) {
		R<Boolean> ret = new R();
		try {
			if (fileService.delFile(absolutePath)) {
				ret.setData(true);
				ret.setMsg("删除文件成功");
			} else {
				ret.setCode(CommonConstants.FAIL);
				ret.setData(false);
				ret.setMsg("删除文件失败");
			}
		} catch (Exception e) {
			logger.error("删除指定文件失败", e);
			return new R(e);
		}
		return ret;
	}

	/**
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @return
	 * @throws Exception
	 * @Title: copylocalFile
	 * @Description: 拷贝文件
	 * @Author xiaohe
	 * @DateTime 2019年11月27日 下午8:46:20
	 */
	@PostMapping("copylocalFile")
	public R<Boolean> copylocalFile(@RequestParam("source") String source, @RequestParam("destin") String destin) {
		R<Boolean> ret = new R<Boolean>();
		try {
			fileService.copylocalFile(source, destin);
			ret.setData(true);
			ret.setMsg("拷贝文件成功");
		} catch (Exception e) {
			logger.error("文件拷贝失败", e);
			ret.setCode(CommonConstants.FAIL);
			ret.setData(false);
			ret.setMsg("文件拷贝失败");
		}
		return ret;
	}

	/**
	 * @param localPath 文件路径
	 * @param charset   编码格式
	 * @return
	 * @Title: readFile
	 * @Desc 读取 txt.doc,docx,.h .m .c .o xlsx xls文件内容
	 * @Author xiaohe
	 * @DateTime 2020年4月1日
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
	}

	/**
	 * @param localPath   文件路径
	 * @param charset     编码格式
	 * @param textContext 文件内容
	 * @return
	 * @Title: writeFile
	 * @Desc 编辑文件 更改文件内容
	 * @Author cvics
	 * @DateTime 2020年4月1日
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
	}

	/**
	 * @Title: uploadLocalFile
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年4月13日
	 * @param ufile
	 * @param type
	 * @return
	 */
	@PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R uploadLocalFile(@RequestPart(value = "file") MultipartFile ufile, HttpServletResponse response) {
		// 临时变量
		MultipartFile[] ufiles = { ufile };
		R<FileEntity> ret = new R<FileEntity>();
		try {
			List<FileCenter> fileCenters = Lists.newArrayList();
//			List<FileCenter> fileCenters = fileService.uploadLocalFile(ufiles, "aaa/bbb");
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
			ret.setException(e);
			logger.error("上传文件失败。。", e.getMessage());
		}
		return ret;
	}

	/**
	 * @Author wang
	 * @Description: 解析xml文件为xmlEntity对象
	 * @Param: [localPath] 文件的绝对路径
	 * @Return: boolean
	 * @Create: 2020/4/13
	 */
	@PostMapping("/analysisXmlFile")
	public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath) {
		R<XmlEntityMap> ret = new R();
		try {
			XmlEntityMap xmlEntityMap = fileService.analysisXmlFileToXMLEntityMap(localPath);
			ret.setData(xmlEntityMap);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			ret.setCode(CommonConstants.FAIL);
			ret.setMsg(e.getMessage());
			ret.setData(null);
		}
		return ret;
	}

	/**
	 * @Author wang
	 * @Description: 在指定位置生成xml文件
	 * @Param: [xMlEntityMapVO]
	 * @Return: boolean
	 * @Create: 2020/4/14
	 */
	@PostMapping("/createXMLFile")
	public R<Boolean> createXMLFile(@RequestBody XMlEntityMapVO xMlEntityMapVO) {
		logger.debug("createXMLFile方法开始运行！");
		R<Boolean> r = new R();
		boolean flag = false;
		try {
			flag = fileService.createXMLFile(xMlEntityMapVO);
			r.setData(flag);
			r.setMsg("xml文件生成成功!");
		} catch (Exception e) {
			r.setCode(CommonConstants.FAIL);
			r.setData(flag);
			r.setMsg("xml文件生成失败！");
			logger.error("xml文件生成失败！");
		}
		logger.debug("createXMLFile方法运结束！");
		return r;
	}

	/**
	 * 程序文本编辑器的文件展示
	 * 
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	@PostMapping("readFiles")
	public R fileReads(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		return fileService.fileReads(threeLibsFilePathDTO);
	}

	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 * 
	 * @param filePath    文件路径
	 * @param textContext 文本内容
	 */
	@PostMapping("saveFileContext")
	public void saveFileContext(@RequestParam("filePath") String filePath,
			@RequestParam("textContext") String textContext) {
		fileService.saveFileContext(filePath, textContext);
	}

	/**
	 * @Title: handleFileUpload
	 * @Desc 单文件上传（MultipartFile）
	 * @Author xiaohe
	 * @DateTime 2020年4月17日
	 * @param file      MultipartFile文件
	 * @param localPath 要上传的全文件路径
	 * @return
	 */
	@PostMapping(value = "/uploadMultipartFile", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R handleFileUpload(@RequestPart(value = "file") MultipartFile file,
			@RequestParam("filePath") String localPath) {
		R<Object> ret = new R<>();
		MultipartFile[] files = { file };
		try {
			boolean vo = fileService.getUploadFilePaths(files, localPath);
			if (vo) {
				ret.setCode(CommonConstants.SUCCESS);
				ret.setData(vo);
				ret.setMsg("文件上传成功");
				logger.info("上传文件成功", file);
			} else {
				ret.setCode(CommonConstants.FAIL);
				ret.setData(vo);
				ret.setMsg("文件上传失败");
				logger.info("上传文件失败", file);
			}

		} catch (IllegalStateException e) {
			logger.error("文件上传失败", e);
			ret.setException(e);
		} catch (IOException e) {
			logger.error("文件上传失败", e);
			ret.setException(e);
		}
		return ret;
	}

	/**
	 * @Title: uploadLocalFiles
	 * @Desc 多文件上传 MultipartFile[]
	 * @Author xiaohe
	 * @DateTime 2020年4月17日
	 * @param ufile     MultipartFile[] 文件数组
	 * @param localPath 要上传的全文件路径
	 * @return
	 */
	@PostMapping(value = "/uploadMultipartFiles", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> uploadLocalFiles(@RequestPart(value = "file") MultipartFile[] ufile,
			@RequestParam("filePath") String localPath) {
		R<Object> ret = new R<>();
		try {
			boolean vo = fileService.getUploadFilePaths(ufile, localPath);
			if (vo) {
				ret.setCode(CommonConstants.SUCCESS);
				ret.setData(vo);
				ret.setMsg("多文件上传成功");
				logger.info("多文件上传 file-service");
			} else {
				ret.setCode(CommonConstants.FAIL);
				ret.setData(vo);
				ret.setMsg("多文件上传失败");
				logger.info("多上传文件失败");
			}
		} catch (IllegalStateException e) {
			logger.error("多文件上传失败", e);
			ret.setException(e);
		} catch (IOException e) {
			logger.error("多文件上传失败", e);
			ret.setException(e);
		}
		return ret;

	}

	/**
	 * @Title: downloadFile
	 * @Desc 下载多文件返回流
	 * @Author xiaohe
	 * @DateTime 2020年4月15日
	 * @param filePaths 文件全路径
	 * @param response  返回zip流
	 */
	@ResponseBody
	@PostMapping(value = "/downloadStreamFiles")
	public void downloadFile(@RequestParam("filePaths") String[] filePaths, HttpServletResponse response) {
		logger.debug("多文件下载开始。。。。");
		InputStream in = null;
		try {
			ByteArrayOutputStream zps = UploadFilesUtils.toZip(filePaths);
			OutputStream out = response.getOutputStream();
			out.write(zps.toByteArray());
		} catch (Exception e) {
			logger.error("多文件下载异常", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("文件流关闭异常", e);
				}
			}
			logger.debug("多文件下载结束。。。。");
		}
	}

	/**
	 * @Title: downloadFile
	 * @Desc 下载单文件返回流
	 * @Author xiaohe
	 * @DateTime 2020年4月15日
	 * @param filePath 文件全路径
	 * @param response
	 */
	@ResponseBody
	@PostMapping(value = "/downloadStreamFile")
	public void downloadFile(@RequestParam("filePath") String filePath, HttpServletResponse response) {
		logger.debug("文件下载开始。。。。", filePath);
		File file = new File(filePath);
		InputStream in = null;
		if (file.exists()) {
			try {
				OutputStream out = response.getOutputStream();
				in = new FileInputStream(file);
				// 1024 * 1024 * 10==>byte设置缓冲区大小
				byte buffer[] = new byte[1024 * 1024 * 10];
				int length = 0;
				while ((length = in.read(buffer)) >= 0) {
					out.write(buffer, 0, length);
				}
			} catch (IOException e) {
				logger.error("文件下载异常", e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						logger.error("文件流关闭异常", e);
					}
				}
				logger.debug("文件下载结束。。。。", filePath);
			}
		}
	}

}
