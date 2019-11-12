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
package com.inforbus.gjk.pro.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 资源管理
 *
 * @author xiaohe
 * @date 2019-04-16 21:06:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_project")
public class Project extends Model<Project> {
	private static final long serialVersionUID = 1L;

	/**
	 * 项目ID
	 */
	@TableId
	private String id;
	/**
	 * 项目名称
	 */
	private String projectName;
	
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 项目图标
	 */
	private String projectImg;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 默认选取的软件框架
	 */
	private String defaultSoftwareId;
	/**
	 * 默认选取的bsp
	 */
	private String defaultBspId;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	private String delFlag;
	/*
	* 基础模板id json串
	* */
	private String basetemplateIds;

}
