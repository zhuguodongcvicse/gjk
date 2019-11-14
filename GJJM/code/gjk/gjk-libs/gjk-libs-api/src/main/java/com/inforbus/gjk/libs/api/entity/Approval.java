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
package com.inforbus.gjk.libs.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 审批
 *
 * @author zhang
 * @date 2019-06-03 13:36:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_approval")
public class Approval extends Model<Approval> {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private String id;
	/**
	 * 用户
	 */
	private String userId;
	/**
	 * 申请编号
	 */
	private String applyId;
	/**
	 * 申请类型
	 */
	private String applyType;
	/**
	 * 操作库类型
	 */
	private String libraryType;
	/**
	 * 申请时间
	 */
	private LocalDateTime applyTime;
	/**
	 * 审批ID
	 */
	private String applyUserId;
	/**
	 * 审批状态
	 */
	private String approvalState;
	/**
	 * 审批时间
	 */
	private LocalDateTime approvalTime;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 备注
	 */
	private String description;

}
