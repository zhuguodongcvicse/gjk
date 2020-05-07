package com.inforbus.gjk.dataCenter.controller;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.dataCenter.service.ExternalInfService;

import appcontrol.AppControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
	public R<Map<String, List<String>>> getCmpSysConfig(String netWorkConfigFileName, String packinfoPath,
			String workModeFileName) {
		Map<String, List<String>> cmpSysConfig = externalInfService.getCmpSysConfig(netWorkConfigFileName, packinfoPath,
				workModeFileName);
		return new R<>(cmpSysConfig);
	}

	@RequestMapping("/createUserDefineTopic")
	public R<Boolean> createUserDefineTopic(String flowFilePath, String ThemeFilePath, String filePath) {
		R<Boolean> r = new R();
		boolean flag = false;
		try {
			externalInfService.createUserDefineTopic(flowFilePath, ThemeFilePath, filePath);
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
	@RequestMapping("/appInstall")
	public boolean appInstall(Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID,@RequestParam("appName") String appName,
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
	@RequestMapping("/appLoad")
	public boolean appLoad(Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName,
			@RequestParam("existDeployConfig") boolean existDeployConfig, @RequestParam("sysconfigPath") String sysconfigPath, @RequestParam("appProPath") String appProPath) {
		return externalInfService.appLoad(cmpNameToHwType, userName, appID, appName, existDeployConfig, sysconfigPath,
				appProPath);

	}

	/**
	 * 卸载
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	@RequestMapping("/appUnload")
	public boolean appUnload(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName) {
		return externalInfService.appUnload(cmpNameToHwType, userName, appID, appName);
	}

	/**
	 * 启动
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	@RequestMapping("/appRestart")
	public boolean appRestart(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName) {
		return externalInfService.appRestart(cmpNameToHwType, userName, appID, appName);
	}

	/**
	 * 停止
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	@RequestMapping("/appStop")
	public boolean appStop(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName) {
		return externalInfService.appStop(cmpNameToHwType, userName, appID, appName);
	}

	/**
	 * 暂停
	 *
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	@RequestMapping("/appPause")
	public boolean appPause(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName) {
		return externalInfService.appPause(cmpNameToHwType, userName, appID, appName);
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
	@RequestMapping("/appUnInstall")
	public boolean appUnInstall(@RequestBody Map<String, String> cmpNameToHwType, @RequestParam("userName") String userName, @RequestParam("appID") int appID, @RequestParam("appName") String appName,
			@RequestParam("packinfoPath") String packinfoPath) {
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
	@RequestMapping("/appTaskExport")
	public void appTaskExport( @RequestParam("userName") String userName, @RequestParam("appId") int appId, @RequestParam("appName") String appName, @RequestParam("appPath") String appPath, @RequestParam("sysconfigPath") String sysconfigPath,
			@RequestParam("packinfoPath") String packinfoPath, @RequestParam("cmpDeployPlanFilePath") String cmpDeployPlanFilePath) {
		externalInfService.appTaskExport(userName, appId, appName, appPath, sysconfigPath, packinfoPath,
				cmpDeployPlanFilePath);
	}

}
