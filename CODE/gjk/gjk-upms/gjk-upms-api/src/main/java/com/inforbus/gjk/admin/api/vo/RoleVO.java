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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.inforbus.gjk.admin.api.entity.SysRole;

import lombok.Data;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Data
public class RoleVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Integer roleId;
	/**
	 * 角色名
	 */
	private String roleName;
	/**
	 * 角色标识
	 */
	private String roleCode;
	/**
	 * 角色描述
	 */
	private String roleDesc;
	
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 0-正常，1-删除
	 */
	private String delFlag;

}
