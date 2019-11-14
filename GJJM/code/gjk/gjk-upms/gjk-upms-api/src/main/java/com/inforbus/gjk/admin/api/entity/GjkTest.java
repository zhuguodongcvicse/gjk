
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
 * 测试库权限表
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GjkTest extends Model<GjkTest> {

	private static final long serialVersionUID = 1L;

	/**
	 * 测试库ID
	 */
	@NotNull(message = "测试库ID不能为空")
	@TableId(value = "test_id", type = IdType.INPUT)
	private String testId;
	/**
	 * 测试名称
	 */
	@NotBlank(message = "测试库名称不能为空")
	private String name;
	/**
	 * 测试权限标识
	 */
	private String permission;
	/**
	 * 父测试ID
	 */
	@NotNull(message = "测试库父ID不能为空")
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
	
	public GjkTest(String testId, String name, String permission, String parentId) {
		super();
		this.testId = testId;
		this.name = name;
		this.permission = permission;
		this.parentId = parentId;
	}
	
	public GjkTest() {
		super();
	}


}
