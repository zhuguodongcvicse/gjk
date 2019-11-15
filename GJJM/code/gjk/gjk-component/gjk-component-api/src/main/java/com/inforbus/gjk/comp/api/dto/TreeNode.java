package com.inforbus.gjk.comp.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: TreeNode
 * @Description: 基本树节点
 * @Author xiaohe
 * @DateTime 2019年4月28日 下午2:45:01 
 */
@Data
public class TreeNode {
	protected String id;
	protected String compName;
	protected String parentId;
	protected List<TreeNode> children = new ArrayList<TreeNode>();

	public void add(TreeNode node) {
		children.add(node);
	}
}