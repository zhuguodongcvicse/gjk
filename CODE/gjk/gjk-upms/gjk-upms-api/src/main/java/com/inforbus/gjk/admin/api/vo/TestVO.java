
package com.inforbus.gjk.admin.api.vo;

import lombok.Data;

import java.io.Serializable;
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
public class TestVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 测试库ID
	 */
	private String testId;
	/**
	 * 测试库名称
	 */
	private String name;
	/**
	 * 测试库权限标识
	 */
	private String permission;
	/**
	 * 测试库单ID
	 */
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
	private String delFlag;


	@Override
	public int hashCode() {
		return testId.hashCode();
	}

	/**
	 * testId 相同则相同
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TestVO) {
			String targetTestId = ((TestVO) obj).getTestId();
			return testId.equals(targetTestId);
		}
		return super.equals(obj);
	}
}
