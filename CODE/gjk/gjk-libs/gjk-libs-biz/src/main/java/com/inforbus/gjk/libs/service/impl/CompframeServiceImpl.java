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
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.libs.api.dto.CompframeTree;
import com.inforbus.gjk.libs.api.entity.Compframe;
import com.inforbus.gjk.libs.mapper.CompframeMapper;
import com.inforbus.gjk.libs.service.CompframeService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 构件框架库
 *
 * @author xiaohe
 * @date 2019-11-25 15:45:11
 */
@Service("compframeService")
public class CompframeServiceImpl extends ServiceImpl<CompframeMapper, Compframe> implements CompframeService {

	@Value("${git.local.path}")
	private String gitFilePath;
	private static final Logger logger = LoggerFactory.getLogger(CompframeServiceImpl.class);

	/**
	 * 构件框架库简单分页查询
	 * 
	 * @param compframe 构件框架库
	 * @return
	 */
	@Override
	public IPage<Compframe> getCompframePage(Page<Compframe> page, Compframe compframe) {
		return baseMapper.getCompframePage(page, compframe);
	}

	/**
	 * @Title: saveCompFrame
	 * @Description: 保存构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月27日 上午10:33:47
	 * @param ufile  构件文件列表
	 * @param resMap 携带信息
	 * @return
	 * @see com.inforbus.gjk.libs.service#saveCompFrame(org.springframework.web.multipart.MultipartFile,
	 *      java.util.Map)
	 */
	@Override
	public R<?> saveCompFrame(MultipartFile ufile, Map<String, Object> resMap) {
		R retR = new R();
		double version = 1.0;
		// 新增
		if (StringUtils.isEmpty(resMap.get("frameId").toString())) {
			List<Compframe> listsVersion = baseMapper
					.selectList(Wrappers.<Compframe>query().lambda().orderByDesc(Compframe::getVersion));
			version = listsVersion.size() == 0 ? version : listsVersion.get(0).getVersion() + 1.0;
		} else {// 修改
		}
		String fileName = "构件框架库_" + version;
		String targetPathStr = new String(
				(gitFilePath + resMap.get("filePath") + File.separator + version + File.separator + fileName)
						.replace("/", File.separator));
		if (ObjectUtils.isNotEmpty(ufile)) {
			File uploadFile = null;
			if (StringUtils.isNotEmpty(targetPathStr)) {
				uploadFile = new File(targetPathStr);
				if (!uploadFile.getParentFile().exists()) {
					uploadFile.getParentFile().mkdirs();
				}
			}
		}
		Thread fileThread = new Thread() {
			@Override
			public void run() {
				try {
					UploadFilesUtils.decompression(ufile.getInputStream(), targetPathStr);
					JGitUtil.commitAndPush(targetPathStr, "多个文件上传");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		fileThread.start();
		Compframe frame = new Compframe(IdGenerate.uuid(), fileName, version,
				resMap.get("filePath") + "/" + version + "/" + fileName, resMap.get("description").toString());
		baseMapper.insertCompframe(frame);
		@SuppressWarnings("unchecked")
		List<String> lists = (List<String>) resMap.get("compSelectArray");
		for (String strpt : lists) {
			baseMapper.insertCompframePlatform(frame.getId(), strpt);
		}
		return retR;
	}

	@Override
	public List<CompframeTree> compframeToTree(Compframe compframe) {
		List<CompframeTree> trees = Lists.newArrayList();
		compframe.setFilePath(gitFilePath + compframe.getFilePath());
		trees.add(new CompframeTree(compframe, "-1"));
		compframe.getFilePath();
		File file = new File(compframe.getFilePath());
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addCompframeTree(trees, childFile, compframe.getId());
			}
		} else {
			trees.add(new CompframeTree(file, IdGenerate.uuid(), compframe.getId()));
		}
		return trees;
	}

	/**
	 * 递归生成文件树
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addCompframeTree(List<CompframeTree> tree, File file, String parentId) {
		String fileId = IdGenerate.uuid();
		tree.add(new CompframeTree(file, fileId, parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addCompframeTree(tree, childFile, fileId);
			}
		}
	}
}
