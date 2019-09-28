package com.inforbus.gjk.pro.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("gjk_project_comp")
public class ProComp {

	public ProComp() {
		super();
	}

	public ProComp(String id, String compId, String projectId) {
		super();
		this.id = id;
		this.compId = compId;
		this.projectId = projectId;
	}

	/**
	 * ID
	 */
	@TableId
	private String id;

	/**
	 * 构件ID
	 */
	private String compId;
	/**
	 * 项目ID
	 */
	private String projectId;
	/**
	 * 构件是否通过审批，1为否，0为是
	 */
	private String canUse;

}
