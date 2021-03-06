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

package com.inforbus.gjk.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.dto.RoleDTO;
import com.inforbus.gjk.admin.api.dto.UserDTO;
import com.inforbus.gjk.admin.api.entity.SysRole;
import com.inforbus.gjk.admin.api.entity.SysRoleMenu;
import com.inforbus.gjk.admin.mapper.SysRoleMapper;
import com.inforbus.gjk.admin.mapper.SysRoleMenuMapper;
import com.inforbus.gjk.admin.service.SysRoleService;

import com.inforbus.gjk.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	private SysRoleMenuMapper sysRoleMenuMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List listRolesByUserId(Integer userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 分页查询角色信息
	 *
	 * @param page    分页对象
	 * @param roleDTO 参数列表
	 * @return
	 */
	@Override
	public IPage getRolePage(Page page, RoleDTO roleDTO) {
		
		IPage IPage = baseMapper.getRoleVosPage(page, roleDTO);
		return IPage;
	}

	
	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 *
	 * @param id
	 * @return
	 */
	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleById(Integer id) {
		sysRoleMenuMapper.delete(Wrappers
			.<SysRoleMenu>update().lambda()
			.eq(SysRoleMenu::getRoleId, id));
		return this.removeById(id);
	}

	@Override
	public R getRoleByRoleCode(SysRole sysRole) {
		SysRole roleByRoleCode = sysRoleMapper.getRoleByRoleCode(sysRole);
		if(roleByRoleCode!=null){
			return new R<>(true);
		}
		return new R<>(false);
	}

	@Override
	public R getSysUserRoleByRoleId(String roleId) {
		return new R<>(baseMapper.getSysUserRoleByRoleId(roleId)>0);
	}

	@Override
	public String getSysRoleCodeByRoleId(int userId) {
		return baseMapper.getSysRoleCodeByRoleId(userId);
	}
}
