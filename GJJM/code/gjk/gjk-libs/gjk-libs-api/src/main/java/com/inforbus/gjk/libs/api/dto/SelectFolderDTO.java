package com.inforbus.gjk.libs.api.dto;

import lombok.Data;

@Data
public class SelectFolderDTO {

	/**
	 * 文件名
	 */
	private String fileName;
	
	/**
	 * 文件全路径
	 */
	private String allFilePath;
	
}
