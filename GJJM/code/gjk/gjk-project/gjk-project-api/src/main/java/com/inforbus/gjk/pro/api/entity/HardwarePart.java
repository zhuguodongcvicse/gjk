package com.inforbus.gjk.pro.api.entity;

import lombok.Data;

/**
 * 部件-节点对应类
 * 
 * @author Lingg
 *
 */
@Data
public class HardwarePart {

	public HardwarePart() {
		super();
	}

	public HardwarePart(String hardwareName, String partName) {
		super();
		this.hardwareName = hardwareName;
		this.partName = partName;
	}

	/**
	 * 节点
	 */
	private String hardwareName;
	/**
	 * 部件
	 */
	private String partName;

}
