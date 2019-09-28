
package com.inforbus.gjk.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.entity.SysMenu;
import com.inforbus.gjk.admin.api.entity.SysRoleMenu;
import com.inforbus.gjk.admin.api.vo.MenuVO;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.admin.api.vo.TestVO;
import com.inforbus.gjk.admin.mapper.GjkPlatformMapper;
import com.inforbus.gjk.admin.mapper.GjkTestMapper;
import com.inforbus.gjk.admin.mapper.SysMenuMapper;
import com.inforbus.gjk.admin.mapper.SysRoleMenuMapper;
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.admin.service.GjkTestService;
import com.inforbus.gjk.admin.service.SysMenuService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 测试库权限表 服务实现类
 * </p>
 *
* @author geng_hxian
 * @since 2019/4/17
 */
@Service
@AllArgsConstructor
public class GjkTestServiceImpl extends ServiceImpl<GjkTestMapper, GjkTest> implements GjkTestService {

	@Override
	@Cacheable(value = "menu_details", key = "#roleId  + '_menu'")
	public List<TestVO> getTestByRoleId() {
		return baseMapper.listTestsByRoleId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "menu_details", allEntries = true)
	public R removeTestById(String id) {
		// 查询父节点为当前节点的节点
		List<GjkTest> testList = this.list(Wrappers.<GjkTest>query()
			.lambda().eq(GjkTest::getParentId, id));
		if (CollUtil.isNotEmpty(testList)) {
			return R.builder()
				.code(CommonConstants.FAIL)
				.msg("测试含有下级不能删除").build();
		}

		//删除当前测试库及其子测试
		return new R(this.removeById(id));
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean updateTestById(GjkTest gjkTest) {
		return this.updateById(gjkTest);
	}

}
