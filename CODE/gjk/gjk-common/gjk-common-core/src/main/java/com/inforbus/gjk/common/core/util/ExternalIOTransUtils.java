package com.inforbus.gjk.common.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.inforbus.gjk.common.core.appinter.appInterface;
import com.inforbus.gjk.common.core.util.vo.*;

import customtoconfig.TopicConfig;
import headerfile.HeadFile;
import headerfile.Performance;
import mapping.SimpleScheme;
import systemconfiguration.SystemConfig;

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
		headerfile.Struc.parseStruct(filePath, structTypeToMember);
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
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @param packinfoPath
	 * @param workModeProPath
	 * @return
	 */
	public static boolean appInstall(Map<String, String> cmpNameToHwType, int workID, String workname,
			String packinfoPath, String workModeProPath) {
		return appInterface.appInstall(cmpNameToHwType, workID, workname, packinfoPath, workModeProPath);
	}

	/**
	 * 加载、更新加载
	 * 
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @param existDeployConfig
	 * @param sysconfigPath
	 * @return
	 */
	public static boolean appLoadStart(Map<String, String> cmpNameToHwType, int workID, String workname,
			boolean existDeployConfig, String sysconfigPath) {
		return appInterface.appLoadStart(cmpNameToHwType, workID, workname, existDeployConfig, sysconfigPath);
	}

	/**
	 * 卸载
	 * 
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @return
	 */
	public static boolean appUnload(Map<String, String> cmpNameToHwType, int workID, String workname) {

		return appInterface.appUnload(cmpNameToHwType, workID, workname);
	}

	/**
	 * 启动
	 * 
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @return
	 */
	public static boolean appTaskRestart(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return appInterface.appTaskRestart(cmpNameToHwType, workID, workname);
	}

	/**
	 * 停止
	 * 
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @return
	 */
	public static boolean appStop(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return appInterface.appStop(cmpNameToHwType, workID, workname);
	}

	/**
	 * 暂停
	 * 
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @return
	 */
	public static boolean appPause(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return appInterface.appPause(cmpNameToHwType, workID, workname);
	}

	/**
	 * 注销
	 * 
	 * @param cmpNameToHwType
	 * @param workID
	 * @param workname
	 * @param packinfoPath
	 * @return
	 */
	public static boolean appDelete(Map<String, String> cmpNameToHwType, int workID, String workname,
			String packinfoPath) {
		return appInterface.appDelete(cmpNameToHwType, workID, workname, packinfoPath);
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
	public static void appTaskExport(int workID, String taskInfoPath, String appPath, String sysconfigPath,
			String packinfoPath, String cmpDeployPlanFilePath) {
		appInterface.appTaskExport(workID, taskInfoPath, appPath, sysconfigPath, packinfoPath, cmpDeployPlanFilePath);
	}

}
