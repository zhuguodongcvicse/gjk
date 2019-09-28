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
package com.inforbus.gjk.libs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 公共构件库表
 *
 * @author pig code generator
 * @date 2019-06-10 14:24:03
 */
public interface CommonComponentDetailMapper extends BaseMapper<CommonComponentDetail> {
	/**
	 * 公共构件库表简单分页查询
	 * 
	 * @param commonComponentDetail 公共构件库表
	 * @return
	 */
	IPage<CommonComponentDetail> getCommonComponentDetailPage(Page page,
			@Param("commonComponentDetail") CommonComponentDetail commonComponentDetail);

	/**
	 * 保存公共构件详细表数据
	 * 
	 * @param commonComponentDetail
	 */
	void saveCommonCompDetail(@Param("compDetail") CommonComponentDetail commonComponentDetail);

	/**
	 * 获取构件集合对应的所有构件详细信息记录
	 * 
	 * @param compList
	 * @return
	 */
	List<CommonComponentDetail> getAllCompDetailByCompId(@Param("compList") List<CommonComponent> compList);
}
