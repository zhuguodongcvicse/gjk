package com.inforbus.gjk.common.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.vo.HeaderFileShowVO;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;

public class HeaderFileAndStructUtils {

	/**
	 * 结构体map解析
	 * 
	 * @param structMap 原始类型结构体map
	 * @return
	 */
	public static Map<String, ParamTreeVO> convertStruct(Map<String, List<String>> structMap) {
		Map<String, ParamTreeVO> resStructMap = Maps.newHashMap();
		for (Map.Entry<String, List<String>> entry : structMap.entrySet()) {
			ParamTreeVO paramTreeVO = new ParamTreeVO(IdGenerate.millsAndRandomId(), IdGenerate.millsAndRandomId(), "0",
					"", "", entry.getKey(), 0, 0.0, "1", "");
			resStructMap.put(entry.getKey(), paramTreeVO);
		}

		for (Map.Entry<String, List<String>> entry : structMap.entrySet()) {
			mapToStruct(structMap, resStructMap, resStructMap.get(entry.getKey()));
		}

		return resStructMap;
	}

	/**
	 * 头文件解析
	 * 
	 * @Title: headFileToShow
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月31日 下午9:19:19
	 * @param headfile
	 * @return
	 */
	public static HeaderFileShowVO headFileToShow(String headfile) {
		HeaderFileShowVO headerFileShowVO = new HeaderFileShowVO();
		// 调用客户接口完成头文件解析
		HeaderFileTransVO headerFileTransVO = ExternalIOTransUtils.parseHeaderFile(headfile);
		if (null == headerFileTransVO) {
			return null;
		}
		// 结构体Map解析
		Map<String, List<String>> structTypeToMember = headerFileTransVO.getStructTypeToMember();
		headerFileShowVO.setStructParams(convertStruct(structTypeToMember));

		// 输入参数解析
		headerFileShowVO.getInputXmlParams().setXmlEntitys(new ArrayList<XmlEntity>());
		headerFileShowVO.getInputXmlParams().setLableName("输入");

		headerFileShowVO.getInputXmlMapParams().setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		headerFileShowVO.getInputXmlMapParams().setLableName("输入");

		paramMapToXml(headerFileShowVO.getInputXmlMapParams(), headerFileShowVO.getInputXmlParams(),
				headerFileShowVO.getInputParams(), headerFileTransVO.getIndexToParaName(),
				headerFileTransVO.getInputParaNameToType());

		// 输出参数解析
		headerFileShowVO.getOutputXmlParams().setXmlEntitys(new ArrayList<XmlEntity>());
		headerFileShowVO.getOutputXmlParams().setLableName("输出");

		headerFileShowVO.getOutputXmlMapParams().setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		headerFileShowVO.getOutputXmlMapParams().setLableName("输出");

		paramMapToXml(headerFileShowVO.getOutputXmlMapParams(), headerFileShowVO.getOutputXmlParams(),
				headerFileShowVO.getOutputParams(), headerFileTransVO.getIndexToParaName(),
				headerFileTransVO.getOutputParaNameToType());

		return headerFileShowVO;
	}

	/**
	 * 将参数字典转换为XML结构
	 * 
	 * @param indexToParaName 参数的index值
	 * @param paramNameToType 参数<名称,类型>字典
	 * @param lableName       标签名称
	 * @return
	 */
	private static void paramMapToXml(XmlEntityMap xmlEntityMap, XmlEntity xmlEntity, List<ParamTreeVO> paramList,
			Map<String, Integer> indexToParaName, Map<String, String> paramNameToType) {

		for (Map.Entry<String, String> entry : paramNameToType.entrySet()) {
			String paramName = entry.getKey();
			String strItem = entry.getValue()+"|测试结构体注释文本"+IdGenerate.millsAndRandomId();
//			String strItem = entry.getValue();
			Integer paramIndex = indexToParaName.get(paramName);
			StringBuilder sType = new StringBuilder();
			StringBuilder sRemarks = new StringBuilder();
			if (splitParamTypeAndName(strItem, sType, sRemarks)) {
				// 参数赋值
				ParamTreeVO paramTreeVO = new ParamTreeVO(IdGenerate.millsAndRandomId(), IdGenerate.millsAndRandomId(), "0",
						"", paramName,  sType.toString(), sRemarks.toString(), 0, 0.0, "1", "");
				paramTreeVO.setIndex(paramIndex);
				paramList.add(paramTreeVO);
				// XmlEntity
				XmlEntity varXmlEntity = ParamVOToXmlEntity(paramTreeVO);
				xmlEntity.getXmlEntitys().add(varXmlEntity);
				// XmlEntityMap
				XmlEntityMap varXmlEntityMap = ParamVOToXmlEntityMap(paramTreeVO);
				xmlEntityMap.getXmlEntityMaps().add(varXmlEntityMap);
			}
		}
	}

	/**
	 * map类型转为结构体类型
	 * 
	 * @Title: mapToStruct
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月1日 下午4:21:02
	 * @param structMap
	 * @param fatherParam
	 */
	private static void mapToStruct(Map<String, List<String>> oriMap, Map<String, ParamTreeVO> parseMap,
			ParamTreeVO fatherParam) {
		String structType = fatherParam.getFparamType();
		if (structType == "")
			return;
		// 防止结构体指针类型
		String structTypeNo = structType.replace("*", "");
		structTypeNo = structTypeNo.trim();
		if (!oriMap.containsKey(structTypeNo)) {
			return;
		}

		// 拆分数据类型和变量名，当变量类型不是基本类型，那么嵌套解析
		List<String> mapValue = oriMap.get(structTypeNo);
		for (int i = 0; i < mapValue.size(); i++) {
//			String strItem = mapValue.get(i);
			String strItem = new String(mapValue.get(i)+"|测试结构体成员注释文本"+IdGenerate.millsAndRandomId());
			StringBuilder sName = new StringBuilder();
			StringBuilder sType = new StringBuilder();
			StringBuilder sRemarks = new StringBuilder();
			if (splitParamTypeAndName(strItem, sName, sType, sRemarks)) {
				ParamTreeVO structChild = new ParamTreeVO(IdGenerate.millsAndRandomId(), IdGenerate.millsAndRandomId(),
						fatherParam.getDbId(), "", sName.toString(), sType.toString(), sRemarks.toString(), i + 1, 0.0,
						"1", "");
				// AssigParamName全名设置
				String qz = "";
				if (fatherParam.getParentId() != "0") {
					// String fg = structChild.getFparamType().contains("*") ? "->" : ".";
					String fg = structType.contains("*") ? "->" : ".";
					qz = fatherParam.getAssigParamName() + fg + structChild.getFparamName();
				} else {
					qz = structChild.getFparamName();
				}
				structChild.setAssigParamName(qz);
				// 判断为基本类型还是结构体类型
				String typeStrNo = sType.toString().replace("*", "");
				typeStrNo = typeStrNo.trim();
				if (oriMap.containsKey(typeStrNo) && parseMap.containsKey(typeStrNo)) {
					structChild.setChildrenIds(parseMap.get(typeStrNo).getDbId());
					mapToStruct(oriMap, parseMap, structChild);
				}
				fatherParam.getChildren().add(structChild);
			}
//			String typeStr, nameStr;
//			StringRef pType = new StringRef();
//			StringRef pName = new StringRef();
//			if (splitParamTypeAndName(strItem, pType, pName)) {
//				typeStr = pType.getVal();
//				nameStr = pName.getVal();
//				ParamTreeVO structChild = new ParamTreeVO(IdGenerate.millsAndRandomId(), IdGenerate.millsAndRandomId(),
//						fatherParam.getDbId(), "", nameStr, typeStr, i + 1, 0.0, "1", "");
//				// AssigParamName全名设置
//				String qz = "";
//				if (fatherParam.getParentId() != "0") {
//					// String fg = structChild.getFparamType().contains("*") ? "->" : ".";
//					String fg = structType.contains("*") ? "->" : ".";
//					qz = fatherParam.getAssigParamName() + fg + structChild.getFparamName();
//				} else {
//					qz = structChild.getFparamName();
//				}
//				structChild.setAssigParamName(qz);
//				// 判断为基本类型还是结构体类型
//				String typeStrNo = typeStr.replace("*", "");
//				typeStrNo = typeStrNo.trim();
//				if (oriMap.containsKey(typeStrNo) && parseMap.containsKey(typeStrNo)) {
//					structChild.setChildrenIds(parseMap.get(typeStrNo).getDbId());
//					mapToStruct(oriMap, parseMap, structChild);
//				}
//				fatherParam.getChildren().add(structChild);
//			}

		}

	}

	/**
	 * 参数的类型与名称拆分，用于结构体和头文件解析
	 * 
	 * @Title: splitParamTypeAndName
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月13日 下午3:26:05
	 * @param strItem
	 * @param pType
	 * @param pName
	 * @return
	 */
	private static boolean splitParamTypeAndName(String strItem, StringRef pType, StringRef pName) {
		boolean res;
		String type = "";
		String name = "";
		String[] strArray = strItem.split("\\|");
		if (strArray.length >= 2) {
			name = strArray[strArray.length - 1];
			type = strItem.substring(0, strItem.length() - name.length() - 1);
			pType.setVal(type);
			pName.setVal(name);
			res = true;
		} else {
			res = false;
		}
		return res;
	}

	/**
	 * 参数的类型与名称拆分，用于结构体和头文件解析
	 * 
	 * @param strItem
	 * @param params
	 * @return
	 */
	private static boolean splitParamTypeAndName(String strItem, StringBuilder... params) {
		String[] strArray = strItem.split("\\|");
		for (int i = 0; i < strArray.length; i++) {
			if (i < params.length) {
				params[i].append(strArray[i]);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 属性结构转为XmlEntity结构
	 * 
	 * @Title: ParamVOToXmlEntity
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月1日 下午4:19:32
	 * @param vo
	 * @return
	 */
	private static XmlEntity ParamVOToXmlEntity(ParamTreeVO vo) {
		// variable
		XmlEntity varXmlEntity = new XmlEntity();
		varXmlEntity.setLableName("variable");
		varXmlEntity.setAttributeName("name");
		varXmlEntity.setAttributeNameValue(vo.getFparamName());
		varXmlEntity.setAttributeStructTypeName("structType");
		varXmlEntity.setAttributeStructTypeValue("");// ?
		varXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());

		// 变量类型
		XmlEntity dataTyeXmlEntity = new XmlEntity();
		dataTyeXmlEntity.setLableName("变量类型");
		dataTyeXmlEntity.setAttributeName("name");
		dataTyeXmlEntity.setAttributeNameValue(vo.getFparamType());
		dataTyeXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());
		varXmlEntity.getXmlEntitys().add(dataTyeXmlEntity);

		// 序号
		XmlEntity indexXmlEntity = new XmlEntity();
		indexXmlEntity.setLableName("序号");
		indexXmlEntity.setAttributeName("name");
		indexXmlEntity.setAttributeNameValue(vo.getIndex().toString());
		indexXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());
		varXmlEntity.getXmlEntitys().add(indexXmlEntity);

		// 长度
		XmlEntity lengthXmlEntity = new XmlEntity();
		lengthXmlEntity.setLableName("长度");
		lengthXmlEntity.setAttributeName("name");
		lengthXmlEntity.setAttributeNameValue(vo.getLength());
		lengthXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());
		varXmlEntity.getXmlEntitys().add(lengthXmlEntity);

		// 类别
		XmlEntity assignTypeXmlEntity = new XmlEntity();
		assignTypeXmlEntity.setLableName("类别");
		assignTypeXmlEntity.setAttributeName("name");
		assignTypeXmlEntity.setAttributeNameValue(vo.getAssignType());
		assignTypeXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());
		varXmlEntity.getXmlEntitys().add(assignTypeXmlEntity);

		// 赋值
		XmlEntity assignValueXmlEntity = new XmlEntity();
		assignValueXmlEntity.setLableName("赋值");
		assignValueXmlEntity.setAttributeName("name");
		assignValueXmlEntity.setAttributeNameValue(vo.getFparamValue());
		assignValueXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());
		varXmlEntity.getXmlEntitys().add(assignValueXmlEntity);

		// 选择变量
		XmlEntity selectVarXmlEntity = new XmlEntity();
		selectVarXmlEntity.setLableName("选择变量");
		selectVarXmlEntity.setAttributeName("name");
		selectVarXmlEntity.setAttributeNameValue(vo.getSelectVar());
		selectVarXmlEntity.setXmlEntitys(new ArrayList<XmlEntity>());
		varXmlEntity.getXmlEntitys().add(selectVarXmlEntity);

		return varXmlEntity;
	}

	/**
	 * 属性结构转为XmlEntityMap结构
	 * 
	 * @Title: ParamVOToXmlEntityMap
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年8月26日 下午4:19:32
	 * @param vo
	 * @return
	 */
	private static XmlEntityMap ParamVOToXmlEntityMap(ParamTreeVO vo) {
		// variable
		XmlEntityMap varXmlEntityMap = new XmlEntityMap();
		varXmlEntityMap.setLableName("variable");
		Map<String, String> varAttributeMap = new HashMap<String, String>();
		varAttributeMap.put("name", vo.getFparamName());
		varAttributeMap.put("structType", "");
		varAttributeMap.put("structId", "");
		varAttributeMap.put("paramRemarks", vo.getParamRemarks());
		varXmlEntityMap.setAttributeMap(varAttributeMap);
		varXmlEntityMap.setXmlEntityMaps(new ArrayList<XmlEntityMap>());

		// 变量类型
		XmlEntityMap dataTyeXmlEntity = new XmlEntityMap();
		dataTyeXmlEntity.setLableName("变量类型");
		Map<String, String> dataTyeAttributeMap = new HashMap<String, String>();
		dataTyeAttributeMap.put("name", vo.getFparamType());
		dataTyeXmlEntity.setAttributeMap(dataTyeAttributeMap);
		dataTyeXmlEntity.setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		varXmlEntityMap.getXmlEntityMaps().add(dataTyeXmlEntity);

		// 序号
		XmlEntityMap indexXmlEntity = new XmlEntityMap();
		indexXmlEntity.setLableName("序号");
		Map<String, String> indexAttributeMap = new HashMap<String, String>();
		indexAttributeMap.put("name", vo.getIndex().toString());
		indexXmlEntity.setAttributeMap(indexAttributeMap);
		indexXmlEntity.setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		varXmlEntityMap.getXmlEntityMaps().add(indexXmlEntity);

		// 长度
		XmlEntityMap lengthXmlEntity = new XmlEntityMap();
		lengthXmlEntity.setLableName("长度");
		Map<String, String> lengthAttributeMap = new HashMap<String, String>();
		lengthAttributeMap.put("name", vo.getLength());
		lengthXmlEntity.setAttributeMap(lengthAttributeMap);
		lengthXmlEntity.setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		varXmlEntityMap.getXmlEntityMaps().add(lengthXmlEntity);

		// 类别
		XmlEntityMap assignTypeXmlEntity = new XmlEntityMap();
		assignTypeXmlEntity.setLableName("类别");
		Map<String, String> assignTypeAttributeMap = new HashMap<String, String>();
		assignTypeAttributeMap.put("name", vo.getAssignType());
		assignTypeXmlEntity.setAttributeMap(assignTypeAttributeMap);
		assignTypeXmlEntity.setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		varXmlEntityMap.getXmlEntityMaps().add(assignTypeXmlEntity);

		// 赋值
		XmlEntityMap assignValueXmlEntity = new XmlEntityMap();
		assignValueXmlEntity.setLableName("赋值");
		Map<String, String> assignValueAttributeMap = new HashMap<String, String>();
		assignValueAttributeMap.put("name", vo.getFparamValue());
		assignValueXmlEntity.setAttributeMap(assignValueAttributeMap);
		assignValueXmlEntity.setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		varXmlEntityMap.getXmlEntityMaps().add(assignValueXmlEntity);

		// 选择变量
		XmlEntityMap selectVarXmlEntity = new XmlEntityMap();
		selectVarXmlEntity.setLableName("选择变量");
		Map<String, String> selectVarAttributeMap = new HashMap<String, String>();
		selectVarAttributeMap.put("name", vo.getSelectVar());
		selectVarXmlEntity.setAttributeMap(selectVarAttributeMap);
		selectVarXmlEntity.setXmlEntityMaps(new ArrayList<XmlEntityMap>());
		varXmlEntityMap.getXmlEntityMaps().add(selectVarXmlEntity);

		return varXmlEntityMap;
	}

	/**
	 * 解析结构体（通用：包括系统表、内部表等）
	 *
	 * @param filePath 结构体文件路径
	 * @return <结构体类型名，list<变量类型+空格+变量名>>
	 */
	public static Map<String, List<String>> parseStruct(String filePath) {
		Map<String, List<String>> resultMap = Maps.newHashMap();

		// 模拟系统表结构体赋值
		String sysTableStrutA = "SysTableA";
		List<String> sysTableStrutItemsA = new ArrayList<>();
		sysTableStrutItemsA.add("int iA");
		sysTableStrutItemsA.add("float fA");
		sysTableStrutItemsA.add("double dA");
		sysTableStrutItemsA.add("string strA");
		sysTableStrutItemsA.add("SysTableB SSA");
		resultMap.put(sysTableStrutA, sysTableStrutItemsA);

		// 模拟系统表结构体赋值
		String sysTableStrutB = "SysTableB";
		List<String> sysTableStrutItemsB = new ArrayList<>();
		sysTableStrutItemsB.add("int iB");
		sysTableStrutItemsB.add("float fB");
		sysTableStrutItemsB.add("double dB");
		sysTableStrutItemsB.add("string strB");
		sysTableStrutItemsB.add("SysTableC SSC");
		resultMap.put(sysTableStrutB, sysTableStrutItemsB);

		// 模拟系统表结构体赋值
		String sysTableStrutC = "SysTableC";
		List<String> sysTableStrutItemsC = new ArrayList<>();
		sysTableStrutItemsC.add("int iC");
		sysTableStrutItemsC.add("float fC");
		sysTableStrutItemsC.add("double dC");
		sysTableStrutItemsC.add("string strC");
		resultMap.put(sysTableStrutC, sysTableStrutItemsC);

		// 模拟内部表结构体赋值
		String inTableStrut = "CMP_PARA";
		List<String> inTableStrutItems = new ArrayList<>();
		inTableStrutItems.add("int ci");
		inTableStrutItems.add("float cf");
		inTableStrutItems.add("double cd");
		inTableStrutItems.add("string cstr");
		resultMap.put(inTableStrut, inTableStrutItems);

		// 模拟内部表结构体赋值
		String otherTableStrut = "DSP_CTRL";
		List<String> otherTableStrutItems = new ArrayList<>();
		otherTableStrutItems.add("int di");
		otherTableStrutItems.add("float df");
		otherTableStrutItems.add("double dd");
		otherTableStrutItems.add("string dstr");
		otherTableStrutItems.add("bool b");
		resultMap.put(otherTableStrut, otherTableStrutItems);

		return resultMap;
	}

	/**
	 * 构件头文件解析方法
	 *
	 * @param headerPath 头文件
	 * @return 输出对象
	 */
	public static HeaderFileTransVO parseHeaderFile(String headerPath) {

		HeaderFileTransVO headerFileTransVO = new HeaderFileTransVO();

		// 输入参数设置
		Map<String, String> inputParaNameToType = Maps.newHashMap();
		inputParaNameToType.put("pfSrcData", "float*");
		inputParaNameToType.put("uiExeFlog", "int");
		headerFileTransVO.setInputParaNameToType(inputParaNameToType);

		// 输出参数设置
		Map<String, String> outputParaNameToType = Maps.newHashMap();
		outputParaNameToType.put("moniSpbInfo", "TYPE_STRUCT*");
		outputParaNameToType.put("moniSpbInfo2", "TYPE_STRUCT*");
		headerFileTransVO.setOutputParaNameToType(outputParaNameToType);

		// 头文件结构体设置
		Map<String, List<String>> structTypeToMember = Maps.newHashMap();
		String structName = "TYPE_STRUCT";
		List<String> strutItems = new ArrayList<>();
		strutItems.add("int i");
		strutItems.add("float f");
		strutItems.add("double d");
		strutItems.add("string str");
		structTypeToMember.put(structName, strutItems);
		headerFileTransVO.setStructTypeToMember(structTypeToMember);

		// 参数顺序字典设置
		Map<String, Integer> indexToParaNam = Maps.newHashMap();
		indexToParaNam.put("pfSrcData", 0);
		indexToParaNam.put("uiExeFlog", 1);
		indexToParaNam.put("moniSpbInfo", 2);
		indexToParaNam.put("moniSpbInfo2", 3);
		headerFileTransVO.setIndexToParaName(indexToParaNam);

		return headerFileTransVO;
	}
}
