package com.inforbus.gjk.libs.api.dto;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.inforbus.gjk.common.core.entity.TreeNode;
import com.inforbus.gjk.libs.api.entity.Compframe;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompframeTree extends TreeNode  {


	private static final long serialVersionUID = 1L;

	private String label;

	private Double version;

	private String description;

	private String compframeDirPath;

	public CompframeTree(Compframe compframe, String root) {
		this.id = compframe.getId();
		this.parentId = root;
		this.nodeName = compframe.getName();
		this.label = StringUtils.isEmpty(compframe.getDescription()) ? compframe.getName()
				: compframe.getName() + "\t(" + compframe.getDescription() + ")";
		this.version = compframe.getVersion();
		this.description = compframe.getDescription();
		this.compframeDirPath = compframe.getFilePath();
	}

	public CompframeTree(File file, String id, String parentId) {
		this.id = id;
		this.parentId = parentId;
		this.nodeName = file.getName();
		this.label = file.getName();
		this.compframeDirPath = file.getParentFile().getAbsolutePath();
	}

}
