package com.inforbus.gjk.libs.api.util;

import java.util.ArrayList;
import java.util.List;

import com.inforbus.gjk.common.core.util.TreeUtil;
import com.inforbus.gjk.libs.api.dto.SoftwareTree;
import com.inforbus.gjk.libs.api.entity.Software;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SoftwareTreeUtil extends TreeUtil {

	/**
	 * @Title: buildTree
	 * @Description: 通过创建树形节点
	 * @Author cvics
	 * @DateTime 2019年4月28日 下午3:10:37
	 * @param softwares
	 * @param root
	 * @return
	 */
	public List<SoftwareTree> buildTree(List<Software> softwares, String root) {
		List<SoftwareTree> trees = new ArrayList<>();
		SoftwareTree tree = null;
		for (Software software : softwares) {
			tree = new SoftwareTree(software, "-1");
			trees.add(tree);
		}
//		System.out.println(JSONUtil.parse(trees));
		return TreeUtil.buildByLoop(trees, root);
	}

}
