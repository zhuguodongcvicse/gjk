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
package com.inforbus.gjk.pro.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.dto.FilePathDTO;
import com.inforbus.gjk.pro.api.dto.FolderPathDTO;
import com.inforbus.gjk.pro.api.dto.ProjectInfoDTO;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.api.entity.Project;

/**
 * 资源管理
 *
 * @author xiaohe
 * @date 2019-04-16 21:06:53
 */
public interface ProjectService extends IService<Project> {

	/**
	 * 资源管理简单分页查询
	 * 
	 * @param project 资源管理
	 * @return
	 */
	IPage<Project> getProjectPage(Page<Project> page, Project project);

	/**
	 * @Title: savePro
	 * @Description: 保存项目
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午3:02:21
	 * @param project
	 * @return
	 */
	Project savePro(Project project);

	/**
	 * @Title: getProById
	 * @Description: 根据id查询项目信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午3:13:50
	 * @param id
	 * @return
	 */
	Project getProById(String id);

	/**
	 * @Title: getByUserId
	 * @Description:查询项目列表
	 * @Author xiaohe
	 * @DateTime 2019年4月23日 下午6:35:41
	 * @param UserInfo 当前用户信息
	 * @return 项目列表
	 */
	List<Project> getByUserId(String userId);

	/**
	 * 获取此用户下所有项目的项目名称集合
	 * 
	 * @param userId
	 * @return
	 */
	List<String> getProNameListByUserId(String userId);

	/**
	 * @Title: saveProCompList
	 * @Description: 保存与项目相关的构件列表
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午6:44:22
	 * @param projectId
	 * @param compList
	 * @return
	 */
	List<ProComp> saveProCompList(String projectId, List<String> compList);

	/**
	 * @Title: selectProMessage
	 * @Description: 查询项目信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午8:42:48
	 * @param projectId
	 * @return
	 */
	ProjectInfoDTO getProMessage(String projectId);

	/**
	 * 获取所有对应项目ID和构件ID的记录
	 * 
	 * @param projectId
	 * @param compList
	 * @return
	 */
	List<ProComp> getAllByProIdCompId(String projectId, List<String> compList, String canUse);

	/**
	 *
	 * @Title: uploadFile
	 * @Description: 上传文件
	 * @Author cvics
	 * @DateTime 2019年10月18日 15:48:36
	 * @param filePathDTO
	 * @return boolean
	 */
	boolean uploadFile(FilePathDTO filePathDTO);

	/**
	 *
	 * @Title: uploadFile
	 * @Description: 上传文件夹
	 * @Author wang
	 * @DateTime 2019年10月18日 15:49:05
	 * @param folderPathDTO
	 * @return String
	 */
	String uploadFiles(FolderPathDTO folderPathDTO);
}
