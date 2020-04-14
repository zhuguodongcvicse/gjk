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
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.pro.api.dto.FilePathDTO;
import com.inforbus.gjk.pro.api.dto.FolderPathDTO;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.service.ProCompService;
import com.inforbus.gjk.pro.service.ProjectService;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
	private static final String uploadPath = JGitUtil.getLOCAL_REPO_PATH();

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
	 * @param userId 用户编号
	 * @return
	 * @Title: 获取项目列表
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年4月23日 下午6:41:31
	 */
	@GetMapping("/getProList/{userId}")
	public R getProject(@PathVariable("userId") String userId) {
		return new R<>(projectService.getByUserId(userId));
	}

	@GetMapping("/getProNameListByUserId/{userId}")
	public R getProNameListByUserId(@PathVariable("userId") String userId) {
		return new R<>(projectService.getProNameListByUserId(userId));
	}

	@RequestMapping("/saveProCompList/{projectId}")
	public R saveProCompList(@PathVariable("projectId") String projectId, @RequestBody List<String> compList) {
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
	@PutMapping("/updateProCompApprovalState/{projectId}/{canUse}")
	public R updateProCompApprovalState(@PathVariable("projectId") String projectId,
			@PathVariable("canUse") String canUse, @RequestBody List<String> compList) {
		List<ProComp> proComps = projectService.getAllByProIdCompId(projectId, compList, canUse);
		return new R<>(proCompService.updateAllProComp(proComps));
	}

	/**
	 * @param filePathDTO
	 * @return
	 * @Title: uploadFile
	 * @Description: 项目树右键菜单上传文件功能
	 * @Author wang
	 * @DateTime 2019年10月17日 13:54:34
	 */
	@SysLog("增加文件")
	@PutMapping("/uploadFile")
	public R uploadFile(@RequestBody FilePathDTO filePathDTO) {
		return new R<>(projectService.uploadFile(filePathDTO));
	}

	/**
	 * @param files
	 * @return
	 * @Title: uploadFolder
	 * @Description: 项目树右键菜单上传文件夹功能
	 * @Author wang
	 * @DateTime 2019年10月17日 19:47:39
	 */
	@SysLog("增加文件夹")
	@PostMapping("/uploadFolder")
	public R uploadFolder(@RequestParam(value = "file") MultipartFile[] files) {
		String aimsFilePath = uploadPath + "gjk" + File.separator + "upload";
		File aimsFile = new File(aimsFilePath);// 上传的目的地文件夹位置
		FolderPathDTO folderPathDTO = new FolderPathDTO();
		if (!aimsFile.exists()) {
			aimsFile.mkdirs();
		}
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				String filePath = aimsFilePath + File.separator + file.getOriginalFilename();// 文件路径
				File uploadFile = new File(filePath);
				String absolutePath = uploadFile.getAbsolutePath();
				String folderPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
				;
				File uploadFolder = new File(folderPath);
				if (uploadFolder.exists()) {// 文件夹是否存在
					uploadFolder.mkdirs();
				}
				uploadFolder.mkdirs();// 不存在就创建
				if (uploadFile.exists()) {
					uploadFile.delete();// 文件存在,删除掉
				}
				try {
					// uploadFile.createNewFile();//创建文件
					file.transferTo(uploadFile);// 读写数据到文件中
					folderPathDTO.getFilePaths().add(absolutePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new R<>(folderPathDTO);
	}

	/**
	 * @param folderPathDTO
	 * @return R
	 * @Title: uploadFiles
	 * @Description: 项目树右键菜单上传文件夹功能
	 * @Author wang
	 * @DateTime 2019年10月18日 15:46:41
	 */
	@SysLog("增加文件夹")
	@PutMapping("/uploadFiles")
	public R uploadFiles(@RequestBody FolderPathDTO folderPathDTO) {
		return new R<>(projectService.uploadFiles(folderPathDTO));
	}

	/**
	 * 流程建模删除构件
	 * 
	 * @param compId
	 * @param projectId
	 */
	@GetMapping
	@RequestMapping("/removeCompProject/{compId}/{projectId}")
	public void removeCompProject(@PathVariable("compId") String compId, @PathVariable("projectId") String projectId) {
		projectService.removeCompProject(compId, projectId);
	}

	@PostMapping("/staticInspect")
	public R staticInspect(String filePath, String fileName) {
		return projectService.staticInspect(filePath, fileName);
	}

	@PutMapping("/updateBaseTemplateIDs")
	public R updateBaseTemplateIDs(@RequestBody Project project) {
		return new R<>(projectService.updateBaseTemplate(project));
	}

	@GetMapping("/getPassCompByProId/{projectId}")
	public R getProjectCompByProId(@PathVariable String projectId) {
		return new R<>(projectService.getProjectCompByProId(projectId));
	}

	@PostMapping("/removeProIdCompIdList/{projectId}")
	public R removeProIdCompIdList(@PathVariable String projectId, @RequestBody List<String> compIdList) {
		return new R<>(projectService.removeProIdCompIdList(projectId, compIdList));
	}

	@GetMapping("/compUseNum/{compId}")
	public R compUseNum(@PathVariable String compId) {
		return new R<>(projectService.compUseNum(compId));
	}

	/**
	 * 查找该项目下的构件,用来在再次申请构件时标识是否可选
	 * @param proId
	 * @return
	 */
	@GetMapping("/getCurrentProApplyedComps/{proId}")
	public R getCurrentProApplyedComps(@PathVariable String proId) {
		return new R<>(projectService.getCurrentProApplyedComps(proId));
	}

}
