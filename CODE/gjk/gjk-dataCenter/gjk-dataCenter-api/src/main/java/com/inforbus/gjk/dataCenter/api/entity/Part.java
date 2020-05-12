package com.inforbus.gjk.dataCenter.api.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部件
 * 
 * @author Lingg
 *
 */
@Data
public class Part {

	private String partName;

	private List<Component> components = new ArrayList<Component>();

}
