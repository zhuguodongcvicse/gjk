package com.inforbus.gjk.common.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.appinter.appInterface;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;

import SpbModel.HeadFile;
import SpbModel.Performance;
import SpbModel.Struc;
import appcontrol.AppControl;
import flowModel.SimpleScheme;
import flowModel.SpbFrameAutoGen;
import flowModel.SystemConfig;
import flowModel.SystemPara;
import flowModel.TopicConfig;
import flowModel.cmpProCreate;



/**
 * 外部接口转换工具类
 *
 * @author hu
 *
 */
public class ExternalIOTransUtils {

	/**
	 * 构件头文件解析
	 *
	 * @param headerPath 头文件
	 * @return 输出对象
	 */
	public static HeaderFileTransVO parseHeaderFile(String headerPath) {
		HeaderFileTransVO headerFileTransVO = new HeaderFileTransVO();
		HeadFile.parseHeaderFile(headerPath, headerFileTransVO.getInputParaNameToType(),
				headerFileTransVO.getOutputParaNameToType(), headerFileTransVO.getStructTypeToMember(),
				headerFileTransVO.getIndexToParaName());
		return headerFileTransVO;
	}

	/**
	 * 解析性能测试表
	 *
	 * @Title: parsePerformanceTable
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月3日 上午10:33:54
	 * @param excelPath 性能测试表路径
	 * @return 通过率
	 */
	public static Map<Integer, Float> parsePerformanceTable(String excelPath) {
		Map<Integer, Float> performanceTable = Maps.newHashMap();
		System.out.println("excelPath" + excelPath);
		try {
			Performance.parsePerformanceTable(excelPath, performanceTable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return performanceTable;
	}

	/**
	 * 软硬件映射的接口，生成缩略方案
	 *
	 * @param schemeFileList
	 */
	public static void simplePlan(ArrayList<String> schemeFileList, String simplePlanFile) {
//        String simplePlanFile = "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\方案展示.xml";
//        ArrayList<String> schemeFileList = new ArrayList<>();
//        schemeFileList.add("D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\方案评价指标.xml");
//        schemeFileList.add("D:\\14S_GJK_GIT\\gjk\\gjk\\project\\gengTest\\geng流程\\模型\\软硬件映射配置.xml");
		try {
			SimpleScheme.createSimpleScheme(schemeFileList, simplePlanFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析结构体（通用：包括系统表、内部表等）
	 *
	 * @param filePath 结构体文件路径
	 * @return <结构体类型名，list<变量类型+空格+变量名>>
	 */
	public static Map<String, List<String>> parseStruct(String filePath) {
		Map<String, List<String>> structTypeToMember = Maps.newHashMap();
		Struc.parseStruct(filePath, structTypeToMember);
		return structTypeToMember;
	}

	public static Map<String, List<String>> getCmpSysConfig(String netWorkConfigFileName, String packinfoPath,
			String workModeFileName) {

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		// 调用客户api
		SystemConfig.getCmpSysConfig(netWorkConfigFileName, packinfoPath, workModeFileName, map);

		return map;
	}

	/**
	 * 自定义配置调用接口
	 */
	public static void createUserDefineTopic(String flowFilePath, String ThemeFilePath, String filePath) {
		try {
			TopicConfig.createUserDefineTopic(flowFilePath, ThemeFilePath, filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public static boolean appInstall(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
			String packinfoPath, String cmpResFilePath, String appProPath) {
//		AppControl appControl = new AppControl();
////		appControl.appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath, appProPath);
//		return appControl.appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath,
//				appProPath);
		return AppControl.getAppControl().appInstall(cmpNameToHwType, userName, appID, appName, packinfoPath, cmpResFilePath, appProPath);
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

	public static boolean appLoad(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
			boolean existDeployConfig, String sysconfigPath, String appProPath) {
//		AppControl appControl = new AppControl();
//		return appControl.appLoad(cmpNameToHwType, userName, appID, appName, existDeployConfig, sysconfigPath,
//				appProPath);
		return AppControl.getAppControl().appLoad(cmpNameToHwType, userName, appID, appName, existDeployConfig, sysconfigPath, appProPath);

	}

	/**
	 * 卸载
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	public static boolean appUnload(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
//		AppControl appControl = new AppControl();
//		return appControl.appUnload(cmpNameToHwType, userName, appID, appName);
		return AppControl.getAppControl().appUnload(cmpNameToHwType, userName, appID, appName);
	}

	/**
	 * 启动
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	public static boolean appRestart(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
//		AppControl appControl = new AppControl();
//		return appControl.appRestart(cmpNameToHwType, userName, appID, appName);
		return AppControl.getAppControl().appRestart(cmpNameToHwType, userName, appID, appName);
	}

	/**
	 * 停止
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	public static boolean appStop(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
//		AppControl appControl = new AppControl();
//		return appControl.appStop(cmpNameToHwType, userName, appID, appName);
		return AppControl.getAppControl().appStop(cmpNameToHwType, userName, appID, appName);
	}

	/**
	 * 暂停
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return
	 */
	public static boolean appPause(Map<String, String> cmpNameToHwType, String userName, int appID, String appName) {
//		AppControl appControl = new AppControl();
//		return appControl.appPause(cmpNameToHwType, userName, appID, appName);
		return AppControl.getAppControl().appPause(cmpNameToHwType, userName, appID, appName);
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
	public static boolean appUnInstall(Map<String, String> cmpNameToHwType, String userName, int appID, String appName,
			String packinfoPath) {
//		AppControl appControl = new AppControl();
//		return appControl.appUnInstall(cmpNameToHwType, userName, appID, appName, packinfoPath);
		return AppControl.getAppControl().appUnInstall(cmpNameToHwType, userName, appID, appName, packinfoPath);
	}

	/**
	 * 导出
	 * 
	 * @param workID
	 * @param taskInfoPath
	 * @param appPath
	 * @param sysconfigPath
	 * @param packinfoPath
	 * @param cmpDeployPlanFilePath
	 */
//	public static void appTaskExport(int workID, String taskInfoPath, String appPath, String sysconfigPath,
//			String packinfoPath, String cmpDeployPlanFilePath) {
//		appInterface.appTaskExport(workID, taskInfoPath, appPath, sysconfigPath, packinfoPath, cmpDeployPlanFilePath);
//	}
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
	public static void appTaskExport(String userName, int appId, String appName, String appPath, String sysconfigPath,
			String packinfoPath, String cmpDeployPlanFilePath) {
		appInterface.appTaskExport(appId, appName, appPath, sysconfigPath, packinfoPath, cmpDeployPlanFilePath);
	}

	/**
	 * 解析系统参数波形
	 */
	public static void parseSystemPara(String flowName, String str, String xmlPath) {
		try {
			SystemPara.parseSystempPara(flowName, str, xmlPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void modifySpbInclude(List<String> compFuncNameList, String cmpAppFilePath) throws IOException {
		cmpProCreate.modifySpbInclude(compFuncNameList, cmpAppFilePath);
	}

	/**
	 * @Title: createSpbFrameFile
	 * @Description: 生成构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月13日 下午2:53:28
	 * @param spbModelXmlFile 构件模型XML文件
	 * @param spbModelDir     保存路径(可以是平台文件路径)
	 */
	public static void createSpbFrameFile(String spbModelXmlFile, String spbModelDir) {
		new SpbFrameAutoGen().createSpbFrameFile(spbModelXmlFile, spbModelDir);
//		new SpbFrameAutoGen().createSpbFrameFile(spbModelXmlFile, headerTemplateFile, srcTemplateFile, saveDir);
	}
}
