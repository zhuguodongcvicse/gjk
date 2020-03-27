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
package com.inforbus.gjk.comp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;

import java.util.List;
import java.util.Map;

import com.inforbus.gjk.comp.api.vo.ComponentVO;

import org.apache.ibatis.annotations.Param;

/**
 * 构件开发
 *
 * @author xiaohe
 * @date 2019-04-20 09:26:14
 */
public interface ComponentMapper extends BaseMapper<Component> {
	/**
	 * 构件开发简单分页查询
	 * 
	 * @param component 构件开发
	 * @return
	 */
	IPage<Component> getComponentPage(Page page, @Param("component") Component component);

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
	 * @Title: listCompByUserId
	 * @Description: 通过用户编号查询构件列表
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午9:45:33
	 * @param userId 用户编号
	 * @return
	 */
	List<Component> listCompByUserId(@Param("userId") Integer userId);

	/**
	 * @Title: getComponentCompDetailById
	 * @Description:根据构件ID关联查询
	 * @Author cvicse
	 * @DateTime 2019年4月29日 上午11:29:53
	 * @param id
	 * @return
	 */
	ComponentVO getComponentCompDetailById(String id);

	/**
	 * @Title: listCompByCompId
	 * @Description: 通过构件编号查询构件列表
	 * @Author
	 * @param compId 构件编号
	 * @return
	 */
	Component listCompByCompId(@Param("compId") String compId);

	/**
	 * @Title: saveComp
	 * @Description:
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:07:01
	 * @param component
	 * @return
	 */
	void saveComp(@Param("comp") Component component);

	/**
	 * @Title: getVersionByCompId
	 * @Description: 根据构架ID查找所有的构件版本号
	 * @Author cvics
	 * @DateTime 2019年5月7日 上午11:26:54
	 * @param component
	 * @return
	 */
	List<Component> getVersionByCompId(@Param("comp") Component component);

	/**
	 * @Title: getCompById
	 * @Description: 根据构件ID查询构件
	 * @Author cvics
	 * @DateTime 2019年5月7日 下午3:23:54
	 * @param id
	 * @return
	 */
	Component getCompById(@Param("id") String id);

	/**
	 * @Title: getAllComp
	 * @Description: 获取所有构件的集合
	 * @Author cvics
	 * @DateTime 2019年5月17日 上午10:36:05
	 * @return
	 */
//	List<Component> getAllComp();

	/**
	 * @Title: editComp 修改构件
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年5月24日 下午1:41:34
	 * @param component
	 */
	void editComp(@Param("comp") Component component);

	/**
	 * @Title: findLibs
	 * @Description: 查询库
	 * @Author xiaohe
	 * @DateTime 2019年5月30日 下午6:10:14
	 * @param libs
	 * @param type
	 * @return
	 */
	Map<String, String> findLibs(@Param("libs") String libs, @Param("type") String type);

	/**
	 * @Title: getImgFile
	 * @Description: 获取构件图片
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param imgId 图片Id
	 * @return
	 */
	ComponentDetail getImgFile(@Param("imgId") String imgId);

	/**
	 * @Title: getImgFile
	 * @Description: 获取构件图片
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param imgId 图片Id
	 * @return
	 */
	ComponentDetail getCommImgFile(@Param("imgId") String imgId);

	/**
	 * 查询表列信息
	 *
	 * @param tableName 表名称
	 * @return
	 */
	List<Map<String, String>> queryColumns(String tableName);

	/**
	 * @Title: saveCompAndStruct
	 * @Description: 保存构件与结构体的关系
	 * @Author xiaohe
	 * @DateTime 2019年10月10日 下午1:43:04
	 * @param id       主键
	 * @param compId   构件编号
	 * @param structId 结构体编号
	 */
	void saveCompAndStruct(@Param("id") String id, @Param("compId") String compId, @Param("structId") String structId);

	/**
	 * @Title: deleteCompAndStruct
	 * @Description: 删除构件与结构体的对应关系
	 * @Author xiaohe
	 * @DateTime 2019年10月10日 下午3:40:05
	 * @param compId 构件Id
	 */
	void deleteCompAndStruct(@Param("compId") String compId);

	/**
	 * @Title: selectByComms
	 * @Description: 查询项目对应公共构件
	 * @Author xiaohe
	 * @DateTime 2019年10月12日 下午5:04:23
	 * @param proId
	 * @return
	 */
	List<Component> selectByComms(@Param("proId") String proId);

	/**
	 * 检查更新
	 */
	List<Component> checkComp(@Param("compIdList") List<String> compIdList);

	/**
	 * 保存结构体
	 * 
	 * @param structlibs
	 */
	void saveStructlibs(@Param("structlibs") Structlibs structlibs);

	/**
	 * 根据id 查询结构体数据
	 * 
	 * @param id
	 * @return
	 */
	Structlibs getStructlibsById(@Param("id") String id);

	/**
	 * 根据id获取未删除的平台库数据
	 * 
	 * @param id
	 * @return
	 */
	GjkPlatform getPlatformByIdNotDelete(@Param("id") String id);

	/**
	 * 根据id获取未删除的测试库数据
	 * 
	 * @param id
	 * @return
	 */
	GjkTest getTestByIdNotDelete(@Param("id") String id);

	/**
	 * 根据id获取未删除的算法库数据
	 * 
	 * @param id
	 * @return
	 */
	GjkAlgorithm getAlgorithmByIdNotDelete(@Param("id") String id);

	// **********************************************************//
	// upms模块用到的东西，用于展示库管理模块的树级关系

	/**
	 * 获取构件建模模块的信息
	 * 
	 * @return
	 */
	List<ComponentDetail> getLibsInfo();

	/**
	 * 获取构件建模模块选择的文件夹的信息
	 * 
	 * @return
	 */
	List<ComponentDetail> getLibsFile(@Param("libsId") String libsId);

	/**
	 * 根据libs_id获取构件建模模块属于哪个库信息
	 * 
	 * @return
	 */
	List<ComponentDetail> getLibsFileType(String libsId);

	/**
	 * 根据详细表的comp_id找到主表里的comp_name
	 * 
	 * @return
	 */
	Component getCompNameById(@Param("id") String id);

	/**
	 * 获取所有构件
	 * 
	 * @param id
	 * @return
	 */
	List<String> getCompIdsGroupCompId();

	ComponentDetail getCompDetailByComponentId(@Param("componentId") String componentId,
			@Param("fileName") String fileName);

	List<Component> getCompByCompId(@Param("compId") String compId);

	/*******************************************************************************/
	/**备份数据库 gjk_common_component  gjk_component
	 * @param timeStr
	 */
	void copyCompAndCommCompDb(@Param("timeStr") String timeStr);
}
