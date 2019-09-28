package com.inforbus.gjk.common.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class XmlEntityMap {

	public XmlEntityMap() {
		super();
	}

	public XmlEntityMap(XmlHasParentEntity parentEntity) {
		super();
		this.lableName = parentEntity.getLableName();
		this.textContent = parentEntity.getTextContent();
		this.attributeMap = parentEntity.getAttributeMap();
		List<XmlEntityMap> xmlEntityMaps = new ArrayList<XmlEntityMap>();
		if (parentEntity.getXmlHasParentEntities() != null) {
			for (XmlHasParentEntity entity : parentEntity.getXmlHasParentEntities()) {
				XmlEntityMap xmlEntity = new XmlEntityMap(entity);
				xmlEntityMaps.add(xmlEntity);
			}
		}
		this.xmlEntityMaps = xmlEntityMaps;
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
	 * 下级标签list
	 */
	private List<XmlEntityMap> xmlEntityMaps;
}
