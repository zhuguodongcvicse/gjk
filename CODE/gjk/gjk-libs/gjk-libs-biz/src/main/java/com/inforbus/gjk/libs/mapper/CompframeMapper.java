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
import com.inforbus.gjk.libs.api.entity.Compframe;
import org.apache.ibatis.annotations.Param;

/**
 * 构件框架库
 *
 * @author xiaohe
 * @date 2019-11-25 15:45:11
 */
public interface CompframeMapper extends BaseMapper<Compframe> {
	/**
	 * 构件框架库简单分页查询
	 * 
	 * @param compframe 构件框架库
	 * @return
	 */
	IPage<Compframe> getCompframePage(Page page, @Param("compframe") Compframe compframe);
	/**
	 * @Title: insertCompframe
	 * @Description: 插入构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月26日 下午2:04:44
	 * @param frame 
	 * @return
	 */
	void insertCompframe( @Param("compframe")Compframe frame);
	/**
	 * @Title: insertCompframePlatform
	 * @Description: 插入构件框架与平台关系
	 * @Author xiaohe
	 * @DateTime 2019年11月26日 下午2:04:44
	 * @param frameId 构件框架Id
	 * @param platformId 平台Id
	 * @return
	 */
	void insertCompframePlatform(@Param("frameId") String frameId, @Param("platformId") String platformId);


}
