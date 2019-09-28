package com.inforbus.gjk.common.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class XmlHasParentEntity {

	public XmlHasParentEntity() {
		super();
	}

	public XmlHasParentEntity(XmlEntityMap xmlEntityMap, XmlHasParentEntity parentEntity) {
		super();
		this.lableName = xmlEntityMap.getLableName();
		this.textContent = xmlEntityMap.getTextContent();
		this.attributeMap = xmlEntityMap.getAttributeMap();
		this.parentEntity = parentEntity;
		List<XmlHasParentEntity> entities = new ArrayList<XmlHasParentEntity>();
		if (xmlEntityMap.getXmlEntityMaps() != null) {
			for (XmlEntityMap entity : xmlEntityMap.getXmlEntityMaps()) {
				XmlHasParentEntity xmlHasParentEntity = new XmlHasParentEntity(entity, this);
				entities.add(xmlHasParentEntity);
			}
		}
		this.xmlHasParentEntities = entities;
	}

	/**
	 * 标签名
	 */
	private String lableName;

	/**
	 * 标签内容
	 */
	private String textContent;

	/**
	 * 属性map
	 */
	private Map<String, String> attributeMap;

	/**
	 * 父级XmlEntityMap
	 */
	private XmlHasParentEntity parentEntity;

	/**
	 * 下级标签list
	 */
	private List<XmlHasParentEntity> xmlHasParentEntities;
}
