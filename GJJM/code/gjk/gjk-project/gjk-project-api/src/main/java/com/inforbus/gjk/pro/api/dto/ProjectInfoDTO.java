package com.inforbus.gjk.pro.api.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProjectInfoDTO {

	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 创建者
	 */
	private String userName;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 项目图标
	 */
	private String projectImg;
	/**
	 * 描述
	 */
	private String description;
	
}
