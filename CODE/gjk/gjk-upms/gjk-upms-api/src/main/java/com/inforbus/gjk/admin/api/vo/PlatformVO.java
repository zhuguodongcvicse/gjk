
package com.inforbus.gjk.admin.api.vo;

import lombok.Data;

import java.io.Serializable;
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
public class PlatformVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 平台库ID
	 */
	private String platformId;
	/**
	 * 平台库名称
	 */
	private String name;
	/**
	 * 平台库权限标识
	 */
	private String permission;
	/**
	 * 平台库单ID
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
		return platformId.hashCode();
	}

	/**
	 * platformId 相同则相同
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PlatformVO) {
			String targetPlatformId = ((PlatformVO) obj).getPlatformId();
			return platformId.equals(targetPlatformId);
		}
		return super.equals(obj);
	}
}
