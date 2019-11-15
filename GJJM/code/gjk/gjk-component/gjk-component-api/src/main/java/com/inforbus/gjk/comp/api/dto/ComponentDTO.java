package com.inforbus.gjk.comp.api.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value=Include.NON_NULL)
public class ComponentDTO extends ComponentDetail {
	private static final long serialVersionUID = 1L;

	public ComponentDTO() {
		super();
	}

	public ComponentDTO(ComponentDetail detail) {
		super(detail);
	}
	private String CompImg;
	/**
	 * 构件名
	 */
	private String compName;
	/**
	 * 构件ID
	 */
	private String compId;
	/**
	 * 构件版本
	 */
	private String compVersion;
	/**
	 * 基本属性显示名
	 */
	private String name;
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
	 * 基本属性属性map
	 */
	private Map<String, String> attributeNumName = new HashMap<String, String>();
	/**
	 * 基本属性输入
	 */
	private List<ComponentInput> inputList = new ArrayList<ComponentInput>();
	/**
	 * 基本属性输出
	 */
	private List<ComponentOutput> outputList = new ArrayList<ComponentOutput>();
	/**
	 * 基本属性系数文件
	 */
	private String coefficientFileName;
	/**
	 * 层级属性所属部件
	 */
	private String partsName;
	/**
	 * 层级属性部署配置属性
	 */
	private Map<String, String> dcNumName = new HashMap<String, String>();
	/**
	 * 部署配置所属节点
	 */
	private List<SubordinateNode> subNodes = new ArrayList<SubordinateNode>();
	/**
	 * 性能属性测试结果路径
	 */
	private String perfAttrTestResultPath;
	/**
	 * 性能属性通过率
	 */
	private List<ProCPB> proCPBs = new ArrayList<ProCPB>();
	/**
	 * 资源属性属性map
	 */
	private Map<String, String> resourceAttrNumName = new HashMap<String, String>();

}
