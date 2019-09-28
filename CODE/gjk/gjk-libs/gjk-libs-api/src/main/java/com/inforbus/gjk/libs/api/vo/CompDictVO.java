package com.inforbus.gjk.libs.api.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inforbus.gjk.libs.api.entity.CommonComponent;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class CompDictVO implements Serializable {

	private static final long serialVersionUID = 1L;

	public CompDictVO(CommonComponent comp) {
		this.id = comp.getId();
		this.compName = comp.getCompName() + "-" + comp.getVersion();
	}

	/**
	 * 构件ID
	 */
	private String id;
	/**
	 * 构件名称
	 */
	private String compName;

}
