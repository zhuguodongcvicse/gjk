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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;
//import com.inforbus.gjk.comp.api.util.UploadFileUtils;   //20190628未完善版本报错，注释
import com.inforbus.gjk.comp.service.ComponentDetailService;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import lombok.AllArgsConstructor;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@RestController
@AllArgsConstructor
@RequestMapping("/componentdetail")
public class ComponentDetailController {

	private final ComponentDetailService componentDetailService;

	/**
	 * 简单分页查询
	 * 
	 * @param page            分页对象
	 * @param componentDetail 构件明细
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<ComponentDetail>> getComponentDetailPage(Page<ComponentDetail> page,
			ComponentDetail componentDetail) {
		return new R<>(componentDetailService.getComponentDetailPage(page, componentDetail));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<ComponentDetail> getById(@PathVariable("id") String id) {
		return new R<>(componentDetailService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param componentDetail
	 * @return R
	 */
	@SysLog("新增构件明细")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('comp_componentdetail_add')")
	public R save(@RequestBody ComponentDetail componentDetail) {
		return new R<>(componentDetailService.save(componentDetail));
	}

	/**
	 * 修改记录
	 * 
	 * @param componentDetail
	 * @return R
	 */
	@SysLog("修改构件明细")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('comp_componentdetail_edit')")
	public R update(@RequestBody ComponentDetail componentDetail) {
		return new R<>(componentDetailService.updateById(componentDetail));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除构件明细")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('comp_componentdetail_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(componentDetailService.removeById(id));
	}

	@PutMapping
	@ResponseBody
	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public R<ComponentDetail> upload(HttpServletRequest request) {
		ComponentDetail componentDetail = componentDetailService.upload(request);
		if (componentDetail != null) {
			System.out.println("上传成功！！！");
			return new R<>(componentDetail, "上传成功！");
		} else {
			return new R<>(componentDetail, "无文件上传！");
		}
	}

	@PutMapping
	@RequestMapping("/createXmlFile/{token}/{compId}")
	public R createXmlFile(@RequestBody XmlEntity entity, @PathVariable("token") String token,
			@PathVariable("compId") String compId) {
		return new R<>(componentDetailService.createXmlFile(entity, token, compId));
	}
	@PutMapping
	@RequestMapping("/createXmlFileMap/{token}/{compId}/{userCurrent}")
	public R createXmlFileMap(@RequestBody XmlEntityMap entity, @PathVariable("token") String token,
			@PathVariable("compId") String compId, @PathVariable("userCurrent") String userCurrent) {
		return new R<>(componentDetailService.createXmlFile(entity, token, compId, userCurrent));
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
	@PostMapping(path = "/uploadUrl", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> uploadReturnUrl(@RequestParam(value = "file", required = false) MultipartFile ufile) {
		System.out.println("上传的文件是:" + ufile);
		return new R<>(UploadFilesUtils.getUploadFilesUrl(ufile));
	}

	/**
	 * @Title: uploadFilesReturnUrl
	 * @Description: 多文件上传，带路径
	 * @Author xiaohe
	 * @DateTime 2019年7月9日 下午4:39:28
	 * @param ufile     文件列表
	 * @param filesPath 文件路径（JSON字符串）
	 * @return 文件夹路径
	 */
	@ResponseBody
	@PostMapping(path = "/uploadFiles", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> uploadFilesReturnUrl(@RequestParam(value = "file", required = false) MultipartFile[] ufile,
			@RequestParam(value = "filesPath", required = false) String filesPath) {
		JSONObject jsonObject = JSONUtil.parseObj(filesPath);
		return new R<>(componentDetailService.getUploadFilesUrl(ufile, jsonObject.toBean(Map.class)));
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
	@PostMapping(path = "/uploadImg", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> uploadImgUrl(@RequestParam(value = "file", required = false) MultipartFile ufile) {
		return new R<>(UploadFilesUtils.getUploadImgUrl(ufile));
	}

	/**
	 * @Title: getDefaultImg
	 * @Description: 
	 * @Author xiaohe
	 * @DateTime 2019年8月3日 下午2:09:39
	 * @return 
	 */
	@GetMapping("/getDefaultImg")
	public R<?> getCompDefaultImg() {
		return new R<>(componentDetailService.getCompDefaultImg());

	}

	/**
	 * @Title: uploadReturnUrll
	 * @Description: 保存图标文件
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:41:10
	 * @param ufile     文件流
	 * @param compParam 其他参数（JSON字符串）
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/saveCompImg", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> saveCompImg(@RequestParam(value = "file", required = false) MultipartFile ufile,
			@RequestParam(value = "compParam", required = false) String compParam) {
		Map<String, Object> resMap = Maps.newHashMap();
		resMap.putAll(JSONUtil.parseObj(compParam));
		return new R<>(componentDetailService.saveCompImg(ufile, resMap));
	}

	@GetMapping("/getAllDetailByCompId/{compId}")
	public R<?> getAllDetailByCompId(@PathVariable String compId) {
		return new R<>(componentDetailService.listCompDetailByCompId(compId));
	}

	@PostMapping("/delFilePath")
	public void delFilePath(@RequestBody List<String> lists) {
		componentDetailService.delFilePath(lists);
	}

	/**
	 * 根据树节点的构件库详细信息的id查找文件路径
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getFilePathById/{id}")
	public R getFilePathById(@PathVariable String id) {
		return new R<>(componentDetailService.getFilePathById(id));
	}

	@PostMapping("/fetchSavefiles")
	public R fetchSavefiles(@RequestBody Map<String, Object> maps) {
		Map<String, String> map = Maps.newHashMap();
		List<CompFilesVO> paths = Lists.newArrayList();
		for (String key : maps.keySet()) {
			if ("paths".equals(key)) {
				paths = (List<CompFilesVO>) maps.get("paths");
			} else {
				map.put(key, (String) maps.get(key));
			}
		}
		return new R<>(componentDetailService.saveCompfiles(map, paths));
	}

}
