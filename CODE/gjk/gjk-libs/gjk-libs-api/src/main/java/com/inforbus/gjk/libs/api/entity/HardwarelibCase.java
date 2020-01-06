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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 机箱设计
 *
 * @author sun
 * @date 2019-06-18 17:00:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_hardwarelib_case")
public class HardwarelibCase extends Model<HardwarelibCase> {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 机箱名称
     */
    private String caseName;
    /**
     * 机箱id
     */
    private String caseId;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 板子数量
     */
    private Integer bdNum;
    /**
     * 备注信息
     */
    private String backupInfo;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 正面机箱json
     */
    private String frontCase;
    /**
     * 背面机箱json
     */
    private String backCase;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 逻辑删除
     */
    private String delFlag;

}
