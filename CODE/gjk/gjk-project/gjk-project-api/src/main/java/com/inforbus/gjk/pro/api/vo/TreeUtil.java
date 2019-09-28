package com.inforbus.gjk.pro.api.vo;

import java.util.ArrayList;
import java.util.List;

import com.inforbus.gjk.pro.api.dto.ProjectTree;
import com.inforbus.gjk.pro.api.dto.TreeNode;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;

import lombok.experimental.UtilityClass;

/**
 * @ClassName: ProTreeUtil
 * @Description: 项目树Util
 * @Author xiaohe
 * @DateTime 2019年5月2日 下午12:14:36
 */
@UtilityClass
public class TreeUtil {
	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByLoop(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getId())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId() == it.getParentId()) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * @Title: buildTree
	 * @Description: 通过ComponentDetail创建树形节点
	 * @Author cvics
	 * @DateTime 2019年4月28日 下午3:10:37
	 * @param files
	 * @param root
	 * @return
	 */
	public List<ProjectTree> buildTree(List<ProjectFileVO> files, String root) {
		List<ProjectTree> trees = new ArrayList<>();
		for (ProjectFileVO file : files) {
			trees.add(new ProjectTree(file));
		}
		return TreeUtil.buildByLoop(trees, root);
	}
}
