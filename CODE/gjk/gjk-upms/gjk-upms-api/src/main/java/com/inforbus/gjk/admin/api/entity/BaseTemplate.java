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
package com.inforbus.gjk.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础模板
 *
 * @author xiaohe
 * @date 2019-07-16 08:40:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_base_template")
public class BaseTemplate extends Model<BaseTemplate> {
	private static final long serialVersionUID = 1L;

	/**
	 * 模板标识
	 */
	@TableId(value = "temp_id",type = IdType.INPUT)
	private String tempId;
	/**
	 * 模板名称
	 */
	private String tempName;
	/**
	 * 模板路径
	 */
	private String tempPath;
	/**
	 * libs_temp_type（配置字典）
	 */
	private String tempType;
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
	private String delFlag;
	/**
	* 
	*/
	private String remarks;


	/**
	 *控制版本字段
	 */
	private String tempVersion;
}
