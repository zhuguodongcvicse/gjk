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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.TreeUtil;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.dto.BSPDTO;
import com.inforbus.gjk.libs.api.entity.BSP;
import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.api.entity.BSPFile;
import com.inforbus.gjk.libs.service.BSPService;
import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
@RestController
@AllArgsConstructor
@RequestMapping("/bsp")
public class BSPController {

	private final BSPService bspService;

	/**
	 * 简单分页查询
	 * 
	 * @param page 分页对象
	 * @param bsp  软件框架库表
	 * @return
	 */
	@PostMapping("/page/{current}/{size}")
	public R<IPage<BSPDTO>> getBSPPage(@PathVariable Long current, @PathVariable Long size,@RequestBody BSP bsp) {
		Page<BSP> page = new Page<BSP>();
		page.setCurrent(current);
		page.setSize(size);
		return new R<>(bspService.getBSPDTOPage(page, bsp));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<BSP> getById(@PathVariable("id") String id) {
		return new R<>(bspService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param BSP
	 * @return R
	 */
	@SysLog("新增软件框架库表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_bsp_add')")
	public R save(@RequestBody BSP bsp) {
		return new R<>(bspService.save(bsp));
	}

	/**
	 * 修改记录
	 * 
	 * @param BSP
	 * @return R
	 */
	@SysLog("修改软件框架库表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_bsp_edit')")
	public R update(@RequestBody BSP bsp) {
		return new R<>(bspService.updateById(bsp));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除软件框架库表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_bsp_del')")
	public R removeById(@PathVariable String id) {
		bspService.removeBspFile(id);
		bspService.removeBspDetail(id);
		return new R<>(bspService.removeById(id));
	}

	/**
	 * 保存软件框架信息
	 * 
	 * @Title: saveComp
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:24:28
	 * @param component
	 * @return
	 */
	@RequestMapping("/saveBSP")
	public R saveBSP(@RequestBody BSP bsp) {
		return new R<>(bspService.saveBSP(bsp));
	}

	/**
	 * 保存软件框架库子表信息
	 * 
	 * @Title: saveBSPDeatil
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:54:03
	 * @param BSPDetail
	 * @return
	 */
	@RequestMapping("/saveBSPDetail")
	public R saveBSPDetail(@RequestBody BSPDetail bspDetail) {
		return new R<>(bspService.saveBSPDetail(bspDetail));
	}

	/**
	 * 保存软件框架库子表信息
	 * 
	 * @Title: saveBSPFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:54:03
	 * @param BSPFile
	 * @return
	 */
	@RequestMapping("/saveBSPFile")
	public R saveBSPFile(@RequestBody BSPFile bspFile) {
		return new R<>(bspService.saveBSPFile(bspFile));
	}

	/**
	 * 根据选择的平台文件对版本进行赋值
	 * 
	 * @param platformId
	 * @return
	 */
	@GetMapping("/setVersionSize")
	public R setVersionSize() {
		return new R<>(bspService.setVersionSize());
	}

	@PostMapping("/uploadFiles/{versionDisc}/{userName}")
	public String uploadFiles(@RequestParam(value = "file") MultipartFile files, @PathVariable String versionDisc, @PathVariable String userName) {
		return bspService.uploadFiles(files, versionDisc, userName);
	}

	@GetMapping("/getBSPTree")
	public R getBSPTree() {
		return new R<>(bspService.getBSPTree());
	}

	@GetMapping("/getTreeById/{id}")
	public R getTreeById(@PathVariable("id") String id) {
		return new R<>(TreeUtil.buildByLoop(bspService.getTreeById(id), "-1"));
	}

}
