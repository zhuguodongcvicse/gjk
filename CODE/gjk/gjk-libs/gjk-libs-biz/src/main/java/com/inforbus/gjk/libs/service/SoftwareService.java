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

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.libs.api.dto.SelectFolderDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareTree;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
public interface SoftwareService extends IService<Software> {

	/**
	 * 软件框架库表简单分页查询
	 * 
	 * @param software 软件框架库表
	 * @return
	 */
	IPage<Software> getSoftwarePage(Page<Software> page, Software software);

	/**
	 * 保存软件框架库信息
	 * 
	 * @Title: saveSoftware
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午1:30:47
	 * @param software
	 * @return
	 */
	Software saveSoftware(@Param("soft") Software software);

	/**
	 * 保存软件框架库子表信息
	 * 
	 * @Title: saveSoftwareDetail
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:50:03
	 * @param softwareDetail
	 * @return
	 */
	SoftwareDetail saveSoftwareDetail(@Param("softDetail") SoftwareDetail softwareDetail);

	/**
	 * 保存软件框架库文件路径子表信息
	 * 
	 * @Title: saveSoftwareDetail
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:50:03
	 * @param softwareFile
	 * @return
	 */
	SoftwareFile saveSoftwareFile(@Param("softFile") SoftwareFile softwareFile);

	/**
	 * 根据选择的平台文件对版本进行赋值
	 */
//  List<Software> setVersionSize( String platformId);
	Software setVersionSize();

	/**
	 * 文件夹的解析存储
	 * 
	 * @param selectFolderDTO
	 * @return
	 */
	void selectFolder(List<SelectFolderDTO> selectFolderDTO);

	/**
	 * 获取软件框架的树
	 * 
	 * @return
	 */
	List<SoftwareTree> getSoftwareTree();

	/**
	 * 获取软件框架库表封装后的DTO分页查询
	 * 
	 * @param page
	 * @param software
	 * @return
	 */

	IPage<SoftwareDTO> getSoftwareDTOPage(Page<Software> page, Software software);

	/**
	 * 根据id级联删除software文件表
	 * 
	 * @param softwareId
	 */
	void removeSoftwareFile(@Param("softwareId") String softwareId);

	/**
	 * 根据id级联删除software详细表
	 * 
	 * @param softwareId
	 */
	void removeSoftwareDetail(@Param("softwareId") String softwareId);

	/**
	 * 根据ID获取软件框架的树
	 * 
	 * @return
	 */
	List<SoftwareTree> getTreeById(String id);

	/**
	 * 上传文件
	 * 
	 * @param files
	 * @param versionDisc
	 * @return
	 */
	String uploadFiles(@RequestParam(value = "file") MultipartFile[] files, @PathVariable String versionDisc, @PathVariable String userName);
	
	/**
	 * 通过id删除本地文件夹 
	 * @param filePath
	 */
	void deleteFolderByFilePath(String filePath);
}
