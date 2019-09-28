package com.inforbus.gjk.libs.api.dto;

import java.io.File;

import com.inforbus.gjk.common.core.entity.TreeNode;
import com.inforbus.gjk.libs.api.entity.BSP;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BSPTree extends TreeNode {

	private String label;

	private Double version;

	private String description;

	private String filePath;

	public BSPTree(BSP bsp, String root) {
		this.id = bsp.getId();
		this.parentId = root;
		this.nodeName = bsp.getBspName();
		this.label = bsp.getBspName() + "\t(" + bsp.getDescription() + ")";
		this.version = bsp.getVersion();
		this.description = bsp.getDescription();
		this.filePath = bsp.getFilePath();
	}

	public BSPTree(File file, String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
		this.nodeName = file.getName();
		this.label = file.getName();
		this.filePath = file.getParentFile().getAbsolutePath();
	}
}
