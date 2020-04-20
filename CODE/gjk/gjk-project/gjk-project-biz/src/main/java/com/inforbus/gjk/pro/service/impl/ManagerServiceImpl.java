package com.inforbus.gjk.pro.service.impl;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.inforbus.gjk.admin.api.entity.*;
import com.inforbus.gjk.common.core.constant.DeploymentConstants;
import com.inforbus.gjk.common.core.entity.*;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.dto.AppDataDTO;
import com.inforbus.gjk.pro.api.dto.BaseTemplateIDsDTO;
import com.inforbus.gjk.pro.api.entity.*;

import com.inforbus.gjk.pro.api.entity.GjkPlatform;
import com.inforbus.gjk.pro.api.entity.Software;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;
import com.inforbus.gjk.pro.api.feign.ExternalInfInvokeService;
import com.inforbus.gjk.pro.api.feign.RemoteCodeGenerationService;
import com.inforbus.gjk.pro.mapper.*;
import com.inforbus.gjk.pro.service.BaseTemplateService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
import com.inforbus.gjk.common.core.constant.CommonConstants;
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
import com.inforbus.gjk.pro.mapper.PartPlatformSoftwareMapper;
import com.inforbus.gjk.pro.service.ManagerService;

import flowModel.CheckResult;

/**
 * @ClassName: ManagerServiceImpl 项目管理实现类
 * @Description:
 * @Author xiaohe
 * @DateTime 2019年4月24日 上午8:46:04
 */
@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, ProjectFile> implements ManagerService {

	private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);
	@Autowired
	protected ProjectMapper projectMapper;
	@Autowired
	protected PartPlatformSoftwareMapper partPlatformSoftwareMapper;
	@Autowired
	private BaseTemplateService baseTemplateService;
	@Autowired
	protected PartPlatformBSPMapper partPlatformBSPMapper;
	@Autowired
	private AppMapper appMapper;
	@Autowired
	private AmqpTemplate rabbitmqTemplate;
	@Autowired
	protected DisposeDataCenterServiceFeign dataCenterServiceFeign;
	@Autowired
	private ExternalInfInvokeService externalInfInvokeService;
//	private static final String proDetailPath = JGitUtil.getLOCAL_REPO_PATH();
//	private static final String integerCodeFileName = JGitUtil.getINTEGER_CODE_FILE_NAME();
//	private static String serverPath = JGitUtil.getLOCAL_REPO_PATH();
//
//	private static final String flowInfPath = JGitUtil.getFlowInfPath();// 获取流程内外部接口存放路径 add by hu
//	private static final String gitDetailPath = JGitUtil.getLOCAL_REPO_PATH();// gitlu路径
//	private static final String generateCodeResult = JGitUtil.getGenerateCodeResult();// 集成代码生成结果存放路径
//	private static final String softToHardResult = JGitUtil.getSoftToHardResult();// 软硬件映射结果文件存放路径

    @Autowired
    private RemoteCodeGenerationService remoteCodeGenerationService;
    @Value("${git.local.path}")
    private String proDetailPath;
    @Value("${integer.code.file.name}")
    private String integerCodeFileName;
    @Value("${git.local.path}")
    private String serverPath;
    // 获取流程内外部接口存放路径 add by hu
    @Value("${gjk.pro.process.flowInfPath}")
    private String flowInfPath;
    // gitlu路径
    @Value("${git.local.path}")
    private String gitDetailPath;
    // 集成代码生成结果存放路径
    @Value("${gjk.pro.process.generateCodeResult}")
    private String generateCodeResult;
    // 软硬件映射结果文件存放路径
    @Value("${gjk.pro.process.softToHardResult}")
    private String softToHardResult;

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

		ProjectFile processFile = new ProjectFile(IdGenerate.uuid(), projectId, processName + "", "9", filePath,
				projectId, defaultSoftwareId, defaultBspId);
		files.add(processFile);

		filePath += processFile.getFileName() + File.separator;
		ProjectFile modelFile = new ProjectFile(IdGenerate.uuid(), projectId, -1, "模型", "10", filePath,
				processFile.getId(), null, null);
		files.add(modelFile);

		filePath += modelFile.getFileName() + File.separator;
		ProjectFile file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "流程建模", "11", filePath, modelFile.getId(),
				null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "硬件建模", "12", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "软硬件映射配置", "13", filePath, modelFile.getId(), null,
				null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "方案展示", "14", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "部署图", "15", filePath, modelFile.getId(), null, null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "自定义配置", "16", filePath, modelFile.getId(), null,
				null);
		files.add(file);

		file = new ProjectFile(IdGenerate.uuid(), projectId, -1, "系统配置", "17", filePath, modelFile.getId(), null, null);
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
	public synchronized String createXmlFile(XmlEntityMap entity, String proDetailId) {
		ProjectFile projectFile = this.getById(proDetailId);
		String filePath = null;
		if (projectFile != null) {
			filePath = proDetailPath + projectFile.getFilePath() + File.separator + projectFile.getFileName() + ".xml";
		} else {
			// return false;
		}
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
		JGitUtil.commitAndPush(file.getAbsolutePath(), "上传项目相关文件");
		return filePath;
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

	/*
	 * 2019年11月5日 15:22:53 wang修改
	 *
	 *
	 */
	@Override
	public XmlEntityMap getcoefficientXmlEntityMap(String proDetailId) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		Project project = projectMapper.getProById(projectFile.getProjectId());
		// 获取到模板idjson串
		String json = project.getBasetemplateIds();
		// 把json串转成json对象
		BaseTemplateIDsDTO baseTemplateIDsDTO = JSON.parseObject(json, BaseTemplateIDsDTO.class);
		// BaseTemplateIDsDTO baseTemplateIDsDTO = (BaseTemplateIDsDTO)JSON.parse(json);
		// 获取到系统配置模板id
		String sysTempId = baseTemplateIDsDTO.getSysTempId();
		// 解析系统配置xml文件返回数据
		return getXmlEntityMap(sysTempId);
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

	@Override
	public List<HardwareNode> getCoeffNodeTree(String proDetailId) {
		File file = null;
		for (ProjectFile projectFile : getProFileListByModelId(this.getById(proDetailId).getParentId())) {
			if ("11".equals(projectFile.getFileType())) {
				file = new File(proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml");
				break;
			}
		}
		if (file.exists()) {
			XmlEntityMap XmlEntityMap = dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(file.getAbsolutePath()).getData();
			return ProcedureXmlAnalysis.getHardwareNodeList(file, XmlEntityMap);
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
	@Override
	public R getSysConfigByApiReturn(String proDetailId) {
		ProjectFile file = getProDetailById(proDetailId);
		String modelId = file.getParentId();
		List<ProjectFile> files = getProFileListByModelId(modelId);
		String flowPath = null;
		String customizeFileName = null;
		String processFileName = null;
		String packinfoFileName = null;

		for (ProjectFile projectFile : files) {
			if (projectFile.getFileType().equals("11")) {
//				System.out.print(proDetailPath + projectFile.getFilePath());
				processFileName = proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
			}
			if (projectFile.getFileType().equals("16")) {
				customizeFileName = proDetailPath + projectFile.getFilePath() + "自定义配置__网络配置.xml";
			}
		}

		ProjectFile processFile = getOne(
				Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, this.getById(proDetailId).getParentId()));
		packinfoFileName = gitDetailPath + processFile.getFilePath() + generateCodeResult + "/packinfo.xml";

		File customizefile = new File(customizeFileName);
		File packinfofile = new File(packinfoFileName);
		File processfile = new File(processFileName);

		if (!customizefile.exists()) {
			return new R<>(new Exception("缺少自定义配置文件，请先配置自定义配置。"));
		}

		if (!packinfofile.exists()) {
			return new R<>(new Exception("缺少集成代码的文件，请先生成集成代码。"));
		}

		if (!processfile.exists()) {
			return new R<>(new Exception("缺少流程建模的文件，请先配置流程建模。"));
		}

		// 解析返回值
		Map<String, List<Object>> map = new HashMap<>();
		// 获取客户api的返回值
//		Map<String, List<String>> apiReturnStringList = ExternalIOTransUtils.getCmpSysConfig(customizeFileName, packinfoFileName, processFileName);
		Map<String, List<String>> apiReturnStringList = externalInfInvokeService.getCmpSysConfig(customizeFileName, packinfoFileName, processFileName).getData();
		analysisApiReturnStringList(apiReturnStringList, map);

		return new R<>(map);
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
		File hsmLocalFile = new File(gitDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml");// 老耿代码
		// XmlEntityMap xmlEntityMap =
		// XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);//老耿代码
//		if (projectFile.getFileType().equals("14")) {
//			file = new File(gitDetailPath+projectFile.getFilePath()+ "方案展示.xml");
//		}
//		 if (projectFile.getFileType().equals("11")) {
//		 file = new File(System.getProperty("user.dir") + "/流程实例.xml");
//		 }
		if (hsmLocalFile.exists()) {
//			return XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(hsmLocalFile);
			return dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(hsmLocalFile.getAbsolutePath()).getData();
		}
		Project project = projectMapper.getProById(projectFile.getProjectId());
		String json = project.getBasetemplateIds();// 获取到模板idjson串
		BaseTemplateIDsDTO baseTemplateIDsDTO = JSON.parseObject(json, BaseTemplateIDsDTO.class);// 把json串转成json对象
		String hsmTempId = baseTemplateIDsDTO.getHsmTempId();// 获取到软硬件映射模板id
		return getXmlEntityMap(hsmTempId);// 解析软硬件映射xml文件返回数据
	}
	
	/**
	 * @Title: getDisposeXmlEntityMap
	 * @Description: 解析软硬件映射配置的xml文件进行前台回显
	 * @Author geng
	 * @DateTime 20200415
	 * @param proDetailId
	 * @return
	 */
	@Override
	public XmlEntityMap getDisposeXmlEntityMap(String proDetailId) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		//项目中是否已经存在的文件路径
		File hsmLocalFile = new File(gitDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml");
		String localFilePath = gitDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
		//如果该项目中已经有该文件，则读取该文件内容显示在页面上；否则读取基础模板里的最新的软硬件映射配置文件
		if (hsmLocalFile.exists()) {
			R<XmlEntityMap> r = new R<XmlEntityMap>();
			r = dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(localFilePath);
			return r.getData();
			
		}
		Project project = projectMapper.getProById(projectFile.getProjectId());
		// 获取到模板idjson串
		String json = project.getBasetemplateIds();
		// 把json串转成json对象
		BaseTemplateIDsDTO baseTemplateIDsDTO = JSON.parseObject(json, BaseTemplateIDsDTO.class);
		// 获取到软硬件映射模板id
		String hsmTempId = baseTemplateIDsDTO.getHsmTempId();
		// 解析软硬件映射xml文件返回数据
		return getXmlEntityMap(hsmTempId);
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
			String str = sb.toString() + map.get("fileName") + ".xml";
			File file1 = new File(str);
			if (file1.exists()) {
				XmlEntityMap xmlJson = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file1);
				retMap.put("xmlJson", xmlJson);
				retMap.put("flowFilePath", str);
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
		ExternalIOTransUtils.simplePlan(schemeFileList, simplePlanFile);// update by zhx
	}

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @returnzz
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
	 * app组件工程生成预处理
	 * 
	 * @param ufile
	 * @param messageMap
	 * @return
	 */
	public R appProCreatePretreatment(Map<String, String> messageMap) {
		String returnStr = "";
		// 获取流程记录
		ProjectFile projectFile = this.getById(messageMap.get("procedureXmlId"));
		// 创建流程xml文件路径
		String procedureFilePath = proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
		File file = new File(procedureFilePath);

		String modelId = projectFile.getParentId();
		String proceId = this.getById(modelId).getParentId();

		List<PartPlatformSoftware> partPlatformSoftwares = partPlatformSoftwareMapper.getByProcedureId(proceId);
		List<PartPlatformBSP> partPlatformBSPs = partPlatformBSPMapper.getByProcedureId(proceId);

		if (file.exists()) {
			List<HardwareNode> hardwareNodes = null;
			try {
				// 解析流程模型xml获取所有根组件
				hardwareNodes = ProcedureXmlAnalysis.getHardwareNodeList(file);
			} catch (Exception e) {
				logger.error("解析流程文件错误，请确保流程建模配置正确。");
				returnStr += "解析流程文件错误，请确保流程建模配置正确。";
			}

			List<Map<String, Object>> maps = null;
			try {
				// 获取硬件存入数据库的数据
				String chipStr = getChipsfromhardwarelibs(messageMap.get("procedureXmlId")).getChips();
				maps = (List<Map<String, Object>>) JSONArray.parse(chipStr);
			} catch (Exception e) {
				logger.error("获取硬件建模数据失败，请确保硬件建模配置正确。");
				returnStr += "获取硬件建模数据失败，请确保硬件建模配置正确。";
			}

			Map platformProp = null;
			// 获取配置文件并解析
			R prop = getMakefileTypeByProperties();
			if (CommonConstants.FAIL.equals(prop.getCode())) {
				returnStr += prop.getMsg();
			} else {
				platformProp = (Map) prop.getData();
			}

			if (hardwareNodes == null || maps == null || platformProp == null) {
				return new R<>(new Exception(returnStr));
			}

			// 遍历所有根组件，创建根组件文件夹
			for (HardwareNode hardwareNode : hardwareNodes) {
				boolean flag = false;
				for (Map<String, Object> map : maps) {
					if (map.containsKey("nodeID") && hardwareNode.getNodeName().equals(map.get("nodeID").toString())) {
						flag = true;
						for (Part part : hardwareNode.getRootPart()) {
							String libsType = map.get("hrTypeName").toString();
							// 读取配置文件中平台类对应的软件平台类型
							String platformType = null;
							try {
								platformType = platformProp.get(libsType).toString();
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("读取配置文件失败，请检查配置文件中" + libsType + "配置是否正确");
								returnStr += "读取配置文件失败，请检查配置文件中" + libsType + "配置是否正确";
							}

							String softwareFilePath = "";
							for (PartPlatformSoftware software : partPlatformSoftwares) {
								if (software.getPlatformName().contains(libsType)) {
									softwareFilePath = software.getSoftwareFilePath();
								}
							}
							if ("".equals(softwareFilePath)) {
								if (returnStr.contains("请配置" + libsType + "对应的软件框架,")) {
									String s = "请配置" + libsType + "对应的软件框架,部件";
									StringBuilder sb = new StringBuilder(returnStr);
									sb.insert(returnStr.indexOf(s) + s.length(), part.getPartName() + ",");
									returnStr = sb.toString();
								} else {
									returnStr += "请配置" + libsType + "对应的软件框架," + "部件" + part.getPartName() + "缺少的软件框架。";
								}
								logger.error("请配置" + libsType + "对应的软件框架," + "部件" + part.getPartName() + "缺少的软件框架。");
							}

							String bspFilePath = "";
							for (PartPlatformBSP bsp : partPlatformBSPs) {
								if (bsp.getPlatformName().contains(libsType)) {
									bspFilePath = bsp.getBspFilePath();
								}
							}
							if ("".equals(bspFilePath)) {
								if(libsType.equals("Sylixos")||libsType.equals("Workbench")) {
								if (returnStr.contains("请配置" + libsType + "对应的bsp,")) {
									String s = "请配置" + libsType + "对应的bsp,部件";
									StringBuilder sb = new StringBuilder(returnStr);
									sb.insert(returnStr.indexOf(s) + s.length(), part.getPartName() + ",");
									returnStr = sb.toString();
								} else {
									returnStr += "请配置" + libsType + "对应的bsp," + "部件" + part.getPartName() + "缺少对应的BSP。";
								}
								logger.error("请配置" + libsType + "对应的bsp," + "部件" + part.getPartName() + "缺少对应的BSP。");
							
								}
							}
						}
					}
				}
				if (!flag) {
					logger.error("流程建模与硬件建模" + hardwareNode.getNodeName() + "匹配错误，请重新配置流程建模与硬件建模。");
					returnStr += "流程建模与硬件建模" + hardwareNode.getNodeName() + "匹配错误，请重新配置流程建模与硬件建模。";
				}
			}
		} else {
			returnStr += "流程配置xml文件不存在，请重新配置流程。";
		}

		if ("".equals(returnStr)) {
			return new R<>(true);
		} else {
			return new R<>(new Exception(returnStr));
		}

	}

	/**
	 * 创建组件工程文件夹
	 * 
	 * @param userName    六位员工号
	 * @param procedureId 流程ID`
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public R appAssemblyProjectCreate(MultipartFile ufile, Map<String, String> messageMap) {
		R r = new R<>();
		App app = null;

		// 获取流程记录
		ProjectFile projectFile = this.getById(messageMap.get("procedureXmlId"));
		// 创建流程xml文件路径
		String procedureFilePath = proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
		File file = new File(procedureFilePath);

		ProjectFile sysConfigXmlFile = getProFileByModelIdAndProFileType(projectFile.getParentId(), "17");
		String sysConfigFilePath = sysConfigXmlFile.getFilePath() + sysConfigXmlFile.getFileName() + ".xml";

		String modelId = projectFile.getParentId();
		String proceId = this.getById(modelId).getParentId();

		int flowId = this.getById(proceId).getFlowId();
		String integerCodeFilePath = proDetailPath + this.getById(modelId).getFilePath() + File.separator
				+ integerCodeFileName;

		List<PartPlatformSoftware> partPlatformSoftwares = partPlatformSoftwareMapper.getByProcedureId(proceId);
		List<PartPlatformBSP> partPlatformBSPs = partPlatformBSPMapper.getByProcedureId(proceId);

		String appFilePath = null;
		if (file.exists()) {
			app = new App();

			List<HardwareNode> hardwareNodes = null;
			try {
				// 解析流程模型xml获取所有根组件
				hardwareNodes = ProcedureXmlAnalysis.getHardwareNodeList(file);
			} catch (Exception e) {
				logger.error("解析流程文件错误，请确保流程建模配置正确。");
				return new R<>(new Exception("解析流程文件错误，请确保流程建模配置正确。"));
			}

			List<Map<String, Object>> maps = null;
			try {
				// 获取硬件存入数据库的数据
				String chipStr = getChipsfromhardwarelibs(messageMap.get("procedureXmlId")).getChips();
				maps = (List<Map<String, Object>>) JSONArray.parse(chipStr);
			} catch (Exception e) {
				logger.error("获取硬件建模数据失败，请确保硬件建模配置正确。");
				return new R<>(new Exception("获取硬件建模数据失败，请确保硬件建模配置正确。"));
			}

			Map platformProp = null;
			// 获取配置文件并解析
			R prop = getMakefileTypeByProperties();
			if (CommonConstants.FAIL.equals(prop.getCode())) {
				return prop;
			} else {
				platformProp = (Map) prop.getData();
			}

			// 创建App的名字及路径，并存放在流程文件夹下
			appFilePath = proDetailPath + File.separator + this.getById(modelId).getFilePath() + File.separator + "app"
					+ File.separator + "AppPro" + File.separator;

			if (new File(appFilePath).exists()) {
				cn.hutool.core.io.FileUtil.del(appFilePath);
			}

			Map<String, String> partnamePlatformMap = new HashMap<String, String>();
			// 遍历所有根组件，创建根组件文件夹
			for (HardwareNode hardwareNode : hardwareNodes) {
				boolean flag = false;
				for (Map<String, Object> map : maps) {
					if (map.containsKey("nodeID") && hardwareNode.getNodeName().equals(map.get("nodeID").toString())) {
						flag = true;
						for (Part part : hardwareNode.getRootPart()) {
							String libsType = map.get("hrTypeName").toString();
							partnamePlatformMap.put(part.getPartName(), libsType);
							// 读取配置文件中平台类对应的软件平台类型
							String platformType = null;
							try {
								platformType = platformProp.get(libsType).toString();
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("读取配置文件失败，请检查配置文件中" + libsType + "配置是否正确");
								return new R<>(new Exception("读取配置文件失败，请检查配置文件中" + libsType + "配置是否正确"));
							}

							String softwareFilePath = "";
							String softwareName = "";
							for (PartPlatformSoftware software : partPlatformSoftwares) {
								if (software.getPlatformName().contains(libsType)) {
									softwareFilePath = software.getSoftwareFilePath();
									softwareName = software.getSoftwareName();
								}
							}
							String bspFilePath = "";
							for (PartPlatformBSP bsp : partPlatformBSPs) {
								if (bsp.getPlatformName().contains(libsType)) {
									bspFilePath = bsp.getBspFilePath();
								}
							}

							// 获取Sylixos工程名
							String sylixosProjectName = null;
							if ("Sylixos".equals(platformType)) {
								sylixosProjectName = new File(softwareFilePath).getName();
							}
							// 拷贝bsp和软件框架
							copySoftwareAndBsp(r, appFilePath, part.getPartName(), libsType, platformType,
									softwareFilePath, softwareName, bspFilePath);
							if (CommonConstants.FAIL.equals(r.getCode())) {
								return r;
							}

							modifyAssemblyDir(r, appFilePath + part.getPartName(), part, platformType,
									integerCodeFilePath, proDetailPath + bspFilePath, sylixosProjectName);
							if (CommonConstants.FAIL.equals(r.getCode())) {
								return r;
							}
						}
					}
				}
				if (!flag) {
					logger.error("流程建模与硬件建模数据匹配错误，请重新配置流程建模与硬件建模。");
					return r.setException(new Exception("流程建模与硬件建模数据匹配错误，请重新配置流程建模与硬件建模。"));
				}
			}

			app.setPartnamePlatform(JSONArray.toJSONString(partnamePlatformMap));
			app.setLocalDeploymentPlan(messageMap.get("localDeploymentPlan"));
			app.setFileName(new File(appFilePath).getName());
			app.setFilePath(new File(appFilePath).getParent().substring(proDetailPath.length()));
			app.setSysconfigFilePath(sysConfigFilePath);
			app.setProcessId(proceId);
			app.setFlowId(flowId);

			try {
				String appDirPath = appFilePath + File.separator + "Image" + File.separator;
				File targetFile = new File(appDirPath);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}

				appDirPath += IdGenerate.uuid()
						+ ufile.getOriginalFilename().substring(ufile.getOriginalFilename().lastIndexOf("."));
				targetFile = new File(appDirPath);
				ufile.transferTo(targetFile);

				app.setId(IdGenerate.uuid());
				app.setBackPath(appDirPath.substring(proDetailPath.length()));
				app.setUserId(Integer.parseInt(messageMap.get("userId")));
			} catch (IOException e) {
				e.printStackTrace();
				return r.setException(new Exception("保存App组件工程img失败。"));
			}

			JGitUtil.commitAndPush(appFilePath, "上传App组件工程");
			return new R<>(app, "生成App组件工程文件夹成功。");

		} else {
			return r.setException(new Exception("流程配置xml文件不存在，请重新配置流程。"));
		}
	}

	private void copySoftwareAndBsp(R r, String appFilePath, String partName, String libsType, String platformType,
			String softwareFilePath, String softwareName, String bspFilePath) {

		if ("".equals(bspFilePath)) {
			if(libsType.equals("Sylixos")||libsType.equals("Workbench")) {
				logger.error("请配置" + libsType + "对应的bsp," + "部件" + partName + "缺少对应的BSP。");
				r.setException(new Exception("请配置" + libsType + "对应的BSP," + "部件" + partName + "缺少对应的BSP。"));
				return;
			}
		}
		Thread thread = new Thread(() -> {
			File file = new File(appFilePath + "bsp" + File.separator + platformType + File.separator
					+ new File(bspFilePath).getName());
			if (!file.exists()) {
				// 拷贝bsp对应的文件夹到app组件工程目录下
				file.mkdirs();
				try {
					FileUtil.copyFile(proDetailPath + bspFilePath, file.getAbsolutePath());
				} catch (IOException e) {
					logger.error("复制BSP文件夹错误，请联系系统管理员");
					r.setException(new Exception("复制BSP文件夹错误，请联系系统管理员"));
					return;
				}
			}
		});
		if(libsType.equals("Sylixos")||libsType.equals("Workbench")) {
			thread.start();
		}

		if ("".equals(softwareFilePath)) {
			logger.error(partName + "寻找软件框架错误，请配置" + libsType + "对应的软件框架。");
			r.setException(new Exception("请配置" + libsType + "对应的软件框架," + "部件" + partName + "缺少对应的软件框架。"));
			return;
		}
		try {
			// 创建根组件文件夹的名称及文件路径
			String assemblyName = appFilePath + partName;
			// 将软件框架所有子文件及文件夹拷贝到根组件文件夹中
			FileUtil.copyFile(proDetailPath + softwareFilePath, assemblyName);
		} catch (IOException e) {
			logger.error("复制软件框架" + softwareName + "文件夹错误，请联系系统管理员。");
			r.setException(new Exception("复制软件框架" + softwareName + "文件夹错误，请联系系统管理员。"));
			return;
		}
	}

	/**
	 * 拷贝.c .h .cpp文件 并在之后调用makeFile
	 * 
	 * @param r
	 * @param assemblyName        根组件文件夹路径
	 * @param part
	 * @param makefileType
	 * @param integerCodeFilePath
	 * @param sylixosProjectName
	 */
	private void modifyAssemblyDir(R r, String assemblyName, Part part, String makefileType, String integerCodeFilePath,
			String bspFilePath, String sylixosProjectName) {

		// 创建空集合，存储所有.h文件路径
		Set<String> hFilePathSet = new HashSet<String>();
		// 创建空集合，存储所有.h文件的makeFile路径
		Set<String> hMakeFilePathSet = new HashSet<String>();
		// 创建空集合，存储所有.c和.cpp文件路径
		Set<String> cFilePathSet = new HashSet<String>();
		// 创建空集合，存储所有.c和.cpp文件的makeFile路径
		Set<String> cMakeFilePathSet = new HashSet<String>();
		// 调接口时使用,储存调用客户接口所需要的数据
		Set<String> apiNeedStringSet = new HashSet<>();
		// 获取Linux编译需要的所有APP/src文件路径下的所有.c文件
		Set<String> linuxCFilePathSet = new HashSet<>();
		// 调接口时使用,存构件的函数名
		List<String> compFuncNameList = new ArrayList<String>();
		// 存储需要查找的文件的后缀集合
		List<String> selectFileExtensionList = new ArrayList<String>();
		selectFileExtensionList.add(".c");
		selectFileExtensionList.add(".cpp");

		Thread copyFile = new Thread(() -> {
			// 查找组件文件夹下文件夹名为App的文件夹路径
//			String appFilePathName = FileUtil.getSelectStrFilePath(assemblyName, "App");
			String appFilePathName = null;
			try {
				appFilePathName = FileUtil.getAppPath(assemblyName, "App");
			} catch (IOException e) {
				logger.error("查找App路径失败，请联系管理员。");
				r.setException(new Exception("查找App路径失败，请联系管理员。"));
				return;
			}
			if (appFilePathName == null) {
				// 如果未找到App文件夹路径，在组件文件夹下创建App文件夹
				appFilePathName = assemblyName + File.separator + "App";
			}
			// 获取集成代码文件夹 文件夹路径规则:../App/Src/
			String partIntegerCodeFilePath = appFilePathName + File.separator + "Src" + File.separator;
			// 获取include文件夹 文件夹路径规则:../App/Include/Spb/
			String includeFilePath = appFilePathName + File.separator + "Include" + File.separator + "Spb"
					+ File.separator;
			// 获取src文件夹 文件夹路径规则:../App/Src/Spb/
			String srcFilePath = appFilePathName + File.separator + "Src" + File.separator + "Spb" + File.separator;

			// 复制集成代码
			Set<String> integerCodeSet = new HashSet<String>();
			FileUtil.getSelectStrFilePathList(integerCodeSet, integerCodeFilePath, "Cmp" + part.getPartName(),
					".c");
			try {
				for (String filepath : integerCodeSet) {
					FileUtil.copyFile(filepath, partIntegerCodeFilePath, "CmpSpbIntg.c");
				}
			} catch (IOException e) {
				logger.error("复制集成代码失败，请联系管理员。");
				r.setException(new Exception("复制集成代码失败，请联系管理员。"));
				return;
			}

			getCompCHFileAndSave(r, part, assemblyName, includeFilePath, srcFilePath, hFilePathSet,
					hMakeFilePathSet, cFilePathSet, cMakeFilePathSet, apiNeedStringSet, compFuncNameList,
					selectFileExtensionList);
			if (CommonConstants.FAIL.equals(r.getCode())) {
				return;
			}

			FileUtil.getSelectStrFilePathList(linuxCFilePathSet, partIntegerCodeFilePath, selectFileExtensionList);

//				try {
//					// 原始需求调用客户接口,apiFileList中存添加的.c .cpp .h文件不带后缀的文件名
//					List<String> apiFileList = new ArrayList<String>();
//					apiFileList.addAll(apiNeedStringSet);
//					ExternalIOTransUtils.modifySpbInclude(apiFileList,
//							new File(appFilePathName).getParentFile().getAbsolutePath());
//				} catch (IOException e) {
//					e.printStackTrace();
//					logger.error("调用客户接口失败，请联系管理员。");
//					r.setException(new Exception("调用客户接口失败，请联系管理员。"));
//					return;
//				}

//				try {
//					// 调用客户接口,list中存添加的构件的函数名
//					ExternalIOTransUtils.modifySpbInclude(compFuncNameList, appFilePathName);
//				} catch (IOException e) {
//					e.printStackTrace();
//					logger.error("调用客户接口失败，请联系管理员。");
//					r.setException(new Exception("调用客户接口失败，请联系管理员。"));
//					return;
//				}
		});
		copyFile.start();

		Thread modifyFile = new Thread() {
			@Override
			public void run() {
				while (copyFile.isAlive()) {
					try {
						sleep(500);
						logger.info("等待文件拷贝中");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 将set集合转成List集合
				List<String> hMakeFilePathList = (List<String>) getListBySet(hMakeFilePathSet);
				List<String> cMakeFilePathList = (List<String>) getListBySet(cMakeFilePathSet);
				List<String> cFilePathList = (List<String>) getListBySet(cFilePathSet);
				List<String> linuxCFilePath = (List<String>) getListBySet(linuxCFilePathSet);

				try {
					if (makefileType.trim().toLowerCase().equals("VS2010".toLowerCase())) {
						modifyVs2010MakeFile(r, assemblyName, hMakeFilePathList, cMakeFilePathList);
					} else if (makefileType.trim().toLowerCase().equals("Workbench".toLowerCase())) {
						WorkbenchUtil.updateWorkbench(assemblyName);
					} else if (makefileType.trim().toLowerCase().equals("Sylixos".toLowerCase())) {
						SylixosUtil.updateSylixos(assemblyName, bspFilePath, sylixosProjectName);
					} else if (makefileType.trim().toLowerCase().startsWith("Linux".toLowerCase())) {
						LinuxUtil.updateLinux(cFilePathList, assemblyName, ".c");
						// LinuxUtil.updateLinux(linuxCFilePath, assemblyName, ".c");
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("调用" + makefileType + "工具类修改MakeFile文件失败，请联系管理员。");
					r.setException(new Exception("调用" + makefileType + "工具类修改MakeFile文件失败，请联系管理员。"));
					return;
				}
			}
		};
		modifyFile.start();
	}

	private void getCompCHFileAndSave(R r, Part part, String assemblyName, String includeFilePath, String srcFilePath,
			Set<String> hFilePathSet, Set<String> hMakeFilePathSet, Set<String> cFilePathSet,
			Set<String> cMakeFilePathSet, Set<String> apiNeedStringSet, List<String> compFuncNameList,
			List<String> selectFileExtensionList) {
		// 遍历组件中所有构件，在构件文件夹中获取所有.h .c .cpp文件路径集合
		List<Component> compList = part.getComponents();
		for (Component comp : compList) {
			compFuncNameList.add(comp.getFunctionName());

			// 查找构件文件夹中所有的.h文件
			FileUtil.getSelectStrFilePathList(hFilePathSet, proDetailPath + comp.getFunctionPath(), ".h");
			// 查找构件文件夹中所有的.c和.cpp文件
			FileUtil.getSelectStrFilePathList(cFilePathSet, proDetailPath + comp.getFunctionPath(),
					selectFileExtensionList);
		}

		// 将.h文件拷贝到指定路径下，并将拷贝后的文件路径按照makeFile文件路径切割并存入集合
		for (String hFilePath : hFilePathSet) {
			try {
				// TODO:makeFile文件路径待修改
				String hMakeFilePath = "." + File.separator
						+ new File(FileUtil.copyFile(hFilePath, includeFilePath, new File(hFilePath).getName()))
								.getAbsolutePath().substring(assemblyName.length() + 1);
				hMakeFilePathSet.add(hMakeFilePath);

				// 调客户Api需要的不带后缀的文件名
				String hFileName = new File(hFilePath).getName();
				apiNeedStringSet.add(hFileName.substring(0, hFileName.lastIndexOf(".")));
			} catch (Exception e) {
				logger.error("复制.h文件错误，请联系管理员。");
				e.printStackTrace();
				r.setException(new Exception("复制" + hFilePath + "文件到" + includeFilePath + "路径下错误，请联系管理员。"));
				return;
			}
		}

		// 将.c .cpp文件拷贝到指定路径下，并将拷贝后的文件路径按照makeFile文件路径切割并存入集合
		for (String cFilePath : cFilePathSet) {
			try {
				String cMakeFilePath = "." + File.separator
						+ new File(FileUtil.copyFile(cFilePath, srcFilePath, new File(cFilePath).getName()))
								.getAbsolutePath().substring(assemblyName.length() + 1);
				cMakeFilePathSet.add(cMakeFilePath);

				// 调客户Api需要的不带后缀的文件名
				String cFileName = new File(cFilePath).getName();
				apiNeedStringSet.add(cFileName.substring(0, cFileName.lastIndexOf(".")));
			} catch (IOException e) {
				logger.error("复制.c文件错误，请联系管理员。");
				r.setException(new Exception("复制" + cFilePath + "文件到" + srcFilePath + "路径下错误，请联系管理员。"));
				return;
			}
		}
	}

	private R getMakefileTypeByProperties() {
		// 获取当前类的路径
		Map father;
		File file = null;
		InputStream inputStream = null;
		try {
			// 通过流的方式来读取配置文件，解决打成jar包后无法读取配置文件的问题
			ClassPathResource classPathResource = new ClassPathResource("platformType.yml");
			inputStream = classPathResource.getInputStream();
			// 建立临时文件
			file = File.createTempFile("bootstrap", ".properties");
			// 将信息写入临时文件
			FileUtils.copyInputStreamToFile(inputStream, file);
			father = Yaml.loadType(file, HashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取配置文件失败，请联系管理员检查配置文件是否正确");
			return new R<>(new Exception("读取配置文件失败，请联系管理员检查配置文件是否正确"));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new R<>(father);
	}

	private void modifyVs2010MakeFile(R r, String assemblyName, List<String> hMakeFilePathList,
			List<String> cMakeFilePathList) {
		// 获取makeFile文件,vs2010中的.filters文件
		Set<String> makeFileList = new HashSet<String>();
		String filtersFileName = null;
		FileUtil.getSelectStrFilePathList(makeFileList, assemblyName, ".filters");
		if (makeFileList.size() > 0) {
			List<String> list = new ArrayList<>();
			list.addAll(makeFileList);
			filtersFileName = list.get(0);
		} else {
			r.setException(
					new Exception("查找" + new File(assemblyName).getName() + "路径下.filters文件失败，请确认选择的软件框架正确及其他配置正确。"));
			return;
		}
		// 获取makeFile文件,vs2010中的. vcxproj文件
		String vcxprojFileName = null;
		makeFileList = new HashSet<String>();
		FileUtil.getSelectStrFilePathList(makeFileList, assemblyName, ".vcxproj");
		if (makeFileList.size() > 0) {
			List<String> list = new ArrayList<>();
			list.addAll(makeFileList);
			vcxprojFileName = list.get(0);
		} else {
			r.setException(
					new Exception("查找" + new File(assemblyName).getName() + "路径下.vcxproj文件失败，请确认选择的软件框架正确及其他配置正确。"));
			return;
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
	}

	private List<?> getListBySet(Collection c) {
		List<?> list = new ArrayList<>();
		list.addAll(c);
		return list;
	}

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
		String newFilePath = filePath + "TopicConfig/";
		File newFile = new File(newFilePath);
		if (!newFile.exists()) {
			newFile.mkdirs();
		}
		ExternalIOTransUtils.createUserDefineTopic(flowFilePath, filePath + fileName,
				newFilePath + "UserDefineTopicFile.xml");
		baseMapper.saveNewFilePath(newFilePath + "UserDefineTopicFile.xml", proDetailId);

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
		ProjectFile modelFile = this.getById(modelId);
		String flowId = modelFile.getParentId();
		Hardwarelibs hardwarelib = baseMapper.getHardwarelibs(flowId);
		return hardwarelib;
	}

	@Override
	public synchronized int saveHardwarelibs(Hardwarelibs hardwarelibs) {
		Map allIdByHardwarelibId = getAllIdByHardwarelibId(hardwarelibs);
		String modelId = (String) allIdByHardwarelibId.get("modelId");
		String flowId = (String) allIdByHardwarelibId.get("flowId");
		String projectId = (String) allIdByHardwarelibId.get("projectId");
		Hardwarelibs hardwarelib = baseMapper.getHardwarelibs(flowId);
//		System.out.println("hardwarelib-------------------------->: " + hardwarelib);
		if (hardwarelib != null) {
			return 0;
		}
		// String projectFile = (String) allIdByHardwarelibId.get("projectFile");
		/*
		 * ProjectFile projectFile = this.getById(hardwarelibs.getId()); String modelId
		 * = projectFile.getParentId(); ProjectFile modelFile = this.getById(modelId);
		 * String flowId = modelFile.getParentId();
		 */
		// hardwarelibs.setId(projectFile.getId());
		hardwarelibs.setVersion(0);
		hardwarelibs.setFlowId(flowId);
		hardwarelibs.setProjectId(projectId);
		hardwarelibs.setModelId(modelId);
//		System.out.println("flowId----------------------------> " + flowId);
		// System.out.println(hardwarelibs);
		int row = baseMapper.saveHardwarelibs(hardwarelibs);
		return row;
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
	public synchronized int updateHardwarelib(Hardwarelibs hardwarelibs) {
//		Map allIdByHardwarelibId = getAllIdByHardwarelibId(hardwarelibs);
//		String modelId = (String) allIdByHardwarelibId.get("modelId");
//		String flowId = (String) allIdByHardwarelibId.get("flowId");
//		String projectId = (String) allIdByHardwarelibId.get("projectId");
//		ProjectFile projectFile = (ProjectFile) allIdByHardwarelibId.get("projectFile");
		int i = baseMapper.updateHardwarelib(hardwarelibs);
		System.out.println("------------------------------------>  " + i);
		return i;
	}

	@Override
	public List<String> getProcedureNameListByProjectId(String projectId) {
		List<ProjectFile> procedureList = baseMapper.getProcedureListByProjectId(projectId);
		List<String> procedureNameList = new ArrayList<String>();
		for (ProjectFile file : procedureList) {
			procedureNameList.add(file.getFileName());
		}
		return procedureNameList;
	}

    /**
     * @param id
     * @Description 部署图获取部件结构数据
     * @Author ZhangHongXu
     * @Return array
     * @Exception
     * @Date 2020/4/17 15:01
     */
    @Override
    public ArrayList<Object> getXmlFile(String id) {
        String Path = null;
        String local_REPO_PATH = null;
        String jopNumber = DeploymentConstants.JOBNUMBER;
        for (ProjectFile projectFile : getProFileListByModelId(this.getById(id).getParentId())) {
            if (jopNumber.equals(projectFile.getFileType())) {
                Path = projectFile.getFilePath();
                local_REPO_PATH = gitDetailPath;
                break;
            }
        }
        String localPath = local_REPO_PATH + Path + DeploymentConstants.PROCESSMODELINGXML;
        XmlEntityMap xmlEntityMap = dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(localPath).getData();
        File file = new File(local_REPO_PATH + Path + DeploymentConstants.PROCESSMODELINGXML);
        List<HardwareNode> hardwareNodeList = ProcedureXmlAnalysis.getHardwareNodeList(file, xmlEntityMap);
        if (hardwareNodeList.size() != 0) {
            List<Arrows> arrowsList = ProcedureXmlAnalysis.getArrowsList(file, xmlEntityMap);
            ArrayList<Object> array = new ArrayList<>();
            array.add(hardwareNodeList);
            array.add(arrowsList);
            return array;
        } else {
            return null;
        }
    }

    /**
     * @param deploymentXMLMap
     * @Description 部署图xml生成
     * @Author ZhangHongXu
     * @Return void
     * @Exception
     * @Date 2020/4/20 10:16
     */
    @Override
    public void updataDeploymentXml(DeploymentXMLMap deploymentXMLMap) {
        String procedureId = null;
        XmlEntityMap entityMap = null;
        String Path = null;
        String local_REPO_PATH = null;
        String jopNumber = DeploymentConstants.JOBNUMBER;
        String id = deploymentXMLMap.getId();
        String localPath = null;
        //获取XmlEntityMap数据
        for (ProjectFile projectFile : getProFileListByModelId(this.getById(id).getParentId())) {
            if (jopNumber.equals(projectFile.getFileType())) {
                procedureId = projectFile.getId();
                Path = projectFile.getFilePath();
                local_REPO_PATH = gitDetailPath;
                 localPath = local_REPO_PATH + Path + DeploymentConstants.PROCESSMODELINGXML;
                entityMap = dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(localPath).getData();
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
                    // 遍历xml解析数据
                    findData(entityMap, nodeId, bakpartName, bakcpuid);
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
                    // 遍历xml解析数据
                    findData(entityMap, nodeId, partName, cpuid);
                }
            }
            //生成xml文件
            //createXmlFile(entityMap, procedureId)
            XMlEntityMapVO xMlEntityMapVO = new XMlEntityMapVO();
            xMlEntityMapVO.setLocalPath(localPath);
            xMlEntityMapVO.setXmlEntityMap(entityMap);
            dataCenterServiceFeign.createXMLFile(xMlEntityMapVO);
        }
    }

    /**
     * @Description 处理xml文件数据
     * @Author ZhangHongXu
     * @param entityMap
     * @param nodeId
     * @param partName
     * @param cpuid
     * @Return void
     * @Exception
     * @Date 2020/4/20 15:44
     */
    private void findData(XmlEntityMap entityMap, String nodeId, String partName, String cpuid) {
        String member = DeploymentConstants.MEMBER;
        String id = DeploymentConstants.ID;
        for (XmlEntityMap xmlMap : entityMap.getXmlEntityMaps()) {
            String xmlNoedId = xmlMap.getAttributeMap().get(id);
            if (xmlMap.getLableName().equals(member) && xmlNoedId.equals(nodeId)) {
                recursiveXML(partName, entityMap, cpuid);
            }
        }
    }

    /**
     * @param partName
     * @param xmlMap
     * @param cpuid
     * @Description 递归解析xml
     * @Author ZhangHongXu
     * @Return void
     * @Exception
     * @Date 2020/4/20 14:42
     */
    private void recursiveXML(String partName, XmlEntityMap xmlMap, String cpuid) {
        String node = DeploymentConstants.NODE;
        for (XmlEntityMap map : xmlMap.getXmlEntityMaps()) {
            if (map.getLableName().equals(node)) {
                String cmpname = map.getAttributeMap().get(DeploymentConstants.CMPNAME);
                if (cmpname.equals(partName)) {
                    map.getAttributeMap().put(DeploymentConstants.NAME, cpuid);
                }
            } else if (map.getXmlEntityMaps() != null && map.getXmlEntityMaps().size() > 0) {
                recursiveXML(partName, map, cpuid);
            }
        }
    }

	@Override
	public void deleteHardwarelibById(String id) {
		baseMapper.deleteHardwarelibById(id);
	}

	public byte[] exportFile(String id, StringRef sr) {
		Map<String, String> map = baseMapper.findProJSON(id);
		String xmlFilepath = proDetailPath + map.get("filePath") + map.get("fileName") + ".xml";
		String jsonFilepath = proDetailPath + map.get("filePath") + map.get("jsonPath");
		sr.setVal(map.get("fileName"));
		File xmlFile = new File(xmlFilepath);
		File jsonFile = new File(jsonFilepath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		ZipEntry zipEntry = null;
		if (xmlFile.exists() && jsonFile.exists()) {
			File[] files = { xmlFile, jsonFile };
			for (int i = 0; i < files.length; i++) {
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
	public synchronized int saveChipsfromhardwarelibs(Chipsfromhardwarelibs chipsfromhardwarelibs) {
//		System.out.println("chipsfromhardwarelibs------------------------> " + chipsfromhardwarelibs);
		if (chipsfromhardwarelibs.getId() == null) {
			return 0;
		}
		ProjectFile projectFile = this.getById(chipsfromhardwarelibs.getId());
		String modelId = projectFile.getParentId();
		ProjectFile modelFile = this.getById(modelId);
		String flowId = modelFile.getParentId();
		Chipsfromhardwarelibs chips = baseMapper.getChipsByFlowId(flowId);
		if (chips != null) {
			baseMapper.updateChipsfromhardwarelibs(chipsfromhardwarelibs);
			return 2;
		} else {
			chipsfromhardwarelibs.setFlowId(flowId);
			chipsfromhardwarelibs.setModelId(modelId);
			chipsfromhardwarelibs.setProjectId(projectFile.getProjectId());
			baseMapper.saveChipsfromhardwarelibs(chipsfromhardwarelibs);
			return 1;
		}
	}

	@Override
	public Chipsfromhardwarelibs getChipsfromhardwarelibs(String id) {
		ProjectFile projectFile = this.getById(id);
		String modelId = projectFile.getParentId();
		ProjectFile modelFile = this.getById(modelId);
		String flowId = modelFile.getParentId();
		Chipsfromhardwarelibs chips = baseMapper.getChipsByFlowId(flowId);
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
		ProjectFile model = baseMapper
				.selectOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getParentId, projectId));
		for (ProjectFile projectFile : getProFileListByModelId(model.getId())) {
			if ("11".equals(projectFile.getFileType())) {
				Path = projectFile.getFilePath();
				local_REPO_PATH = gitDetailPath;
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
		ProjectFile model = baseMapper
				.selectOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getParentId, projectId));
		for (ProjectFile projectFile : getProFileListByModelId(model.getId())) {
			if ("11".equals(projectFile.getFileType())) {
				Path = projectFile.getFilePath();
				// local_REPO_PATH = JGitUtil.getLOCAL_REPO_PATH();
				local_REPO_PATH = gitDetailPath;
				break;
			}
		}
		String userDefineTopicFilePath = local_REPO_PATH + Path + "TopicConfig/UserDefineTopicFile.xml";

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
		String flowPath = proDetailPath + pro.getFilePath() + softToHardResult + File.separator;
		/************************************
		 * update by zhx
		 ********************************************/
		String filePath = flowPath + "系数文件.xml";

		/*****************************************************************************************/
		File flowFile = new File(flowPath);
		if (!flowFile.exists()) {
			flowFile.mkdirs();
		}
		String upLoadFile = flowPath + fileName;
//		String tmpPath = new String();
//		tmpPath = new String(serverPath + "/gjk/upload/" + fileName).replace("\\", "/");
		File newFile = null;
		if (StringUtils.isNotEmpty(upLoadFile)) {
			try {
				newFile = new File(upLoadFile);
//				if (!newFile.getParentFile().exists()) {
//					newFile.getParentFile().mkdirs();
//				}
				if (newFile.exists()) {
					newFile.delete();
				}
				// newFile.createNewFile();
				file.transferTo(newFile);
				ExternalIOTransUtils.parseSystemPara(flowName, upLoadFile, filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

	@Override
	public boolean deleteProcedureById(String procedureId) {
		try {
			List<ProjectFile> projectFiles = new ArrayList<>();
			projectFiles.add(this.getById(procedureId));
			List<ProjectFile> modelFile = getProFileListByModelId(procedureId);
			if (modelFile != null && modelFile.size() > 0) {
				String filePath = proDetailPath + modelFile.get(0).getFilePath();
				cn.hutool.core.io.FileUtil.del(filePath);
				projectFiles.addAll(modelFile);
				for (ProjectFile projectFile : modelFile) {
					List<ProjectFile> list = getProFileListByModelId(projectFile.getId());
					projectFiles.addAll(list);
				}
			}
			for (ProjectFile projectFile : projectFiles) {
				this.removeById(projectFile.getId());
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteFilesFromLocal(Map filePath) {
		Set set = filePath.keySet();
		String keyUrl = (String) set.iterator().next();
		String url = (String) filePath.get(keyUrl);
		File file = new File(url);
		if (!file.exists()) {
			return false;
		} else {
			deleteChildFile(file);
		}
		return !file.exists();
	}

	public void deleteChildFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				file.delete();
			} else {
				for (File fileTemp : files) {
					if (fileTemp.isDirectory()) {
						deleteChildFile(fileTemp);
					} else {
						fileTemp.delete();
					}
				}
				deleteChildFile(file);
			}
		} else {
			file.delete();
		}
	}

	/*
	 * 使用feign获取到基础模板id返回xmlEntityMap对象
	 */
	public XmlEntityMap getXmlEntityMap(String tempId) {
		R<BaseTemplate> R = baseTemplateService.getById(tempId);// feign调用upms接口根据id查模板对象
		File file = new File(gitDetailPath + R.getData().getTempPath());
		return dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(file.getAbsolutePath()).getData();
	}

	@Override
	public R analysisThemeXML(String proDetailId) {
		ProjectFile projectFile = getProDetailById(proDetailId);
		Project project = projectMapper.getProById(projectFile.getProjectId());
		String json = project.getBasetemplateIds();// 获取到模板idjson串
		BaseTemplateIDsDTO baseTemplateIDsDTO = JSON.parseObject(json, BaseTemplateIDsDTO.class);
		String flowFilePath = "";
		String path = "";
		List<Part> partList = new ArrayList<Part>();
		List<ProjectFile> ProjectFileList = this.getFilePathListById(proDetailId);
		for (ProjectFile proFile : ProjectFileList) {
			if (proFile.getFileType().equals("11")) {
				flowFilePath = proDetailPath + proFile.getFilePath() + proFile.getFileName() + ".xml";
				path = proDetailPath + proFile.getFilePath();
			}
		}

		// flowFilePath =
		// "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\测试项目\\测试流程流程\\模型\\流程模型.xml";
		// File file = new File(flowFilePath);
		// 获取部件及部件及部件下构建
		File flowFile = new File(flowFilePath);
		if (flowFile.exists()) {
			// partList.addAll(ProcedureXmlAnalysis.getPartList(new File(flowFilePath)));
			List<Part> partList1 = ProcedureXmlAnalysis.getPartList(new File(flowFilePath));
			partList.addAll(partList1);
		}
		// 解析流程模型文件
		// XmlEntityMap folwModelData
		// =XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(new File(flowFilePath));
		String themePath = path + "自定义配置__主题配置.xml";
		File themeFile = new File(themePath);
		XmlEntityMap themeData = null;
		XmlEntityMap netWorkData = null;
		if (!themeFile.exists()) {
			themeData = this.getXmlEntityMap(baseTemplateIDsDTO.getThemeTempId());
		} else {
			themeData = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(themeFile);
		}
		String netWorkPath = path + "自定义配置__网络配置.xml";
		File netWorkFile = new File(netWorkPath);
		if (!netWorkFile.exists()) {
			netWorkData = this.getXmlEntityMap(baseTemplateIDsDTO.getNetworkTempId());
		} else {
			netWorkData = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(netWorkFile);
		}
		JSONObject obj = new JSONObject();
		obj.put("themeData", themeData);
		obj.put("netWorkData", netWorkData);
		obj.put("partList", partList);
		// obj.put("folwModelData", folwModelData);
		return new R<>(obj);
	}

	/**
	 * 获取修改BSP下拉列表内容
	 * 
	 * @return
	 */
	@Override
	public R getBSPListAndPlatformName() {
		List<BSP> bspList = baseMapper.getAllBSPList();
		for (BSP bsp : bspList) {
			List<GjkPlatform> platformList = baseMapper.getAllPlatformListByBSPId(bsp.getId());
			String platformNames = "";
			for (GjkPlatform gjkPlatform : platformList) {
				platformNames += gjkPlatform.getName() + ";";
			}
			// 因为用不到软件构架库库的描述字段，所有将描述字段存放平台库名称
			bsp.setDescription(platformNames);
		}
		return new R<>(bspList);
	}

	@Override
	public R showPartBSPAndPlatform(String id) {
		return new R<>(partPlatformBSPMapper.getByProcedureId(id));
	}

	/**
	 * 修改bsp库保存
	 * 
	 * @param bsp
	 * @return
	 */
	@Override
	public R updatePartBSPAndPlatform(BSP bsp) {
		String procedureId = bsp.getId();
		String bspIds = bsp.getDescription();
		partPlatformBSPMapper.deleteByProcedureId(procedureId);

		String[] bspIdsArr = bspIds.split(";");
		List<String> bspIdList = Arrays.asList(bspIdsArr);

		String ids = "'" + StringUtils.join(bspIdList, "','") + "'";
		List<BSP> bspList = baseMapper.getAllBSPListByIdIn(ids);
		for (BSP item : bspList) {
			List<GjkPlatform> platformList = baseMapper.getAllPlatformListByBSPId(item.getId());
			String platformNames = "";
			for (GjkPlatform gjkPlatform : platformList) {
				platformNames += gjkPlatform.getName() + ";";
			}

			PartPlatformBSP ppBsp = new PartPlatformBSP();
			ppBsp.setId(IdGenerate.uuid());
			ppBsp.setBspFilePath(item.getFilePath());
			ppBsp.setBspId(item.getId());
			ppBsp.setBspName(item.getBspName());
			ppBsp.setBspVersion(item.getVersion());
			ppBsp.setPlatformName(platformNames);
			ppBsp.setProcedureId(procedureId);
			ppBsp.setCreateTime(LocalDateTime.now());
			partPlatformBSPMapper.insert(ppBsp);
		}
		return new R<>(true);
	}

	@Override
	public byte[] createZip(String projectId, String processId) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		createExcelFileToZipIO(projectId, processId, zip);

		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	@Override
	public int analysisZipFile(MultipartFile ufile, String projectId) {
		Project project = projectMapper.getProById(projectId);
		String filePath = serverPath + "gjk" + File.separator + "zipFile" + File.separator
				+ (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + "-" + ufile.getOriginalFilename();
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			ufile.transferTo(file);

			String descDirPath = filePath.substring(0, filePath.lastIndexOf("."));
			// 解压zip文件夹
			unZipFiles(filePath, descDirPath);

			int tag = 0;

			File[] files = new File(descDirPath).listFiles();
			for (File item : files) {
				// 表数据入库处理
				if (item.getName().equals("mysql")) {
					String excelFilePath = descDirPath + File.separator + "mysql" + File.separator + "MySQL.xls";
					tag = readExcel(descDirPath, excelFilePath, projectId);
					// 硬件建模数据
					excelFilePath = descDirPath + File.separator + "mysql" + File.separator + "gjk_hardwarelibs.csv";
					tag = readCvs(excelFilePath, "gjk_hardwarelibs", projectId);
					// 芯片数据
					excelFilePath = descDirPath + File.separator + "mysql" + File.separator
							+ "gjk_chipsfromhardwarelibs.csv";
					tag = readCvs(excelFilePath, "gjk_chipsfromhardwarelibs", projectId);
				}

				// 引用的资源文件拷贝到指定目录
				else {
					if (item.getName().equals("project")) {
						// project 需要把目录改成导入项目的目录名
						String targetFilePath = serverPath + "gjk" + File.separator + item.getName() + File.separator
								+ project.getProjectName();
						FileUtil.copyFile(item.listFiles()[0].getPath(), targetFilePath);
					} else {
						String targetFilePath = serverPath + "gjk" + File.separator + item.getName();
						FileUtil.copyFile(item.getPath(), targetFilePath);
					}
				}
			}

			// 删除压缩包
			cn.hutool.core.io.FileUtil.del(filePath);
			// 删除解压包
			cn.hutool.core.io.FileUtil.del(descDirPath);
			return tag;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 解析excel文件
	 * 
	 * @param unZipFilePath
	 * @param path
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	public int readExcel(String unZipFilePath, String path, String projectId) throws Exception {
		// 获取整个表格文件对象
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));

		List<Project> projectList = new ArrayList<>();
		List<ProjectFile> projectDetailList = new ArrayList<>();
		List<App> appList = new ArrayList<>();
		List<BaseTemplate> baseTemplateList = new ArrayList<>();
		List<PartPlatformSoftware> partPlatformSoftwareList = new ArrayList<>();
		List<Software> softwareList = new ArrayList<>();
		List<SoftwareDetail> softwareDetailList = new ArrayList<>();
		List<SoftwareFile> softwareFileList = new ArrayList<>();
		List<PartPlatformBSP> partPlatformBSPList = new ArrayList<>();
		List<BSP> bspList = new ArrayList<>();
		List<BSPDetail> bspDetailList = new ArrayList<>();
		List<BSPFile> bspFileList = new ArrayList<>();
		List<ProComp> proCompList = new ArrayList<>();
		List<CommonComponent> compList = new ArrayList<>();
		List<CommonComponentDetail> compDetailList = new ArrayList<>();
		List<CompStruct> compStructList = new ArrayList<>();
		List<Structlibs> structlibsList = new ArrayList<>();
//		List<Hardwarelibs> hardwarelibsList = new ArrayList<>();
//		List<Chipsfromhardwarelibs> chipsfromhardwarelibsList = new ArrayList<>();
		List<SysDict> sysDictList = new ArrayList<>();

		// 获取所有sheet对象
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			String tableName = sheet.getSheetName();
			String className = null;
			// 项目表
			if ("gjk_project".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.Project";
				for (Object obj : parseSheet(sheet, className)) {
					projectList.add((Project) obj);
				}
			}
			// 项目详情表
			else if ("gjk_project_detail".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.ProjectFile";
				for (Object obj : parseSheet(sheet, className)) {
					projectDetailList.add((ProjectFile) obj);
				}
			}
			// app表
			else if ("gjk_app".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.App";
				for (Object obj : parseSheet(sheet, className)) {
					appList.add((App) obj);
				}
			}
			// 基础模版表
			else if ("gjk_base_template".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.BaseTemplate";
				for (Object obj : parseSheet(sheet, className)) {
					baseTemplateList.add((BaseTemplate) obj);
				}
			}
			// 软件框架与流程关系表
			else if ("gjk_app_part_platform_software".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.PartPlatformSoftware";
				for (Object obj : parseSheet(sheet, className)) {
					partPlatformSoftwareList.add((PartPlatformSoftware) obj);
				}
			}
			// 软件框架表
			else if ("gjk_software".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.Software";
				for (Object obj : parseSheet(sheet, className)) {
					softwareList.add((Software) obj);
				}
			}
			// 软件框架与平台关系表
			else if ("gjk_software_detail".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.SoftwareDetail";
				for (Object obj : parseSheet(sheet, className)) {
					softwareDetailList.add((SoftwareDetail) obj);
				}
			}
			// 软件框架文件表
			else if ("gjk_software_file".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.SoftwareFile";
				for (Object obj : parseSheet(sheet, className)) {
					softwareFileList.add((SoftwareFile) obj);
				}
			}
			// 软件框架文件表
			else if ("gjk_app_part_platform_bsp".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.PartPlatformBSP";
				for (Object obj : parseSheet(sheet, className)) {
					partPlatformBSPList.add((PartPlatformBSP) obj);
				}
			}
			// bsp表
			else if ("gjk_bsp".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.BSP";
				for (Object obj : parseSheet(sheet, className)) {
					bspList.add((BSP) obj);
				}
			}
			// bsp和平台关系表
			else if ("gjk_bsp_detail".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.BSPDetail";
				for (Object obj : parseSheet(sheet, className)) {
					bspDetailList.add((BSPDetail) obj);
				}
			}
			// BSP文件表
			else if ("gjk_bsp_file".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.BSPFile";
				for (Object obj : parseSheet(sheet, className)) {
					bspFileList.add((BSPFile) obj);
				}
			}
			// 项目和构件关系表
			else if ("gjk_project_comp".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.ProComp";
				for (Object obj : parseSheet(sheet, className)) {
					proCompList.add((ProComp) obj);
				}
			}
			// 构件库表
			else if ("gjk_common_component".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.CommonComponent";
				for (Object obj : parseSheet(sheet, className)) {
					compList.add((CommonComponent) obj);
				}
			}
			// 结构库详细表
			else if ("gjk_common_component_detail".equals(tableName)) {
				className = "com.inforbus.gjk.pro.api.entity.CommonComponentDetail";
				for (Object obj : parseSheet(sheet, className)) {
					compDetailList.add((CommonComponentDetail) obj);
				}
			}
			// 构件库和结构体表关系表
			else if ("gjk_comp_struct".equals(tableName)) {
				className = "com.inforbus.gjk.common.core.entity.CompStruct";
				for (Object obj : parseSheet(sheet, className)) {
					compStructList.add((CompStruct) obj);
				}
			}
			// 结构体表
			else if ("gjk_structlibs".equals(tableName)) {
				className = "com.inforbus.gjk.common.core.entity.Structlibs";
				for (Object obj : parseSheet(sheet, className)) {
					structlibsList.add((Structlibs) obj);
				}
			}
//			// 硬件建模表
//			else if("gjk_hardwarelibs".equals(tableName)){
//				className = "com.inforbus.gjk.pro.api.entity.Hardwarelibs";
//				for (Object obj : parseSheet(sheet, className)) {
//					hardwarelibsList.add((Hardwarelibs) obj);
//				}
//			}
//			// 芯片表
//			else if("gjk_chipsfromhardwarelibs".equals(tableName)){
//				className = "com.inforbus.gjk.pro.api.entity.Chipsfromhardwarelibs";
//				for (Object obj : parseSheet(sheet, className)) {
//					chipsfromhardwarelibsList.add((Chipsfromhardwarelibs) obj);
//				}
//			}
			// 字典表
			else if ("sys_dict".equals(tableName)) {
				className = "com.inforbus.gjk.admin.api.entity.SysDict";
				for (Object obj : parseSheet(sheet, className)) {
					sysDictList.add((SysDict) obj);
				}
			}
		}
		workbook.close();

		// 数据导入处理
		// 项目表
		Project project = projectMapper.getProById(projectId);
		project.setBasetemplateIds(projectList.get(0).getBasetemplateIds());
		projectMapper.updateById(project);

		// 项目详情表
		for (ProjectFile projectFile : projectDetailList) {
			// 判断id是否存在
			if (baseMapper.getProDetailById(projectFile.getId()) != null) {
				continue;
			}
			// 项目id替换成导入的项目id
			projectFile.setProjectId(projectId);
			String[] filePathArr = projectFile.getFilePath().split(String.format("\\%s", File.separator));
			filePathArr[2] = project.getProjectName();
			String newFilePath = "";
			for (String s : filePathArr) {
				newFilePath += s + File.separator;
			}
			// 项目文件路径替换成导入项目的名称
			projectFile.setFilePath(newFilePath);
			baseMapper.saveProDetail(projectFile);
		}

		// app表
		for (App app : appList) {
			if (appMapper.selectById(app.getId()) == null) {
				appMapper.saveApp(app);
			}
		}

		// 基础模版表
		for (BaseTemplate baseTemplate : baseTemplateList) {
			if (baseTemplateService.getById(baseTemplate.getTempId()).getData() == null) {
				baseMapper.saveBaseTemplate(baseTemplate);
			}
		}

		// 软件框架与流程关系表
		for (PartPlatformSoftware partPlatformSoftware : partPlatformSoftwareList) {
			if (partPlatformSoftwareMapper.selectById(partPlatformSoftware.getId()) == null) {
				partPlatformSoftwareMapper.savePartPlatformSoftware(partPlatformSoftware);
			}
		}

		// 软件框架表
		for (Software software : softwareList) {
			if (baseMapper.getSoftwareById(software.getId()) == null) {
				baseMapper.saveSoftware(software);
			}
		}

		// 软件框架与平台关系表
		for (SoftwareDetail softwareDetail : softwareDetailList) {
			if (baseMapper.getSoftwareDetailBySoftwareIdAndPlatformId(softwareDetail.getSoftwareId(),
					softwareDetail.getPlatformId()) == null) {
				baseMapper.saveSoftwareDetail(softwareDetail);
			}
		}

		// 软件框架文件表
		for (SoftwareFile softwareFile : softwareFileList) {
			if (baseMapper.getSoftwareFileBySoftwareIdAndFileName(softwareFile.getSoftwareId(),
					softwareFile.getFileName()) == null) {
				baseMapper.saveSoftwareFile(softwareFile);
			}
		}

		// bsp与流程关系表
		for (PartPlatformBSP partPlatformBSP : partPlatformBSPList) {
			if (partPlatformBSPMapper.selectById(partPlatformBSP.getId()) == null) {
				partPlatformBSPMapper.insert(partPlatformBSP);
			}
		}

		// bsp表
		for (BSP bsp : bspList) {
			if (baseMapper.getBSPById(bsp.getId()) == null) {
				baseMapper.saveBSP(bsp);
			}
		}

		// bsp和平台关系表
		for (BSPDetail bspDetail : bspDetailList) {
			if (baseMapper.getBSPDetailByBSPIdAndPlatformId(bspDetail.getBspId(), bspDetail.getPlatformId()) == null) {
				baseMapper.saveBSPDetail(bspDetail);
			}
		}

		// BSP文件表
		for (BSPFile bspFile : bspFileList) {
			if (baseMapper.getBSPFileByBSPIdAndFileNameAndFilePath(bspFile.getBspId(), bspFile.getFileName(),
					bspFile.getFilePath()) == null) {
				baseMapper.saveBSPFile(bspFile);
			}
		}

		// 项目和构件关系表 存在则更新 不存在则新增
		for (ProComp proComp : proCompList) {
			if (projectMapper.getIdByProIdCompId(proComp) == null) {
				// 项目id替换成导入项目的id
				proComp.setProjectId(projectId);
				projectMapper.saveProComp(proComp);
			} else {
				projectMapper.updateCompProjectByCompIdAndProId(proComp.getCompId(), proComp.getProjectId(),
						proComp.getCanUse());
			}
		}

		// 构件库表 id存在则更新 不存在则新增
		for (CommonComponent commonComponent : compList) {
			if (baseMapper.getCommonComponentByIdIn("'" + commonComponent.getId() + "'") == null) {
				baseMapper.saveCommonComp(commonComponent);
			} else {
				commonComponent.setUpdateTime(LocalDateTime.now());
				baseMapper.updateCommonComp(commonComponent);
			}
		}

		// 结构库详细表 id存在则更新 不存在则新增
		for (CommonComponentDetail commonComponentDetail : compDetailList) {
			if (baseMapper.getCommonComponentDetailByCompIdIn("'" + commonComponentDetail.getId() + "'") == null) {
				baseMapper.saveCommonCompDetail(commonComponentDetail);
			} else {
				baseMapper.updateCommonCompDetail(commonComponentDetail);
			}
		}

		// 构件库和结构体表关系表
		for (CompStruct compStruct : compStructList) {
			if (baseMapper.getCompStructById(compStruct.getId()) == null) {
				baseMapper.saveCompAndStruct(compStruct);
			}
		}

		// 结构体表
		for (Structlibs structlibs : structlibsList) {
			if (baseMapper.getStructlibsById(structlibs.getId()) == null) {
				baseMapper.saveStructlibs(structlibs);
			} else {
				baseMapper.updateStructlibs(structlibs);
			}
		}

//		// 硬件建模表
//		for (Hardwarelibs hardwarelibs : hardwarelibsList) {
//			hardwarelibs.setProjectId(projectId);
//			if(baseMapper.getHardwarelibsById(hardwarelibs.getId()) == null){
//				baseMapper.saveHardwarelibs(hardwarelibs);
//			}else{
//				baseMapper.updateHardwarelibById(hardwarelibs);
//			}
//		}
//
//		// 芯片表
//		for (Chipsfromhardwarelibs chipsfromhardwarelibs : chipsfromhardwarelibsList) {
//			chipsfromhardwarelibs.setProjectId(projectId);
//			if(baseMapper.getChipsfromhardwarelibsById(chipsfromhardwarelibs.getId()) == null){
//				baseMapper.saveChipsfromhardwarelibs(chipsfromhardwarelibs);
//			}else{
//				baseMapper.updateChipsfromhardwarelibsById(chipsfromhardwarelibs);
//			}
//		}

		// 字典表
		for (SysDict sysDict : sysDictList) {
			if (baseMapper.getSysDictById(sysDict.getId()) == null) {
				baseMapper.saveSysDict(sysDict);
			} else {
				baseMapper.updateSysDict(sysDict);
			}
		}

		return 1;
	}

	/**
	 * 解析工作簿
	 *
	 * @param sheet
	 * @throws Exception
	 */
	private List<Object> parseSheet(Sheet sheet, String className) throws Exception {
		List<Object> objects = new ArrayList<Object>();

		// 获取所有Row对象
		Iterator<Row> iterator = sheet.iterator();
		int index = 0;
		// 存列信息
		List<String> columnList = new ArrayList<>();
		while (iterator.hasNext()) {
			if (index == 0) {
				columnList = parseRow(iterator.next());
			} else {
				objects.add(parseRow(iterator.next(), className, columnList));
			}
			index++;
		}
		return objects;
	}

	/**
	 * 解析列
	 *
	 * @param next
	 */
	private List<String> parseRow(Row next) {
		List<String> list = new ArrayList<String>();
		// 获取所有Cell对象取值
		Iterator<Cell> cellIterator = next.cellIterator();
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			cell.setCellType(CellType.STRING);
			list.add(cell.getStringCellValue());
		}
		return list;
	}

	/**
	 * 解析每行数据
	 *
	 * @param next
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private Object parseRow(Row next, String calssName, List<String> columnList) throws Exception {
		Class<?> cla = Class.forName(calssName);
		Object obj = cla.newInstance();

		// 获取所有Cell对象取值
		Iterator<Cell> cellIterator = next.cellIterator();
		int index = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			cell.setCellType(CellType.STRING);
			if (!"".equals(cell.getStringCellValue())) {
				if ("CreateTime".equals(columnList.get(index)) || "UpdateTime".equals(columnList.get(index))) {
					LocalDateTime parse = LocalDateTime.parse(cell.getStringCellValue());
					setFieldValueByName(columnList.get(index), cla, obj, parse);
				} else {
					setFieldValueByName(columnList.get(index), cla, obj, cell.getStringCellValue());
				}
			}
			index++;
		}
		return obj;
	}

	/**
	 * set属性值
	 *
	 * @param fieldName 字段名称
	 * @param o         对象
	 * @return Object
	 */
	private void setFieldValueByName(String fieldName, Class<?> clazz, Object o, Object value) {
		try {
			String firstLetter = fieldName.substring(0, 1).toLowerCase();
			fieldName = firstLetter + fieldName.substring(1); // 获取方法名
			PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
			Method wM = pd.getWriteMethod();// 获得写方法

			// 判断字段类型 转换类型
			if (pd.getPropertyType().getName().indexOf("Double") > -1) {
				value = Double.valueOf((String) value);
			} else if (pd.getPropertyType().getName().indexOf("Integer") > -1
					|| pd.getPropertyType().getName().indexOf("int") > -1) {
				value = Integer.valueOf((String) value);
			}

			wM.invoke(o, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成Excel文件并添加到压缩文件流中
	 * 
	 * @param projectId
	 * @param processId
	 * @param zip
	 * @throws Exception
	 * @throws IOException
	 */
	private void createExcelFileToZipIO(String projectId, String processId, ZipOutputStream zip)
			throws Exception, IOException {
		// 创建表格工作空间
		XSSFWorkbook workbook = new XSSFWorkbook();

		// 项目表
		List<Project> projectList = new ArrayList<>();
		Project project = projectMapper.selectById(projectId);
		projectList.add(project);
		createSheet(workbook, projectList, "gjk_project");

		// 项目详细信息表
		List<ProjectFile> projectDetailList = new ArrayList<>();
		// 查询流程数据
		ProjectFile projectFile = baseMapper.getProDetailById(processId);
		projectDetailList.add(projectFile);
		// 查询模型数据
		List<ProjectFile> processTemplatList = baseMapper.getProFileListByModelId(processId);
		projectDetailList.addAll(processTemplatList);
		for (ProjectFile file : processTemplatList) {
			// 查询流程建模、硬件建模、软硬件映射配置、方案展示、部署图、自定义配置、系统配置数据
			List<ProjectFile> list11_17 = baseMapper.getProFileListByModelId(file.getId());
			projectDetailList.addAll(list11_17);
		}
		createSheet(workbook, projectDetailList, "gjk_project_detail");

		// 项目文件
		for (ProjectFile projectDetail : projectDetailList) {
			if (projectDetail.getFileType().equals("9")) {
				String peoFlowPath = serverPath + projectDetail.getFilePath() + projectDetail.getFileName();
				if (new File(peoFlowPath).exists()) {
					zipDirOrFile(zip, new File(peoFlowPath), "project" + File.separator + project.getProjectName()
							+ File.separator + projectDetail.getFileName());
				}
				break;
			}
		}

		// app表
		App app = appMapper.getAppByProcessId(projectId);
		List<App> appList = new ArrayList<>();
		if (app != null) {
			appList.add(app);
		}
		createSheet(workbook, appList, "gjk_app");

		// 基础模版表
		JSONObject jsonObject = JSONObject.parseObject(project.getBasetemplateIds());
		R<BaseTemplate> sysTemplateR = baseTemplateService.getById(jsonObject.getString("sysTempId"));
		R<BaseTemplate> themeTemplateR = baseTemplateService.getById(jsonObject.getString("themeTempId"));
		R<BaseTemplate> networkTemplateR = baseTemplateService.getById(jsonObject.getString("networkTempId"));
		R<BaseTemplate> hsmTemplateR = baseTemplateService.getById(jsonObject.getString("hsmTempId"));
		List<BaseTemplate> baseTemplateList = new ArrayList<>();
		baseTemplateList.add(sysTemplateR.getData());
		baseTemplateList.add(themeTemplateR.getData());
		baseTemplateList.add(networkTemplateR.getData());
		baseTemplateList.add(hsmTemplateR.getData());
		createSheet(workbook, baseTemplateList, "gjk_base_template");

		// 基础模版文件
		for (BaseTemplate baseTemplate : baseTemplateList) {
			String peoFlowPath = serverPath + baseTemplate.getTempPath();
			File file = new File(peoFlowPath);
			if (file.exists()) {
				zipDirOrFile(zip, file, "baseTemplate" + File.separator + file.getName());
			}
		}

		// 软件框架与流程关系表
		List<PartPlatformSoftware> partPlatformSoftwareList = partPlatformSoftwareMapper.getByProcedureId(processId);
		createSheet(workbook, partPlatformSoftwareList, "gjk_app_part_platform_software");

		// 软件框架表
		List<String> softwareIdList = partPlatformSoftwareList.stream().map(PartPlatformSoftware::getSoftwareId)
				.collect(Collectors.toList());
		List<Software> softwareList = new ArrayList<>();
		String ids = "";
		if (softwareIdList.size() > 0) {
			ids = "'" + StringUtils.join(softwareIdList, "','") + "'";
			softwareList = baseMapper.getAllSoftwareListByIdIn(ids);
		}
		createSheet(workbook, softwareList, "gjk_software");

		// 软件框架文件
		for (Software software : softwareList) {
			String peoFlowPath = serverPath + software.getFilePath();
			File file = new File(peoFlowPath);
			if (file.exists()) {
				zipDirOrFile(zip, file,
						"software" + File.separator + software.getVersion() + File.separator + file.getName());
			}
		}

		// 软件框架与平台关系表
		List<SoftwareDetail> softwareDetailList = new ArrayList<>();
		if (ids.length() > 0) {
			softwareDetailList = baseMapper.getSoftwareDetailBySoftwareIdIn(ids);
		}
		createSheet(workbook, softwareDetailList, "gjk_software_detail");

		// 软件框架文件表
		List<SoftwareFile> softwareFileList = new ArrayList<>();
		if (ids.length() > 0) {
			softwareFileList = baseMapper.getSoftwareFileBySoftwareIdIn(ids);
		}
		createSheet(workbook, softwareFileList, "gjk_software_file");

		// bsp与流程关系表
		List<PartPlatformBSP> partPlatformBSPList = partPlatformBSPMapper.getByProcedureId(processId);
		createSheet(workbook, partPlatformBSPList, "gjk_app_part_platform_bsp");

		// bsp表
		List<String> bspIdList = partPlatformBSPList.stream().map(PartPlatformBSP::getBspId)
				.collect(Collectors.toList());
		List<BSP> bspList = new ArrayList<>();
		String bspIds = "";
		if (bspIdList.size() > 0) {
			bspIds = "'" + StringUtils.join(bspIdList, "','") + "'";
			bspList = baseMapper.getAllBSPListByIdIn(bspIds);
		}
		createSheet(workbook, bspList, "gjk_bsp");

		// bsp文件
		for (BSP bsp : bspList) {
			String peoFlowPath = serverPath + bsp.getFilePath();
			File file = new File(peoFlowPath);
			if (file.exists()) {
				zipDirOrFile(zip, file, "bsp" + File.separator + bsp.getVersion() + File.separator + file.getName());
			}
		}

		// bsp和平台关系表
		List<BSPDetail> bspDetailList = new ArrayList<>();
		if (bspIds.length() > 0) {
			bspDetailList = baseMapper.getBSPDetailByBSPIdIn(bspIds);
		}
		createSheet(workbook, bspDetailList, "gjk_bsp_detail");

		// BSP文件表
		List<BSPFile> bspFileList = new ArrayList<>();
		if (bspIds.length() > 0) {
			bspFileList = baseMapper.getBSPFileByBSPIdIn(bspIds);
		}
		createSheet(workbook, bspFileList, "gjk_bsp_file");

		// 项目和构件关系表
		List<ProComp> proCompList = projectMapper.getProjectCompByProId(projectId);
		createSheet(workbook, proCompList, "gjk_project_comp");

		// 构件库表
		List<String> compIdList = proCompList.stream().map(ProComp::getCompId).collect(Collectors.toList());
		List<CommonComponent> compList = new ArrayList<>();
		String compIds = "";
		if (compIdList.size() > 0) {
			compIds = "'" + StringUtils.join(compIdList, "','") + "'";
			compList = baseMapper.getCommonComponentByIdIn(compIds);
		}
		createSheet(workbook, compList, "gjk_common_component");

		// 结构库详细表
		List<CommonComponentDetail> compDetailList = new ArrayList<>();
		if (compIds.length() > 0) {
			compDetailList = baseMapper.getCommonComponentDetailByCompIdIn(compIds);
		}
		createSheet(workbook, compDetailList, "gjk_common_component_detail");

		// 结构库文件
		for (CommonComponent commonComponent : compList) {
			String compPath = serverPath + "gjk" + File.separator + "common" + File.separator + "component"
					+ File.separator + commonComponent.getCompId() + File.separator + commonComponent.getVersion()
					+ File.separator;
			File file = new File(compPath);
			if (file.exists()) {
				zipDirOrFile(zip, file, "common" + File.separator + "component" + File.separator
						+ commonComponent.getCompId() + File.separator + commonComponent.getVersion());
			}
		}

		// 构件库和结构体表关系表
		List<CompStruct> compStructList = new ArrayList<>();
		if (compIdList.size() > 0) {
			compStructList = baseMapper.getCompStructByCompIdList(compIdList);
		}
		createSheet(workbook, compStructList, "gjk_comp_struct");

		// 结构体表
		List<Structlibs> structlibsList = new ArrayList<>();
		List<String> structIdList = compStructList.stream().map(CompStruct::getStructId).collect(Collectors.toList());
		List<Structlibs> structlibsListSub = new ArrayList<>();
		if (structIdList.size() > 0) {
			structlibsListSub = baseMapper.getStructlibsByIdList(structIdList);
		}
		structlibsList.addAll(structlibsListSub);
		findStructlibsRecursion(structlibsListSub, structlibsList);
		createSheet(workbook, structlibsList, "gjk_structlibs");

//		// 硬件建模表
//        Hardwarelibs hardwarelibs = baseMapper.getHardwarelibsByFlowId(processId);
//        List<Hardwarelibs> hardwarelibsList = new ArrayList<>();
//        hardwarelibsList.add(hardwarelibs);
//        createSheet(workbook, hardwarelibsList, "gjk_hardwarelibs");
		Hardwarelibs hardwarelibs = baseMapper.getHardwarelibsByFlowId(processId);
		List<String> columnNames = new ArrayList<>();
		StringBuffer columnValues = new StringBuffer();
		List<Map<String, String>> columns = queryColumns("gjk_hardwarelibs");
		for (Map<String, String> column : columns) {
			String colName = columnToJava(column.get("columnName"));
			columnNames.add(colName);
			if (hardwarelibs != null) {
				String cvsStr = String.valueOf(getFieldValueByName(colName, hardwarelibs));
				cvsStr = cvsStr == null || cvsStr.equals("null") ? "" : cvsString(cvsStr);
				columnValues.append(cvsStr).append(",");
			}
		}
		List<String> cvsContent = new ArrayList<>();
		cvsContent.add(String.join(",", columnNames));
		if (columnValues.length() > 0) {
			cvsContent.add(columnValues.substring(0, columnValues.length() - 1));
		}
		String filePath = serverPath + "gjk" + File.separator + "testExcel" + File.separator + "gjk_hardwarelibs.csv";
		File hardwarelibsFile = FileUtil.writeFileContent(filePath, cvsContent);

//        //芯片表
		Chipsfromhardwarelibs chipsfromhardwarelibs = baseMapper.getChipsByFlowId(processId);
//        List<Chipsfromhardwarelibs> chipsfromhardwarelibsList = new ArrayList<>();
//        chipsfromhardwarelibsList.add(chipsfromhardwarelibs);
//        createSheet(workbook, chipsfromhardwarelibsList, "gjk_chipsfromhardwarelibs");
		columnNames = new ArrayList<>();
		columnValues = new StringBuffer();
		columns = queryColumns("gjk_chipsfromhardwarelibs");
		for (Map<String, String> column : columns) {
			String colName = columnToJava(column.get("columnName"));
			columnNames.add(colName);
			if (chipsfromhardwarelibs != null) {
				String cvsStr = String.valueOf(getFieldValueByName(colName, chipsfromhardwarelibs));
				cvsStr = cvsStr == null || cvsStr.equals("null") ? "" : cvsString(cvsStr);
				columnValues.append(cvsStr).append(",");
			}
		}
		cvsContent = new ArrayList<>();
		cvsContent.add(String.join(",", columnNames));
		if (columnValues.length() > 0) {
			cvsContent.add(columnValues.substring(0, columnValues.length() - 1));
		}
		filePath = serverPath + "gjk" + File.separator + "testExcel" + File.separator + "gjk_chipsfromhardwarelibs.csv";
		File chipsFile = FileUtil.writeFileContent(filePath, cvsContent);

		// 字典表
		List<SysDict> sysDictList = baseMapper.getSysDictByRemarksIn("'mapperType','selectType'");
		createSheet(workbook, sysDictList, "sys_dict");

		// 创建Excel文件保存的临时地址
		File file = new File(serverPath + "gjk" + File.separator + "testExcel" + File.separator + "MySQL.xls");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		// 创建Excel文件压缩目录
		ZipEntry entry = new ZipEntry("mysql" + File.separator + "MySQL.xls");
		zip.putNextEntry(entry);
		// 将Excel文件内容写入临时文件
		OutputStream op = new FileOutputStream(file);
		workbook.write(op);
		// 将Excel文件读入压缩文件流
		zip.write(FileUtils.readFileToByteArray(file));
		// 硬件建模数据
		ZipEntry entryH = new ZipEntry("mysql" + File.separator + "gjk_hardwarelibs.csv");
		zip.putNextEntry(entryH);
		zip.write(FileUtils.readFileToByteArray(hardwarelibsFile));
		// 芯片数据
		ZipEntry entryC = new ZipEntry("mysql" + File.separator + "gjk_chipsfromhardwarelibs.csv");
		zip.putNextEntry(entryC);
		zip.write(FileUtils.readFileToByteArray(chipsFile));
		// 将临时文件删除
		file.delete();
		hardwarelibsFile.delete();
		chipsFile.delete();
		zip.flush();
		zip.closeEntry();
	}

	/**
	 * 递归获取结构体数据
	 * 
	 * @param structlibsList
	 * @param resList
	 */
	private void findStructlibsRecursion(List<Structlibs> structlibsList, List<Structlibs> resList) {
		List<String> structIdList = structlibsList.stream().map(Structlibs::getId).collect(Collectors.toList());
		List<Structlibs> list = new ArrayList<>();
		if (structIdList.size() > 0) {
			list = baseMapper.getStructlibsByParentIdList(structIdList);
		}
		if (list.size() > 0) {
			resList.addAll(list);
			findStructlibsRecursion(list, resList);
		}
	}

	/**
	 * 创建一个sheep标签 并处理数据
	 * 
	 * @param workbook
	 * @param dataList
	 * @param tableName
	 * @throws Exception
	 */
	private void createSheet(XSSFWorkbook workbook, List<?> dataList, String tableName) throws Exception {
		XSSFSheet compDetailSheet = workbook.createSheet(tableName);
		// set Sheet页头部
		List<String> compDetailColumnList = setSheetHeader(workbook, compDetailSheet, tableName);
		// set Sheet页内容
		List<Object> list = new ArrayList<Object>();
		list.addAll(dataList);
		setSheetContent(workbook, compDetailSheet, list, compDetailColumnList);
	}

	/**
	 * 配置Excel表格的顶部信息，如：学号 姓名 年龄 出生年月
	 *
	 * @param xWorkbook
	 * @param xSheet
	 */
	private List<String> setSheetHeader(XSSFWorkbook xWorkbook, XSSFSheet xSheet, String tableName) {
		List<String> columnList = new ArrayList<>();

		// 设置表格的宽度 xSheet.setColumnWidth(0, 20 * 256); 中的数字 20 自行设置为自己适用的
		xSheet.setColumnWidth(0, 20 * 256);
		xSheet.setColumnWidth(1, 15 * 256);
		xSheet.setColumnWidth(2, 15 * 256);
		xSheet.setColumnWidth(3, 20 * 256);

		// 创建表格的样式
		XSSFCellStyle cs = xWorkbook.createCellStyle();
		// 设置居中
		cs.setAlignment(HorizontalAlignment.CENTER);
		// 设置字体
		Font headerFont = xWorkbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);
//		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("宋体");
		cs.setFont(headerFont);
		cs.setWrapText(true);// 是否自动换行

		// 创建一行
		XSSFRow row = xSheet.createRow(0);

		int columnNum = 0;
		// 设置每一列
		for (Map<String, String> column : queryColumns(tableName)) {
			XSSFCell cell = row.createCell(columnNum);
			cell.setCellStyle(cs);
			cell.setCellValue(columnToJava(column.get("columnName")));
			columnList.add(columnToJava(column.get("columnName")));
			columnNum++;
		}
		return columnList;
	}

	/**
	 * 配置(赋值)表格内容部分
	 *
	 * @param xWorkbook
	 * @param xSheet
	 * @throws Exception
	 */
	private void setSheetContent(XSSFWorkbook xWorkbook, XSSFSheet xSheet, List<Object> objList,
			List<String> columnList) throws Exception {
		// 创建内容样式（头部以下的样式）
		CellStyle cs = xWorkbook.createCellStyle();
		cs.setWrapText(true);
		cs.setAlignment(HorizontalAlignment.CENTER);

		int rowIndex = 1;
		if (null != objList && objList.size() > 0) {
			for (Object obj : objList) {
				XSSFRow xRow = xSheet.createRow(rowIndex);
				int columnIndex = 0;
				for (String key : columnList) {
					Object temp = getFieldValueByName(key, obj);
					String strTemp = "";
					if (temp != null) {
						strTemp = temp.toString();
					}
					XSSFCell cell = xRow.createCell(columnIndex);
					cell.setCellStyle(cs);
					// 把每个对象此字段的属性写入这一列excel中
					cell.setCellValue(strTemp);

					columnIndex++;
				}
				rowIndex++;
			}
		}
	}

	/**
	 * 获取属性值
	 *
	 * @param fieldName 字段名称
	 * @param o         对象
	 * @return Object
	 */
	private static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1); // 获取方法名
			Method method = o.getClass().getMethod(getter, new Class[] {}); // 获取方法对象
			Object value = method.invoke(o, new Object[] {}); // 用invoke调用此对象的get字段方法
			return value; // 返回值
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取表中的字段名
	 *
	 * @param tableName
	 * @return
	 */
	private List<Map<String, String>> queryColumns(String tableName) {
		return baseMapper.queryColumns(tableName);
	}

	/**
	 * 列名转换成Java属性名
	 */
	private String columnToJava(String columnName) {
		if (columnName.contains("_")) {
			return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
		} else {
			return columnName;
		}
	}

	/**
	 * 根据打包路径打包文件或文件夹
	 *
	 * @param zip  打包压缩文件流
	 * @param file 需要打包的文件或文件夹
	 * @param dir  打包路径
	 * @throws Exception
	 */
	private void zipDirOrFile(ZipOutputStream zip, File file, String dir) throws Exception {
		// 如果当前的是文件夹，则进行进一步处理
		if (file.isDirectory()) {
			// 得到文件列表信息
			File[] files = file.listFiles();
			// 将文件夹添加到下一级打包目录
			zip.putNextEntry(new ZipEntry(dir + "/"));
			dir = dir.length() == 0 ? "" : dir + "/";
			// 循环将文件夹中的文件打包
			for (int i = 0; i < files.length; i++) {
				zipDirOrFile(zip, files[i], dir + files[i].getName()); // 递归处理
			}
		} else { // 当前的是文件，打包处理
			// 文件输入流
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(dir);
			zip.putNextEntry(entry);
			zip.write(FileUtils.readFileToByteArray(file));
			IOUtils.closeQuietly(bis);
			zip.flush();
			zip.closeEntry();
		}
	}

	/**
	 * 解压文件到指定目录
	 *
	 * @param zipPath 压缩文件地址
	 * @param descDir 指定目录
	 * @throws IOException
	 */
	private static void unZipFiles(String zipPath, String descDir) throws IOException {
		try {
			File zipFile = new File(zipPath);
			if (!zipFile.exists()) {
				throw new IOException("需解压文件不存在.");
			}
			File pathFile = new File(descDir);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			ZipFile zip = new ZipFile(zipFile);
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				String outPath = descDir + File.separator + zipEntryName;

				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (entry.isDirectory()) {
					continue;
				}

				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
				}
				InputStream in = zip.getInputStream(entry);
				// 输出文件路径信息
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			zip.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 字符串中的逗号替换成@#
	 * 
	 * @param str
	 * @return
	 */
	public static String cvsString(String str) {
		str = str.replaceAll(",", "@#");
		return str;
	}

	/**
	 * 字符串中的@#替换成逗号
	 * 
	 * @param str
	 * @return
	 */
	public static String cvsStringUn(String str) {
		str = str.replaceAll("@#", ",");
		return str;
	}

	public int readCvs(String path, String tableName, String projectId) throws Exception {
		List<Hardwarelibs> hardwarelibsList = new ArrayList<>();
		List<Chipsfromhardwarelibs> chipsfromhardwarelibsList = new ArrayList<>();
		String className = "";
		// 硬件建模表
		if ("gjk_hardwarelibs".equals(tableName)) {
			className = "com.inforbus.gjk.pro.api.entity.Hardwarelibs";
			for (Object obj : parseCvsFile(path, className)) {
				hardwarelibsList.add((Hardwarelibs) obj);
			}
		}
		// 芯片表
		else if ("gjk_chipsfromhardwarelibs".equals(tableName)) {
			className = "com.inforbus.gjk.pro.api.entity.Chipsfromhardwarelibs";
			for (Object obj : parseCvsFile(path, className)) {
				chipsfromhardwarelibsList.add((Chipsfromhardwarelibs) obj);
			}
		}

		// 硬件建模表
		for (Hardwarelibs hardwarelibs : hardwarelibsList) {
			hardwarelibs.setProjectId(projectId);
			if (baseMapper.getHardwarelibsById(hardwarelibs.getId()) == null) {
				baseMapper.saveHardwarelibs(hardwarelibs);
			} else {
				baseMapper.updateHardwarelibById(hardwarelibs);
			}
		}

		// 芯片表
		for (Chipsfromhardwarelibs chipsfromhardwarelibs : chipsfromhardwarelibsList) {
			chipsfromhardwarelibs.setProjectId(projectId);
			if (baseMapper.getChipsfromhardwarelibsById(chipsfromhardwarelibs.getId()) == null) {
				baseMapper.saveChipsfromhardwarelibs(chipsfromhardwarelibs);
			} else {
				baseMapper.updateChipsfromhardwarelibsById(chipsfromhardwarelibs);
			}
		}

		return 1;
	}

	/**
	 * 解析CVS文件
	 * 
	 * @param filePath
	 * @param className
	 * @return
	 * @throws Exception
	 */
	private List<Object> parseCvsFile(String filePath, String className) throws Exception {
		List<Object> objects = new ArrayList<>();
		List<String> columnList = new ArrayList<>();

		List<String> fileContent = FileUtil.readFileContent(filePath);
		for (int i = 0; i < fileContent.size(); i++) {
			if (i == 0) {
				columnList = parseCvsRow(fileContent.get(i));
			} else {
				objects.add(parseCvsRow(fileContent.get(i), className, columnList));
			}
		}
		return objects;
	}

	/**
	 * 解析cvs列
	 *
	 * @param row
	 */
	private List<String> parseCvsRow(String row) {
		List<String> list = new ArrayList<>();
		String[] strArr = row.split(",");
		for (String col : strArr) {
			list.add(col);
		}
		return list;
	}

	/**
	 * 解析cvs每行数据
	 * 
	 * @param row
	 * @param calssName
	 * @param columnList
	 * @return
	 * @throws Exception
	 */
	private Object parseCvsRow(String row, String calssName, List<String> columnList) throws Exception {
		Class<?> cla = Class.forName(calssName);
		Object obj = cla.newInstance();

		String[] colArr = row.split(",");
		for (int i = 0; i < colArr.length; i++) {
			String value = cvsStringUn(colArr[i]);
			if (StringUtils.isNotBlank(value)) {
				if ("CreateTime".equals(columnList.get(i)) || "UpdateTime".equals(columnList.get(i))) {
					LocalDateTime parse = LocalDateTime.parse(value);
					setFieldValueByName(columnList.get(i), cla, obj, parse);
				} else {
					setFieldValueByName(columnList.get(i), cla, obj, value);
				}
			}
		}
		return obj;
	}

	@Override
	public R completeCheck(String id, String userId) {
		Map<String, String> map = baseMapper.findProJSON(id);
		String xmlFilepath = proDetailPath + map.get("filePath") + map.get("fileName") + ".xml";
		CheckResult checkResult = ExternalIOTransUtils.completeCheck(xmlFilepath);
		String strLog = checkResult.getM_textConsole();
		this.rabbitmqTemplate.convertAndSend(userId, "checkLog" + "===@@@===" + strLog);
		return new R(checkResult);
	}

    /**
     * @Author wang
     * @Description: 集成代码生成
     * @Param: [projectId, username]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/20
     */
    @Override
    public R codeGeneration(String projectId, String username) {
        R r1 = new R<>();
        try {
            // 获取流程对应记录
            ProjectFile procedure = this.getById(projectId);
            Map<String, String> map = new HashMap<>();
            // 获取流程下generateCodeResult文件夹路径
            String tmpGenerateCodeResult = gitDetailPath + procedure.getFilePath() + procedure.getFileName()
                    + File.separator + generateCodeResult;
            //获取流程图id
            String workModeId = String.valueOf(procedure.getFlowId());
            //流程模型文件的绝对路径
            String workModeFilePath = getWorkModeFilePath(projectId);
            //packinfo文件路径（打包解包）
            String packinfoPath = tmpGenerateCodeResult + File.separator + "packinfo.xml";
            //自定义主题XML文件（经过处理的路径）
            String userDefineTopicFilePath = getUserDefineTopicFilePath(projectId);
            map.put("TmpGenerateCodeResult", tmpGenerateCodeResult);
            map.put("WorkModeId", workModeId);
            map.put("WorkModeFilePath", workModeFilePath);
            map.put("PackinfoPath", packinfoPath);
            map.put("UserDefineTopicFilePath", userDefineTopicFilePath);
            map.put("Username", username);
            //调用fegin接口执行集成代码生成方法
            R<Boolean> r = remoteCodeGenerationService.codeGeneration(map);
            r1.setData("集成代码生成成功");
            if (!r.getData()){
                r1.setData("集成代码生成失败,请检查相关配置");
            }
            return r1;
        }catch (Exception e){
            logger.error("集成代码生成失败 :" + e.getMessage());
            return new R<>(new Exception("集成代码生成失败"));
        }
    }

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径，从而截取想要的路径
	 * 
	 * @param id
	 * @return
	 */
	public String getSoftProcessFilePath(String id) {
		List<ProjectFile> lists = this.getFilePathListById(id);
		// 流程建模路径（赋给软硬件映射配置xml的流程文件标签）
		String workModeFilePath = "";
		// 客户自存自取的文件路径，赋给软硬件映射配置ml的方案路径
		String planFilePath = "";
		String str = "";
		for (ProjectFile ls : lists) {
			if (ls.getFileType().equals("11")) {
				String bb = ls.getFilePath().replaceAll("\\\\", "/");
				String www = gitDetailPath + ls.getFilePath() + ls.getFileName() + ".xml";
				String aa = bb.substring(0, bb.lastIndexOf("/"));
				aa = aa.substring(0, aa.lastIndexOf("/"));
				String ppp = gitDetailPath + aa + File.separator + softToHardResult;
				workModeFilePath = www.replaceAll("\\\\", "/");
				planFilePath = ppp.replaceAll("\\\\", "/");
				str = workModeFilePath + "@###@###@@" + planFilePath;
			}

		}
		return str;
	}

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @return
	 */
	public R getworkSpacePathById(String id, AppDataDTO appDataDTO) {
		String local_REPO_PATH = null;
		// id：软硬件映射配置的主键id
		List<ProjectFile> lists = this.getFilePathListById(id);
		// 当前项目id
		String projectId = lists.size() > 0 ? lists.get(0).getProjectId() : "0";
		ProjectFile processFile = this.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, id));
		ProjectFile flowFile = this
				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, processFile.getParentId()));
		// 流程模型文件
		String workModeFilePath = "";
		// cpu模型文件
		String cpuModelFilePath = "";
		// 硬件模型文件
		String hardWareFilePath = "";
		// 软硬件映射配置文件
		String mapConfigPath = "";
		// 系统参数文件
		String sysParamFilePath = "";
		// 工作路径
		String workSpacePath = "";
		// 详细方案的id
		String planId = "";
		// 方案展示的文件路径
		String simplePlanFile = "";
		String flowpath = "";
		String flowName = "";
		for (ProjectFile ls : lists) {
			if (ls.getFileType().equals("11")) {
				workModeFilePath = ls.getFilePath() + ls.getFileName() + ".xml";
			}
			if (ls.getFileType().equals("15")) {
				cpuModelFilePath = ls.getFilePath() + ls.getFileName() + ".xml";
			}
			if (ls.getFileType().equals("12")) {
				hardWareFilePath = ls.getFilePath() + ls.getFileName() + ".xml";
			}
			if (ls.getFileType().equals("13")) {
				mapConfigPath = ls.getFilePath() + ls.getFileName() + ".xml";
			}
			if (ls.getFileType().equals("17")) {
				sysParamFilePath = ls.getFilePath() + ls.getFileName() + ".xml";
			}
			// 方案展示的路径
			if (ls.getFileType().equals("14")) {
				simplePlanFile = gitDetailPath + ls.getFilePath() + ls.getFileName() + ".xml";
			}
			// 详细方案的路径
			if (ls.getFileType().equals("18")) {
				planId = ls.getId();
				workSpacePath = ls.getFilePath();
			}
		}
		// 调用软硬件映射接口
		// String workSpacePaths = softwareInterface.softInter(workModeFilePath,
		// cpuModelFilePath, hardWareFilePath,
		// mapConfigPath, sysParamFilePath, workSpacePath);
		// local_REPO_PATH = JGitUtil.getLOCAL_REPO_PATH();
		local_REPO_PATH = gitDetailPath;
		// 后期把filepath替换成数据库中的workSpacePaths
		// getFile(filepath, planId);
		/**************************************
		 * update by zhx
		 *********************************************/
		hardWareFilePath = local_REPO_PATH + hardWareFilePath;
		mapConfigPath = local_REPO_PATH + mapConfigPath;
		// sysParamFilePath =
		// "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\24141\\12312312流程\\模型\\系数配置.xml";
		// sysParamFilePath = gitDetailPath +
		// flowFile.getFilePath()+flowInfPath+File.separator+"系数文件.xml" ;
		workSpacePath = gitDetailPath + flowFile.getFilePath() + softToHardResult;
		sysParamFilePath = workSpacePath + File.separator + "系数文件.xml";
		// 客户exe文件全路径
		String exe = JGitUtil.getSoftToHard();// "D:\\14S_GJK_GIT\\gjk\\gjk\\exe\\exe.exe";
		// 调用客户接口执行exe
		// appDataDTO.getUserName():当前用户名
		String[] strArray = new String[] { exe, hardWareFilePath, mapConfigPath, sysParamFilePath,
				appDataDTO.getUserName() };
		try {
			Process process = Runtime.getRuntime().exec(strArray);
			InputStreamReader reader = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(reader);

			StringBuffer stringBuffer = new StringBuffer();

			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				stringBuffer.append(str);
			}

			System.out.println(stringBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/****************************************
		 * end
		 ********************************************/

		// 获取方案文件路径列表；调用接口
		// List<ProjectPlan> planFileLists =
		// managerService.getPlanFilePathListByParentId(planId);//需要修改,zhx
		ArrayList<String> schemeFileList = new ArrayList<>();

		List<String> fileName = this.getFile(workSpacePath, 0);

		for (String filename : fileName) {
			String schemeFile = workSpacePath + "/" + filename;
			schemeFileList.add(schemeFile);
		}
		System.out.println("schemeFileList::" + schemeFileList);
		// 调用生成缩略方案的接口
		try {
			this.simplePlan(schemeFileList, simplePlanFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new R<>(simplePlanFile);
	}

	/*
	 * 函数名：getFile 作用：使用递归，输出指定文件夹内的所有文件 参数：path：文件夹路径 deep：表示文件的层次深度，控制前置空格的个数
	 * 前置空格缩进，显示文件层次结构
	 */
	private static List<String> getFile(String path, int deep) {
		List<String> fileNamelist = new ArrayList<String>();
		// 获得指定文件对象
		File file = new File(path);
		// 获得该文件夹内的所有文件
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile())// 如果是文件
			{
				for (int j = 0; j < deep; j++)// 输出前置空格
					System.out.print(" ");
				// 只输出文件名字
				// System.out.println( array[i].getName());
				if (array[i].getName().startsWith("软硬件映射") && array[i].getName().endsWith(".xml")) {

					fileNamelist.add(array[i].getName());
				}
				System.out.println("fileNamelist" + fileNamelist);

				// 输出当前文件的完整路径
				// System.out.println("#####" + array[i]);
				// 同样输出当前文件的完整路径 大家可以去掉注释 测试一下
				// System.out.println(array[i].getPath());
			} else if (array[i].isDirectory())// 如果是文件夹
			{
				for (int j = 0; j < deep; j++)// 输出前置空格
					System.out.print(" ");

				System.out.println(array[i].getName());
				// System.out.println(array[i].getPath());
				// 文件夹需要调用递归 ，深度+1
				getFile(array[i].getPath(), deep + 1);
			}
		}
		return fileNamelist;
	}

	// 调回写部署方案接口
	public void writeBackDeploySchemeById(Map<String, Object> map) {
		// 流程模型文件
		String workModeFilePath = "";
		String simplePlanFiles = "";
		String schemeFile = "";
		map.get("id");
		map.get("path");
		List<ProjectFile> lists = this.getFilePathListById((String) map.get("id"));
		for (ProjectFile ls : lists) {
			if (ls.getFileType().equals("11")) {
				workModeFilePath = gitDetailPath + File.separator + ls.getFilePath() + ls.getFileName() + ".xml";
			}
			// 方案展示的路径
			// if (ls.getFileType().equals("14")) {
			// simplePlanFiles = ls.getFilePath() + ls.getFileName() + ".xml";
			// }
		}
		// 调用回写部署方案接口
		// String path =
		// "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\方案展示.xml";
		// String simplePlanFiles =
		// gitDetailPath+processFile.getFilePath()+processFile.getFileName()+"\\模型\\方案展示.xml"
		// ;

		try {
			// 后期换成(String)map.get("path")
			schemeFile = (String) map.get("path");
			// 报错
			// 输入：流程模型文件、部署方案文件（选择的方案路径）
			try {
				ExternalIOTransUtils.writeBackDeployScheme(workModeFilePath, schemeFile);
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean createXmlFiles(XmlEntity entity, String proDetailId) {
		ProjectFile processFile = this
				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, proDetailId));
		ProjectFile modelFile = this
				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, processFile.getParentId()));
		ProjectFile flowFile = this
				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, modelFile.getParentId()));

		String filePath = gitDetailPath + flowFile.getFilePath() + flowFile.getFileName() + "/softToHardResult";
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return this.createXmlFile(entity, proDetailId);
	}

	@Override
	public String getprocessFile(String projectId) {
		ProjectFile processFile = getOne(
				Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, this.getById(projectId).getParentId()));
		String filePath = processFile.getFilePath();
		return filePath;
	}
}
