
package com.inforbus.gjk.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.vo.AlgorithmVO;

import java.util.List;

/**
 * <p>
 * 算法库权限表 Mapper 接口
 * </p>
 *
* @author geng_hxian
 * @since 2019/4/17
 */
public interface GjkAlgorithmMapper extends BaseMapper<GjkAlgorithm> {

	/**
	 * 通过角色编号查询算法库
	 *
	 * @param roleId 角色ID
	 * @return
	 */
	List<AlgorithmVO> listAlgorithmsByRoleId();

	/**
	 * 删除所有数据
	 */
	void deleteAll();
}
