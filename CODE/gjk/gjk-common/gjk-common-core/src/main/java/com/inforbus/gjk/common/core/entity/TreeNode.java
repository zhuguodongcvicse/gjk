package com.inforbus.gjk.common.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: TreeNode
 * @Description: 基本树节点
 * @Author xiaohe
 * @DateTime 2019年4月28日 下午2:45:01 
 */
@Data
public class TreeNode implements Serializable{
	protected String id;
	protected String nodeName;
	protected String parentId;
	protected List<TreeNode> children = new ArrayList<TreeNode>();

	public void add(TreeNode node) {
		children.add(node);
	}
}
