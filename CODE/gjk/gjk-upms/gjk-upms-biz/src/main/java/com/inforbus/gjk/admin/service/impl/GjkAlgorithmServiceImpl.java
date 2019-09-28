
package com.inforbus.gjk.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.vo.AlgorithmVO;
import com.inforbus.gjk.admin.mapper.GjkAlgorithmMapper;
import com.inforbus.gjk.admin.service.GjkAlgorithmService;
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
 * 算法库权限表 服务实现类
 * </p>
 *
* @author geng_hxian
 * @since 2019/4/17
 */
@Service
@AllArgsConstructor
public class GjkAlgorithmServiceImpl extends ServiceImpl<GjkAlgorithmMapper, GjkAlgorithm> implements GjkAlgorithmService {

	@Override
	@Cacheable(value = "menu_details", key = "#roleId  + '_menu'")
	public List<AlgorithmVO> getAlgorithmByRoleId() {
		return baseMapper.listAlgorithmsByRoleId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "menu_details", allEntries = true)
	public R removeAlgorithmById(String id) {
		// 查询父节点为当前节点的节点
		List<GjkAlgorithm> AlgorithmList = this.list(Wrappers.<GjkAlgorithm>query()
			.lambda().eq(GjkAlgorithm::getParentId, id));
		if (CollUtil.isNotEmpty(AlgorithmList)) {
			return R.builder()
				.code(CommonConstants.FAIL)
				.msg("算法含有下级不能删除").build();
		}

		//删除当前算法库及其子算法
		return new R(this.removeById(id));
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean updateAlgorithmById(GjkAlgorithm gjkAlgorithm) {
		return this.updateById(gjkAlgorithm);
	}

}
