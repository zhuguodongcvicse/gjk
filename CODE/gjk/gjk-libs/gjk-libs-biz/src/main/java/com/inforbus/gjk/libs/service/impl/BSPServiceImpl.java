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
import com.inforbus.gjk.libs.api.dto.BSPDTO;
import com.inforbus.gjk.libs.api.dto.BSPTree;
import com.inforbus.gjk.libs.api.dto.SelectFolderDTO;
import com.inforbus.gjk.libs.api.entity.BSP;
import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.api.entity.BSPFile;
import com.inforbus.gjk.libs.api.util.BSPTreeUtil;
import com.inforbus.gjk.libs.mapper.BSPMapper;
import com.inforbus.gjk.libs.service.BSPService;

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
@Service("BSPService")
public class BSPServiceImpl extends ServiceImpl<BSPMapper, BSP> implements BSPService {

	private static final String gitFilePath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 软件框架库表简单分页查询
	 * 
	 * @param BSP 软件框架库表
	 * @return
	 */
	@Override
	public IPage<BSP> getBSPPage(Page<BSP> page, BSP bsp) {
		return baseMapper.getBSPPage(page, bsp);
	}

	/**
	 * 保存软件框架库信息
	 */
	@Override
	public BSP saveBSP(BSP bsp) {
		bsp.setDelFlag("0");
		bsp.setId(IdGenerate.uuid());
		baseMapper.saveBSP(bsp);
		return bsp;
	}

	/**
	 * 保存软件框架库详细信息
	 */
	@Override
	public BSPDetail saveBSPDetail(BSPDetail BSPDetail) {
		baseMapper.saveBSPDetail(BSPDetail);
		return BSPDetail;
	}

	/**
	 * 保存软件框架库文件路径子表信息
	 */
	@Override
	public BSPFile saveBSPFile(BSPFile bspFile) {
		baseMapper.saveBSPFile(bspFile);
		return bspFile;
	}

	/**
	 * 根据选择的平台文件对版本进行赋值
	 */
	@Override
	public BSP setVersionSize() {
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
	public IPage<BSPDTO> getBSPDTOPage(Page<BSP> bspPage, BSP bsp) {
		IPage<BSP> iPage = getBSPPage(bspPage, bsp);
		List<BSP> bsps = iPage.getRecords();

		List<BSPDTO> bspDTOs = new ArrayList<>();
		for (BSP soft : bsps) {
			BSPDTO dto = new BSPDTO(soft);
			bspDTOs.add(dto);
		}
		Page<BSPDTO> bspDTOPage = new Page<BSPDTO>(bspPage.getCurrent(), bspPage.getSize());
		bspDTOPage.setRecords(bspDTOs);
		bspDTOPage.setPages(iPage.getPages());
		bspDTOPage.setTotal(iPage.getTotal());
		return bspDTOPage;
	}

	@Override
	public List<BSPTree> getBSPTree() {
		List<BSP> list = this.list(Wrappers.<BSP>query().lambda());
		List<BSPTree> trees = BSPTreeUtil.buildTree(list, "-1");
		return trees;
	}

	@Override
	public void removeBspFile(String bspId) {
		baseMapper.removeBspFile(bspId);
	}

	@Override
	public void removeBspDetail(String bspId) {
		baseMapper.removeBspDetail(bspId);
	}

	@Override
	public List<BSPTree> getTreeById(String id) {
		List<BSPTree> tree = Lists.newArrayList();
		BSP bsp = this.getById(id);
		tree.add(new BSPTree(bsp, "-1"));

		File file = new File(gitFilePath + bsp.getFilePath());
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addBSPTree(tree, childFile, id);
			}
		} else {
			tree.add(new BSPTree(file, IdGenerate.uuid(), id));
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
	private void addBSPTree(List<BSPTree> tree, File file, String parentId) {
		String fileId = IdGenerate.uuid();
		tree.add(new BSPTree(file, fileId, parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addBSPTree(tree, childFile, fileId);
			}
		}
	}

}
