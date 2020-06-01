
package com.inforbus.gjk.admin.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.entity.BSP;
import com.inforbus.gjk.admin.api.entity.BSPDetail;
import com.inforbus.gjk.admin.api.entity.BSPFile;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.entity.Software;
import com.inforbus.gjk.admin.api.entity.SoftwareDetail;
import com.inforbus.gjk.admin.api.entity.SoftwareFile;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.common.core.util.R;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
public interface GjkPlatformService extends IService<GjkPlatform> {
	/**
	 * 通过角色编号查询URL 权限
	 *
	 * @param roleId 角色ID
	 * @return 平台库列表
	 */
	List<PlatformVO> getPlatformByRoleId();
//	List<PlatformVO> getPlatformByRoleId(Integer roleId);

	/**
	 * 级联删除平台库
	 *
	 * @param id 平台库ID
	 * @return 成功、失败
	 */
	R removePlatformById(String id);

	/**
	 * 更新平台库信息
	 *
	 * @param sysMenu 平台库信息
	 * @return 成功、失败
	 */
	Boolean updatePlatformById(GjkPlatform gjkPlatform);

	/**
	 * 组装平台树，包括软件框架库
	 * 
	 * @return
	 */
	public List<GjkPlatform> getPlatformTree(List<GjkPlatform> libsList);

	/**
	 * 获取软件框架库的主表信息
	 * 
	 * @return
	 */
	List<Software> getSoftware();

	/**
	 * 获取软件框架库平台id的副表信息
	 * 
	 * @return
	 */
	List<SoftwareDetail> getSoftwarePlatform();

	/**
	 * 获取软件框架库文件夹的副表信息
	 */
	List<SoftwareFile> getSoftwareFile();

	/**
	 * 获取BSP的主表信息
	 * 
	 * @return
	 */
	List<BSP> getBSP();

	/**
	 * 获取BSP平台id的副表信息
	 * 
	 * @return
	 */
	List<BSPDetail> getBSPPlatform();

	/**
	 * 获取BSP文件夹的副表信息
	 */
	List<BSPFile> getBSPFile();

	/**
	 * 获取构件建模模块的信息
	 * 
	 * @return
	 */
//	List<ComponentDetail> getLibsInfo();

	/**
	 * 获取构件建模模块选择的文件夹的信息
	 * 
	 * @return
	 */
//	List<ComponentDetail> getLibsFile(String libsId);
	/**
	 * 组装平台树，包括软件框架库
	 * 
	 * @return
	 */
	public List<GjkAlgorithm> getAlgorithmTree();

	/**
	 * 组装平台树，包括软件框架库
	 * 
	 * @return
	 */
	public List<GjkTest> getTestTree();

	/**
	 * @Title 获取所属平台
	 * @Description 查询所属平台用于下拉框显示
	 * @Author xiaohe
	 * @DateTime 2019年9月28日 下午4:33:08
	 * @return
	 */
	List<GjkPlatform> selectOwnPlatform();

	/**
	 * 获取平台类型根节点列表
	 * @return
	 */
	List<GjkPlatform> getPlatFormTypeList();

	/**
	 * 修改平台类型
	 * @param platformVO
	 * @return
	 */
	int modifyPlatformLibDirectory(PlatformVO platformVO);
}
