package com.inforbus.gjk.comp.api.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableId;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;

import lombok.Data;

@Data
public class ComponentVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private String id;
	/**
	 * 构件编号
	 */
	private String compId;
	/**
	 * 构件名称
	 */
	private String compName;
	/**
	 * 构件函数名
	 */
	private String compFuncname;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 版本
	 */
	private String version;
	/**
	 * git库对应版本号
	 */
	private String gitVersion;
	/**
	 * 构件图标
	 */
	private String compImg;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 审批状态
	 */
	private String applyState;
	/**
	 * 审批描述
	 */
	private String applyDesc;
	/**
	 * 0-正常，1-删除
	 */
	private String delFlag;
	/**
	 * 构件对应详细信息
	 */
	private List<ComponentDetail> detailList;

}
