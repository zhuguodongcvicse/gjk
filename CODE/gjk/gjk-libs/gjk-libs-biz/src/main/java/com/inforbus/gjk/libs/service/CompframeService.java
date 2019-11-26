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

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.entity.Compframe;

/**
 * 构件框架库
 *
 * @author xiaohe
 * @date 2019-11-25 15:45:11
 */
public interface CompframeService extends IService<Compframe> {

	/**
	 * 构件框架库简单分页查询
	 * 
	 * @param compframe 构件框架库
	 * @return
	 */
	IPage<Compframe> getCompframePage(Page<Compframe> page, Compframe compframe);

	R<?> saveCompFrame(MultipartFile[] ufile, Map<String, Object> resMap);

}
