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

/**
 * 构件库详细信息
 *
 * @author he
 * @date 2019-04-16 16:00:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_component_detail")
public class ThreeLibs extends Model<ThreeLibs> {
	private static final long serialVersionUID = 1L;

	public ThreeLibs() {
		super();
	}

	public ThreeLibs(ThreeLibs detail) {
		this.id = detail.getId();
		this.fileName = detail.getFileName();
		this.fileType = detail.getFileType();
		this.filePath = detail.getFilePath();
		this.paraentId = detail.getParaentId();
		this.version = detail.getVersion();
		this.paraentIds = detail.getParaentIds();
		this.libsId = detail.getLibsId();
		this.compId = detail.getCompId();
	}

	/**
	 * 构件详细信息索引
	 */
	@TableId
	private String id;
	/**
	 * 构件索引
	 */
	private String compId;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 1、文件夹；2、c文件；3、h文件；4、doc文件;5、待扩展
	 */
	private String fileType;
	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 父级ID
	 */
	private String paraentId;
	/**
	 * 父级ID群
	 */
	private String paraentIds;
	/**
	 * 库目录树节点
	 */
	private String libsId;

}
