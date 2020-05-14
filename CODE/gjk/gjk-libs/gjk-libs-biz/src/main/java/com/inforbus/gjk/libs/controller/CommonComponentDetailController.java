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
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.api.vo.TreeUtil;
import com.inforbus.gjk.libs.service.CommonComponentDetailService;
import com.inforbus.gjk.libs.service.CommonComponentService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 公共构件库表
 *
 * @author pig code generator
 * @date 2019-06-10 14:24:03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/commoncomponentdetail")
public class CommonComponentDetailController {

	private final CommonComponentDetailService commonComponentDetailService;
	private final CommonComponentService commonComponentService;

	/**
	 * 简单分页查询
	 * 
	 * @param page                  分页对象
	 * @param commonComponentDetail 公共构件库表
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<CommonComponentDetail>> getCommonComponentDetailPage(Page<CommonComponentDetail> page,
			CommonComponentDetail commonComponentDetail) {
		return new R<>(commonComponentDetailService.getCommonComponentDetailPage(page, commonComponentDetail));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<CommonComponentDetail> getById(@PathVariable("id") String id) {
		return new R<>(commonComponentDetailService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param commonComponentDetail
	 * @return R
	 */
	@SysLog("新增公共构件库表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_commoncomponentdetail_add')")
	public R save(@RequestBody CommonComponentDetail commonComponentDetail) {
		return new R<>(commonComponentDetailService.save(commonComponentDetail));
	}

	@SysLog("新增公共构件库表")
	@PostMapping("/saveCompDetailList/{userCurrent}")
	public R saveCompDetailList(@RequestBody List<CommonComponentDetail> commonComponentDetail, @PathVariable String userCurrent) {
		System.out.println("userCurrent---------------->  " + userCurrent);
		CommonComponent component = commonComponentService.getById(commonComponentDetail.get(0).getCompId());
		return new R<>(commonComponentDetailService.saveCommonCompDetailList(commonComponentDetail, component, userCurrent));
	}

	/**
	 * 修改记录
	 * 
	 * @param commonComponentDetail
	 * @return R
	 */
	@SysLog("修改公共构件库表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_commoncomponentdetail_edit')")
	public R update(@RequestBody CommonComponentDetail commonComponentDetail) {
		return new R<>(commonComponentDetailService.updateById(commonComponentDetail));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除公共构件库表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_commoncomponentdetail_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(commonComponentDetailService.removeById(id));
	}

	@PostMapping("/compView")
	public R getCommCompView(@RequestBody CommonComponent comp) {
		return new R<>(commonComponentDetailService.getCommCompView(comp));
	}

	@PostMapping("/compViewTree/{compId}")
	public R getCommCompViewTree(@PathVariable String compId) {
		return new R<>(TreeUtil.buildCommCompTree(
				commonComponentDetailService.getCommCompViewTree(commonComponentService.getById(compId)), "-1"));
	}

	/**
	 * @Author wang
	 * @Description: 根据多个ID查询gjk_CommonComponent_Detail表数据
	 * @Param: [ids]
	 * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.CommonComponentDetail>>
	 * @Create: 2020/5/12
	 */
	@GetMapping("/getCommonComponentDetailByCompIdIn/{ids}")
	public R<List<CommonComponentDetail>>getCommonComponentDetailByCompIdIn(@PathVariable("ids") String ids){
		R<List<CommonComponentDetail>> r = new R<>();
		try {
			List<CommonComponentDetail> list = commonComponentDetailService.getCommonComponentDetailByCompIdIn(ids);
			r.setData(list);
			r.setMsg("查询成功");
		}catch (Exception e){
			r.setData(null);
			r.setMsg("查询失败");
			r.setCode(CommonConstants.FAIL);
		}
		return r;
	}
}
