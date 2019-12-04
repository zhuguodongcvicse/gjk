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

package com.inforbus.gjk.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.dto.UserDTO;
import com.inforbus.gjk.admin.api.dto.UserDictDTO;
import com.inforbus.gjk.admin.api.entity.SysUser;
import com.inforbus.gjk.admin.service.SysUserService;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.common.security.annotation.Inner;
import com.inforbus.gjk.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final SysUserService userService;

	/**
	 * 获取当前用户全部信息
	 *
	 * @return 用户信息
	 */
	@GetMapping(value = { "/info" })
	public R info() {
		String username = SecurityUtils.getUser().getUsername();
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return new R<>(Boolean.FALSE, "获取当前用户信息失败");
		}
		return new R<>(userService.getUserInfo(user));
	}

	/**
	 * 获取指定用户全部信息
	 *
	 * @return 用户信息
	 */
	@Inner
	@GetMapping("/info/{username}")
	public R info(@PathVariable String username) {
		SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return new R<>(Boolean.FALSE, String.format("用户信息为空 %s", username));
		}
		return new R<>(userService.getUserInfo(user));
	}

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping("/{id}")
	public R user(@PathVariable Integer id) {
		return new R<>(userService.getUserVoById(id));
	}

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return
	 */
	@GetMapping("/details/{username}")
	public R user(@PathVariable String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		return new R<>(userService.getOne(new QueryWrapper<>(condition)));
	}

	/**
	 * 删除用户信息
	 *
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除用户信息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_user_del')")
	public R userDel(@PathVariable Integer id) {
		SysUser sysUser = userService.getById(id);
		return new R<>(userService.removeUserById(sysUser));
	}

	/**
	 * 添加用户
	 *
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("添加用户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_user_add')")
	public R user(@RequestBody UserDTO userDto) {
		return new R<>(userService.saveUser(userDto));
	}

	/**
	 * 更新用户信息
	 *
	 * @param userDto 用户信息
	 * @return R
	 */
	@SysLog("更新用户信息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_user_edit')")
	public R updateUser(@Valid @RequestBody UserDTO userDto) {
		return new R<>(userService.updateUser(userDto));
	}

	/**
	 * 分页查询用户
	 *
	 * @param page    参数集
	 * @param userDTO 查询参数列表
	 * @return 用户集合
	 */
	@GetMapping("/page")
	public R getUserPage(Page page, UserDTO userDTO) {
		return new R<>(userService.getUserWithRolePage(page, userDTO));
	}

	/**
	 * 修改个人信息
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@SysLog("修改个人信息")
	@PutMapping("/edit")
	public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
		return userService.updateUserInfo(userDto);
	}

	/**
	 * @param username 用户名称
	 * @return 上级部门用户列表
	 */
	@GetMapping("/ancestor/{username}")
	public R listAncestorUsers(@PathVariable String username) {
		return new R<>(userService.listAncestorUsersByUsername(username));
	}

	/**
	 * 通过用户Id查找用户姓名
	 *
	 * @param id 类型
	 * @return 同类型字典
	 */
	@GetMapping("/info/getUserDict")
	@Cacheable(value = "users")
	public R getDictByType() {
		System.out.println("7878878787");
		List<SysUser> users = userService.list(Wrappers.<SysUser>query().lambda());
		List<UserDictDTO> dictDTOs = new ArrayList<UserDictDTO>();
		for (SysUser user : users) {
			UserDictDTO dictDTO = new UserDictDTO(user);
			dictDTOs.add(dictDTO);
		}
		return new R<>(dictDTOs);
	}

	/**
	 * 查找具有审批权限的全部用户
	 *
	 * @param id 类型
	 * @return 同类型字典
	 */
	@GetMapping("/info/getUserhasApplyAuto")
	@Cacheable(value = "applyAutoUser")
	public R getUserhasApplyAuto() {
		System.out.println("7878878787");
		List<SysUser> users = userService.getUserHasMenuId("3100");
		return new R<>(users);
	}

	/**
	 * 注册用户
	 *
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("注册用户")
	@PostMapping("/registered")
	public R registered(@RequestBody UserDTO userDto) {
		return new R<>(userService.saveUser(userDto));
	}

	@GetMapping("getAllUser")
	public List getAllUser(){
		return userService.list(null);
	}
}
