package com.inforbus.gjk.dataCenter.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inforbus.gjk.common.core.util.R;
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
	@Autowired
	private FileService fileService;

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
	public R<Object> uploadLocalFile(@RequestParam("localPath") String localPath,
			@RequestParam("localFile") List<InputStream> localFile) {
		R<Object> ret = new R<Object>();
		try {
			if (fileService.uploadLocalFile(localPath, localFile)) {
				ret.setData(true);
				ret.setMsg("上传文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("上传文件失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new R<Object>(e);
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
	public R<Object> downloadFile(@RequestParam("sourcePath") String sourcePath,
			@RequestParam("localPath") String localPath, @RequestParam("fileName") String fileName) {
		R<Object> ret = new R<Object>();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
	public R<Object> copylocalFile(@RequestParam("source") String source, @RequestParam("destin") String destin) {
		R<Object> ret = new R<Object>();
		try {
			fileService.copylocalFile(source, destin);
			ret.setData(true);
			ret.setMsg("拷贝文件成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new R<Object>(e);
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
	public R<Object> readFile(@RequestParam("localPath") String localPath, @RequestParam("charset") String charset) {
		R<Object> ret = new R<Object>();
		try {
			String data = fileService.readFile(localPath, charset);
			if (StringUtils.isNotEmpty(data)) {
				ret.setData(data);
				ret.setMsg("上传文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("上传文件失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new R<Object>(e);
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
	public R<Object> writeFile(@RequestParam("localPath") String localPath, @RequestParam("charset") String charset,
			@RequestParam("textContext") String textContext) {
		R<Object> ret = new R<Object>();
		try {
			if (fileService.writeFile(localPath, charset, textContext)) {
				ret.setData(true);
				ret.setMsg("上传文件成功");
			} else {
				ret.setCode(1);
				ret.setData(false);
				ret.setMsg("编辑文件失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new R<Object>(e);
		}
		return ret;

	};
}
