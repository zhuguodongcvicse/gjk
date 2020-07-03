package com.inforbus.gjk.dataCenter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.dataCenter.service.ExternalInfService;

import flowModel.BackNodeInfoForSpb;
import flowModel.CheckResult;
import flowModel.SimpleScheme;

/**
 * @program: gjk
 * @description: 第三方接口
 * @author: SunChao
 * @create: 2020-04-17 14:16
 **/
@RestController
@RequestMapping("/ExternalInfServer")
public class ExternalInfController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private ExternalInfService externalInfService;

	@RequestMapping("/getCmpSysConfig")
	public R<Map<String, List<String>>> getCmpSysConfig(@RequestParam("netWorkConfigFileName") String netWorkConfigFileName, @RequestParam("packinfoPath") String packinfoPath,
														@RequestParam("workModeFileName") String workModeFileName) {
		Map<String, List<String>> cmpSysConfig = externalInfService.getCmpSysConfig(netWorkConfigFileName, packinfoPath,
				workModeFileName);
		return new R<>(cmpSysConfig);
	}

	@RequestMapping("/createUserDefineTopic")
	public R<Boolean> createUserDefineTopic(String flowFilePath, String ThemeFilePath, String filePath, String newFolder) {
		R<Boolean> r = new R();
		boolean flag = false;
		try {
			externalInfService.createUserDefineTopic(flowFilePath, ThemeFilePath, filePath,newFolder);
			flag = true;
			r.setData(flag);
			r.setMsg("UserDefineTopicFile.xml生成成功");
		} catch (Exception e) {
			r.setData(flag);
			r.setMsg("UserDefineTopicFile.xml文件生成失败！");
			logger.error("UserDefineTopicFile.xml文件生成失败！");
		}
		return r;
	}

	/**
	 * 注册
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @param packinfoPath    客户自存自取路径
	 * @param cmpResFilePath  客户自存自取路径
	 * @param appProPath      APP工程文件夹路径
	 * @return
	 */
	@PostMapping("/appInstall")
	public boolean appInstall(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID,@RequestParam("appName") String appName,
			@RequestParam("packinfoPath") String packinfoPath, @RequestParam("cmpResFilePath") String cmpResFilePath, @RequestParam("appProPath") String appProPath) {
		return externalInfService.appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath,
				appProPath);
	}

	/**
	 * 加载、更新加载
	 *
	 * @param cmpNameToHwType   APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID             APP对应的流程ID
	 * @param appName           APP名称
	 * @param existDeployConfig APP组件工程配置
	 * @param sysconfigPath     系统配置模块XML路径
	 * @param appProPath        APP工程文件夹路径
	 * @return
	 */
	@PostMapping("/appLoad")
	public boolean appLoad(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName,
			@RequestParam("existDeployConfig") boolean existDeployConfig, @RequestParam("sysconfigPath") String sysconfigPath, @RequestParam("appProPath") String appProPath) {
		return externalInfService.appLoad(cmpNameToHwType, userName, appID, appName, existDeployConfig, sysconfigPath,
				appProPath);

	}

	/**
	 * 注销
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @param packinfoPath    客户自存自取路径
	 * @return
	 */
	@PostMapping("/appUnInstall")
	public boolean appUnInstall(@RequestBody Map<String, String> cmpNameToHwType,
			@RequestParam("userName") String userName, @RequestParam("appID") int appID,
			@RequestParam("appName") String appName, @RequestParam("packinfoPath") String packinfoPath) {
		return externalInfService.appUnInstall(cmpNameToHwType, userName, appID, appName, packinfoPath);
	}

	/**
	 * 导出
	 *
	 * @param appId                 APP对应的流程ID
	 * @param appName               APP名称
	 * @param appPath               APP工程文件夹路径
	 * @param sysconfigPath         系统配置模块XML路径
	 * @param packinfoPath          客户自存自取路径
	 * @param cmpDeployPlanFilePath 客户自存自取路径
	 */
	@PostMapping("/appTaskExport")
	public void appTaskExport(@RequestParam("userName") String userName, @RequestParam("appId") int appId,
			@RequestParam("appName") String appName, @RequestParam("appPath") String appPath,
			@RequestParam("sysconfigPath") String sysconfigPath, @RequestParam("packinfoPath") String packinfoPath,
			@RequestParam("cmpDeployPlanFilePath") String cmpDeployPlanFilePath) {
		externalInfService.appTaskExport(userName, appId, appName, appPath, sysconfigPath, packinfoPath,
				cmpDeployPlanFilePath);
	}
	
	/**
     * 软硬件映射的接口，生成缩略方案
     *
     * @param schemeFileList
     */
	@RequestMapping("/simplePlan")
    public void simplePlan(@RequestParam("str") String[] str,@RequestParam("simplePlanFile")  String simplePlanFile) {
        try {
        	//调用feign接口，不能传泛型，需要转换为数组
        	ArrayList<String> schemeFileList = new ArrayList<String>();
        	Collections.addAll(schemeFileList, str);
        	//调用客户第三方生成缩略方案的接口
            SimpleScheme.createSimpleScheme(schemeFileList, simplePlanFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	 /**
     * 调回写部署方案接口
     *
     * @param schemeFileList
     */
	@RequestMapping("/writeBackDeployScheme")
    public void writeBackDeployScheme(@RequestParam("workModeFilePath") String workModeFilePath, @RequestParam("schemeFile") String schemeFile ) {
        try {
            BackNodeInfoForSpb.writeBackDeployScheme(workModeFilePath, schemeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	/**
	 * @Title: getHeader
	 * @Desc 解析头文件
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param maps
	 * 
	 * @return
	 */
	@PostMapping("/parseHeaderFile")
	public R<HeaderFileTransVO> getHeader(@RequestParam("path") String path) {
		return new R<>(externalInfService.parseHeaderFile(path));
	}

	/**
	 * @Title: getPerformanceTable
	 * @Desc 解析性能测试表
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param maps key is excelPath
	 * @return
	 */
	@PostMapping("/parsePerformanceTable")
	public R<?> getPerformanceTable(@RequestParam("excelPath") String excelPath) {
		return new R<>(externalInfService.parsePerformanceTable(excelPath));
	}

	/**
	 * @Title: parseStruct
	 * @Desc 解析结构体（通用：包括系统表、内部表等）
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param filePath 文件路径
	 * @return R<?> <结构体类型名，list<变量类型+空格+变量名>>
	 */
	@PostMapping("/parseStruct")
	public R<?> parseStruct(@RequestParam("filePath") String filePath) {
		return new R<>(externalInfService.parseStruct(filePath));
	}
	
	/**
	 * 完备性检查
	 * @param xmlFilepath 流程模型文件路径
	 * @return
	 */
	@RequestMapping("/completeCheck")
	public R<CheckResult> completeCheck(@RequestParam("xmlFilepath") String xmlFilepath){
		R<CheckResult> r = new R();
		try {
			CheckResult cr = externalInfService.completeCheck(xmlFilepath);
			r.setData(cr);
		} catch (Exception e) {
			logger.error("完备性检查completeCheck接口调用失败");
		}
		return r;
	}

    /**
     * 软硬件映射调用c接口
     *
     * @param exe
     * @param hardWareFilePath 硬件模型文件
     * @param mapConfigPath    软硬件映射配置文件
     * @param sysParamFilePath 系统参数文件
     * @param userName         用户名（登录）
     * @return
     * @throws IOException
     */
    @PutMapping("/mapSoftToHard")
    public void mapSoftToHard(@RequestParam("exe") String exe,
                              @RequestParam("hardWareFilePath") String hardWareFilePath,
                              @RequestParam("mapConfigPath") String mapConfigPath,
                              @RequestParam("sysParamFilePath") String sysParamFilePath, @RequestParam("userName") String userName) {
        externalInfService.mapSoftToHard(exe,hardWareFilePath,mapConfigPath,sysParamFilePath,userName);
    }
}
