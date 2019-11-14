
package com.inforbus.gjk.admin.api.entity;

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
 * 算法库权限表
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GjkAlgorithm extends Model<GjkAlgorithm> {

	private static final long serialVersionUID = 1L;

	/**
	 * 算法ID
	 */
	@NotNull(message = "算法库ID不能为空")
	@TableId(value = "algorithm_id", type = IdType.INPUT)
	private String algorithmId;
	/**
	 * 算法名称
	 */
	@NotBlank(message = "算法库名称不能为空")
	private String name;
	/**
	 * 算法权限标识
	 */
	private String permission;
	/**
	 * 父算法ID
	 */
	@NotNull(message = "算法库父ID不能为空")
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
	
	public GjkAlgorithm(String algorithmId, String name, String permission, String parentId) {
		super();
		this.algorithmId = algorithmId;
		this.name = name;
		this.permission = permission;
		this.parentId = parentId;
	}
	
	public GjkAlgorithm() {
		super();
	}


}
