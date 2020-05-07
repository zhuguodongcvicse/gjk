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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.inforbus.gjk.admin.api.entity.SysUser;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.dto.BaseTemplateIDsDTO;
import com.inforbus.gjk.pro.api.dto.FilePathDTO;
import com.inforbus.gjk.pro.api.dto.ProjectInfoDTO;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;
import com.inforbus.gjk.pro.api.util.HttpClientUtil;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import com.inforbus.gjk.pro.mapper.ProjectMapper;
import com.inforbus.gjk.pro.service.ManagerService;
import com.inforbus.gjk.pro.service.ProCompService;
import com.inforbus.gjk.pro.service.ProjectService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inforbus.gjk.pro.thread.StreamManage;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源管理
 *
 * @author xiaohe
 * @date 2019-04-16 21:06:53
 */
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ProCompService proCompService;

	@Autowired
	private DisposeDataCenterServiceFeign disposeDataCenterServiceFeign;

	@Value("${git.local.path}")
	private String LOCALPATH;

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
	 * @param userId
	 * @return
	 * @Title: getUsernameByUserId
	 * @Description: 根据userID查询用户名
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午8:58:51
	 */
	private SysUser getUsernameByUserId(Integer userId) {
		return baseMapper.getUsernameByUserId(userId);
	}

	/**
	 * @param userId用户编号
	 * @return
	 * @Title: getByUserId
	 * @Description: 获取用户列表
	 * @Author xiaohe
	 * @DateTime 2019年4月24日 上午8:33:32
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

	@Override
	public List<ProComp> getAllByProIdCompId(String projectId, List<String> compList, String canUse) {
		List<ProComp> proComps = new ArrayList<ProComp>();
		for (String compId : compList) {
			ProComp proComp = new ProComp(null, compId, projectId);
			proComp = getIdByProIdCompId(proComp);
			if (proComp != null) {
				proComp.setCanUse(canUse);
				proComps.add(proComp);
			}
		}
		return proComps;
	}

	/**
	 * @param filePathDTO
	 * @return
	 * @Title: uploadFile
	 * @Description: 项目树右键菜单上传文件
	 * @Author wang
	 * @DateTime 2019年10月17日 13:54:34
	 */
	@Override
	public R<Boolean> uploadFile(FilePathDTO filePathDTO) {
		R<Boolean> r = disposeDataCenterServiceFeign.copylocalFile(filePathDTO.getOldFilePath(), filePathDTO.getNewFilePath());
		return r;
	}

	@Override
	public List<ProComp> saveProCompList(String projectId, List<String> compList) {
		List<ProComp> proComps = new ArrayList<ProComp>();
		ProComp proComp = null;
		for (String compId : compList) {
			proComp = getIdByProIdCompId(new ProComp(null, compId, projectId));
			if (proComp == null) {
				proComp = new ProComp(IdGenerate.uuid(), compId, projectId);
				saveProComp(proComp);
			} else {
				proComp.setCanUse("1");
				proCompService.updateById(proComp);
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

	@Override
	public void removeCompProject(String compId, String projectId) {
		baseMapper.removeCompProject(compId, projectId);

	}

	@Override
	public R staticInspect(String filePath, String fileName) {
		Map<String, String> params = Maps.newHashMap();
		String projectKey = "s" + fileName.hashCode();
		params.put("name", fileName);
		params.put("project", projectKey);
		String url = "http://127.0.0.1:9000/api/projects/create";
		HttpResponse httpResponse = HttpClientUtil.toPost(url, params);
		if (httpResponse == null) {
			return new R<>(CommonConstants.FAIL, "sonar工具未启动", null);
		}
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		try {
			String s = EntityUtils.toString(httpResponse.getEntity());
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
//        System.out.println(statusCode);
//        if(statusCode!=200){
//            return new R<>(CommonConstants.FAIL,"sonar创建项目失败", null);
//        }
		// 执行sonar-scanner命令
		// 文件所在盘符
		String diskCharacter = filePath.split(":")[0] + ":";
		String execCommand = "cmd.exe /c cd " + filePath + " && " + diskCharacter + " && "
				+ JGitUtil.getSONAR_SCANNER_PATH() + "\\sonar-scanner.bat -D\"sonar.projectKey=" + projectKey
				+ "\" -D\"sonar.sources=.\" -D\"sonar.host.url=http://localhost:9000\"";
		try {
			Process execResult = Runtime.getRuntime().exec(execCommand);
			// 出现error时 单个线程会阻塞
			StreamManage errorStream = new StreamManage(execResult.getErrorStream(), "Error");
			StreamManage outputStream = new StreamManage(execResult.getInputStream(), "Output");
			errorStream.start();
			outputStream.start();
//            execResult.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return new R<>(CommonConstants.FAIL, "执行sonar-scanner扫描项目失败", null);
		}
		File file = new File(filePath + "/.sonar/" + fileName.hashCode() + ".pdf");
		if (file.exists()) {
			file.renameTo(new File(filePath + "/.sonar/" + fileName + "检查报告.pdf"));
		}
		return new R<>(CommonConstants.SUCCESS, "filePath + \"/.sonar/\" + fileName + \".pdf\"", projectKey);
	}

	@Override
	public boolean updateBaseTemplate(Project project) {
		List<ProjectFileVO> treeByProjectId = managerService.getTreeByProjectId(project.getId());
		Project oldProject = baseMapper.getProById(project.getId());
		String path = "";
		for (ProjectFileVO proFile : treeByProjectId) {
			if (proFile.getFileType().equals("11")) {
				path = LOCALPATH + proFile.getFilePath();
			}
		}
		BaseTemplateIDsDTO oldBaseTemplateIDsDTO = JSON.parseObject(oldProject.getBasetemplateIds(),
				BaseTemplateIDsDTO.class);
		BaseTemplateIDsDTO newBaseTemplateIDsDTO = JSON.parseObject(project.getBasetemplateIds(),
				BaseTemplateIDsDTO.class);

		if (newBaseTemplateIDsDTO != null) {
			if (newBaseTemplateIDsDTO.getNetworkTempId() != null && !newBaseTemplateIDsDTO.getNetworkTempId().equals("")
					&& !oldBaseTemplateIDsDTO.getNetworkTempId().equals(newBaseTemplateIDsDTO.getNetworkTempId())) {
				// 把项目路径下的模板删除
				File file = new File(path + "自定义配置__网络配置.xml");
				if (file.exists()) {
					file.delete();
				}
			}
			if (newBaseTemplateIDsDTO.getThemeTempId() != null && !newBaseTemplateIDsDTO.getThemeTempId().equals("")
					&& !oldBaseTemplateIDsDTO.getThemeTempId().equals(newBaseTemplateIDsDTO.getThemeTempId())) {
				// 把项目路径下的模板删除
				File file = new File(path + "自定义配置__主题配置.xml");
				if (file.exists()) {
					file.delete();
				}
			}
		}
		return this.updateById(project);
	}

	@Override
	public List<String> getProjectCompByProId(String projectId) {
		List<String> compIdList = new ArrayList<String>();
		List<ProComp> proComps = baseMapper.getProjectCompByProId(projectId);
		if (proComps != null) {
			for (ProComp proComp : proComps) {
				compIdList.add(proComp.getCompId());
			}
		}
		return compIdList;
	}

	@Override
	public boolean removeProIdCompIdList(String projectId, List<String> compIdList) {
		try {
			for (String compId : compIdList) {
				removeCompProject(compId, projectId);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public int compUseNum(String compId) {
		List<ProComp> proComps = baseMapper.compUse(compId);
		Set<String> proIds = new HashSet<String>();
		if (proComps != null && proComps.size() > 0) {
			for (ProComp comp : proComps) {
				proIds.add(comp.getProjectId());
			}
			return proIds.size();
		}
		return 0;
	}

	@Override
	public List getCurrentProApplyedComps(String proId) {
		List comps = baseMapper.getCurrentProApplyedComps(proId);
		return comps;
	}

	/**
	 * @Author wang
	 * @Description: 文件夹上传
	 * @Param: [files, amisPath]
	 * @Return: com.inforbus.gjk.common.core.util.R
	 * @Create: 2020/5/6
	 */
	@Override
	public R uploadFolder(MultipartFile[] files, String amisPath) {
		//调用fegin接口
		return disposeDataCenterServiceFeign.uploadLocalFiles(files, amisPath);
	}
}
