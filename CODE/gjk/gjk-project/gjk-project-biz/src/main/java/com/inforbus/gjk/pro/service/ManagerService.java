package com.inforbus.gjk.pro.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;

/**
 * @ClassName: ManagerService
 * @Description:项目管理Service
 * @Author xiaohe
 * @DateTime 2019年4月23日 下午5:10:03
 */
public interface ManagerService extends IService<ProjectFile> {

	/**
	 * @Title: getTreeByProjectId
	 * @Description: 根据项目编号查询项目树
	 * @Author xiaohe
	 * @DateTime 2019年5月2日 下午1:50:20
	 * @param projectId 项目编号
	 * @return
	 */
	List<ProjectFileVO> getTreeByProjectId(String projectId);

	/**
	 * @Title: saveProDetail
	 * @Description: 保存项目流程下的各个节点详细信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午6:03:53
	 * @param projectId
	 * @param processName
	 * @return
	 */
	List<ProjectFile> saveProProcess(String projectId, String processName);

	/**
	 * @Title: createXmlFile
	 * @Description: 生成xml文件
	 * @Author cvics
	 * @DateTime 2019年5月10日 上午10:38:23
	 * @param entity
	 * @param proDetailId
	 * @return
	 */
	boolean createXmlFile(XmlEntity entity, String proDetailId);

	/**
	 * @Title: createXmlFile
	 * @Description: 生成xml文件
	 * @Author cvics
	 * @DateTime 2019年5月10日 上午10:38:23
	 * @param entity
	 * @param proDetailId
	 * @return
	 */
	boolean createXmlFile(XmlEntityMap entity, String proDetailId);

	/**
	 * @Title: getProDetailById
	 * @Description: 根据ID查询项目节点
	 * @Author cvics
	 * @DateTime 2019年5月10日 上午10:49:01
	 * @param id
	 * @return
	 */
	ProjectFile getProDetailById(String id);

	/**
	 * 根据proDetail的ID获取当前流程的流程模型xml并解析成节点-部件-构件结构
	 * 
	 * @param proDetailId
	 * @return
	 */
	List<HardwareNode> getCoeffNodeTree(String proDetailId);

	/**
	 * @Title: getSysConfigXmlEntityMap
	 * @Description:
	 * @Author cvics
	 * @DateTime 2019年5月22日 下午3:36:13
	 * @return
	 */
	XmlEntityMap getcoefficientXmlEntityMap(String proDetailId);

	/**
	 * @Title: getSysConfigByApiReturn
	 * @Description: 获取系统配置中api return的内容
	 * @Author cvics
	 * @DateTime 2019年5月24日 上午11:11:18
	 * @param proDetailId
	 * @return
	 */
	Map<String, List<Object>> getSysConfigByApiReturn(String proDetailId);

	/**
	 * @Title: isXmlFileExist
	 * @Description: 根据ID查找路径判断此文件是否存在
	 * @Author cvics
	 * @DateTime 2019年5月25日 下午3:40:17
	 * @param proDetailId
	 * @return
	 */
	boolean isXmlFileExist(@PathVariable("proDetailId") String proDetailId);

	/**
	 * @Title: getSysConfigXmlEntityMap
	 * @Description: 解析系统配置的xml文件进行前台回显
	 * @Author cvics
	 * @DateTime 2019年5月25日 下午5:11:41
	 * @param proDetailId
	 * @return
	 */
	XmlEntityMap getSysConfigXmlEntityMap(String proDetailId);

	/**
	 * @Title: editProJSON
	 * @Description: 保存回显文件
	 * @Author xiaohe
	 * @DateTime 2019年5月31日 上午10:09:53
	 * @param proDetailId
	 * @param objJson
	 * @return
	 */
	boolean editProJSON(String proDetailId, Object objJson);

	/**
	 * @Title: findProJSON
	 * @Description: 回显JSON文件
	 * @Author xiaohe
	 * @DateTime 2019年5月31日 下午1:58:38
	 * @param proId
	 * @return
	 */
	Object findProJSON(String proId);

	/**
	 * 获取流程名
	 * 
	 * @param parentId
	 * @return
	 */
	ProjectFile getProcessName(String id);

	/**
	 * 调缩略方案的接口
	 */
	void simplePlan(ArrayList<String> schemeFileList, String simplePlanFile);

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @return
	 */
	List<ProjectFile> getFilePathListById(String id);

	/**
	 * 保存详细方案信息
	 * 
	 * @param projectPlan
	 */
	void saveProjectPlan(@Param("proPlan") ProjectPlan ProjectPlan);

	/**
	 * 根据当前软硬件映射配置的parentid，查找当前流程下的所有模块的文件路径
	 * 
	 * @param parentId
	 * @return
	 */
	List<ProjectPlan> getPlanFilePathListByParentId(String parentId);

	boolean createThemeXML(XmlEntityMap entity, String proDetailId, String name);

	boolean createNetWorkXML(XmlEntityMap entity, String proDetailId, String name);

	/**
	 * 判断该流程模型是否可以生成集成代码
	 * 
	 * @param modelId
	 * @return
	 */
	boolean canIntegrationCodeCreate(String modelId);

	/**
	 * app组件工程的生成
	 * 
	 * @param userName
	 * @param procedureId
	 * @return
	 */
	R appAssemblyProjectCreate(String userName, String procedureId, String bspDirPath);

	/**
	 * 保存app组建工程的图片
	 * 
	 * @param file
	 * @return
	 */
	App saveAppImage(MultipartFile file, App app);

	/**
	 * 根据项目ID获取到所有的流程名称
	 * 
	 * @param projectId
	 * @return
	 */
	List<String> getProcedureNameListByProjectId(String projectId);

	Hardwarelibs getHardwarelibs(String id);

	void saveHardwarelibs(Hardwarelibs hardwarelibs);

	Hardwarelibs getlibsInDepolyment(String id);

	File getXmlFile(String id);

	void updateHardwarelib(Hardwarelibs hardwarelibs);

	void updataDeploymentXml(DeploymentXMLMap deploymentXMLMap);

	void deleteHardwarelibById(String id);

	/**
	 * 导出流程建模文件
	 */
	byte[] exportFile(String id, StringRef sr);

	void saveChipsfromhardwarelibs(Chipsfromhardwarelibs chipsfromhardwarelibs);

	Chipsfromhardwarelibs getChipsfromhardwarelibs(String id);

	String getWorkModeFilePath(String projectId);

	String getUserDefineTopicFilePath(String projectId);

	void deleteChipsFromHardwarelibs(String id);

	void getWorking(MultipartFile file, String flowName, String id);

	/**
	 * 修改软件框架下拉列表内容
	 * 
	 * @return
	 */
	R getSoftWareListAndPlatformName();

	/**
	 * 修改软件框架保存
	 * 
	 * @param software
	 * @return
	 */
	R updatePartSoftwareAndPlatform(Software software);

	/**
	 * 回显软件框架
	 * 
	 * @param id
	 * @return
	 */
	R showPartSoftwareAndPlatform(String id);

	/**
	 * 得到平台大类
	 * 
	 * @return
	 */
	R getPlatformList();

	public boolean deleteProcedureById(String procedureId);

    boolean deleteFilesFromLocal(Map filePath);

    R analysisThemeXML(String proDetailId);
}
