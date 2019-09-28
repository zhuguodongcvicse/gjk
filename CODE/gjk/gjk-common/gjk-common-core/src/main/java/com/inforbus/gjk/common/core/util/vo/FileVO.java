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
package com.inforbus.gjk.common.core.util.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件VO
 *
 * @author he
 * @date 2019-04-16 16:00:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileVO extends Model<FileVO> {
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields fileId : 文件Id
	 */
	private String fileId;
	/**
	 * @Fields fileName : 文件名称
	 */
	private String fileName;
	/**
	 * @Fields filePath : 文件路径
	 */
	private String filePath;
	/**
	 * @Fields parentId :父级编号
	 */
	private String parentId;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileVO) {
			String targetMenuId = ((FileVO) obj).getFileId();
			return fileId.equals(targetMenuId);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return fileId.hashCode();
	}

	public FileVO() {

	}

	public FileVO(String fileId, String fileName, String filePath, String parentId) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.filePath = filePath;
		this.parentId = parentId;
	}

}
