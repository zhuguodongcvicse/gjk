package com.inforbus.gjk.libs.api.dto;

import com.inforbus.gjk.common.core.entity.Structlibs;

import lombok.Data;

@Data
public class StructDTO extends StructTreeNode {

	protected String dbId;

	/**
	 * 结构体类型（0--公共结构体 1--组件参数结构体 2--其他结构体）
	 */
	private String structClassify;
	/**
	 * 版本
	 */
	private Double version;

	/**
	 * 排序值
	 */
	private Integer sort;

	/**
	 * 参数名
	 */
	private String fparamName;

	/**
	 * 参数类型
	 */
	private String fparamType;
	
	/**
	 * 赋值参数
	 */
	private String assigParamName;
	/**
	 * 参数注释
	 */
	private String paramRemarks;
	private String queryParam;

	public StructDTO() {
		super();
	}

	public StructDTO(Structlibs struct) {
		this.id =struct.getRootId();
		this.parentId = struct.getParentId();
		this.childrenIds = struct.getChildrenIds();
		this.version = struct.getVersion();
		this.sort = struct.getSort();
		this.dbId =  struct.getId();
		this.fparamName = struct.getName();
		this.structClassify = struct.getStructType();
		this.fparamType = struct.getDataType();
		this.paramRemarks=struct.getParamRemarks();
	}

}
