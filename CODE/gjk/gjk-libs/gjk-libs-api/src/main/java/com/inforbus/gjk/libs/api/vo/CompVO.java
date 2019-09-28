package com.inforbus.gjk.libs.api.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompVO extends Model<CompVO> {

	private static final long serialVersionUID = 1L;

	public CompVO(String id, String compName, String version, String treeParent) {
		super();
		this.id = id;
		this.compName = compName;
		this.version = version;
		this.treeParent = treeParent;
	}

	/**
	 * ID
	 */
	private String id;
	/**
	 * 构件编号
	 */
	private String compId;
	/**
	 * 构件名称
	 */
	private String compName;
	/**
	 * 构件函数名
	 */
	private String compFuncname;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 版本
	 */
	private String version;
	/**
	 * 构件图标
	 */
	private String compImg;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 构件树父节点
	 */
	private String treeParent;
}
