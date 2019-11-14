package com.inforbus.gjk.pro.api.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 硬件节点类
 * 
 * @author Lingg
 *
 */
@Data
public class HardwareNode {

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 根组件
	 */
	private List<Part> rootPart = new ArrayList<Part>();

	/**
	 * 备份组件
	 */
	private List<Part> backupParts = new ArrayList<Part>();

}
