
package com.inforbus.gjk.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inforbus.gjk.admin.api.entity.BSP;
import com.inforbus.gjk.admin.api.entity.BSPDetail;
import com.inforbus.gjk.admin.api.entity.BSPFile;
import com.inforbus.gjk.admin.api.entity.Component;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.Software;
import com.inforbus.gjk.admin.api.entity.SoftwareDetail;
import com.inforbus.gjk.admin.api.entity.SoftwareFile;
import com.inforbus.gjk.admin.api.vo.PlatformVO;

/**
 * <p>
 * 平台库权限表 Mapper 接口
 * </p>
 *
* @author geng_hxian
 * @since 2019/4/17
 */
public interface GjkPlatformMapper extends BaseMapper<GjkPlatform> {

	/**
	 * 通过角色编号查询平台库
	 *
	 * @param roleId 角色ID
	 * @return
	 */
	List<PlatformVO> listPlatformsByRoleId();
//	List<PlatformVO> listPlatformsByRoleId(Integer roleId);

	/**
	 * 通过角色ID查询权限
	 *
	 * @param roleIds Ids
	 * @return
	 */
//	List<String> listPermissionsByRoleIds(String roleIds);
	
	/**
	 * 获取软件框架库的主表信息
	 * @return
	 */
	List<Software> getSoftware();
	
	/**
	 * 获取软件框架库平台id的副表信息
	 * @return
	 */
	List<SoftwareDetail> getSoftwarePlatform();
	
	/**
	 * 获取软件框架库文件夹的副表信息
	 */
	List<SoftwareFile> getSoftwareFile();
	
	/**
	 * 获取bsp的主表信息
	 * @return
	 */
	List<BSP> getBSP();
	
	/**
	 * 获取bsp平台id的副表信息
	 * @return
	 */
	List<BSPDetail> getBSPPlatform();
	
	/**
	 * 获取bsp文件夹的副表信息
	 */
	List<BSPFile> getBSPFile();
	
	/**
	 * 获取构件建模模块的信息
	 * @return
	 */
	List<ComponentDetail> getLibsInfo();
	
	/**
	 * 获取构件建模模块选择的文件夹的信息
	 * @return
	 */
	List<ComponentDetail> getLibsFile(@Param("libsId") String libsId);
	
	/**
	 * 根据libs_id获取构件建模模块属于哪个库信息
	 * @return
	 */
	List<ComponentDetail> getLibsFileType(String libsId);
	
	/**
	 * 根据详细表的comp_id找到主表里的comp_name
	 * @return
	 */
	Component getCompNameById(@Param("id") String id);
	/**
	 * 获取所有构件
	 * @param id
	 * @return
	 */
	List<String> getCompIdsGroupCompId();

	ComponentDetail getCompDetailByComponentId(@Param("componentId")String componentId);
	
	List<Component> getCompByCompId(@Param("compId")String compId);
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
