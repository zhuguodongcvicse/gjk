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
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-07-22 13:39:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_app")
public class App extends Model<App> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private String id;
	/**
	 * app文件全路径
	 */
	private String filePath;
	/**
	 * APP名
	 */
	private String fileName;
	/**
	 * 流程id
	 */
	private String processId;
	/**
	 * 背景图片路径
	 */
	private String backPath;
	/**
	 * 组件名称-对应平台大类属性的对应关系
	 */
	private String partnamePlatform;
	/**
	 * 系统配置文件路径
	 */
	private String sysconfigFilePath;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * app组件运行状态，0-初始值
	 */
	private String appState;
	/**
	 * 本地部署方案Boolean值
	 */
	private String localDeploymentPlan;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 删除标识，0-正常，1-删除
	 */
	private String delFlag;

}
