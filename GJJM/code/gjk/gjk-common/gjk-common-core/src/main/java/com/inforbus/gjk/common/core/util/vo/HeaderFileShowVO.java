package com.inforbus.gjk.common.core.util.vo;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;

import lombok.Data;

/**
 * 
 * @ClassName: HeaderFileShowVO
 * @Description: 
 * @Author CVICSE-COMMON
 * @DateTime 2019年5月11日 下午5:24:40
 */
@Data
public class HeaderFileShowVO {
	
	//输入参数标签
	private XmlEntity inputXmlParams = new XmlEntity();
	
	//输出参数标签
	private XmlEntity outputXmlParams = new XmlEntity();
	
	//输入参数标签
	private XmlEntityMap inputXmlMapParams = new XmlEntityMap();
	
	//输出参数标签
	private XmlEntityMap outputXmlMapParams = new XmlEntityMap();
	
	//输入参数
	private List<ParamTreeVO> inputParams = Lists.newArrayList();
	
	//输出参数
	private List<ParamTreeVO> outputParams = Lists.newArrayList();
	
	//结构体
	private Map<String,ParamTreeVO>  structParams = Maps.newHashMap();

}
