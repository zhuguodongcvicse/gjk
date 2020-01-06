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
import java.time.LocalDateTime;

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
     * 板卡名称
     */
    private String boardName;
    /**
     * 板卡id
     */
    private String boardId;
    /**
     * 用户名
     */
    private Integer userId;
    /**
     * CPU数量
     */
    private Integer cpuNum;
    /**
     * 板卡类型
     */
    private String boardType;
    /**
     * 备注信息
     */
    private String backupInfo;
    /**
     * 审批状态
     */
    private String applyState;
    /**
     * 审批描述
     */
    private String applyDesc;
    /**
     *版本
     */
    private Integer version;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     *板卡json数据
     */
    private String boardJson;
    /**
     *逻辑删除标识位
     */
    private String delFlag;

}
