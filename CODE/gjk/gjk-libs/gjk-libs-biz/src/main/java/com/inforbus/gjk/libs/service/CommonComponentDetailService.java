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
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.api.vo.CommCompDetailVO;

/**
 * 公共构件库表
 *
 * @author pig code generator
 * @date 2019-06-10 14:24:03
 */
public interface CommonComponentDetailService extends IService<CommonComponentDetail> {

	/**
	 * 公共构件库表简单分页查询
	 * 
	 * @param commonComponentDetail 公共构件库表
	 * @return
	 */
	IPage<CommonComponentDetail> getCommonComponentDetailPage(Page<CommonComponentDetail> page,
			CommonComponentDetail commonComponentDetail);

	/**
	 * 保存公共构件详细表数据
	 * 
	 * @param commonComponentDetail
	 */
	boolean saveCommonCompDetailList(List<CommonComponentDetail> commonComponentDetail, CommonComponent component, String userCurrent);

	/**
	 * 获取构件集合对应的所有构件详细信息记录
	 * 
	 * @param compList
	 * @return
	 */
	List<CommonComponentDetail> getAllCompDetailByCompId(@Param("compList") List<CommonComponent> compList);
	/**
	 * @Title: getCommCompView
	 * @Description: 根据构件编号查询构件详情及文件
	 * @Author xiaohe
	 * @DateTime 2019年10月17日 上午11:02:23
	 * @param compId 构件编号
	 * @return 
	 */
	Map<String, Object> getCommCompView(CommonComponent compId);

	/**
	 * @Title: getCommCompViewTree
	 * @Description: 查询构件树
	 * @Author xiaohe
	 * @DateTime 2019年10月17日 下午4:26:28
	 * @param compId
	 * @return 
	 */
	List<CommCompDetailVO> getCommCompViewTree(CommonComponent comp);
	/**
	 * 根据libsid查询到已入库的表中是否选中三个库
	 * @param libsId
	 * @return
	 */
	List<CommonComponentDetail> findCommonComponentDetailByLibsId(String libsId);
}
