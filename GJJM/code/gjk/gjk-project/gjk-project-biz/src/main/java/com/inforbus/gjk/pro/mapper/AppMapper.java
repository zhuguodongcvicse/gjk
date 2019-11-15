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
package com.inforbus.gjk.pro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.dto.RoleDTO;
import com.inforbus.gjk.admin.api.vo.RoleVO;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.entity.ProjectFile;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-07-22 13:39:45
 */
public interface AppMapper extends BaseMapper<App> {
	/**
	 * 简单分页查询
	 * 
	 * @param app
	 * @return
	 */
	IPage<App> getAppPage(Page page, @Param("app") App app);

	/**
	 * 保存app
	 * 
	 * @param app
	 * @return
	 */
	void saveApp(@Param("app") App app);

	/**
	 * 获取所有app组件用于展示
	 * 
	 * @return
	 */
	List<App> getAllApp();

	/**
	 * 获取app图片
	 * 
	 * @param id
	 * @return
	 */
	App getAppImgFile(@Param("id") String id);

	/**
	 * 根据app组件名称，模糊查询组件
	 *
	 * @param page    分页
	 * @param roleDTO 查询参数
	 * @return list
	 */
	List<App> getAppVosPage(@Param("fileName") String fileName);

	/**
	 * 根据流程Id查询是否生成app组件工程
	 * 
	 * @param processId
	 * @return
	 */
	App getAppByProcessId(@Param("processId") String processId);
	
	/**
	 * 根据appid 修改app的运行状态
	 * @param id
	 * @param appState
	 */
	void editAppState(@Param("id") String id, @Param("appState") String appState);
	
	/**
	 * 通过项目id拿到流程
	 * @param id
	 * @return
	 */
	ProjectFile getProcessByProjectId(@Param("id") String id);
	
	/**
	 * 通过项目id拿到当前项目信息
	 * @param id
	 * @return
	 */
	Project getprojectByProjectId(@Param("id") String id);
	
	/**
	 * 通过id删除app的数据
	 */
	void deleteAppByAPPId(@Param("id") String id);
	/**
	 * 通过id查询app的数据
	 * @param id
	 * @return
	 */
	App selectAPPByAPPId(@Param("id") String id);
}
