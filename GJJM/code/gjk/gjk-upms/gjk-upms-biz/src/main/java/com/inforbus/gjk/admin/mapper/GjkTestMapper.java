
package com.inforbus.gjk.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.vo.TestVO;

import java.util.List;

/**
 * <p>
 * 测试库权限表 Mapper 接口
 * </p>
 *
* @author geng_hxian
 * @since 2019/4/17
 */
public interface GjkTestMapper extends BaseMapper<GjkTest> {

	/**
	 * 通过角色编号查询测试库
	 *
	 * @return
	 */
	List<TestVO> listTestsByRoleId();

}
