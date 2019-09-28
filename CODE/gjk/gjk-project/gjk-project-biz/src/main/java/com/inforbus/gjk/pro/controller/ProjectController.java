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
package com.inforbus.gjk.pro.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.service.ProCompService;
import com.inforbus.gjk.pro.service.ProjectService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 资源管理
 *
 * @author xiaohe
 * @date 2019-04-16 21:06:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {

	private final ProjectService projectService;
	private final ProCompService proCompService;

	/**
	 * 简单分页查询
	 * 
	 * @param page    分页对象
	 * @param project 资源管理
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<Project>> getProjectPage(Page<Project> page, Project project) {
		return new R<>(projectService.getProjectPage(page, project));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<Project> getById(@PathVariable("id") String id) {
		return new R<>(projectService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param project
	 * @return R
	 */
	@SysLog("新增资源管理")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('pro_project_add')")
	public R save(@RequestBody Project project) {
		return new R<>(projectService.save(project));
	}

	@RequestMapping("/savePro")
	public R saveComp(@RequestBody Project project) {
		return new R<>(projectService.savePro(project));
	}

	/**
	 * 修改记录
	 * 
	 * @param project
	 * @return R
	 */
	@SysLog("修改资源管理")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('pro_project_edit')")
	public R update(@RequestBody Project project) {
		return new R<>(projectService.updateById(project));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除资源管理")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('pro_project_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(projectService.removeById(id));
	}

	/**
	 * @Title: 获取项目列表
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年4月23日 下午6:41:31
	 * @param userId 用户编号
	 * @return
	 */
	@GetMapping("/getProList/{userId}")
	public R getProject(@PathVariable("userId") String userId) {
		return new R<>(projectService.getByUserId(userId));
	}

	@GetMapping("/getProNameListByUserId/{userId}")
	public R getProNameListByUserId(@PathVariable("userId") String userId) {
		return new R<>(projectService.getProNameListByUserId(userId));
	}

	@RequestMapping("/saveProCompList/{projectId}/{compList}")
	public R saveProCompList(@PathVariable("projectId") String projectId,
			@PathVariable("compList") List<String> compList) {
		return new R<>(projectService.saveProCompList(projectId, compList));
	}

	@RequestMapping("/selectProMessage/{projectId}")
	public R selectProMessage(@PathVariable("projectId") String projectId) {
		return new R<>(projectService.getProMessage(projectId));
	}

	/**
	 * 修改项目对应构件的审批状态
	 * 
	 * @param project
	 * @return R
	 */
	@SysLog("修改项目对应构件的审批状态")
	@PutMapping("/updateProCompApprovalState/{projectId}")
	public R updateProCompApprovalState(@PathVariable("projectId") String projectId,
			@RequestBody List<String> compList) {
		List<ProComp> proComps = projectService.getAllByProIdCompId(projectId, compList);
		return new R<>(proCompService.updateAllProComp(proComps));
	}

	@GetMapping("/logs")
	public R getlogs() {
		List<String> s = new ArrayList<String>();
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		s.add("0000000000000");
		s.add("1111111111111");
		s.add("2222222222222");
		s.add("3333333333333");
		s.add("4444444444444");
		return new R<>(s, "成功");
	}
}
