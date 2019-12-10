
package com.inforbus.gjk.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.vo.TestVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

	/**
	 * 查询表列信息
	 *
	 * @param tableName 表名称
	 * @return
	 */
	List<Map<String, String>> queryColumns(String tableName);

	/**
	 * 删除所有数据
	 */
	void deleteAll();

	/**
	 * 查询数据条数
	 * @param id
	 * @return
	 */
	int selectCountById(@Param("id") String id);
}
