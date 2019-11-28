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
package com.inforbus.gjk.pro.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程修改BSP库保存表
 *
 * @author zyk
 * @date 2019-11-28 16:00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_app_part_platform_bsp")
public class PartPlatformBSP extends Model<PartPlatformBSP> {
private static final long serialVersionUID = 1L;

    /**
   * 主键id
   */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * bsp库id
     */
    private String bspId;
    /**
     * bsp库名字
     */
    private String bspName;
    /**
   * 版本
   */
    private Double bspVersion;
    /**
   * 文件路径
   */
    private String bspFilePath;
    /**
   * 平台库名称--多个平台之间用分号隔开
   */
    private String platformName;

    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 流程ID
   */
    private String procedureId;
  
	
    
}
