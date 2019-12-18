package com.inforbus.gjk.pro.api.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.pro.api.entity.Arrows;
import com.inforbus.gjk.pro.api.entity.Component;
import com.inforbus.gjk.pro.api.entity.HardwareNode;
import com.inforbus.gjk.pro.api.entity.HardwarePart;
import com.inforbus.gjk.pro.api.entity.Paramenter;
import com.inforbus.gjk.pro.api.entity.Part;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProcedureXmlAnalysis {

	/**
	 * 节点集合
	 */
	private List<HardwareNode> hardwareNodes;

	/**
	 * 部件集合
	 */
	private List<Part> parts;

	/**
	 * 构件集合
	 */
	private List<Component> components;
	/**
	 * 构件连线集合
	 * 
	 * @param file
	 * @return
	 */
	private List<Arrows> arrowsList;

	public List<HardwareNode> getHardwareNodeList(File file) {
		analysisByProcedureXml(file);
		return hardwareNodes;
	}

	public List<Part> getPartList(File file) {
		analysisByProcedureXml(file);
		return parts;
	}

	public List<Component> getComponentList(File file) {
		analysisByProcedureXml(file);
		return components;
	}

	public List<Arrows> getArrowsList(File file) {
		analysisByProcedureXml(file);
		return arrowsList;
	}

	private void analysisByProcedureXml(File file) {
		hardwareNodes = new ArrayList<>();
		parts = new ArrayList<>();
		components = new ArrayList<Component>();
		arrowsList = new ArrayList<Arrows>();

		XmlEntityMap xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);

		if (xmlEntityMap == null) {
			return;
		}
		for (XmlEntityMap entityMap : xmlEntityMap.getXmlEntityMaps()) {
			if (entityMap.getLableName().equals("node")) {

				// 存入构件详细信息
				Component component = new Component();
				setCompAttr(entityMap, component);
				setCompParame(entityMap, component);
				// 将构件存入构件列表
				components.add(component);
				System.out.println("*/******" + component);
				// 获取构件所属的根部件
				String rootCmpName = getAttributeValue(entityMap, "所属部件", "name");

				// 获取构件所属节点-部件的对应关系集合
				List<HardwarePart> hardwarePartMapList = new ArrayList<HardwarePart>();
				getHardwarePartMapList(hardwarePartMapList, entityMap);

				// 遍历集合，将构件按不同节点不同部件添加到节点-部件-构件结构中
				for (HardwarePart hardwarePart : hardwarePartMapList) {
					saveComp(component, rootCmpName, hardwarePart);
				}
			} else if (entityMap.getLableName().equals("arrow")) {
				Arrows arrows = new Arrows();
				arrows.setArrowId(entityMap.getAttributeMap().get("id"));
				if (entityMap.getXmlEntityMaps() != null) {
					for (XmlEntityMap entityXml : entityMap.getXmlEntityMaps()) {
						if (entityXml.getLableName().equals("画图属性")) {
							if (entityXml.getXmlEntityMaps() != null) {
								for (XmlEntityMap startItemMap : entityXml.getXmlEntityMaps()) {
									if ("startItem".equals(startItemMap.getLableName())) {
										String startItem = startItemMap.getTextContent();
										arrows.setStartItem(startItem);
									} else if ("endItem".equals(startItemMap.getLableName())) {
										String endItem = startItemMap.getTextContent();
										arrows.setEndItem(endItem);
									}
								}
							}
						}
					}
				}
				arrowsList.add(arrows);
				System.out.println("arrowsListline" + arrows);
			}
		}

		// 将所有节点的根部件存入部件列表
		for (HardwareNode hardwareNode : hardwareNodes) {
			if (hardwareNode.getRootPart().size() != 0) {
				parts.addAll(hardwareNode.getRootPart());
			}
		}
	}

	// 获取输入输出参数
	private void setCompParame(XmlEntityMap entityMap, Component component) {
		if (entityMap.getLableName().equals("输入")) {
			for (XmlEntityMap entityXml : entityMap.getXmlEntityMaps()) {
				Paramenter paramenter = new Paramenter();
				setParamenter(paramenter, entityXml);
				component.getInParamenter().add(paramenter);
			}
		} else if (entityMap.getLableName().equals("输出")) {
			for (XmlEntityMap entityXml : entityMap.getXmlEntityMaps()) {
				Paramenter paramenter = new Paramenter();
				setParamenter(paramenter, entityXml);
				component.getOutParamenter().add(paramenter);
			}
		} else {
			if (entityMap.getXmlEntityMaps() != null) {
				for (XmlEntityMap xmlEntity : entityMap.getXmlEntityMaps()) {
					setCompParame(xmlEntity, component);
				}
			}

		}

	}

	private void setParamenter(Paramenter paramenter, XmlEntityMap entityXml) {
		paramenter.setName(getAttributeValue(entityXml, "variable", "name"));
		paramenter.setDataType(getAttributeValue(entityXml, "变量类型", "name"));
		paramenter.setNumber(getAttributeValue(entityXml, "序号", "name"));
		paramenter.setLength(getAttributeValue(entityXml, "长度", "name"));
		paramenter.setType(getAttributeValue(entityXml, "类别", "name"));
		paramenter.setAssignment(getAttributeValue(entityXml, "赋值", "name"));
		paramenter.setSelectVariable(getAttributeValue(entityXml, "选择变量", "name"));
	}

	/**
	 * 保存构件
	 * 
	 * @param component
	 * @param rootCmpName
	 * @param hardwarePart
	 */
	private void saveComp(Component component, String rootCmpName, HardwarePart hardwarePart) {
		// 标记节点是否存在
		boolean isHardwareExist = false;
		// 标记部件是否存在
		boolean isPartExist = false;

		// 循环判断节点是否存在，若存在将标记改为true
		for (HardwareNode node : hardwareNodes) {
			if (node.getNodeName().equals(hardwarePart.getHardwareName())) {
				isHardwareExist = true;
				// 判断部件，根部件进if，备份部件进else
				if (hardwarePart.getPartName().equals(rootCmpName)) {
					// 循环判断部件是否存在，若存在将标记改为true，将构件存入部件
					for (Part part : node.getRootPart()) {
						if (part.getPartName().equals(hardwarePart.getPartName())) {
							isPartExist = true;
							part.getComponents().add(component);
						}
					}
				} else {
					for (Part part : node.getBackupParts()) {
						if (part.getPartName().equals(hardwarePart.getPartName())) {
							isPartExist = true;
							part.getComponents().add(component);
						}
					}
				}
			}
		}

		// 若部件不存在，创建新部件，将构件存入部件
		if (!isPartExist) {
			Part part = new Part();
			part.setPartName(hardwarePart.getPartName());
			part.getComponents().add(component);

			// 若节点不存在，创建新节点，将新部件存入节点
			if (!isHardwareExist) {
				HardwareNode hardwareNode = new HardwareNode();
				hardwareNode.setNodeName(hardwarePart.getHardwareName());
				if (hardwarePart.getPartName().equals(rootCmpName)) {
					hardwareNode.getRootPart().add(part);
				} else {
					hardwareNode.getBackupParts().add(part);
				}
				// 将新节点存入节点列表
				hardwareNodes.add(hardwareNode);
			} else {// 若节点存在，将新部件存入节点
				for (HardwareNode node : hardwareNodes) {
					if (node.getNodeName().equals(hardwarePart.getHardwareName())) {
						if (hardwarePart.getPartName().equals(rootCmpName)) {
							node.getRootPart().add(part);
						} else {
							node.getBackupParts().add(part);
						}
					}
				}
			}
		}
	}

	/**
	 * 将解析出的数据存入构件
	 * 
	 * @param entityMap
	 * @param component
	 */
	private void setCompAttr(XmlEntityMap entityMap, Component component) {
		component.setCompId(getAttributeValue(entityMap, "node", "id"));
		component.setCompName(getAttributeValue(entityMap, "显示名", "name"));
		component.setFunctionName(getAttributeValue(entityMap, "函数名", "name"));
		component.setCompNum(getAttributeValue(entityMap, "构件编号", "name"));
		component.setFunctionPath(new File(getAttributeValue(entityMap, "函数路径", "name")).getParent());
	}

	/**
	 * 递归查询对应的标签名下属性值对应的value
	 * 
	 * @param xmlEntityMap xml结构
	 * @param lableName    标签名
	 * @param attributeKey 属性名
	 * @return
	 */
	private String getAttributeValue(XmlEntityMap xmlEntityMap, String lableName, String attributeKey) {
		String value = null;
		if (xmlEntityMap.getLableName().equals(lableName)) {
			value = xmlEntityMap.getAttributeMap().get(attributeKey);
		} else {
			if (xmlEntityMap.getXmlEntityMaps() != null) {
				for (XmlEntityMap entityMap : xmlEntityMap.getXmlEntityMaps()) {
					value = getAttributeValue(entityMap, lableName, attributeKey);
					if (value != null) {
						break;
					}
				}
			}
		}
		return value;
	}

	/**
	 * 递归获取所属节点标签的属性值
	 * 
	 * @param xmlEntityMap
	 * @param              //lableName
	 * @return
	 */
	private void getHardwarePartMapList(List<HardwarePart> hardwarePartMapList, XmlEntityMap xmlEntityMap) {
		if (xmlEntityMap.getLableName().equals("所属节点")) {
			HardwarePart hardwarePart = new HardwarePart(xmlEntityMap.getAttributeMap().get("name"),
					xmlEntityMap.getAttributeMap().get("cmpName"));
			hardwarePartMapList.add(hardwarePart);
		} else {
			if (xmlEntityMap.getXmlEntityMaps() != null) {
				for (XmlEntityMap entityMap : xmlEntityMap.getXmlEntityMaps()) {
					getHardwarePartMapList(hardwarePartMapList, entityMap);
				}
			}
		}
	}
}
