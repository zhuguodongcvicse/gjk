package com.inforbus.gjk.pro.api.dto;

import com.inforbus.gjk.pro.api.vo.ProjectFileVO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: ProjectTree
 * @Description: 项目树
 * @Author xiaohe
 * @DateTime 2019年4月23日 下午5:22:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectTree extends TreeNode {

	private String name;
	private String type;
	private String parentType;
	private String label;
	private String softwareId;
	private String bspId;
	private String fileName;
	private String filePath;
	private String processId;
	private String icon;
	private String isDirectory;

	public ProjectTree() {
	}

	public ProjectTree(String name, String type, String parentType, String label) {
		this.name = name;
		this.type = type;
		this.parentType = parentType;
		this.label = label;
	}

	public ProjectTree(String id, String name, ProjectTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
		this.parentType = parent.getType();
	}

	public ProjectTree(ProjectFileVO vo) {
		this.id = vo.getFileId();
		this.parentId = vo.getParentId();
		this.name = vo.getFileName();
		this.type = vo.getFileType();
		this.parentType = vo.getFileType();
		if (this.type == "app") {
			this.label = vo.getShowName();
		} else {
			this.label = vo.getFileName();
		}
		this.processId = vo.getProcedureId();
		this.softwareId = vo.getSoftwareId();
		this.bspId = vo.getBspId();
		this.fileName = vo.getFileName();
		this.filePath = vo.getFilePath();
		this.isDirectory = vo.getIsDirectory();
	}

}
