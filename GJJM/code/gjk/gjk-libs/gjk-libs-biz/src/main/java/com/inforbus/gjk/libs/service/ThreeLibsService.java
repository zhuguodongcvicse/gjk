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
package com.inforbus.gjk.libs.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.api.entity.ThreeLibs;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
public interface ThreeLibsService extends IService<ThreeLibs> {
	/*
	 * 通过libs_id获取属于哪个算法类下
	 */
	List<ThreeLibs> getAlgorithmByLibsId();
	/*
	 * 获取算法库下面的文件
	 */
	List<ThreeLibs> getAlgorithmFile();
	
	/*
	 * 通过libs_id获取属于哪个平台类下
	 */
	List<ThreeLibs> getPlatformByLibsId();
	/*
	 * 获取平台库下面的文件
	 */
	List<ThreeLibs> getPlatformFile();
	
	/*
	 * 通过libs_id获取属于哪个测试类下
	 */
	List<ThreeLibs> getTestByLibsId();
	/*
	 * 获取测试库下面的文件
	 */
	List<ThreeLibs> getTestFile();
	
	/*
	 * 根据树的节点id获取当条数据，得到filePath
	 */
	List<ThreeLibs> getAlgorithmFilePath(String id);
	
	/**
	 * 获取软件框架库的主表信息
	 * @return
	 */
	List<Software> getSoftware();
	
	/**
	 * 获取软件框架库平台id的副表信息
	 * @return
	 */
	List<SoftwareDetail> getSoftwarePlatform();
	
	/**
	 * 获取软件框架库文件夹的副表信息
	 */
	List<SoftwareFile> getSoftwareFile();
	/**
	 * 文本编辑器的
	 * @param filePath
	 * @param textContext
	 */
	void saveFileContext(String filePath, String textContext);
}
