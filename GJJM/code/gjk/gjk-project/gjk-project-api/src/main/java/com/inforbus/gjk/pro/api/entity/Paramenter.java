package com.inforbus.gjk.pro.api.entity;

import lombok.Data;

/**
 * 参数实体类
 * @author 
 *
 */
@Data
public class Paramenter {
	/**
	 * 参数名称
	 */
	private String name; 
	/**
	 * 数据类型
	 */
	private String dataType;
	/**
	 * 序号
	 */
	private String number;
	/**
	 * 长度
	 */
	private String length;
	/**
	 * 类别
	 */
	private String type;
	/**
	 * 赋值
	 */
	private String assignment;
	/**
	 * 选择变量
	 */
	private String selectVariable;
}
