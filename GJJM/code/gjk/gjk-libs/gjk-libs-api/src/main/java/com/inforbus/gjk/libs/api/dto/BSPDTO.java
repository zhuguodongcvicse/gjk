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
package com.inforbus.gjk.libs.api.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.inforbus.gjk.libs.api.entity.BSP;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
@Data
public class BSPDTO {

	public BSPDTO(BSP bsp) {
		this.id = bsp.getId();
		this.bspName = bsp.getBspName();
		this.version = bsp.getVersion() + "";
		this.filePath = bsp.getFilePath();
		this.applyState = bsp.getApplyState();
		this.applyDesc = bsp.getApplyDesc();
		this.createTime = bsp.getCreateTime();
		this.updateTime = bsp.getUpdateTime();
		this.description = bsp.getDescription();
		this.delFlag = bsp.getDelFlag();
	}

	/**
	 * 主键id
	 */
	@TableId
	private String id;

	/**
	 * 软件框架库名字
	 */
	private String bspName;
	/**
	 * 版本
	 */
	private String version;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 审批状态0：未审批，1：已审批
	 */
	private String applyState;
	/**
	 * 审批描述
	 */
	private String applyDesc;
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
	 * 删除标识0-正常，1-删除
	 */
	private String delFlag;

}
