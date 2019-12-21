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
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import com.inforbus.gjk.pro.mapper.AppMapper;
import com.inforbus.gjk.pro.service.AppService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-07-22 13:39:45
 */
@Service("appService")
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

	private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

	private static final String proDetailPath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 简单分页查询
	 * 
	 * @param app
	 * @return
	 */
	@Override
	public IPage<App> getAppPage(Page<App> page, App app) {
		return baseMapper.getAppPage(page, app);
	}

	@Override
	public App saveApp(App app) {
		App a = getAppByProcessId(app.getProcessId());
		if (a != null) {
			this.removeById(a.getId());
		}
		baseMapper.saveApp(app);
		return this.getById(app.getId());
	}

	@Override
	public List<App> getAllApp() {
		return baseMapper.getAllApp();
	}

	@Override
	public FileInputStream getAppImgFile(String id) {
		App app = baseMapper.getAppImgFile(id);
		String basePath = JGitUtil.getLOCAL_REPO_PATH();
		FileInputStream fis = null;
		try {
			String filePath = app.getBackPath();
			System.out.println("....." + (basePath + filePath));
			fis = new FileInputStream(basePath + filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}

	@Override
	public List<App> getAppVosPage(String fileName) {
		return baseMapper.getAppVosPage(fileName);
	}

	@Override
	public App getAppByProcessId(String processId) {
		return baseMapper.getAppByProcessId(processId);
	}

	@Override
	public List<ProjectFileVO> getAppFileTree(String processId) {
		App app = getAppByProcessId(processId);
		if (app != null) {
			String appPath = proDetailPath + app.getFilePath() + File.separator + app.getFileName();
			File appFile = new File(appPath);
			List<ProjectFileVO> tree = Lists.newArrayList();
			if (!appFile.exists()) {
				return Lists.newArrayList();
			}

			String id = IdGenerate.uuid();
			tree.add(new ProjectFileVO(id, appFile.getName(), "App组件工程", "app",
					appFile.getParentFile().getAbsolutePath(), processId, processId, "0"));
			File[] fileList = appFile.listFiles();
			for (File file : fileList) {
				if (file.getName().equals("bsp") && appFile.getName().equals("AppPro")) {
					continue;
				}
				addAppFileTree(tree, id, file, processId);
			}

			return tree;
		} else {
			return Lists.newArrayList();
		}
	}

	private void addAppFileTree(List<ProjectFileVO> tree, String parentId, File file, String processId) {
		String fileId = IdGenerate.uuid();

		if (file.isDirectory()) {
			tree.add(new ProjectFileVO(fileId, file.getName(), file.getName(), "app",
					file.getParentFile().getAbsolutePath(), parentId, processId, "0"));
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addAppFileTree(tree, fileId, childFile, processId);
			}
		} else {
			tree.add(new ProjectFileVO(fileId, file.getName(), file.getName(), "app",
					file.getParentFile().getAbsolutePath(), parentId, processId, "1"));
		}
	}

	@Override
	public void editAppState(String id, String appState) {
		baseMapper.editAppState(id, appState);
	}

	@Override
	public ProjectFile getProcessByProjectId(String id) {
		return baseMapper.getProcessByProjectId(id);
	}

	@Override
	public Project getprojectByProjectId(String id) {
		return baseMapper.getprojectByProjectId(id);
	}

	@Override
	public void deleteAppByAPPId(String id) {
		App appData = baseMapper.selectAPPByAPPId(id);
		String appPath = proDetailPath + appData.getFilePath() + File.separator + appData.getFileName();
		UploadFilesUtils.delFolder(appPath);
		baseMapper.deleteAppByAPPId(id);
	}

	@Override
	public App selectAPPByAPPId(String id) {
		return baseMapper.selectAPPByAPPId(id);
	}

	@Override
	public ProjectFile getProcessByProcessId(String parentId) {
		return baseMapper.getProcessByProcessId(parentId);
	}

}
