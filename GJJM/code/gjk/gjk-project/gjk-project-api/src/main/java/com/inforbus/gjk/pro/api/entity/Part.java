package com.inforbus.gjk.pro.api.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

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
