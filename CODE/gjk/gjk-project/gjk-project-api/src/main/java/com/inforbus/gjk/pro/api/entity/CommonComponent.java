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
 * 公共构件库详细表
 *
 * @author pig code generator
 * @date 2019-06-10 14:25:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_common_component")
public class CommonComponent extends Model<CommonComponent> {
	private static final long serialVersionUID = 1L;

	/**
	* 
	*/
	@TableId
	private String id;
	/**
	 * 构件编号
	 */
	private String compId;
	/**
	 * 构件名称
	 */
	private String compName;
	/**
	 * 构件函数名
	 */
	private String compFuncname;
	/**
	 * 用户
	 */
	private String userId;
	/**
	 * 版本
	 */
	private String version;
	/**
	 * 构件图片
	 */
	private String compImg;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 0-正常，1-删除
	 */
	private String delFlag;

}
