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
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.TreeUtil;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.dto.SoftwareDTO;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.service.SoftwareService;
import lombok.AllArgsConstructor;

import java.io.File;

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
@RequestMapping("/software")
public class SoftwareController {

	private final SoftwareService softwareService;
	private static final String softwarePath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 简单分页查询
	 * 
	 * @param page     分页对象
	 * @param software 软件框架库表
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<SoftwareDTO>> getSoftwarePage(Page<Software> page, Software software) {
		return new R<>(softwareService.getSoftwareDTOPage(page, software));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<Software> getById(@PathVariable("id") String id) {
		return new R<>(softwareService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param software
	 * @return R
	 */
	@SysLog("新增软件框架库表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_software_add')")
	public R save(@RequestBody Software software) {
		return new R<>(softwareService.save(software));
	}

	/**
	 * 修改记录
	 * 
	 * @param software
	 * @return R
	 */
	@SysLog("修改软件框架库表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_software_edit')")
	public R update(@RequestBody Software software) {
		return new R<>(softwareService.updateById(software));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除软件框架库表")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_software_del')")
	public R removeById(@PathVariable String id) {
		softwareService.removeSoftwareFile(id);
		softwareService.removeSoftwareDetail(id);
		return new R<>(softwareService.removeById(id));
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
	@RequestMapping("/saveSoftware")
	public R saveSoftware(@RequestBody Software software) {
		return new R<>(softwareService.saveSoftware(software));
	}

	/**
	 * 保存软件框架库子表信息
	 * 
	 * @Title: saveSoftwareDeatil
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:54:03
	 * @param softwareDetail
	 * @return
	 */
	@RequestMapping("/saveSoftwareDetail")
	public R saveSoftwareDetail(@RequestBody SoftwareDetail softwareDetail) {
		return new R<>(softwareService.saveSoftwareDetail(softwareDetail));
	}

	/**
	 * 保存软件框架库子表信息
	 * 
	 * @Title: saveSoftwareFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:54:03
	 * @param softwareFile
	 * @return
	 */
	@RequestMapping("/saveSoftwareFile")
	public R saveSoftwareFile(@RequestBody SoftwareFile softwareFile) {
		return new R<>(softwareService.saveSoftwareFile(softwareFile));
	}

	/**
	 * 根据选择的平台文件对版本进行赋值
	 * 
	 * @param platformId
	 * @return
	 */
	@GetMapping("/setVersionSize")
	public R setVersionSize() {
		return new R<>(softwareService.setVersionSize());
	}

	@PostMapping("/uploadFiles/{versionDisc}")
	public String uploadFiles(@RequestParam(value = "file") MultipartFile[] files, @PathVariable String versionDisc) {
//		String path = "E:/tmp/";
		String path = softwarePath;
		String res = path + ",";
		for (MultipartFile file : files) {
			System.out.println("file.getOriginalFilename():" + file.getOriginalFilename());
			if (file != null) {
				String p = path + "gjk/software/" + versionDisc + ".0" + File.separator + file.getOriginalFilename();
				String ss = p.substring(0, p.lastIndexOf("/"));
				File ff = new File(ss);
				if (ff.exists()) {
					ff.delete();
				}
				// 创建文件夹
				ff.mkdirs();
				// 如果文件已经存在，则删除创建新文件
				if (new File(p).exists()) {
					new File(p).delete();
				}
				try {
					// 上传文件
					file.transferTo(new File(p));
//					return p;
				} catch (Exception e) {
					res += "文件 " + file.getOriginalFilename() + " 上传失败\n";
					e.printStackTrace();
				}
				res += "文件 " + file.getOriginalFilename() + " 上传成功\n";
			}

		}

		return res;
	}

	@GetMapping("/getSoftwareTree")
	public R getSoftwareTree() {
		return new R<>(softwareService.getSoftwareTree());
	}

	@GetMapping("/getTreeById/{id}")
	public R getTreeById(@PathVariable("id") String id) {
		return new R<>(TreeUtil.buildByLoop(softwareService.getTreeById(id), "-1"));
	}
}