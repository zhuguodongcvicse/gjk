package com.inforbus.gjk.comp.api.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ComponentInput {

	/**
	 * variable name
	 */
	private String variableName;
	/**
	 * variable structType
	 */
	private String variableStructType;
	/**
	 * 数据类型
	 */
	private String dataTypeName;
	/**
	 * 序号
	 */
	private String orderNumName;
	/**
	 * 长度
	 */
	private String lengthName;
	/**
	 * 类别
	 */
	private String categoryName;
	/**
	 * 赋值
	 */
	private String voluationName;
	/**
	 * 选择变量
	 */
	private String selectionVariableName;
	/**
	 * 参数
	 */
	private Map<String, String> parameterNumName = new HashMap<String, String>();

}
