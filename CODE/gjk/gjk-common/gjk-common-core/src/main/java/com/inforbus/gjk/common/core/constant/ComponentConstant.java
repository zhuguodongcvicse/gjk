package com.inforbus.gjk.common.core.constant;
/**
 * @ClassName: ComponentConstant
 * @Desc 构件的常量
 * @Author xiaohe
 * @DateTime 2020年5月6日  
 */
public interface ComponentConstant {
	/**
	  * @Fields COMP_TEST : 测试文件
	  */
	String COMP_TEST = "testfile";
	/**
	  * @Fields COMP_ALGORITHM : 算法文件
	  */
	String COMP_ALGORITHM = "algorithmfile";
	/**
	  * @Fields COMP_PLATFORM : 平台文件
	  */
	String COMP_PLATFORM = "platformfile";
	/**
	  * @Fields COMP_IMG : 图标文件
	  */
	String COMP_IMG = "imgfile";
	/**
	  * @Fields COMP : 构件token
	  */
	String COMP = "component";
	/**
	  * @Fields COMP_XML : 构件文件类型
	  */
	String COMP_XML = ".XML";
	
	/**
	 * APP运行与控制
	 */
	String APP_RUN = "app";
	
	/**
	 * 软件框架
	 */
	String SOFTWARE = "software";
	
	/**
	 * 软件框架库
	 */
	String SOFTWARE_DEPOT = "软件框架库";
	
	/**
	 * bsp
	 */
	String BSP = "bsp";
	
	/**
	 * bsp库
	 */
	String BSP_DEPOT = "BSP库";
	
	/**
	 * 构建
	 */
	String COMPONENT = "component";
	
	/**
	 * 构建
	 */
	String COMPONENT_DEPOT = "构件库";
	
	/**
	 *  组件划分方案路径（自存自取）
	 */
	String GENERATECODERESULT_XML = "组件划分方案.xml";
	
	/**
	 *  packinfo文件路径（客户自存自取的路径）
	 */
	String PACKINFO_XML = "packinfo.xml";
	
	/**
	 * 构件有新版本(已更新)
	 */
	String COMP_NEWVERSION = "0";
	
	/**
	 * 构件无新版本(未更新)
	 */
	String COMP_NO_NEWVERSION = "1";
}
