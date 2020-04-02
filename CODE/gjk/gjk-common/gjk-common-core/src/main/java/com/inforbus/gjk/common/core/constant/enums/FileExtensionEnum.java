package com.inforbus.gjk.common.core.constant.enums;

import org.apache.commons.lang.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName: FileExtensionEnum
 * @Desc 文件类型
 * @Author cvics
 * @DateTime 2020年4月7日
 */
@Getter
@AllArgsConstructor
public enum FileExtensionEnum {

	/**
	 * @Fields WORD_DOC : word文档格式
	 */
	WORD_DOC("WORD_DOC", ".doc/.docx/.DOC/.DOCX"),
	/**
	 * @Fields EXCEL_DOC : excel文档格式
	 */
	EXCEL_DOC("EXCEL_DOC", ".xlsx/.xls/.XLSX/.XLS"),
	/**
	 * @Fields TXT_DOC : 记事本文件格式
	 */
	TXT_DOC("TXT_DOC", ".txt/.TXT"),

	/**
	 * @Fields DOC_EXTENSION : doc word文档格式
	 */
	DOC_EXTENSION("DOC", ".doc"),
	/**
	 * @Fields DOC_EXTENSION : docx word文档格式
	 */
	DOCX_EXTENSION("DOCX", ".docx"),
	/**
	 * @Fields TXT_EXTENSION : 记事本文件格式
	 */
	TXT_EXTENSION("TXT", ".txt"),
	/**
	 * @Fields EXCEL_EXTENSION : excel文档格式
	 */
	xlsx_EXTENSION("EXCEL_xlsx", ".xlsx"),
	/**
	 * @Fields EXCEL_EXTENSION : excel文档格式
	 */
	XLS_EXTENSION("EXCEL_xls", ".xls");

	/**
	 * @Fields type : 文件类型
	 */
	private final String type;

	/**
	 * @Fields fileFormat : 文件常用格式
	 */
	private final String fileFormat;
	private static String fileType = "_DOC";

	/**
	 * @Title: containsFileType
	 * @Desc 判断文件后缀
	 * @Author xiaohe
	 * @DateTime 2020年4月7日
	 * @param type
	 * @return
	 */
	public static FileExtensionEnum containsFileType(String type) {
		FileExtensionEnum retEnum = null;
		// 循环枚举项
		for (FileExtensionEnum typeEnum : FileExtensionEnum.values()) {
			// 判断文件类型
			if (!typeEnum.getType().endsWith(fileType)) {
				// 判断文件常用格式
				if (StringUtils.isNotEmpty(typeEnum.getFileFormat())
						&& typeEnum.getFileFormat().equalsIgnoreCase(type)) {
					retEnum = typeEnum;
				}
			}

		}
		return retEnum;
	}

	/**
	 * @Title: containsDocFileType
	 * @Desc 判断文件类型
	 * @Author xiaohe
	 * @DateTime 2020年4月7日
	 * @param type
	 * @return
	 */
	public static FileExtensionEnum containsDocFileType(String type) {
		FileExtensionEnum retEnum = null;
		// 循环枚举项
		for (FileExtensionEnum typeEnum : FileExtensionEnum.values()) {
			// 判断文件类型
			if (typeEnum.getType().endsWith(fileType)) {
				// 判断文件常用格式
				if (StringUtils.isNotEmpty(typeEnum.getFileFormat()) && typeEnum.getFileFormat().contains(type)) {
					retEnum = typeEnum;
				}
			}
		}
		return retEnum;
	}
}
