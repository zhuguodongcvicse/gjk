package com.inforbus.gjk.comp.service;

import java.util.Map;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;

/**
 * 头文件解析处理服务接口
 * 
 * @author hu
 *
 */
public interface HeaderFileDispService {

	/**
	 * 结构体文件解析
	 * 
	 * @Title: parseStruct
	 * @Description: 结构体文件解析
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月7日 下午7:07:42
	 * @param filePath 结构体文件路径
	 * @return 解析结果
	 */
	public Map<String, ParamTreeVO> parseStruct(String filePath);

	/**
	 * @Title: parseHeaderFile
	 * @Description:头文件解析
	 * @Author hu_qinhua
	 * @DateTime 2019年5月6日 下午7:16:48
	 * @param headerFile
	 * @return R<?> 
	 * 
	 * 		2020年5月7日14:04:30 更改返回值类型 xiaohe 和参数传值方式
	 */
	public R<?> parseHeaderFile(String headerFile);

	/**
	 * 解析性能测试表
	 * 
	 * @Title: parsePerformanceTable
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月3日 上午10:44:28
	 * @param excelPath
	 * @return 2020年5月7日14:04:51 更改返回值类型 xiaohe
	 */
	public R<?> parsePerformanceTable(String excelPath);

}
