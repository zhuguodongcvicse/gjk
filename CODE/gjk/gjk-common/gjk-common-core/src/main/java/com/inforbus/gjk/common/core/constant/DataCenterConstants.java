package com.inforbus.gjk.common.core.constant;

import java.io.File;

/**
 * 数据中心常量类
 * 
 * @author dong
 *
 */
public interface DataCenterConstants {

	// 构件
	String COMPONENT = "1";

	// 项目
	String PROJECT = "2";
	/**
	 * @Fields FILE_DIRS_UPLOAD : 上传临时文件路径
	 */
	String FILE_DIRS_UPLOAD = "gjk" + File.separator + "upload";
	/**
	 * @Fields FILE_DIRS_IMAGE : 上传临时图片文件路径
	 */
	String FILE_DIRS_IMAGE = "gjk" + File.separator + "image";

	/**
	 * @Fields FILE_DIRS_COMP : 上传时构件 用户构件文件特定
	 */
	String FILE_DIRS_COMPUSERFILEPATH = "gjk" + File.separator + "component";
	
	//BSP库存储路径
	String BSP_FILEPATH = "gjk"+ File.separator + "bsp" + File.separator;
	
	//软件框架库库存储路径
	String SOFTWARE_FILEPATH = "gjk"+ File.separator + "software" + File.separator;
}
