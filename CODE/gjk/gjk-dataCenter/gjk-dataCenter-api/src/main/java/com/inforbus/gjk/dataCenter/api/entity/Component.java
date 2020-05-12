package com.inforbus.gjk.dataCenter.api.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Component {

	/**
	 * 构件ID
	 */
	private String compId;
	/**
	 * 构件名
	 */
	private String compName;
	/**
	 * 基本属性函数名
	 */
	private String functionName;
	/**
	 * 基本属性构件编号
	 */
	private String compNum;
	/**
	 * 基本属性函数路径
	 */
	private String functionPath;
	/**
	 * 输入参数
	 */
	private List<Paramenter> inParamenter = new ArrayList<>();
	/**
	 * 输出参数
	 */
	private List<Paramenter> outParamenter = new ArrayList<>();
}
