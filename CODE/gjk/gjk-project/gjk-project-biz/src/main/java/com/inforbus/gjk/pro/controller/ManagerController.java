package com.inforbus.gjk.pro.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inforbus.gjk.admin.api.entity.BSP;
import com.inforbus.gjk.pro.api.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.pro.api.dto.AppDataDTO;
import com.inforbus.gjk.pro.api.dto.ProjectTree;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Arrows;
import com.inforbus.gjk.pro.api.entity.Chipsfromhardwarelibs;
import com.inforbus.gjk.pro.api.entity.DeploymentXMLMap;
import com.inforbus.gjk.pro.api.entity.HardwareNode;
import com.inforbus.gjk.pro.api.entity.Hardwarelibs;
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
//	private static final String gitDetailPath = JGitUtil.getLOCAL_REPO_PATH();// gitlu路径
//	private static final String generateCodeResult = JGitUtil.getGenerateCodeResult();// 集成代码生成结果存放路径
//	private static final String softToHardResult = JGitUtil.getSoftToHardResult();// 软硬件映射结果文件存放路径
//	private static final String flowInfPath = JGitUtil.getFlowInfPath();// 获取流程内外部接口存放路径 add by hu

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
//		ProjectFile processFile = managerService
//				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, proDetailId));
//		ProjectFile modelFile = managerService
//				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, processFile.getParentId()));
//		ProjectFile flowFile = managerService
//				.getOne(Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, modelFile.getParentId()));
//
//		String filePath = gitDetailPath + flowFile.getFilePath() + flowFile.getFileName() + "/softToHardResult";
//		File file = new File(filePath);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		return new R<>(managerService.createXmlFile(entity, proDetailId));
		return new R<>(managerService.createXmlFiles(entity, proDetailId));
	}

	@PutMapping
	@RequestMapping("/createXmlFileByXmlEntityMap/{proDetailId}")
	public R createXmlFile(@RequestBody XmlEntityMap entity, @PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.createXmlFile(entity, proDetailId));
	}

	/**
	 * 解析软硬件映射配置xml
	 * @param proDetailId 项目id
	 * @return
	 */
	@GetMapping(value = "/dispose/{proDetailId}")
	public R rollBackDisposeXml(@PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.getDisposeXmlEntityMap(proDetailId));
	}

	@GetMapping("/getCoeffNodeTree/{proDetailId}")
	public R getCoeffNodeTree(@PathVariable("proDetailId") String proDetailId) {
		return new R<>(managerService.getCoeffNodeTree(proDetailId));
	}

	@GetMapping("/getcoefficientXmlEntityMap/{proDetailId}")
	public R getcoefficientXmlEntityMap(@PathVariable String proDetailId) {
		return new R<>(managerService.getcoefficientXmlEntityMap(proDetailId));
	}

	@GetMapping
	@RequestMapping("/getSysConfigByApiReturn/{proDetailId}")
	public R getSysConfigByApiReturn(@PathVariable("proDetailId") String proDetailId) {
		return managerService.getSysConfigByApiReturn(proDetailId);
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

	/**
	 * app组件工程生成预处理
	 * 
	 * @param allMessage
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/appProCreatePretreatment")
	public R<?> appProCreatePretreatment(@RequestParam(value = "allMessage", required = false) String allMessage) {
		Map<String, String> map = (Map<String, String>) JSONUtil.toBean(allMessage, Map.class);
		return managerService.appProCreatePretreatment(map);
	}

	/**
	 * @Title: appAssemblyProjectCreate
	 * @Description: app组件工程的生成
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:41:10
	 * @param ufile
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/appAssemblyProjectCreate", consumes = { "multipart/mixed", "multipart/form-data" })
	public R<?> appImageUpload(@RequestParam(value = "file", required = false) MultipartFile ufile,
			@RequestParam(value = "allMessage", required = false) String allMessage) {
		Map<String, String> map = (Map<String, String>) JSONUtil.toBean(allMessage, Map.class);
		R r = managerService.appAssemblyProjectCreate(ufile, map);
		if (r.getCode() == CommonConstants.SUCCESS) {
			appService.saveApp((App) r.getData());
		}
		return r;
	}

	private void getFile(String path, String id) {
		ProjectPlan projectPlan = new ProjectPlan();

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

				projectPlan.setFileName(array[i].getName().substring(0, (array[i].getName().length() - 4)));
				System.out.println("fileName:" + projectPlan.getFileName());
				projectPlan.setFilePath(array[i].getPath());
				managerService.saveProjectPlan(projectPlan);
			} else if (array[i].isDirectory()) {
				getFile(array[i].getPath(), id);
			}
		}
	}

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径，从而截取想要的路径
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/getSoftProcessFilePath/{id}")
	public R getSoftProcessFilePath(@PathVariable("id") String id) {
		return new R<>(managerService.getSoftProcessFilePath(id));
	}

	/**
	 * 根据当前软硬件映射配置的id，查找当前流程下的所有模块的文件路径
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getFilePathListById/{id}")
	public R getworkSpacePathById(@PathVariable("id") String id, @RequestBody AppDataDTO appDataDTO) {
		return new R<>(managerService.getworkSpacePathById(id, appDataDTO));

	}


	// 调回写部署方案接口
	@PostMapping("/writeBackDeployScheme")
	public void writeBackDeploySchemeById(@RequestBody Map<String, Object> map) {
		managerService.writeBackDeploySchemeById(map);

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

	@PostMapping("deleteSelectFile")
	public boolean deleteSelectFile(@RequestBody Map filePath) {
		boolean deleteFlag = managerService.deleteFilesFromLocal(filePath);
		return deleteFlag;
	}

	// 解析自定义配置xml
	@PutMapping
	@RequestMapping("/analysisThemeXML/{proDetailId}")
	public R analysisThemeXML(@PathVariable("proDetailId") String proDetailId) {
		return managerService.analysisThemeXML(proDetailId);
	}

	/*
	 * public static void main(String[] args) { getCmd(); }
	 */
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
	public int saveHarewarelibs(@RequestBody Hardwarelibs hardwarelibs) {
		System.out.println("hardwarelibs" + hardwarelibs);
		int row = managerService.saveHardwarelibs(hardwarelibs);
		return row;
	}

	/**
	 * 保存硬件建模芯片
	 *
	 * @param chipsfromhardwarelibs
	 */
	@PostMapping("saveChipsFromHardwarelibs")
	public int saveChipsFromHardwarelibs(@RequestBody Chipsfromhardwarelibs chipsfromhardwarelibs) {
		int row = managerService.saveChipsfromhardwarelibs(chipsfromhardwarelibs);
		return row;
	}

	@GetMapping("getChipsfromhardwarelibs/{id}")
	public Chipsfromhardwarelibs getChipsfromhardwarelibs(@PathVariable("id") String id) {
		long startTime = System.currentTimeMillis(); // 获取开始时间

		Chipsfromhardwarelibs chipsfromhardwarelibs = managerService.getChipsfromhardwarelibs(id);

		long endTime = System.currentTimeMillis(); // 获取结束时间

		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间

		return chipsfromhardwarelibs;
	}

    /**
    * @Description 部署图获取部件结构数据
    * @Author ZhangHongXu
     * @param id
    * @Return List
    * @Exception
    * @Date 2020/4/17 14:56
    */
	@GetMapping("getAllList/{id}")
	public List getAllList(@PathVariable("id") String id) {
		return managerService.getXmlFile(id);
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
	public int updateHardwarelib(@RequestBody Hardwarelibs hardwarelibs) {
		return managerService.updateHardwarelib(hardwarelibs);
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
	 * 
	 * @param id
	 */
	@DeleteMapping("deleteChipsFromHardwarelibs/{id}")
	public void deleteChipsFromHardwarelibs(@PathVariable String id) {
		managerService.deleteChipsFromHardwarelibs(id);
	}

	/**
	 * 流程建模完备性检查
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping
	@RequestMapping("/completeCheck/{id}/{userId}")
	public R completeCheck(@PathVariable("id") String id, @PathVariable("userId") String userId) {
		return managerService.completeCheck(id, userId);
	}

	/**
	 * 流程建模导出
	 */
	@PutMapping
	@RequestMapping("/exportFile/{id}")
	public void exportFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		try {
			StringRef sr = new StringRef();
			byte[] data = managerService.exportFile(id, sr);
			ProjectFile retFile = managerService.getById(id);
			for (int i = 0; i < 2; i++) {
				retFile = managerService.getById(retFile.getParentId());
			}
			String zipFileName = retFile.getFileName() + ".zip";
			response.reset();
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip",
					new String(zipFileName.getBytes("UTF-8"), "ISO-8859-1")));
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
		if (Objects.isNull(file) || file.isEmpty()) {
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
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				zipFileName = zipEntry.getName();
				if (zipFileName.split("\\.")[1].equals("json")) {
					bos = new ByteArrayOutputStream();
					int index = 0;
					byte[] jsonBytes = new byte[1024];
					while ((index = bs.read(jsonBytes)) != -1) {
						bos.write(jsonBytes, 0, index);
					}
					byte[] strBytes = bos.toByteArray();
					json = new String(strBytes, 0, strBytes.length, "utf-8");
					System.out.println(json);
					Object jsonStr = JSON.toJavaObject(JSONObject.parseObject(json), Object.class);
					retMap.put("json", jsonStr);
				} else {
					bos = new ByteArrayOutputStream();
					int bytesRead = 0;
					byte[] bytes = new byte[1024];
					while ((bytesRead = bs.read(bytes)) != -1) {
						bos.write(bytes, 0, bytesRead);
					}
					byte[] strByte = bos.toByteArray();
					// xmlData = new String(strByte,0,strByte .length ,"utf-8" );
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
	public R codeGeneration(@PathVariable("projectId") String projectId, @PathVariable("username") String username) {
		return managerService.codeGeneration(projectId, username);
	}

	@ResponseBody
	@PostMapping(path = "/getWorking/{flowName}/{id}", consumes = { "multipart/mixed", "multipart/form-data" })
	public void getWorking(@RequestParam(value = "file", required = false) MultipartFile file,
			@PathVariable("flowName") String flowName, @PathVariable("id") String id) {
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
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/getPlatformList")
	public R getPlatformList() {
		return managerService.getPlatformList();
	}

	@GetMapping("/deleteProcedureById/{procedureId}")
	public R deleteProcedureById(@PathVariable("procedureId") String procedureId) {
		return new R<>(managerService.deleteProcedureById(procedureId));
	}

	/**
	 * 获取修改BSP下拉列表内容
	 * 
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/getBSPListAndPlatformName")
	public R getBSPListAndPlatformName() {
		return managerService.getBSPListAndPlatformName();
	}

	/**
	 * 回显修改BSP选择的值
	 *
	 * @param id
	 * @return
	 */
	@ResponseBody
	@GetMapping("/showPartBSPAndPlatform/{id}")
	public R showPartBSPAndPlatform(@PathVariable("id") String id) {
		return managerService.showPartBSPAndPlatform(id);
	}

	@PutMapping("/updatePartBSPAndPlatform")
	public R updatePartBSPAndPlatform(@RequestBody BSP bsp) {
		return new R<>(managerService.updatePartBSPAndPlatform(bsp));
	}

	/**
	 * 项目流程导出
	 * 
	 * @param request
	 * @param response
	 * @param params
	 */
	@PostMapping("/createZipFile")
	public void createZipFile(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> params) {
		try {
			String projectId = (String) params.get("projectId");
			String processId = (String) params.get("processId");

			byte[] data = managerService.createZip(projectId, processId);

			String zipFileName = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".zip";
			response.reset();
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", zipFileName));
			response.setHeader("FileName", zipFileName);
			response.setHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream; charset=UTF-8");

			IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: importProjectZipUpload
	 * @Description: 单文件上传
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:41:10
	 * @param ufile
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/importProjectZipUpload", consumes = { "multipart/mixed", "multipart/form-data" })
	public R importProjectZipUpload(@RequestParam(value = "file", required = false) MultipartFile ufile,
			@RequestParam(value = "projectId", required = false) String projectId) {
		return new R<>(managerService.analysisZipFile(ufile, projectId));
	}

	@ResponseBody
	@PostMapping(path = "/getFilePath")
	public String getprocessFile(@RequestParam(value = "projectId", required = false) String projectId) {
		return managerService.getprocessFile(projectId);
	}

}
