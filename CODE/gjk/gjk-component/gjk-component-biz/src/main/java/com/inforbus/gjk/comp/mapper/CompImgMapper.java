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
import com.inforbus.gjk.comp.api.entity.CompImg;
import org.apache.ibatis.annotations.Param;

/**
 * 图片
 *
 * @author xiaohe
 * @date 2019-08-02 16:32:26
 */
public interface CompImgMapper extends BaseMapper<CompImg> {
	/**
	 * 图片简单分页查询
	 * 
	 * @param compImg 图片
	 * @return
	 */
	IPage<CompImg> getCompImgPage(Page page, @Param("compImg") CompImg compImg);

	/**
	 * @Title: saveCompImg
	 * @Description: 
	 * @Author xiaohe
	 * @DateTime 2019年8月5日 上午9:57:35
	 * @param img 
	 */
	void saveCompImg( @Param("compImg") CompImg img);

}
