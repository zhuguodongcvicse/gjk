
package com.inforbus.gjk.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.vo.AlgorithmVO;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.admin.api.vo.TestVO;
import com.inforbus.gjk.common.core.util.R;

import java.util.List;

/**
 * <p>
 * 算法权限表 服务类
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
public interface GjkAlgorithmService extends IService<GjkAlgorithm> {
	/**
	 * 通过角色编号查询URL 权限
	 *
	 * @return 算法库列表
	 */
	List<AlgorithmVO> getAlgorithmByRoleId();

	/**
	 * 级联删除算法库
	 *
	 * @param id 算法库ID
	 * @return 成功、失败
	 */
	R removeAlgorithmById(String id);

	/**
	 * 更新算法库信息
	 *
	 * @param gjkAlgorithm 算法库信息
	 * @return 成功、失败
	 */
	Boolean updateAlgorithmById(GjkAlgorithm gjkAlgorithm);
}
