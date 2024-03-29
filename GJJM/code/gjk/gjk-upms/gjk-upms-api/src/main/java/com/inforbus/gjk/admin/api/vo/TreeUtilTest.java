/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inforbus.gjk.admin.api.vo;


import com.inforbus.gjk.admin.api.dto.AlgorithmTree;
import com.inforbus.gjk.admin.api.dto.MenuTree;
import com.inforbus.gjk.admin.api.dto.PlatformTree;
import com.inforbus.gjk.admin.api.dto.TestTree;
import com.inforbus.gjk.admin.api.dto.TreeNodeAl;
import com.inforbus.gjk.admin.api.dto.TreeNodeTest;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.entity.SysMenu;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2017年11月9日23:34:11
 */
@UtilityClass
public class TreeUtilTest {
	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNodeTest> List<T> buildByLoop(List<T> treeNodes, Object root) {

		List<T> trees = new ArrayList<>();

		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}

			for (T it : treeNodes) {
				if (treeNode.getId().equals(it.getParentId())  ) {
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
	public <T extends TreeNodeAl> List<T> buildByRecursive(List<T> treeNodes, Object root) {
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
	public <T extends TreeNodeAl> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}
	/**
	 * 通过gjkTest创建树形节点
	 *
	 * @param gjkTests
	 * @param root
	 * @return
	 */
	public List<TestTree> buildTreess(List<GjkTest> gjkTests, String root) {
		List<TestTree> trees = new ArrayList<>();
		TestTree node;
		for (GjkTest gjkTest : gjkTests) {
			node = new TestTree();
			node.setId(gjkTest.getTestId());
			node.setParentId(gjkTest.getParentId());
			node.setName(gjkTest.getName());
			node.setLabel(gjkTest.getName());
			node.setFilePath(gjkTest.getPermission());
			trees.add(node);
		}
		return TreeUtilTest.buildByLoop(trees, root);
	}
}
