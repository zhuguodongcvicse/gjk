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
package com.inforbus.gjk.comp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.comp.api.entity.CompImg;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
public interface ComponentDetailService extends IService<ComponentDetail> {

	/**
	 * 构件明细简单分页查询
	 * 
	 * @param componentDetail 构件明细
	 * @return
	 */
	IPage<ComponentDetail> getComponentDetailPage(Page<ComponentDetail> page, ComponentDetail componentDetail);

	/**
	 * @Title: upload
	 * @Description: 上传文件
	 * @Author cvics
	 * @DateTime 2019年4月30日 下午5:48:38
	 * @param request
	 * @return
	 */
	ComponentDetail upload(HttpServletRequest request);

	/**
	 * @Title: saveCompDetail
	 * @Description: 保存构件详细信息
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:15:54
	 * @param detail
	 * @return
	 */
	ComponentDetail saveCompDetail(@Param("compDetail") ComponentDetail detail);

	/**
	 * @Title: fileParentCompDetail
	 * @Description: 查询构件详细信息父级记录是否存在
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午4:22:14
	 * @param detail
	 * @return
	 */
	ComponentDetail fileParentCompDetail(@Param("compDetail") ComponentDetail detail);

	/**
	 * @Title: createXmlFile
	 * @Description: 创建xml文件
	 * @Author cvicse
	 * @DateTime 2019年4月29日 上午11:34:34
	 * @param entity
	 * @param token
	 */
	String createXmlFile(XmlEntity entity, String token, String compId);
	/**
	 * @Title: createXmlFile
	 * @Description: 创建xml文件
	 * @Author cvicse
	 * @DateTime 2019年4月29日 上午11:34:34
	 * @param entity
	 * @param token
	 */
	String createXmlFile(XmlEntityMap entMap, String token, String compId, String userCurrent);

	/**
	 * @param ufile
	 * @Title: saveCompImg
	 * @Description: 保存构件图标
	 * @Author xiaohe
	 * @DateTime 2019年5月28日 下午8:19:16
	 * @param obj
	 * @return
	 */
	boolean saveCompImg(MultipartFile ufile, Map<String, Object> map);

	/**
	 * @param compId
	 * @param fileType
	 * @return
	 */
	ComponentDetail findCompXml(@Param("compId") String compId, @Param("fileType") String fileType);

	/**
	 * @Title: editCompDetail
	 * @Description: 修改构架XML
	 * @Author xiaohe
	 * @DateTime 2019年5月24日 下午2:04:44
	 * @param detail 构件
	 */
	void editCompDetail(@Param("compDetail") ComponentDetail detail);

	/**
	 * @Title: listCompDetailByCompId
	 * @Description: 通过构件编号查询文件
	 * @Author cvics
	 * @DateTime 2019年4月29日 上午9:45:30
	 * @param compId
	 * @return
	 */
	List<ComponentDetail> listCompDetailByCompId(@Param("compId") String compId);

	/**
	 * @Title: delFilePath
	 * @Description: 根据文件路径删除文件
	 * @Author xiaohe
	 * @DateTime 2019年7月17日 下午4:36:28
	 * @param bean
	 */
	void delFilePath(List<String> lists);
	
	/**
	 * 根据树节点的构件库详细信息的id查找文件路径
	 * @param id
	 * @return
	 */
	String getFilePathById(String id);

	/**
	 * @Title: getUploadImgUrl
	 * @Description:
	 * @Author Administrator
	 * @DateTime 2019年7月18日 上午10:30:30
	 * @param ufile
	 * @return
	 */
	List<CompFilesVO> getUploadFilesUrl(MultipartFile[] ufile, Map<String,String> bean);

	/**
	 * @Title: saveCompfiles
	 * @Description: 保存构件文件
	 * @Author Administrator
	 * @DateTime 2019年7月18日 上午11:04:34
	 * @param maps
	 * @param paths 
	 * @return 保存路径
	 */
	String saveCompfiles(Map<String, String> maps, List<CompFilesVO> paths);

	CompImg getCompDefaultImg();

	/**
	 * @Title: createSpbFrameFile
	 * @Description: 生成构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月13日 下午4:05:24
	 * @param spbModelXmlFile
	 * @param saveDir 
	 * @throws Exception 
	 */
	public R createSpbFrameFile(String spbModelXmlFile,String headerTemplateFile,String srcTemplateFile, String saveDir);

	/**
	 * @Title: findSpbFrameFile
	 * @Description: 查询构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月13日 下午4:05:24
	 */
	public R findSpbFrameFile();

}
