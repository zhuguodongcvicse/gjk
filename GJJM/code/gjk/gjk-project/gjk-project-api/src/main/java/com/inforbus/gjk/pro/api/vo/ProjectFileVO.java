package com.inforbus.gjk.pro.api.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.inforbus.gjk.pro.api.entity.ProjectFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectFileVO extends Model<ProjectFileVO> {
	private static final long serialVersionUID = 1L;

	/**
	 * @Fields fileId : 构件文件编号
	 */
	private String fileId;
	/**
	 * @Fields fileName : 文件名称
	 */
	private String fileName;
	/**
	 * 文件在树中的显示名字
	 */
	private String showName;
	/**
	 * @Fields fileType : 文件类型
	 */
	private String fileType;
	/**
	 * @Fields filePath : 文件路径
	 */
	private String filePath;
	/**
	 * @Fields parentId :父级编号
	 */
	private String parentId;
	/**
	 * 流程ID
	 */
	private String procedureId;
	/**
	 * 软件框架ID
	 */
	private String softwareId;
	/**
	 * bsp的ID
	 */
	private String bspId;
	/**
	 * 是否是文件
	 */
	private String isDirectory;
	/**
	 * @Fields delFlag : 0--正常 1--删除
	 */
	private String delFlag;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProjectFileVO) {
			String targetMenuId = ((ProjectFileVO) obj).getFileId();
			return fileId.equals(targetMenuId);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return fileId.hashCode();
	}

	public ProjectFileVO(String fileId, String fileName, String fileType, String parentId) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.parentId = parentId;
	}

	public ProjectFileVO(String fileId, String fileName, String showName, String fileType, String filePath,
			String parentId, String procedureId) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.showName = showName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.parentId = parentId;
		this.procedureId = procedureId;
	}

	public ProjectFileVO(String fileId, String fileName, String showName, String fileType, String filePath,
						 String parentId, String procedureId, String isDirectory) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.showName = showName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.parentId = parentId;
		this.procedureId = procedureId;
		this.isDirectory = isDirectory;
	}

	public ProjectFileVO(ProjectFile file) {
		super();
		this.fileId = file.getId();
		this.fileName = file.getFileName();
		this.fileType = file.getFileType();
		this.parentId = file.getParentId();
		this.softwareId = file.getSoftwareId();
		this.bspId = file.getBspId();
	}

	public ProjectFileVO() {

	}
}
