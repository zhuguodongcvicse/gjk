package com.inforbus.gjk.pro.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.inforbus.gjk.pro.api.entity.*;

import com.inforbus.gjk.pro.mapper.PartPlatformSoftwareMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Chipsfromhardwarelibs;
import com.inforbus.gjk.pro.api.entity.Component;
import com.inforbus.gjk.pro.api.entity.DeploymentNode;
import com.inforbus.gjk.pro.api.entity.DeploymentPart;
import com.inforbus.gjk.pro.api.entity.DeploymentXMLMap;

import com.inforbus.gjk.pro.api.entity.HardwareNode;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.Part;
import com.inforbus.gjk.pro.api.entity.PartPlatformSoftware;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.api.entity.ProjectPlan;
import com.inforbus.gjk.pro.api.util.LinuxUtil;
import com.inforbus.gjk.pro.api.util.MakeFileUtil;
import com.inforbus.gjk.pro.api.util.ProcedureXmlAnalysis;
import com.inforbus.gjk.pro.api.util.SylixosUtil;
import com.inforbus.gjk.pro.api.util.WorkbenchUtil;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import com.inforbus.gjk.pro.mapper.ManagerMapper;
import com.inforbus.gjk.pro.mapper.PartPlatformSoftwareMapper;
import com.inforbus.gjk.pro.mapper.ProjectMapper;
import com.inforbus.gjk.pro.service.ManagerService;

import lombok.AllArgsConstructor;

/**
 * @ClassName: ManagerServiceImpl 项目管理实现类
 * @Description:
 * @Author xiaohe
 * @DateTime 2019年4月24日 上午8:46:04
 */
@Service
@AllArgsConstructor
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, ProjectFile> implements ManagerService {

	private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

	@Autowired
	protected ProjectMapper projectMapper;
	@Autowired
	protected PartPlatformSoftwareMapper partPlatformSoftwareMapper;
	
	private static final String proDetailPath = JGitUtil.getLOCAL_REPO_PATH();
	private static String serverPath = JGitUtil.getLOCAL_REPO_PATH();
	private static final String flowInfPath = JGitUtil.getFlowInfPath();//获取流程内外部接口存放路径 add by hu
	private static final String gitDetailPath = JGitUtil.getLOCAL_REPO_PATH();//gitlu路径
	private static final String generateCodeResult = JGitUtil.getGenerateCodeResult();//集成代码生成结果存放路径
	
	/**
	 * @getTreeByProjectId
	 * @Description: 获取项目树形菜单
	 * @Author xiaohe
	 * @DateTime 2019年5月2日 下午2:12:28
	 * @param projectId
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#getTreeByProjectId(java.lang.String)
	 */
	@Override
	public List<ProjectFileVO> getTreeByProjectId(String projectId) {

		Project pro = projectMapper.listProjectByProjectId(projectId);
		List<ProjectFileVO> tree = Lists.newArrayList();

		tree.add(new ProjectFileVO(pro.getId(), pro.getProjectName(), "", "-1"));
		List<ProjectFile> files = baseMapper.listProjectFileByProjectId(projectId);
		// 将构件转成树
		for (ProjectFile file : files) {
			tree.add(new ProjectFileVO(file));
		}
		return tree;
	}

	/**
	 * @saveProDetail
	 * @Description: 保存项目详细信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午6:04:47
	 * @param projectFile
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#saveProDetail(com.inforbus.gjk.pro.api.entity.ProjectFile)
	 */
	private ProjectFile saveProDetail(ProjectFile projectFile) {
		if (projectFile.getId() == null) {
			projectFile.setId(IdGenerate.uuid());
		}
		baseMapper.saveProDetail(projectFile);
		return this.getById(projectFile.getId());
	}

	/**
	 * @saveProDetail
	 * @Description: 保存项目流程下的各个节点详细信息
	 * @Author cvics
	 * @DateTime 2019年5月8日 下午6:06:33
	 * @param projectId
	 * @param processName
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#saveProProcess(java.lang.String,
	 *      java.lang.String)
	 */
	public List<ProjectFile> saveProProcess(String projectId, String processName) {
		List<ProjectFile> files = new ArrayList<ProjectFile>();

		Project project = projectMapper.getProById(projectId);
		String defaultSoftwareId = project.getDefaultSoftwareId();
		String defaultBspId = project.getDefaultBspId();
		String filePath = "gjk" + File.separator + "project" + File.separator + project.getProjectName()
		+ File.separator;

		ProjectFile processFile = new ProjectFile(IdGenerate.uuid(), projectId, processName + "流程", "9", filePath,
				projectId, defaultSoftwareId, defaultBspId);
		files.add(processFile);

		filePath += processFile.getFileName() + File.separator;
		ProjectFile modelFile = new ProjectFile(IdGenerate.uuid(), projectId, "模型", "10", filePath, processFile.getId(),
				null, null);
		files.add(modelFile);

		filePath += modelFile.getFileName() + File.separator;
		ProjectFile file = new ProjectFile(IdGenerate.uuid(), projectId, "流程建模", "11", filePath, modelFile.getId(),
				null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, "硬件建模", "12", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, "软硬件映射配置", "13", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, "方案展示", "14", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, "部署图", "15", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, "自定义配置", "16", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, "系统配置", "17", filePath, modelFile.getId(), null, null);
		files.add(file);

		for (ProjectFile projectFile : files) {
			saveProDetail(projectFile);
		}

		return files;
	}

	/**
	 * @createXmlFile
	 * @Description: 根据proDetail的ID和XMLEntity创建XML文件
	 * @Author cvics
	 * @DateTime 2019年5月10日 下午1:51:04
	 * @param entity
	 * @param proDetailId
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#createXmlFile(com.inforbus.gjk.common.core.entity.XmlEntity,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createXmlFile(XmlEntity entity, String proDetailId) {
		ProjectFile projectFile = this.getById(proDetailId);
		String filePath = null;
		if (projectFile != null) {
			filePath = proDetailPath + projectFile.getFilePath() + File.separator + projectFile.getFileName() + ".xml";
		} else {
			return false;
		}
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
		JGitUtil.commitAndPush(file.getAbsolutePath(), "上传项目相关文件");
		return flag;
	}

	/**
	 * @createXmlFile
	 * @Description: 根据proDetail的ID和XMLEntity创建XML文件
	 * @Author cvics
	 * @DateTime 2019年5月10日 下午1:51:04
	 * @param entity
	 * @param proDetailId
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#createXmlFile(com.inforbus.gjk.common.core.entity.XmlEntity,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createXmlFile(XmlEntityMap entity, String proDetailId) {
		ProjectFile projectFile = this.getById(proDetailId);
		String filePath = null;
		if (projectFile != null) {
			filePath = proDetailPath + projectFile.getFilePath() + File.separator + projectFile.getFileName() + ".xml";
		} else {
			return false;
		}
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
		JGitUtil.commitAndPush(file.getAbsolutePath(), "上传项目相关文件");
		return flag;
	}

	/**
	 * @Title: getProDetailById
	 * @Description: 根据ID查询项目节点
	 * @Author cvics
	 * @DateTime 2019年5月10日 上午10:49:01
	 * @param id
	 * @return
	 */
	@Override
	public ProjectFile getProDetailById(String id) {
		return baseMapper.getProDetailById(id);
	}

	@Override
	public XmlEntityMap getcoefficientXmlEntityMap() {
		// TODO:临时使用，读取基础模板获取基础模板的信息
		String filePath = System.getProperty("user.dir") + "\\系统配置.xml";
		File file = new File(filePath);
		XmlEntityMap xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);
		return xmlEntityMap;
	}

	/**
	 * @Title: getProFileListByModelId
	 * @Description: 根据模型ID查询此ID下所有子节点
	 * @Author cvics
	 * @DateTime 2019年5月24日 上午10:53:25
	 * @param modelId
	 * @return
	 */
	private List<ProjectFile> getProFileListByModelId(String modelId) {
		return baseMapper.getProFileListByModelId(modelId);
	}

	private ProjectFile getProFileByModelIdAndProFileType(String modelId, String proFileType) {
		ProjectFile file = null;
		for (ProjectFile projectFile : getProFileListByModelId(modelId)) {
			if (projectFile.getFileType().equals(proFileType)) {
				file = projectFile;
				break;
			}
		}
		return file;
	}

	public List<HardwareNode> getCoeffNodeTree(String proDetailId) {
		File file = null;
		for (ProjectFile projectFile : getProFileListByModelId(this.getById(proDetailId).getParentId())) {
			if ("11".equals(projectFile.getFileType())) {
				file = new File(proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml");
				break;
			}
		}
		if (file != null) {
			return ProcedureXmlAnalysis.getHardwareNodeList(file);
		} else {
			return null;
		}
	}

	/**
	 * @getSysConfigByApiReturn
	 * @Description: 根据调用客户API获得系统配置的API返回值，组装数据
	 * @Author cvics
	 * @DateTime 2019年5月25日 下午3:49:46
	 * @param proDetailId
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#getSysConfigByApiReturn(java.lang.String)
	 */
	public Map<String, List<Object>> getSysConfigByApiReturn(String proDetailId) {
		ProjectFile file = getProDetailById(proDetailId);
		String modelId = file.getParentId();
		List<ProjectFile> files = getProFileListByModelId(modelId);
		String flowPath = null;
		String customizeFileName = null;
		String processFileName = null;
		String packinfoFileName = null; 
		
		Boolean bool = isXmlFileExist(proDetailId);
		for (ProjectFile projectFile : files) {
			
			if (projectFile.getFileType().equals("11")) {
				System.out.print(proDetailPath+ projectFile.getFilePath());
				 processFileName = proDetailPath + projectFile.getFilePath() +
				 projectFile.getFileName() + ".xml";
			}
			if (projectFile.getFileType().equals("16")) {
				customizeFileName = proDetailPath + projectFile.getFilePath() +
				 "新xml文件/xxx.xml";  //获取处理后的主题配置文件
			}

		}
		
		
		//需要添加判断genarateCode路径或packinfo文件是否存在，判断自定义主题配置文件是否生成
		ProjectFile processFile= getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, this.getById(proDetailId).getParentId()));
		packinfoFileName=gitDetailPath+processFile.getFilePath()+generateCodeResult + "/packinfo.xml";
		// 获取客户api的返回值
		Map<String, List<String>> apiReturnStringList = ExternalIOTransUtils.getCmpSysConfig(customizeFileName,
				packinfoFileName, processFileName);

		// 解析返回值
		Map<String, List<Object>> map = new HashMap<>();
		analysisApiReturnStringList(apiReturnStringList, map);

		return map;
	}

	/**
	 * 将调客户api return回来的值进行处理，以便前端使用 api返回的格式： cmp1=[Cmpcmp1, Cmpcmp1, cmpid_0, 1,
	 * 3, network_in{ip:192.168.1.1,port:8080,protocol:0;,
	 * network_out{ip:192.168.1.2,port:8081,protocol:0;,
	 * datapublish{name:cmp1tocmp3;, datasubscribe{name:cmp2tocmp1;,
	 * ctrlSubscribe{name:MastertoCmpcmpid_0;, ctrlPublish{name:Cmpcmpid_0toMaster;,
	 * moniPublish{name:CmptoStatusMonitor;]}
	 * 
	 * @param apiReturnStringList
	 * @param map
	 */
	private void analysisApiReturnStringList(Map<String, List<String>> apiReturnStringList,
			Map<String, List<Object>> map) {
		// 循环map
		for (Map.Entry<String, List<String>> entry : apiReturnStringList.entrySet()) {
			List<Object> list = new ArrayList<Object>();
			Map<String, List<Map<String, String>>> listMap = new HashMap<>();
			// 获取map中对应的String List
			for (String string : entry.getValue()) {
				if (string.contains("{")) {
					// 如果String中含有{，按{拆分数据
					String[] strArray = string.split("\\{");

					String name = strArray[0];
					String value = strArray[1];

					if (value.contains(";")) {
						// 如果String中含有; 按:拆分
						String[] valueArray = value.split(";");
						for (String str : valueArray) {
							if (str != "") {
								str = stringToHashMapJson(str);

								Map<String, String> valueMap = JSON.parseObject(str, HashMap.class);
								if (listMap.containsKey(name)) {
									listMap.get(name).add(valueMap);
								} else {
									List<Map<String, String>> childList = new ArrayList<Map<String, String>>();
									childList.add(valueMap);
									listMap.put(name, childList);
								}
							}
						}
					}
				} else {
					list.add(string);
				}
			}
			list.add(listMap);

			List<Object> rs = new ArrayList<Object>();
			for (Object obj : list) {
				if (!(obj instanceof String)) {
					for (Map.Entry<String, List<Map<String, String>>> mapEntry : ((Map<String, List<Map<String, String>>>) obj)
							.entrySet()) {
						rs.add(new R<>(mapEntry.getValue(), mapEntry.getKey()));
					}
				} else {
					rs.add(obj);
				}
			}
			map.put(entry.getKey(), rs);
		}
	}

	/**
	 * 将string字符串转化成可以转成HashMap的json格式
	 * 
	 * @param str
	 * @return
	 */
	private String stringToHashMapJson(String str) {
		String jsonString = "";
		if (str.contains(",")) {
			String[] strArray = str.split(",");
			for (String string : strArray) {
				jsonString += addQuotationMarks(string) + ",";
			}
			jsonString = jsonString.substring(0, jsonString.length() - 1);
		} else {
			jsonString = addQuotationMarks(str);
		}
		jsonString = "{" + jsonString + "}";
		return jsonString;
	}

	private String addQuotationMarks(String string) {
		String[] mapArray = string.split(":");
		String jsonString = "\"" + mapArray[0] + "\"" + ":" + "\"" + mapArray[1] + "\"";
		return jsonString;
	}

	/**
	 * @Title: isXmlFileExist
	 * @Description: 根据ID查找路径判断此文件是否存在
	 * @Author cvics
	 * @DateTime 2019年5月25日 下午3:40:17
	 * @param proDetailId
	 * @return
	 */
	public boolean isXmlFileExist(String proDetailId) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		File file = new File(proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml");
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: getSysConfigXmlEntityMap
	 * @Description: 解析系统配置的xml文件进行前台回显
	 * @Author cvics
	 * @DateTime 2019年5月25日 下午5:11:41
	 * @param proDetailId
	 * @return
	 */
	public XmlEntityMap getSysConfigXmlEntityMap(String proDetailId) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		File file = new File(gitDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml");

//		if (projectFile.getFileType().equals("14")) {
//			file = new File(gitDetailPath+projectFile.getFilePath()+ "方案展示.xml");
//		}
		//		if (projectFile.getFileType().equals("11")) {
		//			file = new File(System.getProperty("user.dir") + "/流程实例.xml");
		//		}

		XmlEntityMap xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);
		return xmlEntityMap;
	}

	/**
	 * @editProJSON
	 * @Description: 保存回显文件
	 * @Author xiaohe
	 * @DateTime 2019年5月31日 上午10:10:43
	 * @param proDetailId 流程编号
	 * @param objJson
	 * @return
	 * @see com.inforbus.gjk.pro.service.ManagerService#editProJSON(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean editProJSON(String proDetailId, Object objJson) {
		// String path = JGitUtil.getLOCAL_REPO_PATH();
		Map<String, String> map = baseMapper.findProJSON(proDetailId);
		StringBuffer sb = new StringBuffer(proDetailPath);
		sb.append(map.get("filePath"));
		String str = "\\流程模型-" + proDetailId + ".json";
		// 标记文件生成是否成功
		boolean flag = true;
		try {

			// 拼接文件完整路径
			// String fullPath = path + "流程模型-" + proDetailId + ".json";
			sb.append(str);
			// 保证创建一个新文件
			File file = new File(sb.toString());
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			// String jsonString =
			// JSON.toJSONString(JSON.toJSONString(objJson),SerializerFeature.PrettyFormat,
			// SerializerFeature.WriteMapNullValue,
			// SerializerFeature.WriteDateUseDateFormat);
			String jsonString = JSON.toJSONString(objJson, SerializerFeature.PrettyFormat,
					SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
			baseMapper.editProJSON(proDetailId, str);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * @Title: findProJSON
	 * @Description:查找Json
	 * @Author xiaohe
	 * @DateTime 2019年5月31日 下午1:57:13
	 * @param proId
	 * @return
	 */
	public Map<String, Object> findProJSON(String proId) {
		Map<String, Object> retMap = Maps.newHashMap();

		Map<String, String> map = baseMapper.findProJSON(proId);
		StringBuffer sb = new StringBuffer(proDetailPath);
		sb.append(map.get("filePath"));
		// String gitPath = JGitUtil.getLOCAL_REPO_PATH();
		// String parentFilePath = map.get("parentFilePath");
		File file = new File(sb.toString());
		if (file.exists()) {
			String str = sb.toString() + "/" + map.get("fileName") + ".xml";
			File file1 = new File(str);
			if (file1.exists()) {
				XmlEntityMap xmlJson = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file1);
				retMap.put("xmlJson", xmlJson);
			}
		}
		if (map.get("jsonPath") == null) {
			return null;
		}
		String jsonStr = "";
		try {
			File jsonFile = new File(sb.toString() + map.get("jsonPath"));
			FileReader fileReader = new FileReader(jsonFile);

			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
			int ch = 0;
			sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			Object json = JSON.toJavaObject(JSONObject.parseObject(jsonStr), Object.class);
			retMap.put("json", json);
			return retMap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// return jsonPath;
	}

	/**
	 * 查找当前流程的流程名
	 */
	@Override
	public ProjectFile getProcessName(String id) {
		System.out.println("sssssss" + baseMapper.getProcessName(baseMapper.getProcessName(id).getParentId()));
		return baseMapper.getProcessName(baseMapper.getProcessName(id).getParentId());
	}

	/**
	 * 调用软硬件映射按钮的接口
	 */
	@Override
	public void simplePlan(ArrayList<String> schemeFileList, String simplePlanFile) {
		 ExternalIOTransUtils.simplePlan(schemeFileList, simplePlanFile);//update by zhx
	}

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @returnzz
	 * 
	 */
	@Override
	public List<ProjectFile> getFilePathListById(String id) {
		return baseMapper.getFilePathListById(id);
	}

	@Override
	public void saveProjectPlan(ProjectPlan projectPlan) {
		baseMapper.saveProjectPlan(projectPlan);
	}

	@Override
	public List<ProjectPlan> getPlanFilePathListByParentId(String parentId) {
		return baseMapper.getPlanFilePathListByParentId(parentId);
	}

	@Override
	public boolean canIntegrationCodeCreate(String modelId) {
		boolean flag = true;
		// 获取模型下所有记录
		List<ProjectFile> projectFiles = getProFileListByModelId(modelId);
		// 遍历所有记录判断记录对应文件是否存在
		for (ProjectFile file : projectFiles) {
			// 若其中之一文件不存在，将标记修改为false并返回
			if (!isXmlFileExist(file.getId())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * 创建组件工程文件夹
	 * 
	 * @param userName    六位员工号
	 * @param procedureId 流程ID
	 * @return
	 */
	@Override
	public App appAssemblyProjectCreate(String userName, String procedureId, String bspDirPath) {
		App app = null;

		// 获取流程记录
		ProjectFile projectFile = this.getById(procedureId);
		// 创建流程xml文件路径
		String procedureFilePath = proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
		File file = new File(procedureFilePath);

		ProjectFile sysConfigXmlFile = getProFileByModelIdAndProFileType(projectFile.getParentId(), "17");
		String sysConfigFilePath = sysConfigXmlFile.getFilePath() + sysConfigXmlFile.getFileName() + ".xml";

		String proceId = this.getById(this.getById(procedureId).getParentId()).getParentId();
		List<PartPlatformSoftware> partPlatformSoftwares = partPlatformSoftwareMapper.getByProcedureId(proceId);

		String appFilePath = null;
		if (file.exists()) {
			app = new App();

			List<HardwareNode> hardwareNodes = null;
			try {
				// 解析流程模型xml获取所有根组件
				hardwareNodes = ProcedureXmlAnalysis.getHardwareNodeList(file);
			} catch (Exception e) {
				logger.error("解析流程文件错误，请确保流程xml文件配置正确。");
			}

			List<Map<String, Object>> maps = null;
			try {
				// 获取硬件存入数据库的数据
				String chipStr = getChipsfromhardwarelibs(procedureId).getChips();
				maps = (List<Map<String, Object>>) JSONArray.parse(chipStr);
			} catch (Exception e) {
				logger.error("获取硬件建模数据失败，请确保硬件建模配置正确。");
			}

			// 获取流程对应记录
			ProjectFile proFile = this.getById(proceId);
			// 根据"员工号_项目名称_流程名称APP"格式创建App的名字及路径
			appFilePath = proDetailPath + "gjk" + File.separator + "APP" + File.separator + userName + "_"
					+ projectMapper.getProById(projectFile.getProjectId()).getProjectName() + "_"
					+ proFile.getFileName() + "APP" + File.separator;

			if (new File(appFilePath).exists()) {
				cn.hutool.core.io.FileUtil.del(appFilePath);
			}

			Map<String, String> partnamePlatformMap = new HashMap<String, String>();

			// 遍历所有根组件，创建根组件文件夹
			for (HardwareNode hardwareNode : hardwareNodes) {
				for (Map<String, Object> map : maps) {
					if (hardwareNode.getNodeName().equals(map.get("ID").toString())) {
						for (Part part : hardwareNode.getRootPart()) {
							partnamePlatformMap.put(part.getPartName(), map.get("hrTypeName").toString());
							createAssemblyDir(appFilePath, part, map.get("hrTypeName").toString(),
									partPlatformSoftwares);
						}
					}
				}
			}
			app.setPartnamePlatform(JSONArray.toJSONString(partnamePlatformMap));
			app.setFileName(new File(appFilePath).getName());
			app.setFilePath(new File(appFilePath).getParent().substring(proDetailPath.length()));
			app.setSysconfigFilePath(sysConfigFilePath);
			app.setProcessId(proceId);

			// 拷贝bsp对应的文件夹到app组件工程目录下
			createBspDir(bspDirPath, appFilePath);

			JGitUtil.commitAndPush(appFilePath, "上传App组件工程");
		}

		return app;
	}

	private void createBspDir(String bspDirPath, String appFilePath) {
		String bspFilePath = appFilePath + "bsp" + File.separator;
		File bspFile = new File(bspFilePath);
		if (!bspFile.exists()) {
			bspFile.mkdirs();
		}
		try {
			FileUtil.copyDir(proDetailPath + bspDirPath, bspFilePath);
		} catch (IOException e) {
			logger.error("复制BSP文件夹错误，请联系系统管理员");
		}
	}

	/**
	 * 创建根组件对应的文件夹
	 * 
	 * @param softwareFilePath 软件框架文件路径
	 * @param appFilePath      app组件工程文件路径
	 * @param part             组件
	 */
	private void createAssemblyDir(String appFilePath, Part part, String libsType,
			List<PartPlatformSoftware> partPlatformSoftwares) {
		// 创建根组件文件夹的名称及文件路径
		String assemblyName = appFilePath + part.getPartName();

		String softwareFilePath = "";
		String softwareName = "";
		String platformName = libsType;
		for (PartPlatformSoftware software : partPlatformSoftwares) {
			if (software.getPlatformName().contains(";")) {
				String[] platforms = software.getPlatformName().split(";");
				for (String s : platforms) {
					if (s.trim() != "" && s.equals(libsType)) {
						softwareFilePath = software.getSoftwareFilePath();
						softwareName = software.getSoftwareName();
					}
				}
			}
		}

		try {
			// 将软件框架所有子文件及文件夹拷贝到根组件文件夹中
			if (!"".equals(softwareFilePath)) {
				FileUtil.copyDir(proDetailPath + softwareFilePath, assemblyName);
			}
		} catch (IOException e) {
			logger.error("复制软件框架文件夹错误，请联系系统管理员");
		}

		String makefileType = null;

		// 获取当前类的路径
		String filePath = ManagerServiceImpl.class.getResource("").getPath();
		try {
			// 中文乱码问题
			filePath = URLDecoder.decode(filePath, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			logger.error("中文路径转义失败。");
		}
		// 找到bootstrap.properties的地址
		filePath = filePath.substring(0, filePath.indexOf("target/classes/") + "target/classes/".length())
				+ "platformType.yml";
		File dumpFile = new File(filePath);

		Map father;
		try {
			father = Yaml.loadType(dumpFile, HashMap.class);
			makefileType = father.get(platformName).toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("读取配置文件失败，请检查配置文件中配置是否正确");
		}

		if (makefileType == null) {
			makefileType = "VS2010";
		}

		// 查找组件文件夹下文件夹名为App的文件夹路径
		String appFilePathName = FileUtil.getSelectStrFilePath(assemblyName, "App");
		if (appFilePathName == null) {
			// 如果未找到App文件夹路径，在组件文件夹下创建App文件夹
			appFilePathName = assemblyName + File.separator + "App";
		}
		// 获取include文件夹 文件夹路径规则:../App/Include/Spb/
		String includeFilePath = appFilePathName + File.separator + "Include" + File.separator + "Spb" + File.separator;
		// 获取src文件夹 文件夹路径规则:../App/Src/Spb/
		String srcFilePath = appFilePathName + File.separator + "Src" + File.separator + "Spb" + File.separator;

		// 创建空集合，存储所有.h文件路径
		Set<String> hFilePathSet = new HashSet<String>();
		// 创建空集合，存储所有.h文件的makeFile路径
		Set<String> hMakeFilePathSet = new HashSet<String>();

		// 创建空集合，存储所有.c和.cpp文件路径
		Set<String> cFilePathSet = new HashSet<String>();
		// 存储需要查找的文件的后缀集合
		List<String> selectFileExtensionList = new ArrayList<String>();
		selectFileExtensionList.add(".c");
		selectFileExtensionList.add(".cpp");
		// 创建空集合，存储所有.c和.cpp文件的makeFile路径
		Set<String> cMakeFilePathSet = new HashSet<String>();

		// TODO
		Set<String> apiNeedStringSet = new HashSet<>();

		// 遍历组件中所有构件，在构件文件夹中获取所有.h .c .cpp文件路径集合
		List<Component> compList = part.getComponents();
		for (Component comp : compList) {
			FileUtil.getSelectStrFilePathList(hFilePathSet, proDetailPath + comp.getFunctionPath(), ".h");

			FileUtil.getSelectStrFilePathList(cFilePathSet, proDetailPath + comp.getFunctionPath(),
					selectFileExtensionList);
		}

		List<String> cFilePathList = new ArrayList<String>();
		cFilePathList.addAll(cFilePathSet);

		// 将.h文件拷贝到指定路径下，并将拷贝后的文件路径按照makeFile文件路径切割并存入集合
		for (String hFilePath : hFilePathSet) {
			try {
				// TODO:makeFile文件路径待修改
				String hMakeFilePath = "." + File.separator + new File(FileUtil.copyFile(hFilePath, includeFilePath))
						.getAbsolutePath().substring(assemblyName.length() + 1);
				hMakeFilePathSet.add(hMakeFilePath);

				// 调客户Api需要的不带后缀的文件名
				String hFileName = new File(hFilePath).getName();
				apiNeedStringSet.add(hFileName.substring(0, hFileName.lastIndexOf(".")));
			} catch (Exception e) {
				logger.error("拷贝h文件错误，请联系管理员。");
			}
		}

		// 将.c .cpp文件拷贝到指定路径下，并将拷贝后的文件路径按照makeFile文件路径切割并存入集合
		for (String cFilePath : cFilePathSet) {
			try {
				String cMakeFilePath = "." + File.separator + new File(FileUtil.copyFile(cFilePath, srcFilePath))
						.getAbsolutePath().substring(assemblyName.length() + 1);
				cMakeFilePathSet.add(cMakeFilePath);

				String cFileName = new File(cFilePath).getName();
				apiNeedStringSet.add(cFileName.substring(0, cFileName.lastIndexOf(".")));
			} catch (IOException e) {
				logger.error("拷贝c文件错误，请联系管理员。");
			}
		}

		// 将set集合转成List集合
		List<String> hMakeFilePathList = new ArrayList<>();
		hMakeFilePathList.addAll(hMakeFilePathSet);
		List<String> cMakeFilePathList = new ArrayList<>();
		cMakeFilePathList.addAll(cMakeFilePathSet);

		if (makefileType.trim().toLowerCase().equals("VS2010".toLowerCase())) {
			// 获取makeFile文件,vs2010中的.filters文件
			Set<String> makeFileList = new HashSet<String>();
			String filtersFileName = null;
			FileUtil.getSelectStrFilePathList(makeFileList, assemblyName, ".filters");
			if (makeFileList.size() > 0) {
				List<String> list = new ArrayList<>();
				list.addAll(makeFileList);
				filtersFileName = list.get(0);
			}
			// 获取makeFile文件,vs2010中的. vcxproj文件
			String vcxprojFileName = null;
			makeFileList = new HashSet<String>();
			FileUtil.getSelectStrFilePathList(makeFileList, assemblyName, ".vcxproj");
			if (makeFileList.size() > 0) {
				List<String> list = new ArrayList<>();
				list.addAll(makeFileList);
				vcxprojFileName = list.get(0);
			}

			// 调用makeFile工具类，传入参数makeFile文件路径、需要修改的文件路径集合、文件类型
			if (filtersFileName != null) {
				MakeFileUtil.updateVcxprojFiltersFile(filtersFileName, hMakeFilePathList, MakeFileUtil.hFile);
				MakeFileUtil.updateVcxprojFiltersFile(filtersFileName, cMakeFilePathList, MakeFileUtil.cFile);
			}
			if (vcxprojFileName != null) {
				MakeFileUtil.updateVcxprojFiltersFile(vcxprojFileName, hMakeFilePathList, MakeFileUtil.hFile);
				MakeFileUtil.updateVcxprojFiltersFile(vcxprojFileName, cMakeFilePathList, MakeFileUtil.cFile);
			}
		} else if (makefileType.trim().toLowerCase().equals("Workbench".toLowerCase())) {
			WorkbenchUtil.updateWorkbench(assemblyName);
		} else if (makefileType.trim().toLowerCase().equals("Sylixos".toLowerCase())) {
			SylixosUtil.updateSylixos(assemblyName, softwareName);
		} else if (makefileType.trim().toLowerCase().equals("Linux".toLowerCase())) {
			LinuxUtil.updateLinux(cFilePathList, assemblyName, ".c");
		}

		// TODO:调客户接口,参数：apiNeedStringSet转list,apiFilePath
		List<String> apiFileList = new ArrayList<String>();
		apiFileList.addAll(apiNeedStringSet);
		String apiFilePath = new File(appFilePathName).getParentFile().getAbsolutePath();
	}

	public App saveAppImage(MultipartFile file, App app) {
		String appDirPath = proDetailPath + app.getFilePath() + File.separator + app.getFileName() + File.separator;
		try {
			appDirPath += "Image" + File.separator;
			File targetFile = new File(appDirPath);
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}

			appDirPath += IdGenerate.uuid()
					+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			targetFile = new File(appDirPath);
			file.transferTo(targetFile);

			JGitUtil.commitAndPush(appDirPath, "上传App组件工程图片");

			app.setId(IdGenerate.uuid());
			app.setBackPath(appDirPath.substring(proDetailPath.length()));
		} catch (IOException e) {
			logger.error("保存App组件工程img失败。");
			app = null;
			e.printStackTrace();
		}
		return app;
	}

	// public static void main(String[] args) {
	// String simplePlanFile =
	// "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\缩略方案.xml";
	// ArrayList<String> schemeFileList = new ArrayList<>();
	// schemeFileList.add("D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\方案评价指标.xml");
	// schemeFileList.add("D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\软硬件映射配置.xml");
	// System.out.println(schemeFileList);
	// try {
	// SimpleScheme.createSimpleScheme(schemeFileList, simplePlanFile);
	// System.out.println(simplePlanFile);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	@Override
	public boolean createThemeXML(XmlEntityMap entity, String proDetailId, String name) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		String filePath = null;
		String fileName = null;
		if (projectFile != null) {
			filePath = proDetailPath + projectFile.getFilePath();
			fileName = projectFile.getFileName() + "__" + name + ".xml";
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean flag = XmlFileHandleUtil.createXmlFile(entity, new File(filePath + fileName));
		String flowFilePath = "";
		List<ProjectFile> ProjectFileList = baseMapper.getFilePathListById(proDetailId);
		for (ProjectFile proFile : ProjectFileList) {
			if (proFile.getFileType().equals("11")) {
				flowFilePath = proDetailPath + proFile.getFilePath() + proFile.getFileName() + ".xml";
			}
		}
		String newFilePath = filePath + "新xml文件/";
		File newFile = new File(newFilePath);
		if (!newFile.exists()) {
			newFile.mkdirs();
		}
		 ExternalIOTransUtils.createUserDefineTopic(flowFilePath, filePath + fileName,
		 newFilePath + "xxx.xml");
		baseMapper.saveNewFilePath(newFilePath + "xxx.xml", proDetailId);

		JGitUtil.commitAndPush(filePath + fileName, "上传构件相关文件");
		return flag;
	}

	@Override
	public boolean createNetWorkXML(XmlEntityMap entity, String proDetailId, String name) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		String filePath = null;
		String fileName = null;
		if (projectFile != null) {
			filePath = proDetailPath + projectFile.getFilePath();
			fileName = projectFile.getFileName() + "__" + name + ".xml";
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean flag = XmlFileHandleUtil.createXmlFile(entity, new File(filePath + fileName));
		JGitUtil.commitAndPush(filePath + fileName, "上传构件相关文件");
		return flag;
	}

	/**
	 * 根据当前流程的id，获取流程文件路径
	 * 
	 * @param procedureId
	 * @return
	 */
	private File getProcedureXmlFile(String procedureId) {
		ProjectFile projectFile = this.getById(procedureId);
		String filePath = proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
		File file = new File(filePath);
		return file;
	}

	/**
	 * 查找硬件建模数据
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Hardwarelibs getHardwarelibs(String id) {
		ProjectFile projectFile = this.getById(id);
		String modelId = projectFile.getParentId();
		Hardwarelibs hardwarelib = baseMapper.getHardwarelibs(modelId);
		return hardwarelib;
	}

	@Override
	public void saveHardwarelibs(Hardwarelibs hardwarelibs) {
		Map allIdByHardwarelibId = getAllIdByHardwarelibId(hardwarelibs);
		String modelId = (String) allIdByHardwarelibId.get("modelId");
		String flowId = (String) allIdByHardwarelibId.get("flowId");
		String projectId = (String) allIdByHardwarelibId.get("projectId");
		//		String projectFile = (String) allIdByHardwarelibId.get("projectFile");
		/*
		 * ProjectFile projectFile = this.getById(hardwarelibs.getId()); String modelId
		 * = projectFile.getParentId(); ProjectFile modelFile = this.getById(modelId);
		 * String flowId = modelFile.getParentId();
		 */
		//		hardwarelibs.setId(projectFile.getId());
		hardwarelibs.setFlowId(flowId);
		hardwarelibs.setProjectId(projectId);
		hardwarelibs.setModelId(modelId);
		// System.out.println(hardwarelibs);
		baseMapper.saveHardwarelibs(hardwarelibs);
	}

	public Map getAllIdByHardwarelibId(Hardwarelibs hardwarelibs) {
		ProjectFile projectFile = this.getById(hardwarelibs.getId());
		String modelId = projectFile.getParentId();
		ProjectFile modelFile = this.getById(modelId);
		String flowId = modelFile.getParentId();
		Map<String, Object> allIdMap = new HashMap<>();
		allIdMap.put("modelId", modelId);
		allIdMap.put("flowId", flowId);
		allIdMap.put("projectId", projectFile.getProjectId());
		allIdMap.put("projectFile", projectFile);
		return allIdMap;
	}

	@Override
	public Hardwarelibs getlibsInDepolyment(String id) {
		ProjectFile deploymentFile = this.getById(id);
		String projectId = deploymentFile.getProjectId();
		Hardwarelibs hardwarelibs = baseMapper.getHardwarelibByProjectId(projectId);

		return this.getHardwarelibs(hardwarelibs.getId());
	}

	@Override
	public void updateHardwarelib(Hardwarelibs hardwarelibs) {
		Map allIdByHardwarelibId = getAllIdByHardwarelibId(hardwarelibs);
		String modelId = (String) allIdByHardwarelibId.get("modelId");
		String flowId = (String) allIdByHardwarelibId.get("flowId");
		String projectId = (String) allIdByHardwarelibId.get("projectId");
		ProjectFile projectFile = (ProjectFile) allIdByHardwarelibId.get("projectFile");
		System.out.println("modelId" + modelId);
		System.out.println("flowId" + flowId);
		System.out.println("projectFile" + projectFile);
		System.out.println("projectId" + projectId);
		baseMapper.updateHardwarelib(hardwarelibs);
	}

	@Override
	public List<String> getProcedureNameListByProjectId(String projectId) {
		List<ProjectFile> procedureList = baseMapper.getProcedureListByProjectId(projectId);
		List<String> procedureNameList = new ArrayList<String>();
		for (ProjectFile file : procedureList) {
			procedureNameList.add(file.getFileName().substring(0, file.getFileName().length() - 2));
		}
		return procedureNameList;
	}

	/*
	 * 部署图获取流程建模xml文件地址
	 */
	@Override
	public File getXmlFile(String id) {
		String Path = null;
		String local_REPO_PATH = null;
		for (ProjectFile projectFile : getProFileListByModelId(this.getById(id).getParentId())) {
			if ("11".equals(projectFile.getFileType())) {
				Path = projectFile.getFilePath();
				local_REPO_PATH = JGitUtil.getLOCAL_REPO_PATH();
				break;
			}
		}
		File file = new File(local_REPO_PATH + Path + "/流程建模.xml");
		return file;
	}

	@Override
	public void updataDeploymentXml(DeploymentXMLMap deploymentXMLMap) {
		String procedureId = null;
		XmlEntityMap entityMap = null;
		String id = deploymentXMLMap.getId();
		for (ProjectFile projectFile : getProFileListByModelId(this.getById(id).getParentId())) {
			if ("11".equals(projectFile.getFileType())) {
				entityMap = getSysConfigXmlEntityMap(projectFile.getId());
				procedureId = projectFile.getId();
				break;
			}
		}
		if (entityMap != null) {
			// 遍历部署图回传数据
			for (DeploymentNode bakCompAllArray : deploymentXMLMap.getBakcompAllArray()) {
				String nodeId = bakCompAllArray.getCompId();
				System.out.println(bakCompAllArray);
				List<DeploymentPart> bakPart = bakCompAllArray.getBakPart();
				// 遍历备份组件
				for (DeploymentPart bakParts : bakPart) {
					String bakpartName = bakParts.getPartName();
					String bakcpuid = bakParts.getCpuid();
					// List<DeploymentPart> partname = compAllArray.getRootPart();
					// 遍历xml解析数据
					for (XmlEntityMap entityMap1 : entityMap.getXmlEntityMaps()) {
						if (entityMap1.getLableName().equals("node")) {
							String xmlNoedId = entityMap1.getAttributeMap().get("id");
							if (xmlNoedId.equals(nodeId)) {
								for (XmlEntityMap entityMap2 : entityMap1.getXmlEntityMaps()) {
									if (entityMap2.getLableName().equals("层级属性")) {
										for (XmlEntityMap entityMap3 : entityMap2.getXmlEntityMaps()) {
											if (entityMap3.getLableName().equals("所属部件")) {
												for (XmlEntityMap entityMap4 : entityMap3.getXmlEntityMaps()) {
													if (entityMap4.getLableName().equals("部署配置")) {
														for (XmlEntityMap entityMap5 : entityMap4.getXmlEntityMaps()) {
															if (entityMap5.getLableName().equals("所属节点")) {
																String cmpname = entityMap5.getAttributeMap()
																		.get("cmpName");
																if (cmpname.equals(bakpartName)) {
																	entityMap5.getAttributeMap().put("name", bakcpuid);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			// 遍历部署图回传数据
			for (DeploymentNode compAllArray : deploymentXMLMap.getCompAllArray()) {
				String nodeId = compAllArray.getCompId();
				System.out.println(compAllArray);
				List<DeploymentPart> rootPart = compAllArray.getRootPart();
				List<DeploymentPart> bakPart = compAllArray.getBakPart();
				// 遍历根组件
				for (DeploymentPart Part : rootPart) {
					String partName = Part.getPartName();
					String cpuid = Part.getCpuid();

					// List<DeploymentPart> partname = compAllArray.getRootPart();
					// 遍历xml解析数据
					for (XmlEntityMap entityMap1 : entityMap.getXmlEntityMaps()) {
						if (entityMap1.getLableName().equals("node")) {
							String xmlNoedId = entityMap1.getAttributeMap().get("id");
							if (xmlNoedId.equals(nodeId)) {
								for (XmlEntityMap entityMap2 : entityMap1.getXmlEntityMaps()) {
									if (entityMap2.getLableName().equals("层级属性")) {
										for (XmlEntityMap entityMap3 : entityMap2.getXmlEntityMaps()) {
											if (entityMap3.getLableName().equals("所属部件")) {
												for (XmlEntityMap entityMap4 : entityMap3.getXmlEntityMaps()) {
													if (entityMap4.getLableName().equals("部署配置")) {
														for (XmlEntityMap entityMap5 : entityMap4.getXmlEntityMaps()) {
															if (entityMap5.getLableName().equals("所属节点")) {
																String cmpname = entityMap5.getAttributeMap()
																		.get("cmpName");
																if (cmpname.equals(partName)) {
																	entityMap5.getAttributeMap().put("name", cpuid);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			System.out.println(entityMap.getXmlEntityMaps());
			createXmlFile(entityMap, procedureId);

		}
	}

	@Override
	public void deleteHardwarelibById(String id) {
		baseMapper.deleteHardwarelibById(id);
	}
	public byte[] exportFile(String id,StringRef sr) {
		Map<String, String> map = baseMapper.findProJSON(id);
		String xmlFilepath = proDetailPath + map.get("filePath") + map.get("fileName") + ".xml";
		String jsonFilepath = proDetailPath + map.get("filePath") + map.get("jsonPath");
		sr.setVal(map.get("fileName"));
		File xmlFile = new File(xmlFilepath);
		File jsonFile = new File(jsonFilepath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		ZipEntry zipEntry = null;
		if(xmlFile.exists() && jsonFile.exists()) {
			File[] files = {xmlFile,jsonFile};
			for(int i = 0;i<files.length;i++) {
				try {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(files[i]));
					zipEntry = new ZipEntry(files[i].getName());
					zip.putNextEntry(zipEntry);
					zip.write(FileUtils.readFileToByteArray(files[i]));
					IOUtils.closeQuietly(bis);
					zip.flush();
					zip.closeEntry();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	@Override
	public void saveChipsfromhardwarelibs(Chipsfromhardwarelibs chipsfromhardwarelibs) {
		ProjectFile projectFile = this.getById(chipsfromhardwarelibs.getId());
		String modelId = projectFile.getParentId();
		Chipsfromhardwarelibs chips = baseMapper.getChipsById(modelId);
		if (chips != null){
			baseMapper.updateChipsfromhardwarelibs(chipsfromhardwarelibs);
		} else {
			ProjectFile modelFile = this.getById((modelId));
			String flowId = modelFile.getParentId();
			chipsfromhardwarelibs.setFlowId(flowId);
			chipsfromhardwarelibs.setModelId(modelId);
			chipsfromhardwarelibs.setProjectId(projectFile.getProjectId());
			baseMapper.saveChipsfromhardwarelibs(chipsfromhardwarelibs);
		}
	}

	@Override
	public Chipsfromhardwarelibs getChipsfromhardwarelibs(String id) {
		ProjectFile projectFile = this.getById(id);
		String modelId = projectFile.getParentId();
		Chipsfromhardwarelibs chips = baseMapper.getChipsById(modelId);
		/*List<ChildrenNodes> childrenNodes = baseMapper.getAllChildrenNodes(modelId);
		Chipsfromhardwarelibs chips = null;
		for (ChildrenNodes childrenNode : childrenNodes) {
			if ("硬件建模".equals(childrenNode.getFileName())){
				chips =  baseMapper.getChipsById(childrenNode.getId());
				break;
			}
		}*/
		return chips;
	}	

	/**
	 * 获取流程建模xml文件地址
	 */
	@Override
	public String getWorkModeFilePath(String projectId) {
		// TODO Auto-generated method stub
		String Path = null;
		String local_REPO_PATH = null;
		ProjectFile model=	baseMapper.selectOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getParentId, projectId));
		for (ProjectFile projectFile : getProFileListByModelId(model.getId())) {
			if ("11".equals(projectFile.getFileType())) {
				Path = projectFile.getFilePath();
				local_REPO_PATH = JGitUtil.getLOCAL_REPO_PATH();
				break;
			}
		}
		String workModeFilePath = local_REPO_PATH + Path + "流程建模.xml";

		return workModeFilePath;
	}

	/**
	 * 获取主题配置值xml地址
	 */

	@Override
	public String getUserDefineTopicFilePath(String projectId) {
		// TODO Auto-generated method stub
		String Path = null;
		String local_REPO_PATH = null;
		ProjectFile model=	baseMapper.selectOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getParentId, projectId));
		for (ProjectFile projectFile : getProFileListByModelId(model.getId())) {
			if ("11".equals(projectFile.getFileType())) {
				Path = projectFile.getFilePath();
				local_REPO_PATH = JGitUtil.getLOCAL_REPO_PATH();
				break;
			}
		}
		String userDefineTopicFilePath = local_REPO_PATH + Path + "/新xml文件/xxx.xml";

		return userDefineTopicFilePath;
	}
	@Override
	public void deleteChipsFromHardwarelibs(String id) {
		baseMapper.deleteChipsFromHardwarelibs(id);
	}
	@Override
	public void getWorking(MultipartFile file, String flowName, String id) {
		String fileName = file.getOriginalFilename();
		ProjectFile pro = baseMapper.getProDetailById(id);
		String flowPath = proDetailPath + pro.getFilePath()+"工作模式\\";
		/************************************update by  zhx********************************************/
		//String filePath = flowPath+"xxx.xml";
		String filePath = proDetailPath+pro.getFilePath()+flowInfPath+File.separator+"系数文件.xml" ;
		/*****************************************************************************************/
		File flowFile = new File(flowPath);
		if(!flowFile.exists()) {
			flowFile.mkdirs();
		}
		String tmpPath = new String();
		tmpPath = new String(serverPath+"/gjk/upload/" + fileName).replace("\\", "/");
		
		File newFile = null;
		if (StringUtils.isNotEmpty(tmpPath)) {	
			try {
				newFile = new File(tmpPath);
				if (!newFile.getParentFile().exists()) {
					newFile.getParentFile().mkdirs();
				}
				newFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ExternalIOTransUtils.parseSystemPara(flowName, tmpPath,filePath);
		System.out.println(file);
		
	}
		@Override
	public R getSoftWareListAndPlatformName() {
		List<Software> softwareList = baseMapper.getAllSoftwareList();
		for (Software software : softwareList) {
			List<GjkPlatform> platformList = baseMapper.getAllPlatformListBySoftwareId(software.getId());
			String platformNames = "";
			for (GjkPlatform gjkPlatform : platformList) {
				platformNames += gjkPlatform.getName() + ";";
			}
			// 因为用不到软件构架库库的描述字段，所有将描述字段存放平台库名称
			software.setDescription(platformNames);
		}
		return new R<>(softwareList);
	}

	@Override
	public R updatePartSoftwareAndPlatform(Software software) {
		String procedureId = software.getId();
		String softwareIds = software.getDescription();
		partPlatformSoftwareMapper.deletePartPlatformSoftware(procedureId);
		String[] softwareIdsArr = softwareIds.split(";");
		List<String> softwareIdList = new ArrayList<>();
		for (String softwareId : softwareIdsArr) {
			softwareIdList.add(softwareId);
		}
		String ids = "'" + StringUtils.join(softwareIdList, "','") + "'";
		List<Software> softwareList = baseMapper.getAllSoftwareListByIdIn(ids);
		for (Software soft : softwareList) {
			List<GjkPlatform> platformList = baseMapper.getAllPlatformListBySoftwareId(soft.getId());
			String platformNames = "";
			for (GjkPlatform gjkPlatform : platformList) {
				platformNames += gjkPlatform.getName() + ";";
			}
			PartPlatformSoftware partPlatformSoftware = new PartPlatformSoftware();
			partPlatformSoftware.setId(IdGenerate.uuid());
			partPlatformSoftware.setSoftwareId(soft.getId());
			partPlatformSoftware.setSoftwareName(soft.getSoftwareName());
			partPlatformSoftware.setSoftwareVersion(soft.getVersion());
			partPlatformSoftware.setSoftwareFilePath(soft.getFilePath());
			partPlatformSoftware.setPlatformName(platformNames);
			partPlatformSoftware.setProcedureId(procedureId);
			partPlatformSoftware.setCreateTime(LocalDateTime.now());
			partPlatformSoftwareMapper.savePartPlatformSoftware(partPlatformSoftware);
		}
		return new R<>(true);
	}
    @Override
    public R showPartSoftwareAndPlatform(String id) {
        return new R<>(partPlatformSoftwareMapper.getAllPartPlatformSoftwareByProcedureId(id));
    }

    @Override
    public R getPlatformList() {
        return new R<>(baseMapper.getPlatformList());
    }
}
