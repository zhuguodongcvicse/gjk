package com.inforbus.gjk.common.core.util.vo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Data;

/**
 * 头文件解析实体
 * 
 * @author hu
 *
 */
@Data
public class HeaderFileTransVO {

	/**
	 * 头文件路径
	 */
	private String headerPath;

	/**
	 * 输入参数<变量名，变量类型>字典
	 */
	private Map<String, String> inputParaNameToType = Maps.newHashMap();

	/**
	 * 输出参数<变量名，变量类型>字典
	 */
	private Map<String, String> outputParaNameToType = Maps.newHashMap();

	/**
	 * 结构体参数解析字典
	 */
	private Map<String, List<String>> structTypeToMember = Maps.newHashMap();

	/**
	 * 参数顺序字典
	 */
	private Map<String, Integer> indexToParaName = Maps.newHashMap();
}
