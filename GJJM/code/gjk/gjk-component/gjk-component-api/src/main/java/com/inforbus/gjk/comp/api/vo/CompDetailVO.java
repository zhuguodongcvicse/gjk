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
package com.inforbus.gjk.comp.api.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;

/**
 * 构件文件VO
 *
 * @author he
 * @date 2019-04-16 16:00:57
 */
@Data
public class CompDetailVO extends Model<CompDetailVO> {
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields fileId : 构件文件编号
	 */
	private String fileId;
	/**
	 * @Fields fileName : 文件名称
	 */
	private String fileName;
	/**
	 * @Fields fileType : 文件类型
	 */
	private String fileType;
	/**
	 * @Fields filePath : 文件路径
	 */
	private String filePath;
	/**
	 * @Fields parentId :父级编号
	 */
	private String parentId;
	/**
	 * @Fields version : 版本
	 */
	private String version;
	/**
	 * @Fields delFlag : 0--正常 1--删除
	 */
	private String delFlag;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CompDetailVO) {
			String targetMenuId = ((CompDetailVO) obj).getFileId();
			return fileId.equals(targetMenuId);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return fileId.hashCode();
	}

	public CompDetailVO(String fileId, String fileName, String fileType, String filePath, String parentId,
			String version) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.parentId = parentId;
		this.version = version;
	}

	public CompDetailVO() {

	}

}
