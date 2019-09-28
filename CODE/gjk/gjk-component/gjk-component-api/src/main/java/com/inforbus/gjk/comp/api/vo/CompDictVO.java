package com.inforbus.gjk.comp.api.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inforbus.gjk.comp.api.entity.Component;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class CompDictVO implements Serializable {

	private static final long serialVersionUID = 1L;

	public CompDictVO(Component component) {
		this.id = component.getId();
		this.compName = component.getCompName() + "-" + component.getVersion();
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
