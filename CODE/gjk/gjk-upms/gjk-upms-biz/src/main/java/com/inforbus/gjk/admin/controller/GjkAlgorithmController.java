
package com.inforbus.gjk.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.vo.AlgorithmVO;
import com.inforbus.gjk.admin.api.vo.TreeUtilAl;
import com.inforbus.gjk.admin.api.vo.TreeUtilPl;
import com.inforbus.gjk.admin.service.GjkAlgorithmService;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author geng_hxian
 * @date 2019/4/17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/algorithm")
public class GjkAlgorithmController {
	private final GjkAlgorithmService gjkAlgorithmService;
	private final GjkPlatformService gjkPlatformService;
	/**
	 * 返回树形算法库集合
	 *
	 * @return 树形算法库
	 */
	@GetMapping(value = "/tree")
	public R getTree() {
		return new R<>(TreeUtilAl.buildTreesss(gjkAlgorithmService.list(Wrappers.emptyWrapper()), "-1"));
	}

	/**
	 * 返回树形平台库集合
	 *
	 * @return 树形平台库
	 */
	@GetMapping(value = "/trees")
	public R getTrees() {
		List<GjkAlgorithm> platformList = gjkAlgorithmService.list(Wrappers.emptyWrapper());
		List<GjkAlgorithm> softwareList = gjkPlatformService.getAlgorithmTree();
		if (CollectionUtils.isNotEmpty(platformList) && CollectionUtils.isNotEmpty(softwareList)) {
			platformList.addAll(softwareList);
		}

		return new R<>(TreeUtilAl.buildTreesss(platformList, "-1"));
	}

	/**
	 * 返回角色的算法库集合
	 *
	 * @param roleId 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/tree/{roleId}")
	public List getRoleTree() {
		return gjkAlgorithmService.getAlgorithmByRoleId()
			.stream()
			.map(AlgorithmVO::getAlgorithmId)
			.collect(Collectors.toList());
	}

	/**
	 * 通过ID查询算法库的详细信息
	 *
	 * @param id 算法库ID
	 * @return 算法库详细信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable String id) {
		return new R<>(gjkAlgorithmService.getById(id));
	}

	/**
	 * 新增算法库
	 *
	 * @param gjkAlgorithm 算法库信息
	 * @return success/false
	 */
	@SysLog("新增算法库")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_algorithm_add')")
	public R save(@Valid @RequestBody GjkAlgorithm gjkAlgorithm) {
		gjkAlgorithm.setAlgorithmId(IdGenerate.uuid());
		return new R<>(gjkAlgorithmService.save(gjkAlgorithm));
	}

	/**
	 * 删除算法库
	 *
	 * @param id 算法ID
	 * @return success/false
	 */
	@SysLog("删除算法库")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_algorithm_del')")
	public R removeById(@PathVariable String id) {
		return gjkAlgorithmService.removeAlgorithmById(id);
	}

	/**
	 * 更新算法库
	 *
	 * @param gjkAlgorithm
	 * @return
	 */
	@SysLog("更新算法库")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_algorithm_edit')")
	public R update(@Valid @RequestBody GjkAlgorithm gjkAlgorithm) {
		return new R<>(gjkAlgorithmService.updateAlgorithmById(gjkAlgorithm));
	}

}
