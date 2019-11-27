
package com.inforbus.gjk.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.admin.api.vo.TestVO;
import com.inforbus.gjk.common.core.util.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 测试权限表 服务类
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
public interface GjkTestService extends IService<GjkTest> {
	/**
	 * 通过角色编号查询URL 权限
	 *
	 * @return 测试库列表
	 */
	List<TestVO> getTestByRoleId();

	/**
	 * 级联删除测试库
	 *
	 * @param id 测试库ID
	 * @return 成功、失败
	 */
	R removeTestById(String id);

	/**
	 * 更新测试库信息
	 *
	 * @param gjkTest 测试库信息
	 * @return 成功、失败
	 */
	Boolean updateTestById(GjkTest gjkTest);

	/**
	 * 导出平台库、算法库、测试库，生成压缩文件流
	 * @param libs
	 * @return
	 * @throws Exception
	 */
	byte[] createZip(List<String> libs) throws Exception;

	/**
	 * 解析传入的压缩文件
	 *
	 * @param ufile
	 * @param importType
	 */
	int analysisZipFile(MultipartFile ufile, String importType);
	
}
