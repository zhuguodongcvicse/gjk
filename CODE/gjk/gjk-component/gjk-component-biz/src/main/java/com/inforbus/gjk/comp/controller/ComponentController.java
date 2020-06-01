/*
 *    Copyright (c) 2018-2025, inforbus All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the inforbus.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: inforbus
 */
package com.inforbus.gjk.comp.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.comp.api.dto.CompIdDTO;
import com.inforbus.gjk.comp.api.dto.CompTree;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.feign.RemoteDataCenterService;
import com.inforbus.gjk.comp.api.util.CompTreeUtil;
import com.inforbus.gjk.comp.service.ComponentService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;
import lombok.AllArgsConstructor;

/**
 * 构件开发
 *
 * @author xiaohe
 * @date 2019-04-20 09:26:14
 */

@RestController
@AllArgsConstructor
@RequestMapping("/component")
public class ComponentController {

	private final ComponentService componentService;
	private final RemoteDataCenterService rdcService;

	/**
	 * 简单分页查询
	 * 
	 * @param page      分页对象
	 * @param component 构件开发
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<Component>> getComponentPage(Page<Component> page, Component component) {
		return new R<>(componentService.getComponentPage(page, component));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<Component> getById(@PathVariable("id") String id) {
		Component component = componentService.getById(id);
		if ("1".equals(component.getDelFlag())) {
			return new R<>();
		}
		return new R<>(component);
	}

	/**
	 * 新增记录
	 * 
	 * @param component
	 * @return R
	 */
	@SysLog("新增构件开发")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('comp_component_add')")
	public R save(@RequestBody Component component) {
		return new R<>(componentService.saveComp(component));
	}

	@RequestMapping("/saveComp")
	@PreAuthorize("@pms.hasPermission('comp_component_add')")
	public R saveComp(@RequestBody Component component) {
		return new R<>(componentService.saveComp(component));
	}

	/**
	 * 修改记录
	 * 
	 * @param component
	 * @return R
	 */
	@SysLog("修改构件开发")
	@PutMapping("/modifyComp")
	public R modifyComp(@RequestBody Component component) {
		return new R<>(componentService.updateById(component));
	}

	/**
	 * 修改记录
	 * 
	 * @param component
	 * @return R
	 */
	@SysLog("修改构件开发")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('comp_component_edit')")
	public R update(@RequestBody Component component) {
		return new R<>(componentService.updateById(component));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除构件开发")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('comp_component_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(componentService.deleteCompAndCompDetail(id));
	}

	/**
	 * 返回树形菜单集合
	 *
	 * @return 树形菜单
	 */
	@PutMapping("/compTree/{compId}/{isShowCompXml}")
	public R getTrees(@PathVariable String compId, @PathVariable boolean isShowCompXml) {
		List<CompTree> list = CompTreeUtil.buildTree(componentService.getCompByCompId(compId, isShowCompXml), "-1");
		return new R<>(list);
	}

	/**
	 * @Title: getCompByUserId
	 * @Description: 通过用户编号查询构件
	 * @Author xiaohe
	 * @DateTime 2019年5月5日 下午1:32:00
	 * @param userId 用户编号
	 * @return
	 */
	@SysLog("查询构件")
	@PostMapping("/compList/{userId}")
	public R getCompByUserId(@PathVariable String userId) {
		return new R<>(componentService.getCompAndDetailMap(userId));
	}

	/**
	 * 返回构件树形选择器的树
	 *
	 * @return 树形菜单
	 */
	@PutMapping("/compSelectTree")
	public R getCompSelectTree() {
		List<CompTree> list = componentService.getCompSelectTree();
		return new R<>(list);
	}

	/**
	 * @Title getCompFiles
	 * @Description: 获取构件文件
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 上午11:32:09
	 * @param compId
	 * @return
	 */
	@PostMapping("/compFiles/{compId}")
	public R getCompFiles(@PathVariable String compId) {
		return componentService.getCompFiles(compId);
	}

	/**
	 * @Title getImgFile
	 * @Description: 获取图标文件
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 上午11:32:09
	 * @param compId
	 * @return
	 */
	@GetMapping("/comImg/{imgId}")
	public void getImgFile(@PathVariable String imgId, HttpServletRequest req, HttpServletResponse res) {
		OutputStream os = null;
		InputStream fis = null;
		try {
			fis = componentService.getImgFile(imgId, new StringBuilder());
			os = res.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((count = fis.read(buffer)) != -1) {
				os.write(buffer, 0, count);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis) {
					fis.close();
				}
				if (null != os) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: analysisXmlFile
	 * @Description: 解析构件基础模板
	 * @Author xiaohe
	 * @DateTime 2019年8月19日 下午4:29:07
	 * @return XmlEntityMap
	 * 
	 */
	@PostMapping(path = "/analysisXmlFile", consumes = { "multipart/mixed", "multipart/form-data" })
//	@Cacheable(value = "compDicts{filePath}")
	public R<?> analysisXmlFile(@RequestParam(value = "filePath", required = false) String filePath) {
		System.out.println("filePath" + filePath);
		return componentService.analysisXmlFile(filePath);
	}

	/**
	 * @Title: uploadReturnUrll
	 * @Description: 单文件上传
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:41:10
	 * @param ufile
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/importCompZipUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> appImageUpload(@RequestParam(value = "file", required = false) MultipartFile ufile,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "userName", required = false) String userName) {
		return new R<>(componentService.analysisZipFile(ufile, userId, userName));
	}

	@ResponseBody
	@PostMapping(path = "/checkComp")
	public R checkComp(@RequestBody List<Object> obj) {
		return new R<>(componentService.checkComp(obj));
	}

	/**
	 * 构件入库前的判断 选择的库目录文件是否存在
	 * 
	 * @param compId
	 * @return
	 */
	@GetMapping("/isSelectLibs/{compId}")
	public R isSelectLibs(@PathVariable("compId") String compId) {
		return new R<>(componentService.isSelectLibs(compId));
	}

	@PostMapping("/compByUserId")
	public R listCompByUserId(@RequestParam(value = "userId") Integer userId) {
		return new R<>(componentService.listCompByUserId(userId));
	}

	// upms模块用到的东西，用于展示库管理模块的树级关系
	// 根据libsId获取库文件
	@PostMapping("/getLibsFile/{libsId}")
	public List<ComponentDetail> getLibsFile(@PathVariable("libsId") String libsId) {
		return componentService.getLibsFile(libsId);
	}

	// 根据libsId获取库类型
	@PostMapping("/getLibsFileType")
	public List<ComponentDetail> getLibsFileType(String libsId) {
		return componentService.getLibsFileType(libsId);
	}

	// 根据id获取构件名
	@PostMapping("/getCompNameById/{id}")
	public Component getCompNameById(@PathVariable("id") String id) {
		return componentService.getCompNameById(id);
	}

	// 通过分组compId获取CompIds
	@PostMapping("/getCompIdsGroupCompId")
	public List<String> getCompIdsGroupCompId() {
		return componentService.getCompIdsGroupCompId();
	}

	// 根据构件id、文件名获取构件详细信息
	@PostMapping("/getCompDetailByComponentId/{componentId}/{fileName}")
	public ComponentDetail getCompDetailByComponentId(@PathVariable("componentId") String componentId,
			@PathVariable("fileName") String fileName) {
		return componentService.getCompDetailByComponentId(componentId, fileName);
	}

	// 根据compId获取构件信息
	@PostMapping("/getCompByCompId")
	public List<Component> getCompByCompId(@RequestBody CompIdDTO compIdDTO) {
		return componentService.getCompByCompId(compIdDTO.getCompId());
	}

	/**
	 * Jsplumb 转Gojs 图片样式
	 * 
	 * @return
	 */
	@PostMapping("/jsplumbToGojsImg")
	public R compJsplumbToGojsImg() {
		return componentService.compJsplumbToGojsImg();
	}

	/**
	 * Jsplumb 转Gojs 图片样式
	 *
	 * @return
	 */
	@PostMapping("/gojsToJsplumbImg")
	public R compGojsToJspumbImg() {
		return componentService.compGojsToJspumbImg();
	}

	@PostMapping(value = "/uploadMultipartFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R handleFileUpload(@RequestPart(value = "file") MultipartFile file,
			@RequestParam("filePath") String filePath) {
		return rdcService.uploadLocalFile(file, filePath);
	}

	@PostMapping(value = "/uploadMultipartFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R handleFileUploads(@RequestPart(value = "file") MultipartFile[] file,
			@RequestParam("filePath") String filePath) {
		return rdcService.uploadLocalFiles(file, filePath);
	}

	@PostMapping(value = "/downloadStreamFiles", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R downFile(@RequestParam(value = "file") MultipartFile file) {
		InputStream inputStream = null;
		String[] strArr = new String[2];
//		String[] strArr = files.toArray(new String[files.size()]);
		try {
			Map<String, String> filePaths = Maps.newHashMap();
			filePaths.put("D:\\14S_GJK_GIT\\gjk\\gjk\\common\\component\\xiaohe99\\1.0", "component/xiaohe99/");
			filePaths.put("D:\\14S_GJK_GIT\\gjk\\gjk\\common\\component\\xiaohe99\\2.0", "component/xiaohe99/");
//			Response response = rdcService.downloadStreamFiles(file, "component\\", filePaths);
			// feign文件下载
//			Response response = rdcService.downloadStreamFiles(strArr);
//			Response.Body body = response.body();
//			inputStream = body.asInputStream();
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			in = new BufferedInputStream(inputStream);
			out = new BufferedOutputStream(new FileOutputStream("D:\\0000\\测试.zip"));
			int len = -1;
			byte[] b = new byte[1024];
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new R<>();
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R handleFileUploads(@RequestPart(value = "file") MultipartFile file) {
		InputStream inputStream = null;
		try {
			Map<String, String> filePaths = Maps.newHashMap();
			filePaths.put("D:\\14S_GJK_GIT\\gjk\\gjk\\common\\component\\xiaohe99\\1.0", "component/xiaohe99/");
			filePaths.put("D:\\14S_GJK_GIT\\gjk\\gjk\\common\\component\\xiaohe99\\2.0", "component/xiaohe99/");

			//
			JSONObject json = JSONUtil.parseFromMap(filePaths);
			Response response = rdcService.downloadStreamFiles(file, "component\\", json.toJSONString(0));
			Response.Body body = response.body();
			inputStream = body.asInputStream();
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			in = new BufferedInputStream(inputStream);
			out = new BufferedOutputStream(new FileOutputStream("D:\\0000\\测试.zip"));
			int len = -1;
			byte[] b = new byte[1024];
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new R<>();
	}
}
