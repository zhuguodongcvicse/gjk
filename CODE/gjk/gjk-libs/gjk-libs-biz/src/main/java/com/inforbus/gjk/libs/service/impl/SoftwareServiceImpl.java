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
package com.inforbus.gjk.libs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.libs.api.dto.SelectFolderDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareTree;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.api.util.SoftwareTreeUtil;
import com.inforbus.gjk.libs.mapper.SoftwareMapper;
import com.inforbus.gjk.libs.service.SoftwareService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
@Service("softwareService")
public class SoftwareServiceImpl extends ServiceImpl<SoftwareMapper, Software> implements SoftwareService {

	private static final String gitFilePath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 软件框架库表简单分页查询
	 * 
	 * @param software 软件框架库表
	 * @return
	 */
	@Override
	public IPage<Software> getSoftwarePage(Page<Software> page, Software software) {
		return baseMapper.getSoftwarePage(page, software);
	}

	/**
	 * 保存软件框架库信息
	 */
	@Override
	public Software saveSoftware(Software software) {
		software.setDelFlag("0");
		software.setId(IdGenerate.uuid());
//		software.setVersion(1.0);
		baseMapper.saveSoftware(software);
		return software;
	}

	/**
	 * 保存软件框架库详细信息
	 */
	@Override
	public SoftwareDetail saveSoftwareDetail(SoftwareDetail softwareDetail) {
		baseMapper.saveSoftwareDetail(softwareDetail);
		return softwareDetail;
	}

	/**
	 * 保存软件框架库文件路径子表信息
	 */
	@Override
	public SoftwareFile saveSoftwareFile(SoftwareFile softwareFile) {
		baseMapper.saveSoftwareFile(softwareFile);
		return softwareFile;
	}

	/**
	 * 根据选择的平台文件对版本进行赋值
	 */
	@Override
	public Software setVersionSize() {
		return baseMapper.setVersionSize();
	}

	@Override
	public void selectFolder(List<SelectFolderDTO> selectFolderDTO) {
		// TODO Auto-generated method stub
		for (SelectFolderDTO selectFolder : selectFolderDTO) {
			System.out.println("selectFolder:" + selectFolder);

		}
	}

	@Override
	public List<SoftwareTree> getSoftwareTree() {
		List<Software> list = this.list(Wrappers.<Software>query().lambda());
		List<SoftwareTree> trees = SoftwareTreeUtil.buildTree(list, "-1");
		return trees;
	}

	@Override
	public IPage<SoftwareDTO> getSoftwareDTOPage(Page<Software> softwarePage, Software software) {
		List<Software> softwares = getSoftwarePage(softwarePage, software).getRecords();

		List<SoftwareDTO> softwareDTOs = new ArrayList<>();
		for (Software soft : softwares) {
			SoftwareDTO dto = new SoftwareDTO(soft);
			softwareDTOs.add(dto);
		}
		Page<SoftwareDTO> softwareDTOPage = new Page<SoftwareDTO>(softwarePage.getCurrent(), softwarePage.getSize());
		softwareDTOPage.setRecords(softwareDTOs);
		return softwareDTOPage;
	}

	@Override
	public void removeSoftwareFile(String softwareId) {
		baseMapper.removeSoftwareFile(softwareId);
	}

	@Override
	public void removeSoftwareDetail(String softwareId) {
		baseMapper.removeSoftwareDetail(softwareId);
	}

	@Override
	public List<SoftwareTree> getTreeById(String id) {
		List<SoftwareTree> tree = Lists.newArrayList();
		Software software = this.getById(id);
		tree.add(new SoftwareTree(software, "-1"));

		File file = new File(gitFilePath + software.getFilePath());
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addSoftwareTree(tree, childFile, id);
			}
		} else {
			tree.add(new SoftwareTree(file, IdGenerate.uuid(), id));
		}
		return tree;
	}

	/**
	 * 递归生成文件树
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addSoftwareTree(List<SoftwareTree> tree, File file, String parentId) {
		String fileId = IdGenerate.uuid();
		tree.add(new SoftwareTree(file, fileId, parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addSoftwareTree(tree, childFile, fileId);
			}
		}
	}

}
