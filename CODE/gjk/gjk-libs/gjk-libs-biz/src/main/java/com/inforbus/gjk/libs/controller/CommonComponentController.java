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
package com.inforbus.gjk.libs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.service.CommonComponentDetailService;
import com.inforbus.gjk.libs.service.CommonComponentService;
import com.inforbus.gjk.libs.service.ComponentServiceFeign;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 公共构件库详细表
 *
 * @author pig code generator
 * @date 2019-06-10 14:25:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/commoncomponent")
public class CommonComponentController {

	private final CommonComponentService commonComponentService;
	private final CommonComponentDetailService commonComponentDetailService;
	private final ComponentServiceFeign componentServiceFeign;

	/**
	 * 简单分页查询
	 * 
	 * @param page            分页对象
	 * @param commonComponent 公共构件库详细表
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<CommonComponent>> getCommonComponentPage(Page<CommonComponent> page,
			CommonComponent commonComponent) {
		return new R<>(commonComponentService.getCommonComponentPage(page, commonComponent));
	}
	/**
	 * @Title: getCommonComponentPage
	 * @Desc 查询所有数据
	 * @Author cvics
	 * @DateTime 2020年3月30日
	 * @return 
	 */
	@PostMapping("/pageAll")
	public List<CommonComponent> getCommonComponentPage() {
		return commonComponentService.getCommonComponentPage(new Page<>(), new CommonComponent()).getRecords();
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<CommonComponent> getById(@PathVariable("id") String id) {
		return new R<>(commonComponentService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param commonComponent
	 * @return R
	 */
	@SysLog("新增公共构件库详细表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_commoncomponent_add')")
	public R save(@RequestBody CommonComponent commonComponent) {
		return new R<>(commonComponentService.save(commonComponent));
	}

	@RequestMapping("/saveCommonComp")
	public R saveCommonComp(@RequestBody CommonComponent commonComponent) {
		commonComponentService.saveCommonComp(commonComponent);
		CommonComponent component = commonComponentService.getById(commonComponent.getId());
		return new R<>(component);
	}

	/**
	 * 修改记录
	 * 
	 * @param commonComponent
	 * @return R
	 */
	@SysLog("修改公共构件库详细表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_commoncomponent_edit')")
	public R update(@RequestBody CommonComponent commonComponent) {
		return new R<>(commonComponentService.updateById(commonComponent));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除公共构件库详细表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_commoncomponent_del')")
	public R removeById(@PathVariable String id) {
		// 删除文件
		List<CommonComponentDetail> commonComponentDetails = commonComponentService.deleteCompById(id);
		for (CommonComponentDetail detail : commonComponentDetails) {
			commonComponentDetailService.removeById(detail.getId());
		}
//		// 更改构件建模状态
		Component component = new Component();
		component.setId(id);
		component.setApplyDesc("构架库中已删除");
		component.setApplyState("0");
		componentServiceFeign.modifyComp(component);
		return new R<>(commonComponentService.removeById(id));
	}

	@PostMapping("/getAllVersionByCompId")
	public R getAllVersionByCompId(@RequestBody CommonComponent commonComponent) {
		return new R<>(commonComponentService.getAllVersionByCompId(commonComponent));
	}

	@PostMapping("/screenComp/{current}/{size}")
	public R getScreenComp(@PathVariable Integer current, @PathVariable Integer size,
			@RequestBody List<String> libsList) {
		return new R<>(commonComponentService.getScreenComp(new Page<>(current, size), libsList));
	}

	@PostMapping("/screenCompByLibs")
	public R screenCompByLibs(@RequestBody List<String> libsList) {
		return new R<>(commonComponentService.getScreenComp(libsList));
	}

	@PostMapping("/createZipFile")
	public void createZipFile(HttpServletRequest request, HttpServletResponse response,
			@RequestBody List<CommonComponent> list) {
		try {
			List<CommonComponentDetail> details = commonComponentDetailService.getAllCompDetailByCompId(list);
			byte[] data = commonComponentService.createZip(list, details);

			String zipFileName = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".zip";
			response.reset();
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", zipFileName));
			response.setHeader("FileName", zipFileName);
			response.setHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream; charset=UTF-8");

			IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回构件树形选择器的树
	 *
	 * @return 树形菜单
	 */
	@PutMapping("/compSelectTree")
	public R getCompSelectTree() {
		return new R<>(commonComponentService.getCompSelectTree());
	}

	/**
	 * 获取构件ID和构件名称对应的字典
	 *
	 * @param compIdList 类型
	 * @return 同类型字典
	 */
	@PostMapping("/info/getCompDict")
	public R getCompDict(@RequestBody List<String> compIdList) {
		return new R<>(commonComponentService.getCompDictList(compIdList));
	}

	@PostMapping("/getCompListByString/{current}/{size}")
	public R<?> getCompListByString(@PathVariable Integer current, @PathVariable Integer size,
			@RequestBody List<String> selectList) {
		return new R<>(commonComponentService.getCompListByString(new Page<>(current, size), selectList));
	}

	@PostMapping("/getCompListByStringAndLibsId/{current}/{size}")
	public R<?> getCompListByStringAndLibsId(@PathVariable Integer current, @PathVariable Integer size,
			@RequestBody List<List<String>> list) {
		return new R<>(commonComponentService.getCompListByStringAndLibsId(new Page<>(current, size), list.get(0),
				list.get(1)));
	}

	@PostMapping("/findPageByBatchApprovalId/{applyId}/{page}/{size}")
	public R<IPage<CommonComponent>> findPageByBatchApprovalId(@PathVariable("page") Integer current,
			@PathVariable("size") Integer size, @PathVariable("applyId") String id) {
		return new R<>(commonComponentService.findPageByBatchApprovalId(new Page<>(current, size), id));
	}

	@PostMapping("/saveCompList")
	public R saveCompDetailList(@RequestBody List<CommonComponent> commonComponents) {
		return new R<>(commonComponentService.saveCommonCompList(commonComponents));
	}

	@PostMapping("/getRemoveCompIdList")
	public R getRemoveCompIdList(@RequestBody List<String> compIdList) {
		return new R<>(commonComponentService.getRemoveCompIdList(compIdList));
	}

}
