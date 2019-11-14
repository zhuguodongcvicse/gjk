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
 * this BSP without specific prior written permission.
 * Author: inforbus
 */
package com.inforbus.gjk.libs.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.libs.api.dto.BSPDTO;
import com.inforbus.gjk.libs.api.dto.BSPTree;
import com.inforbus.gjk.libs.api.dto.SelectFolderDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareTree;
import com.inforbus.gjk.libs.api.entity.BSP;
import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.api.entity.BSPFile;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
public interface BSPService extends IService<BSP> {

	/**
	 * 软件框架库表简单分页查询
	 * 
	 * @param BSP 软件框架库表
	 * @return
	 */
	IPage<BSP> getBSPPage(Page<BSP> page, BSP bsp);

	/**
	 * 保存软件框架库信息
	 * 
	 * @Title: saveBSP
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午1:30:47
	 * @param BSP
	 * @return
	 */
	BSP saveBSP(@Param("bs") BSP bsp);

	/**
	 * 保存软件框架库子表信息
	 * 
	 * @Title: saveBSPDetail
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:50:03
	 * @param BSPDetail
	 * @return
	 */
	BSPDetail saveBSPDetail(@Param("bspDetail") BSPDetail bspDetail);

	/**
	 * 保存软件框架库文件路径子表信息
	 * 
	 * @Title: saveBSPDetail
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:50:03
	 * @param BSPFile
	 * @return
	 */
	BSPFile saveBSPFile(@Param("bsptFile") BSPFile bspFile);

	/**
	 * 根据选择的平台文件对版本进行赋值
	 */
//  List<Software> setVersionSize( String platformId);
	BSP setVersionSize();

	/**
	 * 文件夹的解析存储
	 * 
	 * @param selectFolderDTO
	 * @return
	 */
	void selectFolder(List<SelectFolderDTO> selectFolderDTO);

	/**
	 * 获取BSP库表封装后的DTO分页查询
	 * 
	 * @param page
	 * @param software
	 * @return
	 */

	IPage<BSPDTO> getBSPDTOPage(Page<BSP> page, BSP bsp);

	/**
	 * 获取BSP的树
	 * 
	 * @return
	 */
	List<BSPTree> getBSPTree();

	/**
	 * 根据id级联删除bsp文件表
	 */
	void removeBspFile(@Param("bspId") String bspId);

	/**
	 * 根据id级联删除bsp详细表
	 */
	void removeBspDetail(@Param("bspId") String bspId);

	/**
	 * 根据ID获取BSP的树
	 * 
	 * @return
	 */
	List<BSPTree> getTreeById(String id);

}
