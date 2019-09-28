package com.inforbus.gjk.libs.api.util;

import java.util.ArrayList;
import java.util.List;

import com.inforbus.gjk.common.core.util.TreeUtil;
import com.inforbus.gjk.libs.api.dto.BSPTree;
import com.inforbus.gjk.libs.api.entity.BSP;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BSPTreeUtil extends TreeUtil {

	/**
	 * @Title: buildTree
	 * @Description: 通过创建树形节点
	 * @Author cvics
	 * @DateTime 2019年4月28日 下午3:10:37
	 * @param bsps
	 * @param root
	 * @return
	 */
	public List<BSPTree> buildTree(List<BSP> bsps, String root) {
		List<BSPTree> trees = new ArrayList<>();
		BSPTree tree = null;
		for (BSP bsp : bsps) {
			tree = new BSPTree(bsp, "-1");
			trees.add(tree);
		}
		return TreeUtil.buildByLoop(trees, root);
	}

}
