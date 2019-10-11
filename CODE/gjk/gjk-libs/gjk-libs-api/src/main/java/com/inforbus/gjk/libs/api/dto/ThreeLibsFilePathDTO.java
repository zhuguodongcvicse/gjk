package com.inforbus.gjk.libs.api.dto;

import lombok.Data;

@Data
public class ThreeLibsFilePathDTO {

	/**
	 * 文件内容
	 */
	private String filePathName;
	
	/**
	 * 文件路径
	 */
	private String filePath;
	
	/**
	 * 文件编码格式
	 */
	private String code;
	
}
