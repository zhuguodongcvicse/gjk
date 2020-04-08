package com.inforbus.gjk.admin.api.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 构件设计
 *
 * @author zhang
 * @date 2019-04-11 20:09:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
//@TableName("gjk_component")
public class Component extends Model<Component> {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
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
	 * 构件图标
	 */
	private String compImg;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 描述
	 */
	private String compBackupinfo;
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

}
