package com.inforbus.gjk.libs.api.dto;

import com.inforbus.gjk.common.core.entity.TreeNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: CompTree
 * @Description: 构件树
 * @Author xiaohe
 * @DateTime 2019年4月28日 下午2:48:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompTree extends TreeNode {

	public CompTree() {

	}

	/**
	 * @param id       自身编号
	 * @param name     显示名字
	 * @param parentId 父级编号
	 */
	public CompTree(String id, String name, String parentId, String parentType, String version) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.label = name;
		this.parentType = parentType;
		this.version = version;
	}

	/**
	 * @param id     自身编号
	 * @param name   显示名字
	 * @param parent 父级菜单
	 */
	public CompTree(String id, String name, CompTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
		this.parentType = parent.getType();
		this.version = parent.getVersion();
	}

	/**
	 * @Fields icon : 图标
	 */
	private String icon;
	private String name;
	private String code;
	private String type;
	private String parentType;
	private String label;
	private Integer sort;
	private String filePath;
	private String version;

}
