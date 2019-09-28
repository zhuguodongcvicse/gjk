package com.inforbus.gjk.libs.api.dto;

import java.io.File;

import com.inforbus.gjk.common.core.entity.TreeNode;
import com.inforbus.gjk.libs.api.entity.Software;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SoftwareTree extends TreeNode {

	private String label;

	private Double version;

	private String description;

	private String softwareDirPath;

	public SoftwareTree(Software software, String root) {
		this.id = software.getId();
		this.parentId = root;
		this.nodeName = software.getSoftwareName();
		this.label = software.getSoftwareName() + "\t(" + software.getDescription() + ")";
		this.version = software.getVersion();
		this.description = software.getDescription();
		this.softwareDirPath = software.getFilePath();
	}

	public SoftwareTree(File file, String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
		this.nodeName = file.getName();
		this.label = file.getName();
		this.softwareDirPath = file.getParentFile().getAbsolutePath();
	}
}
