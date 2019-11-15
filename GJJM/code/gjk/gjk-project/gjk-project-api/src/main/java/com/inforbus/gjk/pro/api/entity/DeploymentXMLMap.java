package com.inforbus.gjk.pro.api.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DeploymentXMLMap {
	private String id;
	
	private List<DeploymentNode> bakcompAllArray = new ArrayList<DeploymentNode>();
	
	private List<DeploymentNode> compAllArray = new ArrayList<DeploymentNode>();
}
