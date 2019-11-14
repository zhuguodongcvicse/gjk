
package com.inforbus.gjk.admin.api.vo;

import lombok.Data;

import java.io.Serializable;
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
public class AlgorithmVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 算法库ID
	 */
	private String algorithmId;
	/**
	 * 算法库名称
	 */
	private String name;
	/**
	 * 算法库权限标识
	 */
	private String permission;
	/**
	 * 算法库单ID
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
		return algorithmId.hashCode();
	}

	/**
	 * algorithmId 相同则相同
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AlgorithmVO) {
			String targetAlgorithmId = ((AlgorithmVO) obj).getAlgorithmId();
			return algorithmId.equals(targetAlgorithmId);
		}
		return super.equals(obj);
	}
	
	public AlgorithmVO(String algorithmId, String name, String permission, String parentId) {
		super();
		this.algorithmId = algorithmId;
		this.name = name;
		this.permission = permission;
		this.parentId = parentId;
	}
	
	public AlgorithmVO() {
		super();
	}
}
