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
import java.math.BigDecimal;

/**
 * 板子设计
 *
 * @author pig code generator
 * @date 2019-06-01 10:28:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_hardwarelib_board")
public class HardwarelibBoard extends Model<HardwarelibBoard> {
private static final long serialVersionUID = 1L;

    /**
   *
   */
	@TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
   *
   */
    private String boardName;
    /**
   *
   */
    private String sn;
    /**
   *
   */
    private String boardId;
    /**
   *
   */
    private BigDecimal cpuNum;
    /**
   *
   */
    private String hrTypeName;
    /**
   *
   */
    private String userId;
    /**
   *
   */
    private String createTime;
    /**
   *
   */
    private String updateTime;
    /**
   *
   */
    private Integer version;
    /**
   *
   */
    private String delFlag;
    /**
   *
   */
    private BigDecimal boardType;

	private String boardJson;

}
