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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.admin.api.dto.RoleDTO;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.admin.api.vo.DictVO;
import com.inforbus.gjk.admin.service.SysDictService;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;

import lombok.AllArgsConstructor;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
public class DictController {
	@Autowired
	private final SysDictService sysDictService;

	/**
	 * 通过ID查询字典信息
	 *
	 * @param id ID
	 * @return 字典信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Integer id) {
		return new R<>(sysDictService.getById(id));
	}

	/**
	 * 分页查询字典信息
	 *
	 * @param page 分页对象
	 * @return 分页对象
	 */
	/*
	 * @GetMapping("/page") public R<IPage> getDictPage(Page page, SysDict sysDict)
	 * { return new R<>(sysDictService.page(page, Wrappers.query(sysDict))); }
	 */
	/**
	 * 分页查询字典信息
	 *
	 * @param page    参数集
	 * @param roleDTO 查询参数列表
	 * @return 字典集合
	 */
	@GetMapping("/page")
	public R getDictPage(Page page, SysDict sysDict) {
		return new R<>(sysDictService.getDictPage(page, sysDict));
	}

	/**
	 * 通过字典类型查找字典
	 *
	 * @param type 类型
	 * @return 同类型字典
	 */
	@GetMapping("/type/{type}")
	@Cacheable(value = "dict_details", key = "#type")
	public R getDictByType(@PathVariable String type) {
		return new R<>(sysDictService.list(Wrappers.<SysDict>query().lambda().eq(SysDict::getType, type)));
	}

	/**
	 * 添加字典
	 *
	 * @param sysDict 字典信息
	 * @return success、false
	 */
	@SysLog("添加字典")
	@PostMapping
	@CacheEvict(value = "dict_details", key = "#sysDict.type")
	@PreAuthorize("@pms.hasPermission('sys_dict_add')")
	public R save(@Valid @RequestBody SysDict sysDict) {
		return new R<>(sysDictService.save(sysDict));
	}

	/**
	 * 删除字典，并且清除字典缓存
	 *
	 * @param id   ID
	 * @param type 类型
	 * @return R
	 */
	@SysLog("删除字典")
	@DeleteMapping("/{id}/{type}")
	@CacheEvict(value = "dict_details", key = "#type")
	@PreAuthorize("@pms.hasPermission('sys_dict_del')")
	public R removeById(@PathVariable Integer id, @PathVariable String type) {
		return new R<>(sysDictService.removeById(id));
	}

	/**
	 * 修改字典
	 *
	 * @param sysDict 字典信息
	 * @return success/false
	 */
	@PutMapping
	@SysLog("修改字典")
	@CacheEvict(value = "dict_details", key = "#sysDict.type")
	@PreAuthorize("@pms.hasPermission('sys_dict_edit')")
	public R updateById(@Valid @RequestBody SysDict sysDict) {
		return new R<>(sysDictService.updateById(sysDict));
	}

	/**
	 * 通过字典类型查找字典
	 *
	 * @param type 类型
	 * @return 同类型字典
	 */
	/**
	 * @Title: getDictValue
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年5月6日 下午7:35:40
	 * @return
	 */
	@GetMapping("/types/{type}")
	// @Cacheable(value = "dict_details", key = "#type")
	public R getDictValue(@PathVariable String type) {
		return new R<>(sysDictService.getDictValue(type));
	}

	/**
	 * @Title: getLableValue
	 * @Description: 获取中英文映射的值
	 * @Author xiaohe
	 * @DateTime 2019年8月21日 上午9:51:42
	 * @param type
	 * @return
	 */
	@GetMapping("/lable/{type}")
	@Cacheable(value = "dict_Lable", key = "#type")
	public R getLableValue(@PathVariable String type) {
		return new R<>(sysDictService.getOne(Wrappers.<SysDict>query().lambda().eq(SysDict::getLabel, type)));

	}

	/**
	 * 通过字典value与字典类型查找字典
	 *
	 * @param type 类型
	 * @return 同类型字典
	 */
	/**
	 * @Title: getDictValue
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年9月10日 10:55:00
	 * @return
	 */
	@PutMapping("/getDicts")
	@SysLog("获取字典中的全部模板类型及其对应的值")
	public R getDicts(@RequestBody SysDict sysDict) {
		return new R<>(sysDictService.getDicts(sysDict));
	}

	/**
	 * 通过字典value与字典类型查找字典
	 *
	 *
	 * @return 字典中的全部类型
	 */
	/**
	 * @Title: getDictTypes
	 * @Description:
	 * @Author wang
	 * @DateTime 2019年9月25日 09:03:39
	 * @return
	 */
	@GetMapping("/getDictTypes")
	@SysLog("获取字典中的全部模板类型及其对应的值")
	public R getDictTypes() {
		Map<String, Object> maps = Maps.newHashMap();
		// 查询出从字典表中的获取的下拉框数据（不包含从库中查询）
		maps.put("dictSel", sysDictService.getDictTypes());
		// 查询出从字典表中的获取的下拉框数据（不包含从库中查询）
		maps.put("dictDb", sysDictService.getBaseMapper()
				.selectList((Wrappers.<SysDict>query().lambda().eq(SysDict::getType, "comp_selectbin_type"))));
		return new R<>(maps);
	}
}
