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

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.comp.api.dto.CompTree;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.vo.CompDetailVO;
import com.inforbus.gjk.comp.api.vo.CompDictVO;
import com.inforbus.gjk.comp.api.vo.CompVO;
import com.inforbus.gjk.comp.api.vo.ComponentVO;

/**
 * 构件
 *
 * @author xiaohe
 * @date 2019-04-17 16:01:51
 */
public interface ComponentService extends IService<Component> {

	/**
	 * 构件开发简单分页查询
	 * 
	 * @param component 构件开发
	 * @return
	 */
	IPage<Component> getComponentPage(Page<Component> page, Component component);

	/**
	 * 删除构件并删除构件详细信息
	 * 
	 * @param compId
	 * @return
	 */
	boolean deleteCompAndCompDetail(String compId);

	/**
	 * @Title: getComponentCompDetailById
	 * @Description: 根据构件ID关联查询
	 * @Author cvicse
	 * @DateTime 2019年4月29日 上午11:34:29
	 * @param id
	 * @return
	 */
	ComponentVO getComponentCompDetailById(String id);

	/**
	 * @Title: getCompByUserId
	 * @Description: 根据用户编号查询构件菜单
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午11:15:24
	 * @param userId 用户编号
	 * @return 构件树（包含文件列表）
	 */
	List<CompDetailVO> getCompByUserId(Integer userId);

	/**
	 * @Title: getCompByCompId
	 * @Description: 根据构件编号查询构件菜单
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午11:15:24
	 * @param compId 构件编号
	 * @return 构件树（包含文件列表）
	 */
	List<CompDetailVO> getCompByCompId(String compId, boolean isShowCompXml);

	/**
	 * @Title: getCompAndDetailMap
	 * @Description:根据用户查询构件及明细
	 * @Author xiaohe
	 * @DateTime 2019年5月5日 下午2:41:09
	 * @param userId 用户编号
	 * @return map<>
	 */
	Map<String, Object> getCompAndDetailMap(String userId);

	/**
	 * @Title: deleteCompById
	 * @Description:根据ID删除构件
	 * @Author cvicse
	 * @DateTime 2019年4月29日 上午11:30:38
	 * @param id
	 * @return
	 */
	boolean deleteCompById(String id);

	/**
	 * @Title: saveComp
	 * @Description:保存构件
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:06:53
	 * @param component
	 * @return
	 */
	Component saveComp(@Param("comp") Component component);

	/**
	 * @Title: getVersionByCompId
	 * @Description: 根据构架ID查找所有的构件版本号
	 * @Author cvics
	 * @DateTime 2019年5月7日 上午11:26:54
	 * @param component
	 * @return
	 */
	List<String> getVersionByCompId(@Param("comp") Component component);

	/**
	 * @Title: getCompSelectTree
	 * @Description: 返回构件树形选择器的树的节点集合
	 * @Author cvics
	 * @DateTime 2019年5月17日 上午11:34:08
	 * @return
	 */
	List<CompTree> getCompSelectTree();

	/**
	 * @Title: getCompFiles
	 * @Description: 获取构件文件
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param compId 构件Id
	 * @return
	 */
	Map<String, Object> getCompFiles(String compId);

	/**
	 * @Title: getImgFileStr
	 * @Description: 获取构件图片
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param imgId 图片Id
	 * @return
	 */
	String getImgFileStr(String imgId);

	/**
	 * @Title: getImgFile
	 * @Description: 获取构件图片
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param imgId 图片Id
	 * @return FileInputStream文件流
	 */
	FileInputStream getImgFile(String imgId, StringBuilder fileName);

	/**
	 * 获取构件ID和构件名称对应的字典
	 * 
	 * @return
	 */
	List<CompDictVO> getCompDictList();

	XmlEntityMap analysisXmlFile(String filePath);

	/**
	 * 解析传入的压缩文件
	 * 
	 * @param ufile
	 */
	List<Component> analysisZipFile(MultipartFile ufile, String userId, String userName);

	/**
	 * 检查更新
	 * 
	 * @param obj
	 */
	Map<String, Object> checkComp(List<Object> obj);

	/**
	 * @Title: getImgFile
	 * @Description: listCompByUserId
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param userId 用户Id
	 */
	List<Component> listCompByUserId(Integer userId);

	/**
	 * 判断选择的库目录文件是否存在
	 * 
	 * @param id
	 * @return
	 */
	String isSelectLibs(String id);
}
