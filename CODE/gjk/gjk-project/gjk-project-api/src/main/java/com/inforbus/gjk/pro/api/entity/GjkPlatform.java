
package com.inforbus.gjk.pro.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 平台库权限表
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GjkPlatform extends Model<GjkPlatform> {

	private static final long serialVersionUID = 1L;

	/**
	 * 平台ID
	 */
	@NotNull(message = "平台库ID不能为空")
	@TableId(value = "platform_id", type = IdType.INPUT)
	private String platformId;
	/**
	 * 平台名称
	 */
	@NotBlank(message = "平台库名称不能为空")
	private String name;
	/**
	 * 平台类型值，App组件生成时根据此值进行判断
	 */
	private String typeValue;
	/**
	 * 平台权限标识
	 */
	private String permission;
	/**
	 * 父平台ID
	 */
	@NotNull(message = "平台库父ID不能为空")
	private String parentId;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
	/**
	 * 0--正常 1--删除
	 */
	@TableLogic
	private String delFlag;
	
	public GjkPlatform(String platformId, String name, String permission, String parentId) {
		super();
		this.platformId = platformId;
		this.name = name;
		this.permission = permission;
		this.parentId = parentId;
	}

	public GjkPlatform(@NotNull(message = "平台库ID不能为空") String platformId, @NotBlank(message = "平台库名称不能为空") String name, String typeValue, String permission, @NotNull(message = "平台库父ID不能为空") String parentId, Integer sort, LocalDateTime createTime, LocalDateTime updateTime, String delFlag) {
		this.platformId = platformId;
		this.name = name;
		this.typeValue = typeValue;
		this.permission = permission;
		this.parentId = parentId;
		this.sort = sort;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.delFlag = delFlag;
	}

	public GjkPlatform() {
		super();
	}


}
