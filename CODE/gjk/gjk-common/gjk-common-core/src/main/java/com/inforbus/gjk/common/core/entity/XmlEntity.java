package com.inforbus.gjk.common.core.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value=Include.NON_NULL)
public class XmlEntity {
	
	/**
	 * 前台判断，后台不做实用
	 */
	private boolean isshow;
	/**
	 * 标签类型
	 */
	private String labelType;
	/**
	 * 标签类型
	 */
	private String structId;
	
	/**
	 * 标签动作
	 */
	private String actionType;

	/**
	 * 标签名
	 */
	private String lableName;
	
	/**
	 * 标签内容
	 */
	private String textContent;

	/**
	 * 属性Id
	 */
	private String attributeId;

	/**
	 * 属性Id的value
	 */
	private String attributeIdValue;

	/**
	 * 属性name
	 */
	private String attributeName;

	/**
	 * 属性name的value
	 */
	private String attributeNameValue;

	
	/**
	 * 属性StructType
	 */
	private String attributeStructTypeName;

	/**
	 * 属性StructType的value
	 */
	private String attributeStructTypeValue;

	/**
	 * 属性CmpName
	 */
	private String attributeCmpNameName;

	/**
	 * 属性CmpName的value
	 */
	private String attributeCmpNameValue;

	/**
	 * 属性ProRate
	 */
	private String attributeProRateName;

	/**
	 * 属性ProRate的value
	 */
	private String attributeProRateValue;

	/**
	 * 属性CoreNum
	 */
	private String attributeCoreNumName;

	/**
	 * 属性CoreNum的value
	 */
	private String attributeCoreNumValue;
	
	/**
	 * 属性End
	 */
	private String attributeEndName;

	/**
	 * 属性End的value
	 */
	private String attributeEndValue;
	
	/**
	 * 属性Start
	 */
	private String attributeStartName;

	/**
	 * 属性Start的value
	 */
	private String attributeStartValue;
	
	/**
	 * 属性length
	 */
	private String attributeLengthName;

	/**
	 * 属性length的value
	 */
	private String attributeLengthValue;

	/**
	 * 下级标签list
	 */
	private List<XmlEntity> xmlEntitys;
}
