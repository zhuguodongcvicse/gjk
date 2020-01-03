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
package com.inforbus.gjk.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 结构体库
 *
 * @author hu
 * @date 2019-06-14 10:51:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_structlibs")
public class Structlibs extends Model<Structlibs> {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private String id;
	/**
	 * 父ID
	 */
	private String parentId;
	/**
	 * 结构体类型根节点id
	 */
	private String rootId;
	/**
	 * 子结构体id集合
	 */
	private String childrenIds;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 类型（0--基本类型 1--基本指针类型 2--结构体类型 3--结构体指针类型
	 */
	private String type;
	/**
	 * 数据类型
	 */
	private String dataType;
	/**
	 * 结构体类型（0--公共结构体 1--组件参数结构体 2--其他结构体）
	 */
	private String structType;
	/**
	 * 版本
	 */
	private Double version;
	/**
	 * 数据长度
	 */
	private Integer dataLength;
	/**
	 * 测试权限标识
	 */
	private String permission;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 逻辑删除标记(0--正常 1--删除)
	 */
	private String delFlag;
	/**
	 * 逻辑入库标记（0--未入库；1--已入库；2,3--其他）
	 */
	private String storageFlag;
	/**
	 * 审批描述
	 */
	private String applyDesc;
	/**
	 * 结构体注释
	 */
	private String paramRemarks;
	/**
	 * 用户编号
	 */
	private Integer userId;
	

}
