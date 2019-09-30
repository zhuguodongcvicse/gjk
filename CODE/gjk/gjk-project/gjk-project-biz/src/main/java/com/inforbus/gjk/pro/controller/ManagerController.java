package com.inforbus.gjk.pro.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inforbus.gjk.pro.api.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.pro.api.dto.ProjectTree;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Arrows;
import com.inforbus.gjk.pro.api.entity.Chipsfromhardwarelibs;
import com.inforbus.gjk.pro.api.entity.DeploymentXMLMap;
import com.inforbus.gjk.pro.api.entity.HardwareNode;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
import com.inforbus.gjk.pro.api.entity.Part;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.api.entity.ProjectPlan;
import com.inforbus.gjk.pro.api.util.ProcedureXmlAnalysis;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import com.inforbus.gjk.pro.api.vo.TreeUtil;
import com.inforbus.gjk.pro.service.AppService;
import com.inforbus.gjk.pro.service.ManagerService;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import mapping.BackNodeInfoForSpb;

/**
 * @ClassName: ManagerController
 * @Description:
 * @Author xiaohe
 * @DateTime 2019年4月24日 上午8:17:16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

	private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
	private static final String gitDetailPath = JGitUtil.getLOCAL_REPO_PATH();//gitlu路径
	private static final String generateCodeResult = JGitUtil.getGenerateCodeResult();//集成代码生成结果存放路径
	private static final String softToHardResult = JGitUtil.getSoftToHardResult();//软硬件映射结果文件存放路径
	private static final String flowInfPath = JGitUtil.getFlowInfPath();//获取流程内外部接口存放路径 add by hu
	
	/**
	 * @Fields managerService : 项目管理
	 */
	private final ManagerService managerService;
	private final AppService appService;
	private static final String proDetailPath = JGitUtil.getLOCAL_REPO_PATH();

	@PostMapping("/getTwoNode/{length}")
	public R getOneIdByFourId(@RequestBody ProjectFile file, @PathVariable("length") Integer length) {
		ProjectFile retFile = file;
		for (int i = 0; i < length; i++) {
			retFile = managerService.getById(retFile.getParentId());
		}
		return new R<>(retFile);
		
	}
	/**
	 * @Title: 获取树形菜单集合
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年4月24日 上午8:16:57
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree/{projectId}")
	public R getTree(@PathVariable("projectId") String projectId) {
		List<ProjectFileVO> proDetailTree = managerService.getTreeByProjectId(projectId);
		List<ProjectFileVO> appFileTreeList = Lists.newArrayList();
		for (ProjectFileVO projectFileVO : proDetailTree) {
			if ("9".equals(projectFileVO.getFileType())) {
				appFileTreeList.addAll(appService.getAppFileTree(projectFileVO.getFileId()));
			}
		}
		proDetailTree.addAll(appFileTreeList);
		List<ProjectTree> projectTrees = TreeUtil.buildTree(proDetailTree, "-1");
		return new R<>(projectTrees);
	}

	@GetMapping("/getProcedureNameListByProId/{projectId}")
	public R getProcedureNameListByProId(@PathVariable("projectId") String projectId) {
		return new R<>(managerService.getProcedureNameListByProjectId(projectId));
	}

	@RequestMapping(value = "/saveProProcess/{projectId}/{processName}")
	public R saveProProcess(@PathVariable("projectId") String projectId,
			@PathVariable("processName") String processName) {
		return new R<>(managerService.saveProProcess(projectId, processName));
	}

	@PutMapping
	@RequestMapping("/createXmlFile/{proDetailId}")
	public R createXmlFile(@RequestBody XmlEntity entity, @PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.createXmlFile(entity, proDetailId));
	}

	@PutMapping
	@RequestMapping("/createXmlFileByXmlEntityMap/{proDetailId}")
	public R createXmlFile(@RequestBody XmlEntityMap entity, @PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.createXmlFile(entity, proDetailId));
	}

	@GetMapping(value = "/dispose/{proDetailId}")
	public R rollBackDisposeXml(@PathVariable("proDetailId") String proDetailId) {
		String filePath = "";
		ProjectFile projectFile = managerService.getProDetailById(proDetailId);
		Boolean bool = managerService.isXmlFileExist(proDetailId);
		// 如果文件存在
		if (bool == true) {
			filePath = proDetailPath + projectFile.getFilePath() + projectFile.getFileName() + ".xml";
		} else {
			filePath = System.getProperty("user.dir") + "\\软硬件映射配置.xml";
		}
		return new R<>(XmlFileHandleUtil.analysisXmlFile(new File(filePath)));
	}

	@GetMapping("/getCoeffNodeTree/{proDetailId}")
	public R getCoeffNodeTree(@PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.getCoeffNodeTree(proDetailId));
	}

	@GetMapping
	@RequestMapping("/getcoefficientXmlEntityMap")
	public R getcoefficientXmlEntityMap() {
		return new R<>(managerService.getcoefficientXmlEntityMap());
	}

	@GetMapping
	@RequestMapping("/getSysConfigByApiReturn/{proDetailId}")
	public R getSysConfigByApiReturn(@PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.getSysConfigByApiReturn(proDetailId));
	}

	@GetMapping
	@RequestMapping("/isXmlFileExist/{proDetailId}")
	public R isXmlFileExist(@PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.isXmlFileExist(proDetailId));
	}

	@GetMapping
	@RequestMapping("/getSysConfigXmlEntityMap/{proDetailId}")
	public R getSysConfigXmlEntityMap(@PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.getSysConfigXmlEntityMap(proDetailId));
	}

	@PostMapping("/editProJson/{proId}")
	public R editProJSON(@RequestBody Object objJson, @PathVariable("proId") String proId) {
		return new R<>(managerService.editProJSON(proId, objJson));
	}

	@PostMapping("/findProJSON/{proId}")
	public R findProJSON(@PathVariable("proId") String proId) {
		return new R<>(managerService.findProJSON(proId));
	}

	@GetMapping("/getProcessName/{id}")
	public R getProcessName(@PathVariable("id") String id) {
		System.out.println("aaaaaaaa" + id);
		System.out.println("pppppp" + managerService.getProcessName(id));
		return new R<>(managerService.getProcessName(id));
	}

	@PutMapping("/updateProcedureDetail")
	public R updateProcedureDetail(@RequestBody ProjectFile projectFile) {
		return new R<>(managerService.updateById(projectFile));
	}

	@GetMapping("/canIntegrationCodeCreate/{modelId}")
	public R canIntegrationCodeCreate(@PathVariable("modelId") String modelId) {
		return new R<>();
	}

	@PostMapping("/appAssemblyProjectCreate/{userName}/{procedureId}")
	public R appAssemblyProjectCreate(@PathVariable("userName") String userName,
			@PathVariable("procedureId") String procedureId, @RequestBody Map<String, String> appDirFilePath) {
		return new R<>(
				managerService.appAssemblyProjectCreate(userName, procedureId, appDirFilePath.get("bspDirPath")));
	}

	/**
	 * @Title: uploadReturnUrll
	 * @Description: 单文件上传
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:41:10
	 * @param ufile
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/appImageUpload", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> appImageUpload(@RequestParam(value = "file", required = false) MultipartFile ufile,
			@RequestParam(value = "appJSON", required = false) String appJSON) {
		return new R<>(managerService.saveAppImage(ufile, JSONUtil.toBean(appJSON, App.class)));
	}

	/**
	 * 软硬件映射调接口缩略方案
	 */
	//	@PostMapping("/simplePlan")
	//	public void simplePlan(String id) {
	//		//File file = new File(filePaths);
	//		//getFile(filePaths);
	//		//查找路径下的所有文件
	//        
	//		
	//		//managerService.simplePlan(schemeFileList, simplePlanFile);
	//		System.out.println("lllllllll");
	//	}
	//	
	private void getFile(String path, String id) {
		ProjectPlan projectPlan = new ProjectPlan();

		// get file list where the path has
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		// get the folder list
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				// 设置详细方案的id
				projectPlan.setId(IdGenerate.uuid());
				projectPlan.setParentId(id);

				// only take file name
				//                System.out.println("^^^^^" + array[i].getName());
				projectPlan.setFileName(array[i].getName().substring(0, (array[i].getName().length() - 4)));
				System.out.println("fileName:" + projectPlan.getFileName());
				// take file path and name
				//                System.out.println("#####" + array[i]);
				projectPlan.setFilePath(array[i].getPath());
				// take file path and name
				//                System.out.println("*****" + array[i].getPath());
				managerService.saveProjectPlan(projectPlan);
			} else if (array[i].isDirectory()) {
				getFile(array[i].getPath(), id);
			}
		}
	}

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getFilePathListById/{id}")
	public R getworkSpacePathById(@PathVariable("id") String id) {
		String local_REPO_PATH = null;
		// id：软硬件映射配置的主键id
		List<ProjectFile> lists = managerService.getFilePathListById(id);
		// 当前项目id
		String projectId = lists.size()>0?lists.get(0).getProjectId():"0";
		ProjectFile processFile= managerService.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, id));
		ProjectFile flowFile= managerService.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, processFile.getParentId()));
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
		String flowName ="";
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
				simplePlanFile = gitDetailPath +ls.getFilePath() + ls.getFileName() + ".xml";
			}
			// 详细方案的路径
			if (ls.getFileType().equals("18")) {
				planId = ls.getId();
				workSpacePath = ls.getFilePath();
			}
		}
		// 调用软硬件映射接口
		//String workSpacePaths = softwareInterface.softInter(workModeFilePath, cpuModelFilePath, hardWareFilePath,
			//	mapConfigPath, sysParamFilePath, workSpacePath);
		local_REPO_PATH = JGitUtil.getLOCAL_REPO_PATH();
		// 后期把filepath替换成数据库中的workSpacePaths
		//getFile(filepath, planId);
		/**************************************update by  zhx*********************************************/
		hardWareFilePath = local_REPO_PATH + hardWareFilePath;
		mapConfigPath = local_REPO_PATH + mapConfigPath;
		//update by huqinghua
		//sysParamFilePath = "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\24141\\12312312流程\\模型\\系数配置.xml";			sysParamFilePath = gitDetailPath + flowFile.getFilePath()+flowInfPath+File.separator+"系数文件.xml" ;
		workSpacePath = gitDetailPath + flowFile.getFilePath()+softToHardResult ;
				
		//客户exe文件全路径
		String exe = JGitUtil.getSoftToHard();//"D:\\14S_GJK_GIT\\gjk\\gjk\\exe\\exe.exe";
		//调用客户接口执行exe
		String[] strArray = new String[] {
				exe,
				hardWareFilePath,
				mapConfigPath,
				sysParamFilePath,
				workSpacePath
		};
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


		/****************************************end********************************************/


		// 获取方案文件路径列表；调用接口
		//List<ProjectPlan> planFileLists = managerService.getPlanFilePathListByParentId(planId);//需要修改,zhx
		ArrayList<String> schemeFileList = new ArrayList<>();
		
		List<String> fileName = getFile(workSpacePath,0);   
		
		for (String filename : fileName) {
		String	schemeFile = workSpacePath +"/" +filename;
		schemeFileList.add(schemeFile);
		}
		System.out.println("schemeFileList::" + schemeFileList);
		// 调用生成缩略方案的接口
				try {
					managerService.simplePlan(schemeFileList, simplePlanFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
		return new R<>(simplePlanFile);

	}

	/*
	 * 函数名：getFile
	 * 作用：使用递归，输出指定文件夹内的所有文件
	 * 参数：path：文件夹路径   deep：表示文件的层次深度，控制前置空格的个数
	 * 前置空格缩进，显示文件层次结构
	 */
	private static List<String> getFile(String path,int deep){   
		List<String> fileNamelist=new ArrayList<String>();
		// 获得指定文件对象  
		File file = new File(path);   
		// 获得该文件夹内的所有文件   
		File[] array = file.listFiles();   

		for(int i=0;i<array.length;i++)
		{   
			if(array[i].isFile())//如果是文件
			{   
				for (int j = 0; j < deep; j++)//输出前置空格
					System.out.print(" ");
				// 只输出文件名字  
				//                System.out.println( array[i].getName());  
				if(array[i].getName().startsWith("软硬件映射") && array[i].getName().endsWith(".xml")) {

					fileNamelist.add(array[i].getName());
				}
				System.out.println("fileNamelist"+fileNamelist);

				// 输出当前文件的完整路径   
				// System.out.println("#####" + array[i]);   
				// 同样输出当前文件的完整路径   大家可以去掉注释 测试一下   
				// System.out.println(array[i].getPath());   
			}
			else if(array[i].isDirectory())//如果是文件夹
			{  
				for (int j = 0; j < deep; j++)//输出前置空格
					System.out.print(" ");

				System.out.println( array[i].getName());
				//System.out.println(array[i].getPath());
				//文件夹需要调用递归 ，深度+1
				getFile(array[i].getPath(),deep+1);  
			}   
		}
		return fileNamelist;   
	}   

	// 调回写部署方案接口
	@PostMapping("/writeBackDeployScheme")
	public void writeBackDeploySchemeById(@RequestBody Map<String, Object> map) {
		// 流程模型文件
		String workModeFilePath = "";
		String simplePlanFiles = "";
		String schemeFile = "";
		map.get("id");
		map.get("path");
		List<ProjectFile> lists = managerService.getFilePathListById((String) map.get("id"));
		for (ProjectFile ls : lists) {
			if (ls.getFileType().equals("11")) {
				workModeFilePath = ls.getFilePath() + ls.getFileName() + ".xml";
			}
			// 方案展示的路径
			//			if (ls.getFileType().equals("14")) {
			//				simplePlanFiles = ls.getFilePath() + ls.getFileName() + ".xml";
			//			}
		}
		// 调用回写部署方案接口
		//	String path = "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\方案展示.xml";
		//String simplePlanFiles = gitDetailPath+processFile.getFilePath()+processFile.getFileName()+"\\模型\\方案展示.xml" ;

		try {
			// 后期换成(String)map.get("path")
			schemeFile = (String)map.get("path");
			// 报错
			// 输入：流程模型文件、部署方案文件（选择的方案路径）
			BackNodeInfoForSpb.writeBackDeployScheme(workModeFilePath,schemeFile );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("0000000::");

	}

	// 主题配置生成xml
	@PutMapping
	@RequestMapping("/createThemeXML/{proDetailId}/{name}")
	public R createThemeXML(@RequestBody XmlEntityMap entity, @PathVariable("proDetailId") String proDetailId,
			@PathVariable("name") String name) {
		return new R<>(managerService.createThemeXML(entity, proDetailId, name));
	}

	// 网络配置生成xml
	@PutMapping
	@RequestMapping("/createNetWorkXML/{proDetailId}/{name}")
	public R createNetWorkXML(@RequestBody XmlEntityMap entity, @PathVariable("proDetailId") String proDetailId,
			@PathVariable("name") String name) {
		return new R<>(managerService.createNetWorkXML(entity, proDetailId, name));
	}

	// 解析自定义配置xml
	@PutMapping
	@RequestMapping("/analysisThemeXML/{proDetailId}")
	public R analysisThemeXML(@PathVariable("proDetailId") String proDetailId) {
		String flowFilePath = "";
		String path = "";
		List<Part> partList = new ArrayList<Part>();
		List<ProjectFile> ProjectFileList = managerService.getFilePathListById(proDetailId);
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
			String filePath = System.getProperty("user.dir") + "\\主题配置.xml";
			themeData = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(new File(filePath));
		} else {
			themeData = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(themeFile);
		}
		String netWorkPath = path + "自定义配置__网络配置.xml";
		File netWorkFile = new File(netWorkPath);
		if (!netWorkFile.exists()) {
			String filePath1 = System.getProperty("user.dir") + "\\网络配置.xml";
			netWorkData = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(new File(filePath1));
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

	public static void main(String[] args) {
		getCmd();
	}

	public static void getCmd() {
		String path = "D:\\Program Files (x86)\\Microsoft Visual Studio\\2019\\Community\\Common7\\IDE";
		File dir = new File(path);
		// 编译解决方案
		String str = "devenv D:\\Build\\CCode\\ConsoleApplication1\\ConsoleApplication1.sln /Rebuild";
		// 编译单个项目
		String str1 = "devenv D:\\CCode\\ConsoleApplication1\\ConsoleApplication1\\ConsoleApplication1.vcxproj /Build 'Release|Win32'/project D:\\CCode\\ConsoleApplication1\\ConsoleApplication1\\ConsoleApplication1.vcxproj";
		String[] cmd = new String[] { "cmd", "/c", str1 };
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec(cmd, null, dir);
			InputStreamReader ir = new InputStreamReader(p.getInputStream(), "GBK");
			LineNumberReader input = new LineNumberReader(ir);
			String line = null;
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@GetMapping("/getProcessFilePathById/{id}")
	public R getProcessFilePathById(@PathVariable("id") String id) {
		// id：软硬件映射配置的主键id
		List<ProjectFile> lists = managerService.getFilePathListById(id);
		// 流程模型文件
		String workModeFilePath = "";
		for (ProjectFile ls : lists) {
			if (ls.getFileType().equals("11")) {
				workModeFilePath = proDetailPath + ls.getFilePath() + ls.getFileName() + ".xml";
			}
		}
		File file = new File(workModeFilePath);
		if (file.exists()) {
			System.out.println("iiii::" + ProcedureXmlAnalysis.getComponentList(file));
			return new R<>(ProcedureXmlAnalysis.getComponentList(file));
		} else {
			logger.error("缺少流程配置文件，请先配置流程。");
			return new R<>();
		}
	}

	/**
	 * 根据id获取硬件建模数据getlibsInDepolyment
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("getHardwarelibs/{id}")
	public Hardwarelibs getHardwarelibs(@PathVariable("id") String id) {
		Hardwarelibs hardwarelib = managerService.getHardwarelibs(id);
		if (hardwarelib != null) {
			return hardwarelib;
		} else {
			return null;
		}
	}

	/**
	 * 保存硬件建模
	 * 
	 * @param hardwarelibs
	 */
	@PostMapping("saveHardwarelibs")
	public void saveHarewarelibs(@RequestBody Hardwarelibs hardwarelibs) {
		managerService.saveHardwarelibs(hardwarelibs);
	}
	/**
	 * 保存硬件建模
	 *
	 * @param chipsfromhardwarelibs
	 */
	@PostMapping("saveChipsFromHardwarelibs")
	public void saveChipsFromHardwarelibs(@RequestBody Chipsfromhardwarelibs chipsfromhardwarelibs) {
		managerService.saveChipsfromhardwarelibs(chipsfromhardwarelibs);
	}
	@GetMapping("getChipsfromhardwarelibs/{id}")
	public Chipsfromhardwarelibs getChipsfromhardwarelibs(@PathVariable("id") String id) {
		long startTime = System.currentTimeMillis(); //获取开始时间

		Chipsfromhardwarelibs chipsfromhardwarelibs = managerService.getChipsfromhardwarelibs(id);

		long endTime = System.currentTimeMillis(); //获取结束时间

		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间

		return chipsfromhardwarelibs;
	}
	/**
	 * xml文件解析
	 * 
	 * @return
	 */

	@GetMapping("getAllList/{id}")
	public List getAllList(@PathVariable("id") String id) {
		File xmlFile = managerService.getXmlFile(id);
		List<HardwareNode> hardwareNodeList = ProcedureXmlAnalysis.getHardwareNodeList(xmlFile);
		System.out.println("hardwareNodeList----" + hardwareNodeList);
		if (hardwareNodeList.size() != 0) {
			List<Arrows> arrowsList = ProcedureXmlAnalysis.getArrowsList(xmlFile);
			ArrayList<Object> array = new ArrayList<>();
			array.add(hardwareNodeList);
			array.add(arrowsList);
			return array;
		} else {
			return null;
		}

	}

	/**
	 * 部署图xml生成
	 */
	@PostMapping("/sendXmlMap")
	public void updataDeploymentXml(@RequestBody DeploymentXMLMap deploymentXMLMap) {
		managerService.updataDeploymentXml(deploymentXMLMap);
		System.out.println("xmlparams" + deploymentXMLMap);
	}

	/**
	 * 更新硬件建模
	 * 
	 * @param hardwarelibs
	 */
	@PostMapping("updateHardwarelib")
	public void updateHardwarelib(@RequestBody Hardwarelibs hardwarelibs) {
		managerService.updateHardwarelib(hardwarelibs);
	}

	/**
	 * 删除硬件建模
	 * 
	 * @param id
	 */
	@DeleteMapping("deleteHardwarelibById/{id}")
	public void deleteHardwarelibById(@PathVariable String id) {
		managerService.deleteHardwarelibById(id);
	}

	/**
	 * 删除硬件建模芯片
	 * @param id
	 */
	@DeleteMapping("deleteChipsFromHardwarelibs/{id}")
	public void deleteChipsFromHardwarelibs(@PathVariable String id) {
		managerService.deleteChipsFromHardwarelibs(id);
	}
	/**
	 *流程建模导出 
	 */
	@PutMapping
	@RequestMapping("/exportFile/{id}")
	public void exportFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") String id) {
		try {
			StringRef sr = new StringRef();
			byte[] data = managerService.exportFile(id,sr);
			ProjectFile retFile = managerService.getById(id);
			for (int i = 0; i < 2; i++) {
				retFile = managerService.getById(retFile.getParentId());
			}
			String zipFileName = retFile.getFileName() + ".zip";
			response.reset();
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", new String(zipFileName.getBytes("UTF-8"), "ISO-8859-1")));
			response.setHeader("FileName", new String(zipFileName.getBytes("utf-8"), "ISO-8859-1"));
			response.setHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream; charset=utf-8");
			IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	/**
	 * 流程建模导入
	 */

	@ResponseBody
	@PostMapping(path = "/importFile", consumes = { "multipart/mixed", "multipart/form-data" })
	public R importFile(@RequestParam(value = "file", required = false) MultipartFile file) {
		if(Objects.isNull(file) || file.isEmpty()) {
			logger.error("请选中文件导入");
		}
		String filename = file.getOriginalFilename();
		if (!filename.endsWith("zip")) {
			logger.info("传入文件格式不是zip文件" + filename);
		}
		Map<String, Object> retMap = Maps.newHashMap();
		String zipFileName = "";
		try {
			ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream(), Charset.forName("utf-8"));
			BufferedInputStream bs = new BufferedInputStream(zipInputStream);
			ByteArrayOutputStream bos = null;
			ZipEntry zipEntry;
			String json = "";
			String xmlData = "";
			while((zipEntry = zipInputStream.getNextEntry()) != null) {
				zipFileName = zipEntry.getName();
				if(zipFileName.split("\\.")[1].equals("json")) {
					bos =  new ByteArrayOutputStream();
					int index  = 0;
					byte[] jsonBytes = new byte[1024];
					while((index = bs.read(jsonBytes)) != -1) {
						bos.write(jsonBytes,0,index);
					}
					byte[] strBytes = bos.toByteArray();
					json = new String(strBytes,0,strBytes.length ,"utf-8" );
					System.out.println(json);
					Object jsonStr = JSON.toJavaObject(JSONObject.parseObject(json), Object.class);
					retMap.put("json", jsonStr);
				}else {
					bos =  new ByteArrayOutputStream();
					int bytesRead  = 0;
					byte[] bytes = new byte[1024];
					while((bytesRead = bs.read(bytes)) != -1) {
						bos.write(bytes,0,bytesRead);
					}
					byte[] strByte = bos.toByteArray();
					//xmlData = new String(strByte,0,strByte .length ,"utf-8" );
					XmlEntityMap xmlJson = XmlFileHandleUtil.analysisXmlStr(strByte);
					retMap.put("xmlJson", xmlJson);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new R<>(retMap);
	}

	/**
	 * 集成代码生成
	 */
	@PutMapping("/codeGeneration/{projectId}/{username}")
	public void codeGeneration(@PathVariable("projectId") String projectId,@PathVariable("username") String username) {
		ProjectFile processFile=	managerService.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, projectId));
		//		processFile.getFilePath()
		String tmpGenerateCodeResult=gitDetailPath+processFile.getFilePath()+processFile.getFileName()+File.separator+generateCodeResult;
		//		String tmpSoftToHardResult=gitDetailPath+processFile.getFilePath()+softToHardResult;

		String generatecode = JGitUtil.getGeneratecode();//"D:\\14S_GJK_GIT\\gjk\\gjk\\generateCodeExe\\generatecode.exe";
		String userName = 	username;
		String workModeId = projectId;
		String workModeFilePath = managerService.getWorkModeFilePath(projectId);
		String packinfoPath = tmpGenerateCodeResult+File.separator+"packinfo.xml";
		String userDefineTopicFilePath = managerService.getUserDefineTopicFilePath(projectId);
		String cmpIntgCodeFilePath = tmpGenerateCodeResult;

		String[] strArray = new String[] {
				generatecode,
				userName,
				projectId,
				workModeFilePath,
				packinfoPath,
				userDefineTopicFilePath,
				cmpIntgCodeFilePath };
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

	}
	
	@ResponseBody
	@PostMapping(path = "/getWorking/{flowName}/{id}", consumes = { "multipart/mixed", "multipart/form-data" })
	public void getWorking(@RequestParam(value = "file", required = false) MultipartFile file, @PathVariable("flowName") String flowName,@PathVariable("id") String id) {
		managerService.getWorking(file, flowName, id);
	}
	
		@ResponseBody
	@PostMapping(path = "/getSoftWareListAndPlatformName")
	public R getSoftWareListAndPlatformName() {
		return managerService.getSoftWareListAndPlatformName();
}

	@PutMapping("/updatePartSoftwareAndPlatform")
	public R updatePartSoftwareAndPlatform(@RequestBody Software software) {
		return new R<>(managerService.updatePartSoftwareAndPlatform(software));
	}
    @ResponseBody
    @GetMapping("/showPartSoftwareAndPlatform/{id}")
    public R showPartSoftwareAndPlatform(@PathVariable("id") String id) {
        return managerService.showPartSoftwareAndPlatform(id);
    }

    /**
     * 得到平台大类
     * @return
     */
    @ResponseBody
    @PostMapping(path = "/getPlatformList")
    public R getPlatformList() {
        return managerService.getPlatformList();
    }
}
