package com.inforbus.gjk.comp.api.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.inforbus.gjk.common.core.constant.ComponentConstant;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.comp.api.dto.ComponentDTO;
import com.inforbus.gjk.comp.api.dto.ComponentInput;
import com.inforbus.gjk.comp.api.dto.ComponentOutput;
import com.inforbus.gjk.comp.api.dto.ProCPB;
import com.inforbus.gjk.comp.api.dto.SubordinateNode;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;

public class XmlAnalysisUtil {

	private static final Logger logger = LoggerFactory.getLogger(XmlAnalysisUtil.class);

	public static ComponentDTO xmlFileToComponentDTO(Component component, ComponentDetail comp,InputStream is) {
		ComponentDTO componentDTO = new ComponentDTO(comp);

		// 创建解析器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			String path = comp.getFileName();
			File file = null;
			if (path.startsWith(ComponentConstant.COMP) && path.toUpperCase().endsWith(ComponentConstant.COMP_XML)) {
				file = new File(JGitUtil.getLOCAL_REPO_PATH() + "/" + comp.getFilePath() + "/" + path);
			}

			// 由工厂创建一个DocumentBuilder解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 创建一个Document对象
			Document document = builder.parse(is);
			// 获取所有节点
			NodeList nodeList = document.getChildNodes();
			System.out.println(nodeList.getLength());
			analysisXmlToComponentDTO(componentDTO, nodeList, null, null, null);
			// 设置构件基本信息
			componentDTO.setId(component.getId());
			componentDTO.setCompId(component.getCompId());
			componentDTO.setCompName(component.getCompName());
			componentDTO.setCompVersion(component.getVersion());
			componentDTO.setCompImg(component.getCompImg());
			logger.info("解析Xml文件成功");
			System.out.println("解析Xml文件成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("解析Xml文件失败");
			System.out.println("解析Xml文件失败");
		}

		return componentDTO;
	}

	private static void analysisXmlToComponentDTO(ComponentDTO componentDTO, NodeList nodeList, String rootNodeName,
			String variName, String variType) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Element) {
				NodeList list = null;
				String childrootNodeName = null;
				String childVariName = null;
				String childVariType = null;

				// 获取根节点
				Node root = nodeList.item(i);

				try {
					// 解析node节点，保存节点下的属性id的值
					if (root.getNodeName().equals("node")) {
						setAttribute(componentDTO, root, "id", "setId");
					}

					// 解析基本属性节点，获取子节点存入childrootNodeName，使用递归
					if (root.getNodeName().equals("基本属性")) {
						if (root.hasChildNodes()) {
							childrootNodeName = root.getNodeName();
						}
					}

					if (root.getNodeName().equals("显示名") && rootNodeName == ("基本属性")) {
						setAttribute(componentDTO, root, "name", "setName");
					}

					if (root.getNodeName().equals("函数名") && rootNodeName == ("基本属性")) {
						setAttribute(componentDTO, root, "name", "setFunctionName");
					}

					if (root.getNodeName().equals("构件编号") && rootNodeName == ("基本属性")) {
						setAttribute(componentDTO, root, "name", "setCompNum");
					}

					if (root.getNodeName().equals("函数路径") && rootNodeName == ("基本属性")) {
						setAttribute(componentDTO, root, "name", "setFunctionPath");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (root.getNodeName().startsWith("属性") && "基本属性".equals(rootNodeName)) {
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("name")) {
								componentDTO.getAttributeNumName().put(root.getNodeName(), node.getNodeValue());
							}
						}
					}
				}

				if (root.getNodeName().equals("输入") && rootNodeName == ("基本属性")) {
					if (root.hasChildNodes()) {
						childrootNodeName = root.getNodeName();
					}
				}

				if (root.getNodeName().equals("variable") && rootNodeName == ("输入")) {
					ComponentInput input = null;
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("name")) {
								if (input == null) {
									input = new ComponentInput();
								}
								input.setVariableName(node.getNodeValue());

							}
							if (node.getNodeName().equals("structType")) {
								if (input == null) {
									input = new ComponentInput();
								}
								input.setVariableStructType(node.getNodeValue());
							}
						}
					}
					componentDTO.getInputList().add(input);
					if (root.hasChildNodes()) {
						childrootNodeName = "输入";
						childVariName = input.getVariableName();
						childVariType = input.getVariableStructType();
					}
				}

				try {
					if (root.getNodeName().equals("变量类型") && rootNodeName == ("输入")) {
						setInputAttribute(componentDTO, variName, variType, root, "setDataTypeName");
					}

					if (root.getNodeName().equals("序号") && rootNodeName == ("输入")) {
						setInputAttribute(componentDTO, variName, variType, root, "setOrderNumName");
					}

					if (root.getNodeName().equals("长度") && rootNodeName == ("输入")) {
						setInputAttribute(componentDTO, variName, variType, root, "setLengthName");
					}

					if (root.getNodeName().equals("类别") && rootNodeName == ("输入")) {
						setInputAttribute(componentDTO, variName, variType, root, "setCategoryName");
					}

					if (root.getNodeName().equals("赋值") && rootNodeName == ("输入")) {
						setInputAttribute(componentDTO, variName, variType, root, "setVoluationName");
					}

					if (root.getNodeName().equals("选择变量") && rootNodeName == ("输入")) {
						setInputAttribute(componentDTO, variName, variType, root, "setSelectionVariableName");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (root.getNodeName().startsWith("参数") && rootNodeName == ("输入")) {
					for (ComponentInput input : componentDTO.getInputList()) {
						if (input.getVariableName() == null && variName == null && input.getVariableStructType() == null
								&& variType == null) {
							continue;
						}
						if (!input.getVariableName().equals(variName)
								&& !input.getVariableStructType().equals(variType)) {
							continue;
						}
						if (root.hasAttributes()) {
							NamedNodeMap attributeMap = root.getAttributes();
							for (int j = 0; j < attributeMap.getLength(); j++) {
								Node node = attributeMap.item(j);
								if (node.getNodeName().equals("name")) {
									input.getParameterNumName().put(root.getNodeName(), node.getNodeValue());
								}
							}
						}
					}
				}

				if (root.getNodeName().equals("输出") && rootNodeName == ("基本属性")) {
					if (root.hasChildNodes()) {
						childrootNodeName = root.getNodeName();
					}
				}

				if (root.getNodeName().equals("variable") && rootNodeName == ("输出")) {
					ComponentOutput output = null;
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("name")) {
								if (output == null) {
									output = new ComponentOutput();
								}
								output.setVariableName(node.getNodeValue());

							}
							if (node.getNodeName().equals("structType")) {
								if (output == null) {
									output = new ComponentOutput();
								}
								output.setVariableStructType(node.getNodeValue());
							}
						}
					}
					componentDTO.getOutputList().add(output);
					if (root.hasChildNodes()) {
						childrootNodeName = "输出";
						childVariName = output.getVariableName();
						childVariType = output.getVariableStructType();
					}
				}

				try {
					if (root.getNodeName().equals("变量类型") && rootNodeName == ("输出")) {
						setOutputAttribute(componentDTO, variName, variType, root, "setDataTypeName");
					}

					if (root.getNodeName().equals("序号") && rootNodeName == ("输出")) {
						setOutputAttribute(componentDTO, variName, variType, root, "setOrderNumName");
					}

					if (root.getNodeName().equals("长度") && rootNodeName == ("输出")) {
						setOutputAttribute(componentDTO, variName, variType, root, "setLengthName");
					}

					if (root.getNodeName().equals("类别") && rootNodeName == ("输出")) {
						setOutputAttribute(componentDTO, variName, variType, root, "setCategoryName");
					}

					if (root.getNodeName().equals("赋值") && rootNodeName == ("输出")) {
						setOutputAttribute(componentDTO, variName, variType, root, "setVoluationName");
					}

					if (root.getNodeName().equals("选择变量") && rootNodeName == ("输出")) {
						setOutputAttribute(componentDTO, variName, variType, root, "setSelectionVariableName");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (root.getNodeName().startsWith("参数") && rootNodeName == ("输出")) {
					for (ComponentOutput output : componentDTO.getOutputList()) {
						if (output.getVariableName() == null && variName == null
								&& output.getVariableStructType() == null && variType == null) {
							continue;
						}
						if (!output.getVariableName().equals(variName)
								&& !output.getVariableStructType().equals(variType)) {
							continue;
						}
						if (root.hasAttributes()) {
							NamedNodeMap attributeMap = root.getAttributes();
							for (int j = 0; j < attributeMap.getLength(); j++) {
								Node node = attributeMap.item(j);
								if (node.getNodeName().equals("name")) {
									output.getParameterNumName().put(root.getNodeName(), node.getNodeValue());
								}
							}
						}
					}
				}

				if (root.getNodeName().equals("层级属性")) {
					if (root.hasChildNodes()) {
						childrootNodeName = root.getNodeName();
					}
				}

				try {
					if (root.getNodeName().equals("所属部件") && rootNodeName == ("层级属性")) {
						setAttribute(componentDTO, root, "name", "setPartsName");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (root.getNodeName().equals("部署配置")) {
					if (root.hasChildNodes()) {
						childrootNodeName = root.getNodeName();
					}
				}

				if (root.getNodeName().startsWith("属性") && rootNodeName == ("部署配置")) {
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("name")) {
								componentDTO.getDcNumName().put(root.getNodeName(), node.getNodeValue());
							}
						}
					}
				}

				if (root.getNodeName().equals("所属节点") && rootNodeName == ("部署配置")) {
					SubordinateNode sub = null;
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("name")) {
								if (sub == null) {
									sub = new SubordinateNode();
								}
								sub.setSubNodeName(node.getNodeValue());

							}
							if (node.getNodeName().equals("cmpName")) {
								if (sub == null) {
									sub = new SubordinateNode();
								}
								sub.setSubNodeCmpName(node.getNodeValue());
							}
						}
					}
					componentDTO.getSubNodes().add(sub);
				}

				if (root.getNodeName().equals("性能属性")) {
					if (root.hasChildNodes()) {
						childrootNodeName = root.getNodeName();
					}
				}

				try {
					if (root.getNodeName().equals("测试结果路径") && rootNodeName == ("性能属性")) {
						setAttribute(componentDTO, root, "name", "setPerfAttrTestResultPath");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

//				if (root.getNodeName().equals("通过率")) {
//					if (root.hasChildNodes()) {
//						childrootNodeName = root.getNodeName();
//					}
//				}

				if (root.getNodeName().equals("proCPB") /* && rootNodeName.equals("通过率") */) {
					ProCPB pro = null;
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("proRate")) {
								if (pro == null) {
									pro = new ProCPB();
								}
								pro.setProRateValue(node.getNodeValue());

							}
							if (node.getNodeName().equals("coreNum")) {
								if (pro == null) {
									pro = new ProCPB();
								}
								pro.setCoreNumValue(node.getNodeValue());
							}
						}
					}
					componentDTO.getProCPBs().add(pro);
				}

				if (root.getNodeName().equals("资源属性")) {
					if (root.hasChildNodes()) {
						childrootNodeName = root.getNodeName();
					}
				}

				if (root.getNodeName().startsWith("属性") && rootNodeName == ("资源属性")) {
					if (root.hasAttributes()) {
						NamedNodeMap attributeMap = root.getAttributes();
						for (int j = 0; j < attributeMap.getLength(); j++) {
							Node node = attributeMap.item(j);
							if (node.getNodeName().equals("name")) {
								componentDTO.getResourceAttrNumName().put(root.getNodeName(), node.getNodeValue());
							}
						}
					}
				}

				if (root.hasChildNodes()) {
					list = root.getChildNodes();
					analysisXmlToComponentDTO(componentDTO, list, childrootNodeName, childVariName, childVariType);
				}
			}
		}
	}

	/**
	 * @Title: setAttribute
	 * @Description: 将节点的属性解析添加到DTO中
	 * @Author cvics
	 * @DateTime 2019年5月25日 上午10:02:28
	 * @param componentDTO  主DTO
	 * @param root          将要解析属性的节点
	 * @param attributeName 属性的名称
	 * @param methodName    将要存储的调用set方法名称
	 * @throws Exception
	 */
	private static void setAttribute(ComponentDTO componentDTO, Node root, String attributeName, String methodName)
			throws Exception {
		Class<?> comp = Class.forName("com.inforbus.gjk.comp.api.dto.ComponentDTO");
		if (root.hasAttributes()) {
			NamedNodeMap attributeMap = root.getAttributes();
			for (int j = 0; j < attributeMap.getLength(); j++) {
				Node node = attributeMap.item(j);
				if (attributeName.equals(node.getNodeName())) {
					Method method = comp.getMethod(methodName, String.class);
					method.invoke(componentDTO, node.getNodeValue());
				}
			}
		}
	}

	private static void setInputAttribute(ComponentDTO componentDTO, String variName, String variType, Node root,
			String methodName) throws Exception {
		Class<?> inputClass = Class.forName("com.inforbus.gjk.comp.api.dto.ComponentInput");
		for (ComponentInput input : componentDTO.getInputList()) {
			if (input.getVariableName() == null && variName == null && input.getVariableStructType() == null
					&& variType == null) {
				continue;
			}
			if (!input.getVariableName().equals(variName) || !input.getVariableStructType().equals(variType)) {
				continue;
			}
			if (root.hasAttributes()) {
				NamedNodeMap attributeMap = root.getAttributes();
				for (int i = 0; i < attributeMap.getLength(); i++) {
					Node node = attributeMap.item(i);
					if (node.getNodeName().equals("name")) {
						Method setMethod = inputClass.getMethod(methodName, String.class);
						setMethod.invoke(input, node.getNodeValue());
					}
				}
			}
		}
	}

	private static void setOutputAttribute(ComponentDTO componentDTO, String variName, String variType, Node root,
			String methodName) throws Exception {
		Class<?> outputClass = Class.forName("com.inforbus.gjk.comp.api.dto.ComponentOutput");
		for (ComponentOutput output : componentDTO.getOutputList()) {
			if (output.getVariableName() == null && variName == null && output.getVariableStructType() == null
					&& variType == null) {
				continue;
			}
			if (!output.getVariableName().equals(variName) || !output.getVariableStructType().equals(variType)) {
				continue;
			}
			if (root.hasAttributes()) {
				NamedNodeMap attributeMap = root.getAttributes();
				for (int i = 0; i < attributeMap.getLength(); i++) {
					Node node = attributeMap.item(i);
					if (node.getNodeName().equals("name")) {
						Method setMethod = outputClass.getMethod(methodName, String.class);
						setMethod.invoke(output, node.getNodeValue());
					}
				}
			}
		}
	}

}
