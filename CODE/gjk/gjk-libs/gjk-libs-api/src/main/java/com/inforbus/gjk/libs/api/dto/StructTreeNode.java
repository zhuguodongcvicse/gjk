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

package com.inforbus.gjk.libs.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.math.MathUtil;

/**
 * @author lengleng
 * @date 2017年11月9日23:33:45
 */
@Data
public class StructTreeNode {

	protected String id;
	protected String parentId;
	protected String childrenIds;

	protected List<StructTreeNode> children = new ArrayList<StructTreeNode>();

	public void add(StructTreeNode node) {
		children.add(node);
	}

	public StructTreeNode(String id, String parentId, String childrenIds, List<StructTreeNode> children) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.childrenIds = childrenIds;
		this.children = children;
	}

	public StructTreeNode() {
		super();
	}

	public StructTreeNode(String id, String parentId, String childrenIds) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.childrenIds = childrenIds;
	}
}
