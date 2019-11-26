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
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.libs.api.entity.Compframe;
import com.inforbus.gjk.libs.mapper.CompframeMapper;
import com.inforbus.gjk.libs.service.CompframeService;

import cn.hutool.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final String compframePath = JGitUtil.getLOCAL_REPO_PATH();
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

	@Override
	public R<?> saveCompFrame(MultipartFile[] ufile, Map<String, Object> resMap) {
		R retR = new R();
		Double version = 1.0;

		// 新增
		if (StringUtils.isEmpty(resMap.get("frameId").toString())) {
			List<Compframe> listsVersion = baseMapper
					.selectList(Wrappers.<Compframe>query().lambda().orderByDesc(Compframe::getVersion));
			version = listsVersion.size() == 0 ? version : listsVersion.get(0).getVersion() + 1.0;
//			version += 1.0;
		} else {// 修改
		}
		List<String> lists = (List<String>) resMap.get("compSelectArray");
		if (ObjectUtils.isNotEmpty(ufile)) {
			try {
				for (MultipartFile mfile : ufile) {
					String path = new String((compframePath + resMap.get("filePath") + File.separator + version
							+ File.separator + mfile.getOriginalFilename()).replace("/", File.separator));
					File uploadFile = null;
					if (StringUtils.isNotEmpty(path)) {
						uploadFile = new File(path);
						if (!uploadFile.getParentFile().exists()) {
							uploadFile.getParentFile().mkdirs();
						}
						uploadFile.createNewFile();
					}
					// 将上传文件保存到路径
					if (uploadFile.exists()) {
						uploadFile.delete();
					}
					mfile.transferTo(uploadFile);
					JGitUtil.commitAndPush(path, "多个文件上传");
				}
				Compframe frame = new Compframe(IdGenerate.uuid(), "构件框架库_" + version, version,
						resMap.get("filePath") + "/" + version, resMap.get("description").toString());
				baseMapper.insertCompframe(frame);
				for (String strpt : lists) {
					baseMapper.insertCompframePlatform(frame.getId(), strpt);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return new R<>(e);
			} catch (IOException e) {
				e.printStackTrace();
				return new R<>(e);
			}

		}
		return retR;
	}

}
