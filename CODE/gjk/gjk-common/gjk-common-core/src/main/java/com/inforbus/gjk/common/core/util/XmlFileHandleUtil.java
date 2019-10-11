package com.inforbus.gjk.common.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;

import lombok.experimental.UtilityClass;

@UtilityClass
public class XmlFileHandleUtil {

	private static final Logger logger = LoggerFactory.getLogger(XmlFileHandleUtil.class);

	/**
	 * 根据XmlEntity实体类生成的Element对象生成xml文件
	 * 
	 * @param xmlEntity
	 */
	public static boolean createXmlFile(XmlEntity xmlEntity, File file) {
		try {
			// 创建解析器工厂
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document document = db.newDocument();

			analysisToElement(document, xmlEntity);

			// 创建TransformerFactory对象
			TransformerFactory tff = TransformerFactory.newInstance();
			// 创建 Transformer对象
			Transformer tf = tff.newTransformer();

			// 输出内容是否使用换行
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			// 设置输出内容缩进 数字设置缩进长度
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			// 创建xml文件并写入内容
			tf.transform(new DOMSource(document), new StreamResult(file));

			// 将生成的xml文件中的转义特殊字符替换掉
			replaceXmlSpecialChar(file);

			logger.info("生成xml文件成功");
			System.out.println("生成xml文件成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("生成xml文件失败");
			System.out.println("生成xml文件失败");
			return false;
		}
	}

	/**
	 * 使用Java DOM 根据xmlEntity实体类生成Element对象
	 * 
	 * @param document
	 * @param xmlEntity
	 * @return
	 */
	private static Element analysisToElement(Document document, XmlEntity xmlEntity) {

		Element xmlNode = document.createElement(xmlEntity.getLableName());

		if (xmlEntity.getTextContent() != null) {
			xmlNode.setTextContent(xmlEntity.getTextContent());
		}

		if (xmlEntity.getLabelType() != null) {
			xmlNode.setAttribute("lableType", xmlEntity.getLabelType());
		}
		if (xmlEntity.getStructId() != null) {
			xmlNode.setAttribute("structId", xmlEntity.getStructId());
		}

		if (xmlEntity.getActionType() != null) {
			xmlNode.setAttribute("actionType", xmlEntity.getActionType());
		}

		if (xmlEntity.getAttributeId() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeId(), xmlEntity.getAttributeIdValue());
		}

		if (xmlEntity.getAttributeName() != null) {

			xmlNode.setAttribute(xmlEntity.getAttributeName(), xmlEntity.getAttributeNameValue());
		}
		//新增加的为了区分构建函数名
		if (xmlEntity.getAttributeCompId() != null) {

			xmlNode.setAttribute(xmlEntity.getAttributeCompId(), xmlEntity.getAttributeCompIdValue());
		}

		if (xmlEntity.getAttributeStructTypeName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeStructTypeName(), xmlEntity.getAttributeStructTypeValue());
		}

		if (xmlEntity.getAttributeCmpNameName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeCmpNameName(), xmlEntity.getAttributeCmpNameValue());
		}

		if (xmlEntity.getAttributeProRateName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeProRateName(), xmlEntity.getAttributeProRateValue());
		}

		if (xmlEntity.getAttributeCoreNumName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeCoreNumName(), xmlEntity.getAttributeCoreNumValue());
		}

		if (xmlEntity.getAttributeStartName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeStartName(), xmlEntity.getAttributeStartValue());
		}

		if (xmlEntity.getAttributeEndName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeEndName(), xmlEntity.getAttributeEndValue());
		}

		if (xmlEntity.getAttributeLengthName() != null) {
			xmlNode.setAttribute(xmlEntity.getAttributeLengthName(), xmlEntity.getAttributeLengthValue());
		}

		if (xmlEntity.getXmlEntitys() != null) {
			for (XmlEntity entity : xmlEntity.getXmlEntitys()) {
				Element element = analysisToElement(document, entity);
				xmlNode.appendChild(element);
			}
		}

		document.appendChild(xmlNode);

		return xmlNode;
	}

	/**
	 * 根据XmlEntityMap实体类生成的Element对象生成xml文件
	 * 
	 * @param xmlEntity
	 */
	public static boolean createXmlFile(XmlEntityMap xmlEntity, File file) {
		try {
			// 创建解析器工厂
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document document = db.newDocument();

			analysisToElement(document, xmlEntity);

			// 创建TransformerFactory对象
			TransformerFactory tff = TransformerFactory.newInstance();
			// 创建 Transformer对象
			Transformer tf = tff.newTransformer();

			// 输出内容是否使用换行
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			// 设置输出内容缩进 数字设置缩进长度
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			// 创建xml文件并写入内容
			tf.transform(new DOMSource(document), new StreamResult(file));

			// 将生成的xml文件中的转义特殊字符替换掉
			replaceXmlSpecialChar(file);

			logger.info("生成xml文件成功");
			System.out.println("生成xml文件成功");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("生成xml文件失败");
			System.out.println("生成xml文件失败");
			return false;
		}
	}

	/**
	 * 使用Java DOM 根据xmlEntityMap实体类生成Element对象
	 * 
	 * @param document
	 * @param xmlEntityMap
	 * @return
	 */
	private static Element analysisToElement(Document document, XmlEntityMap xmlEntityMap) {
		Element xmlNode = null;
		if(!"".equals(xmlEntityMap.getLableName())) {
			xmlNode = document.createElement(xmlEntityMap.getLableName());
		}

		if (xmlEntityMap.getTextContent() != null) {
			xmlNode.setTextContent(xmlEntityMap.getTextContent());
		}

		if (xmlEntityMap.getAttributeMap() != null) {
			for (Map.Entry<String, String> entry : xmlEntityMap.getAttributeMap().entrySet()) {
				xmlNode.setAttribute(entry.getKey(), entry.getValue());
			}
		}

		if (xmlEntityMap.getXmlEntityMaps() != null) {
			for (XmlEntityMap entity : xmlEntityMap.getXmlEntityMaps()) {
				Element element = analysisToElement(document, entity);
				if(element!= null) {
					xmlNode.appendChild(element);
				}
				
			}
		}
		if(xmlNode!= null) {
			document.appendChild(xmlNode);
		}

		return xmlNode;
	}

	private static void replaceXmlSpecialChar(File file) {
		FileReader reader = null;
		FileWriter writer = null;
		StringBuffer stringBuffer = null;
		try {
			if (file.exists()) {
				reader = new FileReader(file);
				stringBuffer = new StringBuffer();
				char[] buffer = new char[2048];
				while (true) {
					int len = reader.read(buffer);

					if (len == -1) {
						break;
					} else {
						stringBuffer.append(new String(buffer, 0, len));
					}
				}
			}
			String str = stringBuffer.toString();
			str = str.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&")
					.replaceAll("&apos;", "\"").replaceAll("&quot", "\"");

			writer = new FileWriter(file);
			writer.write(str);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("xml文件替换特殊字符失败");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 使用Java 解析xml文件(参数为String类型)
	 */
	public static XmlEntityMap analysisXmlStr(byte[] bytes) {
		List<XmlEntityMap> xmlEntityMaps = new ArrayList<XmlEntityMap>();

		// 创建解析器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// 由工厂创建一个DocumentBuilder解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 创建一个Document对象
			Document document = builder.parse(new InputSource(new ByteArrayInputStream(bytes)));
			// 获取所有节点
			NodeList nodeList = document.getChildNodes();

			xmlEntityMaps = analysisXmlToEntityMap(nodeList);

			logger.info("解析Xml文件成功");
			System.out.println("解析Xml文件成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("解析Xml文件失败");
			System.out.println("解析Xml文件失败");
		}

		return xmlEntityMaps.get(0);
	}
	
	/**
	 * 使用Java DOM 解析XML文件
	 * 
	 * @return
	 */
	public static XmlEntity analysisXmlFile(File file) {
		List<XmlEntity> xmlEntity = new ArrayList<XmlEntity>();

		// 创建解析器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// 由工厂创建一个DocumentBuilder解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 创建一个Document对象
			Document document = builder.parse(file);
			// 获取所有节点
			NodeList nodeList = document.getChildNodes();

			xmlEntity = analysisXmlToEntity(nodeList);

			logger.info("解析Xml文件成功");
			System.out.println("解析Xml文件成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("解析Xml文件失败");
			System.out.println("解析Xml文件失败");
		}

		return xmlEntity.get(0);
	}

	private static List<XmlEntity> analysisXmlToEntity(NodeList nodeList) {
		List<XmlEntity> xmlEntities = new ArrayList<XmlEntity>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Element) {
				XmlEntity entity = new XmlEntity();

				// 获取根节点
				Node root = nodeList.item(i);
				entity.setLableName(root.getNodeName());

				if (root.getFirstChild() != null && !"".equals(root.getFirstChild().getNodeValue().trim())) {
					entity.setTextContent(root.getFirstChild().getNodeValue().trim());
				}

				if (root.hasAttributes()) {
					NamedNodeMap attributeMap = root.getAttributes();
					for (int j = 0; j < attributeMap.getLength(); j++) {
						Node node = attributeMap.item(j);
						String name = node.getNodeName();
						String value = node.getNodeValue();

						if (name.equals("lableType")) {
							entity.setLabelType(value);
						}

						if (name.equals("structId")) {
							entity.setStructId(value);
						}

						if (name.equals("actionType")) {
							entity.setLabelType(value);
						}

						if (name.equals("id")) {
							entity.setAttributeId("id");
							entity.setAttributeIdValue(value);
						}

						if (name.equals("name")) {
							entity.setAttributeName("name");
							entity.setAttributeNameValue(value);
						}
						
						if (name.equals("compId")) {
							entity.setAttributeCompId("compId");
							entity.setAttributeCompIdValue(value);
						}

						if (name.equals("structType")) {
							entity.setAttributeStructTypeName("structType");
							entity.setAttributeStructTypeValue(value);
						}

						if (name.equals("cmpName")) {
							entity.setAttributeCmpNameName("cmpName");
							entity.setAttributeCmpNameValue(value);
						}

						if (name.equals("proRate")) {
							entity.setAttributeProRateName("proRate");
							entity.setAttributeProRateValue(value);
						}

						if (name.equals("coreNum")) {
							entity.setAttributeCoreNumName("coreNum");
							entity.setAttributeCoreNumValue(value);
						}

						if (name.equals("start")) {
							entity.setAttributeStartName("start");
							entity.setAttributeStartValue(value);
						}

						if (name.equals("end")) {
							entity.setAttributeEndName("end");
							entity.setAttributeEndValue(value);
						}

						if (name.equals("length")) {
							entity.setAttributeLengthName("length");
							entity.setAttributeLengthValue(value);
						}
					}
				}

				// 获取根节点的所有子节点
				if (root.hasChildNodes()) {
					NodeList list = root.getChildNodes();
					List<XmlEntity> entities = analysisXmlToEntity(list);
					entity.setXmlEntitys(entities);
				}
				xmlEntities.add(entity);
			}
		}
		return xmlEntities;
	}

	/**
	 * 使用Java DOM 解析XML成XmlEntityMap结构
	 * 
	 * @return
	 */
	public static XmlEntityMap analysisXmlFileToXMLEntityMap(File file) {
		List<XmlEntityMap> xmlEntityMaps = new ArrayList<XmlEntityMap>();

		// 创建解析器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// 由工厂创建一个DocumentBuilder解析器
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 创建一个Document对象
			Document document = builder.parse(file);
			// 获取所有节点
			NodeList nodeList = document.getChildNodes();

			xmlEntityMaps = analysisXmlToEntityMap(nodeList);

			logger.info("解析Xml文件成功");
			System.out.println("解析Xml文件成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("解析Xml文件失败");
			System.out.println("解析Xml文件失败");
			return null;
		}

		return xmlEntityMaps.get(0);
	}

	private static List<XmlEntityMap> analysisXmlToEntityMap(NodeList nodeList) {
		List<XmlEntityMap> xmlEntities = new ArrayList<XmlEntityMap>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Element) {
				XmlEntityMap entity = new XmlEntityMap();

				// 获取根节点
				Node root = nodeList.item(i);
				entity.setLableName(root.getNodeName());

				if (root.getFirstChild() != null && !"".equals(root.getFirstChild().getNodeValue().trim())) {
					entity.setTextContent(root.getFirstChild().getNodeValue().trim());
				}

				if (root.hasAttributes()) {
					NamedNodeMap attributeMap = root.getAttributes();
					Map<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < attributeMap.getLength(); j++) {
						Node node = attributeMap.item(j);
						map.put(node.getNodeName(), node.getNodeValue());
					}
					entity.setAttributeMap(map);
				}

				// 获取根节点的所有子节点
				if (root.hasChildNodes()) {
					NodeList list = root.getChildNodes();
					List<XmlEntityMap> entities = analysisXmlToEntityMap(list);
					entity.setXmlEntityMaps(entities);
				}
				xmlEntities.add(entity);
			}
		}
		return xmlEntities;
	}

}
