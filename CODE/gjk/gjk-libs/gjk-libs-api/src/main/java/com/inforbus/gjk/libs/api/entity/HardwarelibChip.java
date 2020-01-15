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
import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 芯片设计
 *
 * @author pig code generator
 * @date 2019-05-30 09:45:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_hardwarelib_chip")
public class HardwarelibChip extends Model<HardwarelibChip> {
private static final long serialVersionUID = 1L;

    /**
   * ID
   */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
   * 芯片名称
   */
    private String chipName;
    /**
     * 芯片ID
     */
    private String chipId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 内核数量
     */
    private BigDecimal coreNum;
    /**
     * 内存大小
     */
    private BigDecimal memSize;
    /**
   * 硬件类型名称
   */
    private String hrTypeName;
    /**
   * 接收速率
   */
    private String recvRate;
    /**
     * 审批状态
     */
    private String applyState;
    /**
     * 审批描述
     */
    private String applyDesc;
    /**
     * 备注信息
     */
    private String backupInfo;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 芯片数据
     */
    private String chipData;
    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 修改时间
   */
    private LocalDateTime updateTime;
    /**
   * 删除标志
   */
    private String delFlag;

}
