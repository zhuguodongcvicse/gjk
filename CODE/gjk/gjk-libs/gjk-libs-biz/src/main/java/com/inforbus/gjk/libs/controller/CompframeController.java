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
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.TreeUtil;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.entity.Compframe;
import com.inforbus.gjk.libs.service.CompframeService;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 构件框架库
 *
 * @author xiaohe
 * @date 2019-11-25 15:45:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/compframe")
public class CompframeController {

	private final CompframeService compframeService;

	/**
	 * 简单分页查询
	 * 
	 * @param page      分页对象
	 * @param compframe 构件框架库
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<Compframe>> getCompframePage(Page<Compframe> page, Compframe compframe) {
		return new R<>(compframeService.getCompframePage(page, compframe));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<Compframe> getById(@PathVariable("id") String id) {
		return new R<>(compframeService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param compframe
	 * @return R
	 */
	@SysLog("新增构件框架库")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_compframe_add')")
	public R save(@RequestBody Compframe compframe) {
		return new R<>(compframeService.save(compframe));
	}

	/**
	 * 修改记录
	 * 
	 * @param compframe
	 * @return R
	 */
	@SysLog("修改构件框架库")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_compframe_edit')")
	public R update(@RequestBody Compframe compframe) {
		return new R<>(compframeService.updateById(compframe));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除构件框架库")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_compframe_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(compframeService.removeById(id));
	}

	/**
	 * @Title: getCompByUserId
	 * @Description: 保存构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月26日 下午1:32:00
	 * @param userId 用户编号
	 * @return
	 */
	@ResponseBody
	@PreAuthorize("@pms.hasPermission('libs_compframe_add')")
	@PostMapping(path = "/saveCompFrame", consumes = { "multipart/mixed", "multipart/form-data" })
	public R saveCompFrame(@RequestParam(value = "files", required = false) MultipartFile ufile,
			@RequestParam(value = "dataParams", required = false) String dataParams) {
		Map<String, Object> resMap = Maps.newHashMap();
		resMap.putAll(JSONUtil.parseObj(dataParams));
		return compframeService.saveCompFrame(ufile,resMap);
	}
	@SysLog("修改构件框架库")
	@PostMapping("/compframeToTree")
//	@Cacheable(value = "compframe_tree", key = "#compframe.id")
	public R compframeToTree(@Valid @RequestBody Compframe compframe) {
		return new R<>(TreeUtil.buildByLoop(compframeService.compframeToTree(compframe), "-1"));
	}
}
