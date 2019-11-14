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

package com.inforbus.gjk.libs.api.vo;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import com.inforbus.gjk.common.core.entity.TreeNode;
import com.inforbus.gjk.libs.api.dto.CompTree;
import com.inforbus.gjk.libs.api.dto.StructDTO;
import com.inforbus.gjk.libs.api.dto.StructTreeNode;
import com.inforbus.gjk.libs.api.entity.Structlibs;

import cn.hutool.json.JSONUtil;

/**
 * @author lengleng
 * @date 2017年11月9日23:34:11
 */
@UtilityClass
public class TreeUtil {
	/**
	 * @Title: buildByLoopCh 两层循环实现建树
	 * @Description: 根据ChildrenIds和ParentId组装树形结构
	 * @Author xiaohe
	 * @DateTime 2019年7月6日 上午8:04:22
	 * @param treeNodes 传入的树节点列表
	 * @param root      根节点
	 * @return
	 */
	public <T extends StructTreeNode> List<T> buildByLoopCh(List<T> treeNodes, String root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getChildrenIds())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	public <T extends StructTreeNode> List<T> buildByLoop(List<T> treeNodes, String root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId() == treeNode.getId()) {
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
	public <T extends StructTreeNode> List<T> buildByRecursive(List<T> treeNodes, String root) {
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
	public <T extends StructTreeNode> T findChildren(T treeNode, List<T> treeNodes) {
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
	 * 通过sysMenu创建树形节点
	 *
	 * @param menus
	 * @param root
	 * @return
	 */
	public List<StructDTO> buildTree(List<Structlibs> structs, String root) {
		List<StructDTO> trees = new ArrayList<>();
		for (Structlibs st : structs) {
			trees.add(new StructDTO(st));
		}
		return TreeUtil.buildByLoopCh(trees, root);
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
			node.setNodeName(comp.getCompId());
			node.setParentId(comp.getTreeParent());
			node.setLabel(comp.getCompId());
			trees.add(node);
		}
		System.out.println(JSONUtil.parse(trees));
		return TreeUtil.buildCompTreeByLoop(trees, root);
	}

	/**
	 * 两层循环实现建comp树形选择器的树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> buildCompTreeByLoop(List<T> treeNodes, Object root) {
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
	 * @Title: buildCompTree
	 * @Description: 建comp树形选择器的树
	 * @Author cvics
	 * @DateTime 2019年5月17日 上午10:33:10
	 * @param vos
	 * @param root
	 * @return
	 */
	public List<CompTree> buildCommCompTree(List<CommCompDetailVO> vos, String root) {
		List<CompTree> trees = new ArrayList<>();
		CompTree node;
		for (CommCompDetailVO comp : vos) {
			node = new CompTree();
			node.setId(comp.getFileId());
			node.setParentId(comp.getParentId());
			node.setLabel(comp.getFileName());
			node.setType(comp.getFileType());
			node.setFilePath(comp.getFilePath());
			node.setVersion(comp.getVersion());
			trees.add(node);
		}
		return TreeUtil.buildCommTreeByLoop(trees, root);
	}

	/**
	 * 两层循环实现建comp树形选择器的树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> buildCommTreeByLoop(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			String parentID = treeNode.getParentId();
			if (root.equals(parentID)) {
				trees.add(treeNode);
			}
			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getId())) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.getChildren().add(it);
				}
			}
		}
		return trees;
	}
}
