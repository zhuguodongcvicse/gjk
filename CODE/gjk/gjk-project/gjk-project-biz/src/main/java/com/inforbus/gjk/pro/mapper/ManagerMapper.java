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

	Hardwarelibs getHardwarelibs(@Param("flowId") String flowId);

	int saveHardwarelibs(@Param("hardwarelibs") Hardwarelibs hardwarelibs);

    Hardwarelibs getHardwarelibByProjectId(@Param("projectId") String projectId);

    Hardwarelibs getHardwarelibsByFlowId(@Param("flowId") String flowId);

	Hardwarelibs getHardwarelibsById(@Param("id") String id);

    /**
     * 根据项目ID获取到所有的流程记录
     *
     * @param projectId
     * @return
     */
    List<ProjectFile> getProcedureListByProjectId(@Param("projectId") String projectId);

	int updateHardwarelib(Hardwarelibs hardwarelibs);

	void deleteHardwarelibById(@Param("hardwarelibId") String hardwarelibId);

    void saveChipsfromhardwarelibs(@Param("chipsfromhardwarelibs") Chipsfromhardwarelibs chipsfromhardwarelibs);

    Chipsfromhardwarelibs getChipsfromhardwarelibsById(@Param("id") String id);

	Chipsfromhardwarelibs getChipsByFlowId(@Param("flowId") String flowId);

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

	/**
	 * 保存基础模版
	 * @param baseTemplate
	 * @return
	 */
	int saveBaseTemplate(@Param("baseTemplate") BaseTemplate baseTemplate);

	/**
	 *
	 * @Title: saveSoftware  保存软件框架库信息
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 上午11:40:43
	 * @param software
	 */
	void saveSoftware(@Param("soft") Software software);

	/**
	 * 保存软件框架详情
	 * @param softwareDetail
	 */
	void saveSoftwareDetail(@Param("softDetail") SoftwareDetail softwareDetail);

	/**
	 * 保存软件框架文件数据
	 * @param softwareFile
	 */
	void saveSoftwareFile(@Param("softFile") SoftwareFile softwareFile);

	/**
	 *
	 * @Title: saveBSP  保存bsp信息
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 上午11:40:43
	 * @param bsp
	 */
	void saveBSP(@Param("bs") BSP bsp);

	/**
	 * 保存bsp子表
	 * @Title: saveBSPDetail
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:48:49
	 * @param BSPDetail
	 */
	void saveBSPDetail(@Param("bspDetail") BSPDetail BSPDetail);

	/**
	 * 保存bsp文件路径子表
	 * @Title: saveBSPFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月14日 下午4:48:49
	 * @param BSPFile
	 */
	void saveBSPFile(@Param("bspFile") BSPFile BSPFile);

	/**
	 * @Title: saveCommonComp
	 * @Description: 保存公共构件
	 * @Author cvics
	 * @DateTime 2019年6月11日 下午2:27:22
	 * @param commonComponent
	 */
	void saveCommonComp(@Param("comp") CommonComponent commonComponent);
	/**
	 * 保存公共构件详细表数据
	 *
	 * @param commonComponentDetail
	 */
	void saveCommonCompDetail(@Param("compDetail") CommonComponentDetail commonComponentDetail);

	/**
	 * 保存构件库和结构体表关系
	 * @param compStruct
	 */
	void saveCompAndStruct(@Param("compStruct") CompStruct compStruct);
	/**
	 * 保存结构体
	 * @param structlibs
	 */
	void saveStructlibs(@Param("structlibs") Structlibs structlibs);

	/**
	 * 根据id获取软件框架数据
	 * @param id
	 * @return
	 */
	Software getSoftwareById(@Param("id") String id);

	/**
	 * 根据软件id和平台id获取数据
	 * @param softwareId
	 * @param platformId
	 * @return
	 */
	SoftwareDetail getSoftwareDetailBySoftwareIdAndPlatformId(@Param("softwareId") String softwareId, @Param("platformId") String platformId);

	/**
	 * 根据软件id和文件名称查询数据
	 * @param softwareId
	 * @param fileName
	 * @return
	 */
	SoftwareFile getSoftwareFileBySoftwareIdAndFileName(@Param("id") String softwareId, @Param("fileName") String fileName);

	/**
	 * 根据id获取bsp数据
	 * @param id
	 * @return
	 */
	BSP getBSPById(@Param("id") String id);

	/**
	 * 查询bsp关系数据
	 * @param bspId
	 * @param platformId
	 * @return
	 */
	BSPDetail getBSPDetailByBSPIdAndPlatformId(@Param("bspId") String bspId, @Param("platformId") String platformId);

	/**
	 * 查询bsp文件数据
	 * @param bspId
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	BSPFile getBSPFileByBSPIdAndFileNameAndFilePath(@Param("bspId") String bspId,@Param("fileName") String fileName,@Param("filePath") String filePath);

	/**
	 * 更新构件库
	 * @param commonComponent
	 * @return
	 */
	int updateCommonComp(@Param("comp") CommonComponent commonComponent);

	/**
	 * 更新构件详情
	 * @param commonComponent
	 * @return
	 */
	int updateCommonCompDetail(@Param("comp") CommonComponentDetail commonComponent);

	/**
	 * 根据id获取构件结构体关系数据
	 * @param id
	 * @return
	 */
	CompStruct getCompStructById(@Param("id") String id);

	/**
	 * 根据id获取结构体数据
	 * @param id
	 * @return
	 */
	Structlibs getStructlibsById(@Param("id") String id);

	/**
	 * 更新结构体
	 * @param tructlibs
	 * @return
	 */
	int updateStructlibs(@Param("structlibs") Structlibs tructlibs);

	List<SysDict> getSysDictByRemarksIn(@Param("remarks") String remarks);

	/**
	 * 根据id获取字典数据
	 * @param id
	 * @return
	 */
	SysDict getSysDictById(@Param("id") String id);

	/**
	 * 保存字典数据
	 * @param sysDict
	 */
	void saveSysDict(@Param("sysDict") SysDict sysDict);

	/**
	 * 更新字典
	 * @param sysDict
	 * @return
	 */
	int updateSysDict(@Param("sysDict") SysDict sysDict);

	/**
	 * 更新芯片
	 * @param chips
	 * @return
	 */
	int updateChipsfromhardwarelibsById(@Param("chips") Chipsfromhardwarelibs chips);

	/**
	 * 更新硬件建模
	 * @param hardware
	 * @return
	 */
	int updateHardwarelibById(@Param("hardware") Hardwarelibs hardware);

	/**
	 * 得到子结构体数据，根据id集合
	 * @param parentIdList
	 * @return
	 */
	List<Structlibs> getStructlibsByParentIdList(@Param("parentIdList") List<String> parentIdList);
}
