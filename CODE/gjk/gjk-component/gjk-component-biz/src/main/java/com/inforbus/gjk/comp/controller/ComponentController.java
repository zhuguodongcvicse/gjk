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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.comp.api.dto.CompTree;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.util.CompTreeUtil;
import com.inforbus.gjk.comp.api.vo.ComponentVO;
import com.inforbus.gjk.comp.service.ComponentService;

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
		return new R<>(componentService.getById(id));
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
	@PutMapping
	@RequestMapping("/modifyComp")
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
//	@PutMapping("/compTree/{userId}")
//	public R getTree(@PathVariable String userId) {
//		List<CompTree>  list =TreeUtil.buildTree(componentService.getCompByUserId(userId), "-1");
//		return new R<>(list);
//	}

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

	@RequestMapping("/getCompDetailById/{id}")
	public ComponentVO getComponentCompDetailById(@PathVariable("id") String id) {
		return componentService.getComponentCompDetailById(id);
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
		return new R<>(componentService.getCompFiles(compId));
	}

	@GetMapping("/comImg1/{imgId}")
	public R getImgFileStr(@PathVariable String imgId) {
		return new R<>(componentService.getImgFileStr(imgId));
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
//		FileInputStream fis = null;
		OutputStream os = null;
//		URL url = null;
//		HttpURLConnection httpUrl = null;
//		String imgId=(String) req.getAttribute("imgId");
		FileInputStream fis = componentService.getImgFile(imgId, new StringBuilder());
		try {
			// url = new URL("");
			// httpUrl =(HttpURLConnection) url.openConnection();
			// fis= (FileInputStream) httpUrl.getInputStream();
//			fis = new FileInputStream("D:/14S_GJK_GIT/gjk/gjk/image/111.png");
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
//				httpUrl.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取构件ID和构件名称对应的字典
	 *
	 * @param id 类型
	 * @return 同类型字典
	 */
	@GetMapping("/info/getCompDict")
	@Cacheable(value = "compDicts")
	public R getCompDict() {
		return new R<>(componentService.getCompDictList());
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
		return new R<>(componentService.analysisXmlFile(filePath));
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
	@PostMapping(path = "/importCompZipUpload", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> appImageUpload(@RequestParam(value = "file", required = false) MultipartFile ufile) {
		return new R<>(componentService.analysisZipFile(ufile));
	}
	@ResponseBody
	@PostMapping(path = "/checkComp")
	public R checkComp(@RequestBody List<Object> obj){
		return new R<>(componentService.checkComp(obj));
	}

	/**
	 * 构件入库前的判断 选择的库目录文件是否存在
	 * @param compId
	 * @return
	 */
	@GetMapping("/isSelectLibs/{compId}")
	public R isSelectLibs(@PathVariable("compId") String compId) {
		return new R<>(componentService.isSelectLibs(compId));
	}
}
