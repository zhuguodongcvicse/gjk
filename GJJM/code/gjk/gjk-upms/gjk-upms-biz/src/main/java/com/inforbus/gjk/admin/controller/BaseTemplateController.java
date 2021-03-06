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
package com.inforbus.gjk.admin.controller;

import  com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.dto.BaseTemplateDTO;
import com.inforbus.gjk.admin.api.dto.BaseTemplatePathDTO;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.admin.api.entity.BaseTemplate;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.admin.service.BaseTemplateService;
import com.inforbus.gjk.admin.service.SysDictService;

import lombok.AllArgsConstructor;

import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 基础模板
 *
 * @author xiaohe
 * @date 2019-07-16 08:40:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basetemplate")
public class BaseTemplateController {

	private final BaseTemplateService baseTemplateService;
	/**
	 * 简单分页查询
	 * 
	 * @param page         分页对象
	 * @param baseTemplate 基础模板
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<BaseTemplate>> getBaseTemplatePage(Page<BaseTemplate> page, BaseTemplate baseTemplate) {
		return new R<>(baseTemplateService.getBaseTemplatePage(page, baseTemplate));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param tempId
	 * @return R
	 */
	@GetMapping("/{tempId}")
	public R<BaseTemplate> getById(@PathVariable("tempId") String tempId) {
		return new R<>(baseTemplateService.getById(tempId));
	}

	/**
	 * 新增基础模板
	 * 2019年9月5日 16:29:52 修改基础模板新增功能
	 * 2019年9月19日 16:20:24 修改了代码,把原在控制层的代码移至service层,以及修改了
	 * 新增后保存的地址
	 *基础模板新增功能
	 * @param
	 * @return R
	 */
	@SysLog("新增基础模板")
	@PostMapping("addBaseTemplate")
	@PreAuthorize("@pms.hasPermission('admin_basetemplate_add')")
	public R save(@RequestBody BaseTemplateDTO baseTemplateDTO) {
		boolean b = false;
		try {
			b = baseTemplateService.saveBaseTemplate(baseTemplateDTO);
			if (!b){
				R<Object> R = new R<>(null,"基础模板保存失败,请检查模板文件是否正确");
				R.setCode(1);
				return R;
			}
		} catch (Exception e) {
			R<Object> R = new R<>(null,"基础模板保存失败,请检查模板文件是否正确");
			R.setCode(1);
			return R;
		}
		return new R<>(b);
	}

	/**
	 * 修改记录
	 * 
	 * @param baseTemplate
	 * @return R
	 */
	@SysLog("修改基础模板")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_basetemplate_edit')")
	public R update(@RequestBody BaseTemplate baseTemplate) {
		boolean b = baseTemplateService.update(baseTemplate);
		if (!b){
			R<Object> R = new R<>(b,"表格修改失败,请检查相关信息");
			R.setCode(1);
			return R;
		}
		return new R<>(b,"修改成功");
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param tempId
	 * @return R
	 */
	@SysLog("删除基础模板")
	@DeleteMapping("/{tempId}")
	@PreAuthorize("@pms.hasPermission('admin_basetemplate_del')")
	public R removeById(@PathVariable String tempId) {
		boolean b = baseTemplateService.removeById(tempId);
		if (!b){
			R<Object> R = new R<>(b,"删除失败,请联系管理员");
			R.setCode(1);
			return R;
		}
		return new R<>(b,"删除成功");
	}

	/**
	 * 解析xml功能
	 * @param baseTemplatePathDTO
	 * @return R
	 */
	@SysLog("解析xml")
	@PostMapping("/parseXml")
	public R parseXml(@RequestBody BaseTemplatePathDTO baseTemplatePathDTO) {
		XmlEntityMap xmlEntityMap = null;
		try {
			xmlEntityMap = baseTemplateService.parseXml(baseTemplatePathDTO.getPath());
		} catch (FileNotFoundException e) {
			R<Object> R = new R<>(null, baseTemplatePathDTO.getPath() + "文件不存在");
			R.setCode(1);
			return R;
		}
		return new R<>(xmlEntityMap);
	}

	/**
	 * 编辑基础模板的解析xml功能
	 * 2019年9月5日 16:32:04
	 *2019年9月19日 16:22:09,修改了代码,把原在控制层的代码移至service层
	 * @param baseTemplate
	 * @return R
	 */
	@SysLog("编辑功能解析xml文件")
	@PreAuthorize("@pms.hasPermission('admin_basetemplate_edit')")
	@PostMapping("/editParseXml")
	public R editParseXml(@RequestBody BaseTemplate baseTemplate){
		XmlEntityMap xmlEntityMap = null;
		try {
			xmlEntityMap = baseTemplateService.editParseXml(baseTemplate);
		} catch (FileNotFoundException e) {
			R<Object> R = new R<>(null, baseTemplate.getTempPath() + "文件不存在");
			R.setCode(1);
			return R;
		}
		return new R<>(xmlEntityMap);
	}

	/**
	 * 基础模板保存编辑
	 * 2019年9月5日 16:32:20
	 * 2019年9月19日 16:22:49,修改了代码,把原在控制层的代码移至service层
	 * @param baseTemplateDTO
	 * @return R
	 */
	@SysLog("编辑模板")
	@PreAuthorize("@pms.hasPermission('admin_basetemplate_edit')")
	@PutMapping("/editBaseTemplate")
	public R editBaseTemplate(@RequestBody BaseTemplateDTO baseTemplateDTO) {
		boolean b = baseTemplateService.editBaseTemplate(baseTemplateDTO);
		if (!b){
			R<Object> R = new R<>(b,"模板修改失败,请联系管理员");
			R.setCode(1);
			return R;
		}
		return new R<>(b,"修改成功");
	}

	/**
	 * 校验模板名称是否存在接口
	 *创建: 2019年9月23日 15:36:58
	 * @param tempName
	 * @return R
	 */
	@SysLog("校验模板名称")
	@GetMapping("/checkTempName/{tempName}")
	@PreAuthorize("@pms.hasPermission('admin_basetemplate_add')")
	public R checkTempName(@PathVariable String tempName) {
		BaseTemplate baseTemplate = new BaseTemplate();
		baseTemplate.setTempName(tempName);
		return new R<>(baseTemplateService.getOne(new QueryWrapper<>(baseTemplate)));
	}

	@GetMapping("/getBaseTemplate")
	public List<BaseTemplate> getBaseTemplate() {
		return baseTemplateService.getBaseTemplate();
	}

	@GetMapping("/getBaseTemplates/{tempType}")
	public R getBaseTemplates(@PathVariable String tempType) {
		return new R<>(baseTemplateService.getBaseTemplateByTempType(tempType));
	}
}
