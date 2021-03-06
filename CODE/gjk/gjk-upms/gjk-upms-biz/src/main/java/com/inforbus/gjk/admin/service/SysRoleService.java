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

package com.inforbus.gjk.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.dto.RoleDTO;
import com.inforbus.gjk.admin.api.dto.UserDTO;
import com.inforbus.gjk.admin.api.entity.SysRole;
import com.inforbus.gjk.common.core.util.R;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> listRolesByUserId(Integer userId);

	/**
	 * 分页查询角色信息
	 *
	 * @param page    分页对象
	 * @param roleDTO 参数列表
	 * @return
	 */
	IPage getRolePage(Page page, RoleDTO roleDTO);

	
	/**
	 * 通过角色ID，删除角色
	 *
	 * @param id
	 * @return
	 */
	Boolean removeRoleById(Integer id);

	/**
	 * 判断角色标识是否存在
	 * @param sysRole
	 * @return
	 */
	R getRoleByRoleCode(SysRole sysRole);

	/**
	 * 校验角色是否有用户引用
	 * @param roleId
	 * @return
	 */
	R getSysUserRoleByRoleId(String roleId);
	
	/**
	 * 根據用戶id查找到系統管理員
	 * @param userId
	 * @return
	 */
	String getSysRoleCodeByRoleId(@Param("userId") int userId);
}
