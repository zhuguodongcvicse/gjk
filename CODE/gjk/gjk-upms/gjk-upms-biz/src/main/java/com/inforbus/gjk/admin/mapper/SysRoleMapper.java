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

package com.inforbus.gjk.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.dto.RoleDTO;
import com.inforbus.gjk.admin.api.dto.UserDTO;
import com.inforbus.gjk.admin.api.entity.SysRole;
import com.inforbus.gjk.admin.api.vo.RoleVO;
import com.inforbus.gjk.admin.api.vo.UserVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> listRolesByUserId(Integer userId);
	
	/**
	 * 通过角色名查询角色信息
	 *
	 * @param roleName 角色名
	 * @return roleVo
	 */
//	RoleVO getRoleVoByUsername(String roleName);
	/**
	 * 分页查询角色信息
	 *
	 * @param page    分页
	 * @param roleDTO 查询参数
	 * @return list
	 */
	IPage<List<RoleVO>> getRoleVosPage(Page page, @Param("query") RoleDTO roleDTO);

	/**
	 * 根据roleCode查询role
	 * @param sysRole
	 * @return
	 */
	SysRole getRoleByRoleCode(@Param("query") SysRole sysRole);
}
