package com.inforbus.gjk.pro.mapper;

import java.util.List;
import java.util.Map;

import com.inforbus.gjk.admin.api.entity.*;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.pro.api.entity.*;
import com.inforbus.gjk.pro.api.entity.GjkPlatform;
import com.inforbus.gjk.pro.api.entity.Software;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.inforbus.gjk.pro.api.entity.ChildrenNodes;
import com.inforbus.gjk.pro.api.entity.Chipsfromhardwarelibs;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.api.entity.ProjectPlan;

/**
 * @ClassName: ManagerMapper 项目管理类
 * @Description: 用于对项目管理的操作
 * @Author xiaohe
 * @DateTime 2019年4月24日 上午8:36:38
 */
public interface ManagerMapper extends BaseMapper<ProjectFile> {

	/**
	 * @Title: listProjectFileByProjectId
	 * @Description: 通过项目编号查询项目文件信息
	 * @Author cvics
	 * @DateTime 2019年5月2日 下午2:04:33
	 * @param projectId
	 * @return
	 */
	List<ProjectFile> listProjectFileByProjectId(@Param("projectId") String projectId);

	/**
	 * @Title: saveProDetail
	 * @Description: 保存项目详细信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午4:52:48
	 * @param projectFile
	 */
	void saveProDetail(@Param("proDetail") ProjectFile projectFile);

	/**
	 * @Title: getProDetailById
	 * @Description: 根据ID查询项目节点
	 * @Author cvics
	 * @DateTime 2019年5月10日 上午10:49:01
	 * @param id
	 * @return
	 */
	ProjectFile getProDetailById(@Param("id") String id);

	/**
	 * @Title: getProFileListByModelId
	 * @Description: 根据模型ID查询此ID下所有子节点
	 * @Author cvics
	 * @DateTime 2019年5月24日 上午10:53:25
	 * @param modelId
	 * @return
	 */
	List<ProjectFile> getProFileListByModelId(@Param("modelId") String modelId);

	/**
	 * @Title: editProJSON
	 * @Description: 修改流程建模回显JSON
	 * @Author xiaohe
	 * @DateTime 2019年5月31日 上午11:36:56
	 * @param proDetailId
	 * @param fullPath
	 */
	void editProJSON(@Param("proId") String proId, @Param("fullPath") String fullPath);

	/**
	 * @Title: findProJSON
	 * @Description: 查询JSON路径
	 * @Author xiaohe
	 * @DateTime 2019年5月31日 上午11:42:55
	 * @param proId
	 * @return
	 */
	Map<String, String> findProJSON(@Param("proId") String proId);

	/**
	 * 获取流程名
	 * 
	 * @param id
	 * @return
	 */
	ProjectFile getProcessName(@Param("id") String id);

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @return
	 */
	List<ProjectFile> getFilePathListById(@Param("id") String id);

	/**
	 * 保存详细方案信息
	 * 
	 * @param projectPlan
	 * @return
	 */
	void saveProjectPlan(@Param("proPlan") ProjectPlan projectPlan);

	/**
	 * 根据当前软硬件映射配置的parentid，查找当前流程下的所有模块的文件路径
	 * 
	 * @param parentId
	 * @return
	 */
	List<ProjectPlan> getPlanFilePathListByParentId(@Param("parentId") String parentId);

	/**
	 * 主题配置保存调用客户接口生成xml文件
	 */
	void saveNewFilePath(@Param("newFilePath") String newFilePath, @Param("proDetailId") String proDetailId);

	Hardwarelibs getHardwarelibs(@Param("modelId") String modelId);

	void saveHardwarelibs(@Param("hardwarelibs") Hardwarelibs hardwarelibs);

    Hardwarelibs getHardwarelibByProjectId(@Param("projectId") String projectId);

    /**
     * 根据项目ID获取到所有的流程记录
     *
     * @param projectId
     * @return
     */
    List<ProjectFile> getProcedureListByProjectId(@Param("projectId") String projectId);

	void updateHardwarelib(Hardwarelibs hardwarelibs);

	void deleteHardwarelibById(@Param("hardwarelibId") String hardwarelibId);

    void saveChipsfromhardwarelibs(@Param("chipsfromhardwarelibs") Chipsfromhardwarelibs chipsfromhardwarelibs);

	Chipsfromhardwarelibs getChipsById(@Param("modelId") String modelId);

	void updateChipsfromhardwarelibs(Chipsfromhardwarelibs chipsfromhardwarelibs);

	List<ChildrenNodes> getAllChildrenNodes(@Param("modelId") String modelId);

	void deleteChipsFromHardwarelibs(@Param("id") String id);
    /**
     * 得到软件框架库列表
     *
     * @return
     */
    List<Software> getAllSoftwareList();

    /**
     * 根据多个ID查询数据列表
     * @param ids
     * @return
     */
    List<Software> getAllSoftwareListByIdIn(@Param("ids") String ids);

    /**
     * 根据软件框架库ID得到平台库列表
     *
     * @param id
     * @return
     */
    List<GjkPlatform> getAllPlatformListBySoftwareId(@Param("id") String id);

    /**
     * 得到平台大类列表
     * @return
     */
    List<GjkPlatform> getPlatformList();

	/**
	 * 得到BSP库列表
	 *
	 * @return
	 */
	List<BSP> getAllBSPList();

	/**
	 * 根据bsp库ID得到平台库列表
	 *
	 * @param id
	 * @return
	 */
	List<GjkPlatform> getAllPlatformListByBSPId(@Param("id") String id);

	/**
	 * 根据多个ID查询数据列表
	 * @param ids
	 * @return
	 */
	List<BSP> getAllBSPListByIdIn(@Param("ids") String ids);

	/**
	 * 查询表列信息
	 *
	 * @param tableName 表名称
	 * @return
	 */
	List<Map<String, String>> queryColumns(String tableName);

	/**
	 * 根据多个ID查询数据列表 软件框架和平台关系
	 * @param ids
	 * @return
	 */
	List<SoftwareDetail> getSoftwareDetailBySoftwareIdIn(@Param("ids") String ids);

	/**
	 * 根据多个ID查询数据列表 软件框架文件
	 * @param ids
	 * @return
	 */
	List<SoftwareFile> getSoftwareFileBySoftwareIdIn(@Param("ids") String ids);

	/**
	 * 根据多个ID查询数据列表 BSP和平台关系
	 * @param ids
	 * @return
	 */
	List<BSPDetail> getBSPDetailByBSPIdIn(@Param("ids") String ids);

	/**
	 * 根据多个ID查询数据列表 BSP文件
	 * @param ids
	 * @return
	 */
	List<BSPFile> getBSPFileByBSPIdIn(@Param("ids") String ids);
	/**
	 * 根据多个ID查询数据列表 结构库
	 * @param ids
	 * @return
	 */
	List<CommonComponent> getCommonComponentByIdIn(@Param("ids") String ids);
	/**
	 * 根据多个ID查询数据列表 结构库详情
	 * @param ids
	 * @return
	 */
	List<CommonComponentDetail> getCommonComponentDetailByCompIdIn(@Param("ids") String ids);
	/**
	 * 根据构件库id集合，获取构件库和结构体关系数据
	 * @param compIdList
	 * @return
	 */
	List<CompStruct> getCompStructByCompIdList(@Param("compIdList") List<String> compIdList);
	/**
	 * 得到结构体数据，根据id集合
	 * @param idList
	 * @return
	 */
	List<Structlibs> getStructlibsByIdList(@Param("idList") List<String> idList);
}
