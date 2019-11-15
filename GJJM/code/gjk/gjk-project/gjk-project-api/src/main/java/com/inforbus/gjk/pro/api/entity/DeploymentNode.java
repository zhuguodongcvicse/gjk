package com.inforbus.gjk.pro.api.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DeploymentNode {
	
	private String compId;
	
	private List<DeploymentPart> bakPart = new ArrayList<DeploymentPart>();
	
	private List<DeploymentPart> rootPart = new ArrayList<DeploymentPart>();

}
