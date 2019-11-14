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
import com.inforbus.gjk.admin.api.entity.SysUser;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.api.entity.Project;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 资源管理
 *
 * @author xiaohe
 * @date 2019-04-16 21:06:53
 */
public interface ProjectMapper extends BaseMapper<Project> {
	/**
	 * 资源管理简单分页查询
	 * 
	 * @param project 资源管理
	 * @return
	 */
	IPage<Project> getProjectPage(Page page, @Param("project") Project project);

	/**
	 * @Title: listCompByProjectId
	 * @Description: 通过项目编号查询项目信息
	 * @Author xiaohe
	 * @DateTime 2019年5月2日 下午1:55:12
	 * @param projectId 项目编号
	 * @return
	 */
	public Project listProjectByProjectId(@Param("projectId") String projectId);

	/**
	 * @Title: savePro
	 * @Description: 保存项目
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午2:58:29
	 * @param project
	 * @return
	 */
	void savePro(@Param("pro") Project project);

	/**
	 * @Title: getProById
	 * @Description: 根据id查询项目信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午3:13:50
	 * @param id
	 * @return
	 */
	Project getProById(@Param("id") String id);

	/**
	 * @Title: getProByProName
	 * @Description: 根据projectName查询项目信息
	 * @Author cvics
	 * @DateTime 2019年5月16日 下午4:26:30
	 * @param projectName
	 * @return
	 */
	Project getProByProName(@Param("projectName") String projectName);

	/**
	 * @Title: getByUserId 获取项目列表
	 * @Description: 通过用户编号获取项目列表
	 * @Author xiaohe
	 * @DateTime 2019年4月24日 上午8:36:24
	 * @param userId
	 * @return
	 */
	List<Project> getByUserId(@Param("userId") String userId);

	/**
	 * 根据项目ID和构件ID查找id
	 * 
	 * @param proComp
	 * @return
	 */
	ProComp getIdByProIdCompId(@Param("proComp") ProComp proComp);

	/**
	 * @Title: saveProCompList
	 * @Description: 保存与项目相关的构件
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午6:22:52
	 * @param proComp
	 */
	void saveProComp(@Param("proComp") ProComp proComp);

	/**
	 * @Title: getUsernameByUserId
	 * @Description: 根据userID查询用户名
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午8:58:51
	 * @param userId
	 * @return
	 */
	SysUser getUsernameByUserId(@Param("userId") String userId);

}
