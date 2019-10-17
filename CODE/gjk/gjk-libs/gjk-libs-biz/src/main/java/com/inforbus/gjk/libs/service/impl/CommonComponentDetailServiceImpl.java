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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.mapper.CommonComponentDetailMapper;
import com.inforbus.gjk.libs.service.CommonComponentDetailService;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 公共构件库表
 *
 * @author pig code generator
 * @date 2019-06-10 14:24:03
 */
@Service("commonComponentDetailService")
public class CommonComponentDetailServiceImpl extends ServiceImpl<CommonComponentDetailMapper, CommonComponentDetail>
		implements CommonComponentDetailService {

	private static final String gitFilePath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 公共构件库表简单分页查询
	 * 
	 * @param commonComponentDetail 公共构件库表
	 * @return
	 */
	@Override
	public IPage<CommonComponentDetail> getCommonComponentDetailPage(Page<CommonComponentDetail> page,
			CommonComponentDetail commonComponentDetail) {
		return baseMapper.getCommonComponentDetailPage(page, commonComponentDetail);
	}

	/**
	 * 保存构件的详细信息并拷贝用户构件的文件
	 */
	@Override
	public boolean saveCommonCompDetailList(List<CommonComponentDetail> commonComponentDetailList,
			CommonComponent component) {
		try {
			// 格式：gjk/common/component/构件名/构件版本/
			String compPath = "gjk" + File.separator + "common" + File.separator + "component" + File.separator
					+ component.getCompName() + File.separator + component.getVersion() + File.separator;
			String subStr = null;
			for (CommonComponentDetail detail : commonComponentDetailList) {
				if ("xml".equals(detail.getFileType())) {
					subStr = detail.getFilePath();
					break;
				}
			}
			for (CommonComponentDetail detail : commonComponentDetailList) {
				String originalFileName = gitFilePath + detail.getFilePath() + File.separator + detail.getFileName();
				detail.setFilePath(compPath + detail.getFilePath().substring(subStr.length()));
				File originalFile = new File(originalFileName);	
				if (!originalFile.exists()) {
					continue;
				}
				if (originalFile.isDirectory()) {
					FileUtil.copyDir(originalFileName, gitFilePath + detail.getFilePath() + detail.getFileName());
				} else {
					FileUtil.copyFile(originalFileName, gitFilePath + detail.getFilePath());
				}
				baseMapper.saveCommonCompDetail(detail);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<CommonComponentDetail> getAllCompDetailByCompId(List<CommonComponent> compList) {
		return baseMapper.getAllCompDetailByCompId(compList);
	}

}
