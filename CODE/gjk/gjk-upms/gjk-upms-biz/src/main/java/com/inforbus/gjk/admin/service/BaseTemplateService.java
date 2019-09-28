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
package com.inforbus.gjk.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.dto.BaseTemplateDTO;
import com.inforbus.gjk.admin.api.entity.BaseTemplate;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;

/**
 * 基础模板
 *
 * @author xiaohe
 * @date 2019-07-16 08:40:33
 */
public interface BaseTemplateService extends IService<BaseTemplate> {

	/**
	 * 基础模板简单分页查询
	 * 
	 * @param baseTemplate 基础模板
	 * @return
	 */
	IPage<BaseTemplate> getBaseTemplatePage(Page<BaseTemplate> page, BaseTemplate baseTemplate);

	XmlEntityMap editParseXml(BaseTemplate baseTemplate);
	boolean editBaseTemplate(BaseTemplateDTO baseTemplateDTO);

	boolean saveBaseTemplate(BaseTemplateDTO baseTemplateDTO);

	XmlEntityMap parseXml(String baseTemplatePath);

}
