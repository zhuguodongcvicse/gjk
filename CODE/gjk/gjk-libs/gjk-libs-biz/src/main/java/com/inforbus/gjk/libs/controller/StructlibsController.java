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
//import com.inforbus.gjk.admin.api.vo.TreeUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.dto.StructDTO;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.libs.api.vo.TreeUtil;
import com.inforbus.gjk.libs.service.StructlibsService;
import lombok.AllArgsConstructor;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 结构体库
 *
 * @author hu
 * @date 2019-06-14 10:51:14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/structlibs")
public class StructlibsController {

	private final StructlibsService structlibsService;

	@PostMapping("getStructTree")
	public R getStructTree(@Valid @RequestBody StructDTO struct) {
		System.out.println(struct);
//		return new R<>(TreeUtil.buildTree(structlibsService.getStructTree(struct), struct.getId()));
		return new R<>(TreeUtil.buildByLoopCh(structlibsService.getStructTreeDto(struct), struct.getDbId()));
	}

	@PostMapping("structTypeAll")
	public R getStructTypeAll() {
		return new R<>(structlibsService.getStructTypeAll());

	}
	/**
	 * @Title: getStructIncludePointer
	 * @Description: 获取所有的结构体类型
	 * @Author xiaohe
	 * @DateTime 2019年8月23日 下午1:35:18
	 * @return 
	 */
	@PostMapping("strInPointer")
	public R getStructIncludePointer() {
		return new R<>(structlibsService.getStructIncludePointer());
	}

	/**
	 * 通过解析文件获取结构体
	 * 
	 * @param maps 用Map包装的结构体文件路径
	 * @return
	 */
	@PostMapping("/parseStructFile")
	public R getStructByFile(@RequestBody Map<String, String> maps) {
		return structlibsService.parseStructFile(maps.get("path"));
	}

	/**
	 * 简单分页查询
	 * 
	 * @param page       分页对象
	 * @param structlibs 结构体库
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<Structlibs>> getStructlibsPage(Page<Structlibs> page, Structlibs structlibs) {
		return new R<>(structlibsService.getStructlibsPage(page, structlibs));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<Structlibs> getById(@PathVariable("id") String id) {
		return new R<>(structlibsService.getById(id));
	}

	/**
	 * 新增一个结构体树
	 * 
	 * @param paramTreeVO 结构体树
	 * @return R
	 */
	@SysLog("新增一个结构体树")
	@RequestMapping("/saveOneStruct")
	public R saveOneStruct(@RequestBody ParamTreeVO paramTreeVO) {
		return new R<>(structlibsService.saveOneStruct(paramTreeVO));
	}

	@PostMapping("/saveStructMap")
	public void saveStructMap(@RequestBody Map<String, Object> structMaps) {
		structlibsService.saveStructMap(structMaps);
	}

	/**
	 * 新增记录
	 * 
	 * @param structlibs
	 * @return R
	 */
	@SysLog("新增结构体库")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_structlibs_add')")
	public R save(@RequestBody Structlibs structlibs) {
		return new R<>(structlibsService.save(structlibs));
	}

	/**
	 * 修改记录
	 * 
	 * @param structlibs
	 * @return R
	 */
	@SysLog("修改结构体库")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_structlibs_edit')")
	public R update(@RequestBody Structlibs structlibs) {
		return new R<>(structlibsService.updateById(structlibs));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除结构体库")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_structlibs_del')")
	public R removeById(@PathVariable String id) {
        System.out.println("id:"+id);
	    return structlibsService.deleteStructLibById(id);
	}
	@SysLog("修改结构体库--回显")
	@PostMapping("/editStruct")
	@PreAuthorize("@pms.hasPermission('libs_structlibs_edit')")
	public R editStruct(@RequestBody Structlibs structlibs) {
		return structlibsService.editStruct(structlibs);
	}
	@SysLog("修改结构体库--回显")
	@PostMapping("/rKuStructObj")
	@PreAuthorize("@pms.hasPermission('libs_structlibs_del')")
	public R rKuStruct(@RequestBody Structlibs structlibs) {
		return structlibsService.rKuStruct(structlibs);
	}
	/**
	 * 修改一个结构体树
	 *
	 * @param paramTreeVO 结构体树
	 * @return R
	 */
	@SysLog("修改一个结构体树")
	@RequestMapping("/updateOneStruct")
	public R updateOneStruct(@RequestBody ParamTreeVO paramTreeVO) {
		return new R<>(structlibsService.updateOneStruct(paramTreeVO));
	}
	@SysLog("得到库中所有结构体")
	@RequestMapping("/findAllStructs")
	public R findAllStructs(){
		return structlibsService.findAllStructs();
	}
}
