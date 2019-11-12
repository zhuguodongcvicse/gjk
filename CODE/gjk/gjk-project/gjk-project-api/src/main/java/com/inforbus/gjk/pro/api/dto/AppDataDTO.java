package com.inforbus.gjk.pro.api.dto;

import java.util.Map;

import lombok.Data;

@Data
public class AppDataDTO {

	/**
	 * <组件名称，执行类型（平台）>
	 */
//	private Map<String,String> cmpNameToHwType;
	private String cmpNameToHwType;
	
	/**
	 *工作模式标识id
	 */
	private int flowId;
	/**
	 * APP名
	 */
	private String appName;
	/**
	 * packinfo文件路径
	 */
	private String packinfoPath;
	/**
	 * 工程文件夹路径
	 */
	private String workModeProPath;
	/**
	 * 是否带部署方案
	 */
	private String existDeployConfig;
	/**
	 * sysconfig文件路径（系统配置）
	 */
	private String sysconfigPath;
	/**
	 * 导出路径
	 */
	private String taskInfoPath;
	/**
	 * app工程路径
	 */
	private String appProPath;
	
	/**
	 * 组件划分方案路径
	 */
	private String cmpDeployPlanFilePath;
	/**
	 * 用户名
	 */
	private String userName;
}
