package com.inforbus.gjk.admin.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inforbus.gjk.admin.api.entity.SysUser;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class UserDictDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserDictDTO(SysUser sysUser) {
		this.userId = sysUser.getUserId();
		this.name = sysUser.getName();
		this.showName=sysUser.getName()+"("+sysUser.getUsername()+")";
	}

	private Integer userId;

	private String name;
	private String showName;

}
