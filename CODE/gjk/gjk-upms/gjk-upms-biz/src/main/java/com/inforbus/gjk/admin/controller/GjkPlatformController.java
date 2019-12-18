
package com.inforbus.gjk.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.inforbus.gjk.admin.api.dto.PlatformTree;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.admin.api.vo.TreeUtil;
import com.inforbus.gjk.admin.api.vo.TreeUtilAl;
import com.inforbus.gjk.admin.api.vo.TreeUtilPl;
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author geng_hxian
 * @date 2019/4/17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform")
public class GjkPlatformController {
	private final GjkPlatformService gjkPlatformService;

	/**
	 * 返回当前用户的树形平台库集合
	 *
	 * @return 当前用户的树形平台库
	 */
//	@GetMapping
//	public R getUserMenu() {
//		// 获取符合条件的平台库
//		Set<PlatformVO> all = new HashSet<>();
//		SecurityUtils.getRoles()
//			.forEach(roleId -> all.addAll(gjkPlatformService.getPlatformByRoleId(roleId)));
//		List<PlatformTree> menuTreeList = all.stream()
//			.filter(platformVO -> CommonConstants.MENU.equals(platformVO.getType()))
//			.map(PlatformTree::new)
//			.sorted(Comparator.comparingInt(PlatformTree::getSort))
//			.collect(Collectors.toList());
//		return new R<>(TreeUtil.buildByLoop(menuTreeList, -1));
//	}

	/**
	 * 返回树形平台库集合
	 *
	 * @return 树形平台库
	 */
	@GetMapping(value = "/tree")
	public R getTree() {
		List<GjkPlatform> platformList = gjkPlatformService.list(Wrappers.emptyWrapper());
		
		List<GjkPlatform> softwareList = gjkPlatformService.getPlatformTree(platformList);
		if (CollectionUtils.isNotEmpty(platformList) && CollectionUtils.isNotEmpty(softwareList)) {
			platformList.addAll(softwareList);
		}

		return new R<>(TreeUtilPl.buildTrees(platformList, "-1"));
	}

	/**
	 * 返回树形算法库集合
	 *
	 * @return 树形算法库
	 */
	@GetMapping(value = "/trees")
	public R getTrees() {
		return new R<>(TreeUtilPl.buildTrees(gjkPlatformService.list(Wrappers.emptyWrapper()), "-1"));
	}

	/**
	 * 返回角色的平台库集合
	 *
	 * @param roleId 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/tree/{roleId}")
//	public List getRoleTree(@PathVariable Integer roleId) {
	public List getRoleTree() {
		return gjkPlatformService.getPlatformByRoleId().stream().map(PlatformVO::getPlatformId)
				.collect(Collectors.toList());
	}

	/**
	 * 通过ID查询平台库的详细信息
	 *
	 * @param id 平台库ID
	 * @return 平台库详细信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable String id) {
		return new R<>(gjkPlatformService.getById(id));
	}

	/**
	 * 新增平台库
	 *
	 * @param gjkPlatform 平台库信息
	 * @return success/false
	 */
	@SysLog("新增平台库")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_platform_add')")
	public R save(@RequestBody GjkPlatform gjkPlatform) {
		gjkPlatform.setPlatformId(IdGenerate.uuid());
		return new R<>(gjkPlatformService.save(gjkPlatform));
	}

	/**
	 * 删除平台库
	 *
	 * @param id 菜单ID
	 * @return success/false
	 */
	@SysLog("删除平台库")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_platform_del')")
	public R removeById(@PathVariable String id) {
		return gjkPlatformService.removePlatformById(id);
	}

	/**
	 * 更新平台库
	 *
	 * @param gjkPlatform
	 * @return
	 */
	@SysLog("更新平台库")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_platform_edit')")
	public R update(@Valid @RequestBody GjkPlatform gjkPlatform) {
		return new R<>(gjkPlatformService.updatePlatformById(gjkPlatform));
	}

	/**
	 * @Title: ownPlatform
	 * @Description: 
	 * @Author xiaohe
	 * @DateTime 2019年9月29日 上午11:19:42
	 * @return 
	 */
	@PostMapping("ownPlatform")
	public R ownPlatform() {
		return new R<>(gjkPlatformService.selectOwnPlatform());
	}
}
