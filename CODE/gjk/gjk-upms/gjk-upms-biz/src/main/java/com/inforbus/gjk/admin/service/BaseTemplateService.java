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

import java.util.List;

/**
 * 基础模板
 *
 * @author wang
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

	/**
	 * 基础模板编辑功能解析xml文件
	 * @param baseTemplate 基础模板
	 * @return XmlEntityMap
	 */
	XmlEntityMap editParseXml(BaseTemplate baseTemplate);

	/**
	 * 基础模板编辑功能保存数据
	 * @param baseTemplateDTO 基础模板
	 * @return boolean
	 */
	boolean editBaseTemplate(BaseTemplateDTO baseTemplateDTO);

	/**
	 * 基础模板新增功能保存数据
	 * 保存xml文件到指定目录下,以及在数据库添加一条数据
	 * @param baseTemplateDTO 基础模板
	 * @return boolean
	 */
	boolean saveBaseTemplate(BaseTemplateDTO baseTemplateDTO);

	/**
	 * 基础模板新增功能按指定路径读取xml文件
	 * @param baseTemplatePath 将要读取的xml文件的路径
	 * @return XmlEntityMap
	 */
	XmlEntityMap parseXml(String baseTemplatePath);

	/**
	 * 基础模板修改功能
	 * @param baseTemplate 将要读取的xml文件的路径
	 * @return boolean
	 */
	boolean update(BaseTemplate baseTemplate);

	List<BaseTemplate> getBaseTemplate();

}
