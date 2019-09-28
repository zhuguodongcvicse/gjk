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
package com.inforbus.gjk.pro.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.entity.SysUser;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.pro.api.dto.ProjectInfoDTO;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.mapper.ProjectMapper;
import com.inforbus.gjk.pro.service.ProjectService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 资源管理
 *
 * @author xiaohe
 * @date 2019-04-16 21:06:53
 */
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

	/**
	 * 资源管理简单分页查询
	 * 
	 * @param project 资源管理
	 * @return
	 */
	@Override
	public IPage<Project> getProjectPage(Page<Project> page, Project project) {
		return baseMapper.getProjectPage(page, project);
	}

	@Override
	public Project savePro(Project project) {
//		Project pro = getProByProName(project.getProjectName());
//		if (pro != null) {
//			return pro;
//		}
		project.setId(IdGenerate.uuid());
		baseMapper.savePro(project);
		return this.getById(project.getId());
	}

	@Override
	public Project getProById(String id) {
		return baseMapper.getProById(id);
	}

	private Project getProByProName(String projectName) {
		return baseMapper.getProByProName(projectName);
	}

	/**
	 * @Title: getUsernameByUserId
	 * @Description: 根据userID查询用户名
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午8:58:51
	 * @param userId
	 * @return
	 */
	private SysUser getUsernameByUserId(String userId) {
		return baseMapper.getUsernameByUserId(userId);
	}

	/**
	 * @Title: getByUserId
	 * @Description: 获取用户列表
	 * @Author xiaohe
	 * @DateTime 2019年4月24日 上午8:33:32
	 * @param userId用户编号
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#getByUserId(java.lang.String)
	 */
	@Override
	public List<Project> getByUserId(String userId) {
		return baseMapper.getByUserId(userId);
	}
	@Override
	public List<String> getProNameListByUserId(String userId) {
		List<Project> projects = getByUserId(userId);
		List<String> proNameList = new ArrayList<String>();
		for (Project project : projects) {
			proNameList.add(project.getProjectName());
		}
		return proNameList;
	}
	private void saveProComp(ProComp proComp) {
		baseMapper.saveProComp(proComp);
	}

	private ProComp getIdByProIdCompId(ProComp proComp) {
		return baseMapper.getIdByProIdCompId(proComp);
	}

	public List<ProComp> getAllByProIdCompId(String projectId, List<String> compList) {
		List<ProComp> proComps = new ArrayList<ProComp>();
		for (String compId : compList) {
			ProComp proComp = new ProComp(null, compId, projectId);
			proComp = getIdByProIdCompId(proComp);
			if (proComp != null) {
				proComp.setCanUse("0");
				proComps.add(proComp);
			}
		}
		return proComps;
	}

	public List<ProComp> saveProCompList(String projectId, List<String> compList) {
		List<ProComp> proComps = new ArrayList<ProComp>();
		ProComp proComp = null;
		for (String compId : compList) {
			proComp = getIdByProIdCompId(new ProComp(null, compId, projectId));
			if (proComp == null) {
				proComp = new ProComp(IdGenerate.uuid(), compId, projectId);
				saveProComp(proComp);
			}
			proComps.add(proComp);
		}
		return proComps;
	}

	@Override
	public ProjectInfoDTO getProMessage(String projectId) {
		ProjectInfoDTO infoDTO = new ProjectInfoDTO();
		Project project = getProById(projectId);
		infoDTO.setProjectName(project.getProjectName());
		infoDTO.setUserName(getUsernameByUserId(project.getUserId()).getName());
		infoDTO.setCreateTime(project.getCreateTime());
		infoDTO.setUpdateTime(project.getUpdateTime());
		infoDTO.setProjectImg(project.getProjectImg());
		infoDTO.setDescription(project.getDescription());
		return infoDTO;
	}

}
