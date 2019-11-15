package com.inforbus.gjk.comp.api.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.inforbus.gjk.comp.api.entity.ComponentDetail;

import lombok.Data;

/**
 * 输入输出参数实体
 * @ClassName: ParamsVO
 * @Description: 
 * @Author hu
 * @DateTime 2019年5月7日 下午7:17:24
 */
@Data
public class ParamsVO {
	
	/**
	 * 节点id
	 */
	private String id;
	/**
	 * 父节点id
	 */
	private String parentId;
	/**
	 * 名称 
	 */
	private String paramsName;
	/**
	 * 类型
	 */
	private String paramsType;
	/**
	 * 值
	 */
	private String paramsValue;
	/**
	 * 序号
	 */
	private Integer index;
	/**
	 * 数据长度
	 */
	private Integer Length;

}
