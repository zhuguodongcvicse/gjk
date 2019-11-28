package com.inforbus.gjk.common.core.util.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * 输入输出参数（树结构）
 * @ClassName: ParamTreeVO
 * @Description:输入输出参数（树结构）
 * @Author CVICSE-COMMON
 * @DateTime 2019年5月28日 下午9:43:12
 */
@Data
public class ParamTreeVO {
	
	public ParamTreeVO() {
		super();
	}
	
	public ParamTreeVO(String id, String dbId, String parentId, String childrenIds, String fparamName,
			String fparamType, Integer sort, Double version, String structClassify, String assigParamName) {
		super();
		this.id = id;
		this.dbId = dbId;
		this.parentId = parentId;
		this.childrenIds = childrenIds;
		this.fparamName = fparamName;
		this.fparamType = fparamType;
		this.sort = sort;
		this.version = version;
		this.structClassify = structClassify;
		this.assigParamName = assigParamName;
	}
	public ParamTreeVO(String id, String dbId, String parentId, String childrenIds, String fparamName,
			String fparamType, String paramRemarks,Integer sort, Double version, String structClassify, String assigParamName) {
		super();
		this.id = id;
		this.dbId = dbId;
		this.parentId = parentId;
		this.childrenIds = childrenIds;
		this.fparamName = fparamName;
		this.fparamType = fparamType;
		this.paramRemarks = paramRemarks;
		this.sort = sort;
		this.version = version;
		this.structClassify = structClassify;
		this.assigParamName = assigParamName;
	}

	/**
	 * id
	 */
	private String id = "";
	
	/**
	 * 数据库id
	 */
	private String dbId = "";
	
	/**
	 * 父级id
	 */
	private String parentId = "";
	
	/**
     * 子结构体id集合
     */
    private String childrenIds = "";
	
    /**
	 * 参数名
	 */
	private String fparamName = "";
	
	/**
	 * 参数类型
	 */
	private String fparamType = "";
	
	/**
	 * 结构体排序值
	 */
	private Integer sort;
	
	/**
	 * 版本
	 */
	private Double version;
	
	/**
	 * 结构体类别（0--公共结构体 1--组件参数结构体 2--其他结构体）
	 */
	private String structClassify;
	
	/**
	 * 赋值名称
	 */
	private String assigParamName;
	
	/**
	 * 序号（XML中）
	 */
	private Integer index = 0;
	
	/**
	 * 数据长度
	 */
	private String length = "";
		
	/**
	 * 赋值类别
	 */
	private String assignType = "";
	
	/**
	 * 值
	 */
	private String fparamValue = "";
	
	/**
	 * 赋值
	 */
	private String assignValue = "";
	
	/**
	 * 选择变量
	 */
	private String selectVar = "";

	/**
	 * 结构体成员
	 */
	private List<ParamTreeVO> children = Lists.newArrayList();
	/**
	 * 入库标识
	 */
	private String storageFlag;
	
	/**
	 * 参数注释
	 */
	private String paramRemarks;

}
