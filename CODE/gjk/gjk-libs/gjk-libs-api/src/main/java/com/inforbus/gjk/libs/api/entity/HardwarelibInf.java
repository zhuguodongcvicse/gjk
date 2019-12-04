/*
 *    Copyright (c) 2018-2025, inforbus All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the inforbus.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: inforbus
 */
package com.inforbus.gjk.libs.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.simpleframework.xml.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口设计
 *
 * @author pig code generator
 * @date 2019-05-25 15:15:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_hardwarelib_inf")
public class HardwarelibInf extends Model<HardwarelibInf> {
private static final long serialVersionUID = 1L;

    /**
   *
   */
	@TableId(value = "id", type = IdType.ID_WORKER)
    private String id;
    /**
   *
   */
    private String infName;
    /**
   *
   */
    private String infId;
    /**
   *
   */
    private String infRate;
    /**
   *
   */
    private String infType;
    /**
   *
   */
    private String userId;
    /**
   *
   */
    private LocalDateTime createTime;
    /**
   *
   */
    private LocalDateTime updateTime;
    /**
   *
   */
    private String backupInfo;
    /**
   *
   */
    private Integer version;
    /**
   *
   */
    private String delFlag;

	private String opticalNum;

	private String ioType;

	private String applyState;

	private String applyDesc;

}
