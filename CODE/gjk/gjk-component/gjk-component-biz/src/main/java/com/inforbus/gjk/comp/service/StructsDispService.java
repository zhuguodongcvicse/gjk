package com.inforbus.gjk.comp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;
import com.inforbus.gjk.comp.api.vo.ParamsVO;


/**
 * 结构体处理服务
 * @author CVICSE-COMMON
 *
 */
public interface StructsDispService  {
	
	/**
	 * 结构体文件解析
	 * @Title: parseStruct
	 * @Description: 结构体文件解析
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月7日 下午7:07:42
	 * @param filePath 结构体文件路径
	 * @return 解析结果
	 */
	public Map<String,ParamTreeVO> parseStruct(String filePath);
	
	/**
	 * 获取结构体名称集合
	 * @Title: getAllStructNames
	 * @Description: 获取结构体名称集合
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月7日 下午7:02:01
	 * @return 结构体名称集合
	 */
	public Set<String> getAllStructTypes(String groupType);
	
	/**
	 * 获取指定名称的结构体明细元素
	 * @Title: getStructByName
	 * @Description: 获取指定名称的结构体明细元素
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月7日 下午7:30:46
	 * @param structName 结构体名称
	 * @return 
	 */
	public ParamTreeVO getStructByType(String structName);

}
