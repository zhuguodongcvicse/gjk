package com.inforbus.gjk.common.core.appinter;

import java.util.Map;

public interface appInterface {
	// 注册
	public static boolean appInstall(Map<String, String> cmpNameToHwType, int workID, String workname,
			String packinfoPath, String workModeProPath) {
		return true;
	}

	// 加载、更新加载
	public static boolean appLoadStart(Map<String, String> cmpNameToHwType, int workID, String workname,
			boolean existDeployConfig, String sysconfigPath) {
		return true;
	}

	// 卸载
	public static boolean appUnload(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return true;
	}

	// 启动
	public static boolean appTaskRestart(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return true;
	}

	// 停止
	public static boolean appStop(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return true;
	}

	// 暂停
	public static boolean appPause(Map<String, String> cmpNameToHwType, int workID, String workname) {
		return true;
	}

	// 注销
	public static boolean appDelete(Map<String, String> cmpNameToHwType, int workID, String workname,
			String packinfoPath) {
		return true;
	}

	// 导出
	public static void appTaskExport(int workID, String taskInfoPath, String appPath, String sysconfigPath,
			String packinfoPath, String cmpDeployPlanFilePath) {
	}

}
