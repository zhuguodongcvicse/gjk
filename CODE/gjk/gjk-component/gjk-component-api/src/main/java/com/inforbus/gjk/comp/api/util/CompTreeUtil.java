package com.inforbus.gjk.comp.api.util;

import java.util.ArrayList;
import java.util.List;

import com.inforbus.gjk.common.core.entity.TreeNode;
import com.inforbus.gjk.common.core.util.TreeUtil;
import com.inforbus.gjk.comp.api.dto.CompTree;
import com.inforbus.gjk.comp.api.vo.CompDetailVO;
import com.inforbus.gjk.comp.api.vo.CompVO;

import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CompTreeUtil extends TreeUtil {

	/**
	 * 两层循环实现建comp树形选择器的树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByLoop(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			String parentID = treeNode.getParentId();
			if (root.equals(parentID)) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getNodeName())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.getChildren().add(it);
				}
			}
		}
		return trees;
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
	public List<CompTree> buildTree(List<CompDetailVO> files, String root) {
		List<CompTree> trees = new ArrayList<>();
		CompTree node;
		for (CompDetailVO file : files) {
			node = new CompTree();
			node.setId(file.getFileId());
			node.setParentId(file.getParentId());
			node.setLabel(file.getFileName());
			node.setType(file.getFileType());
			node.setFilePath(file.getFilePath());
			node.setVersion(file.getVersion());
			trees.add(node);
		}
		System.out.println(JSONUtil.parse(trees));
		return TreeUtil.buildByLoop(trees, root);
	}

	/**
	 * @Title: buildCompTree
	 * @Description: 建comp树形选择器的树
	 * @Author cvics
	 * @DateTime 2019年5月17日 上午10:33:10
	 * @param vos
	 * @param root
	 * @return
	 */
	public List<CompTree> buildCompTree(List<CompVO> vos, String root) {
		List<CompTree> trees = new ArrayList<>();
		CompTree node;
		for (CompVO comp : vos) {
			node = new CompTree();
			node.setId(comp.getId());
			node.setNodeName(comp.getCompName());
			node.setParentId(comp.getTreeParent());
			node.setLabel(comp.getCompName());
			trees.add(node);
		}
		System.out.println(JSONUtil.parse(trees));
		return CompTreeUtil.buildByLoop(trees, root);
	}

}
