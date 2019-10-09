package com.inforbus.gjk.pro.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: Manager
 * @Description: 项目明细
 * @Author xiaohe
 * @DateTime 2019年5月2日 上午11:38:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_project_detail")
public class ProjectFile extends Model<Project> {

	private static final long serialVersionUID = 1L;

	public ProjectFile(String id, String projectId, int flowId, String fileName, String fileType, String filePath, String parentId,
			String softwareId, String bspId) {
		this.id = id;
		this.projectId = projectId;
		this.flowId = flowId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.parentId = parentId;
		this.softwareId = softwareId;
		this.bspId = bspId;
	}
	
	public ProjectFile(String id, String projectId, String fileName, String fileType, String filePath, String parentId,
			String softwareId, String bspId) {
		this.id = id;
		this.projectId = projectId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.parentId = parentId;
		this.softwareId = softwareId;
		this.bspId = bspId;
	}

	public ProjectFile() {
		super();
	}

	/**
	 * ID
	 */
	@TableId
	private String id;
	/**
	 * 项目ID
	 */
	private String projectId;
	/**
	 * 工作模式ID
	 */
	private int flowId;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 文件类型
	 */
	private String fileType;
	/**
	 * 文件地址
	 */
	private String filePath;
	/**
	 * 父级ID
	 */
	private String parentId;
	/**
	 * 软件框架id
	 */
	private String softwareId;
	/**
	 * 调用客户接口生成新文件路径
	 */
//	private String newFilePath;

	/**
	 * bsp的id
	 */
	private String bspId;

}
