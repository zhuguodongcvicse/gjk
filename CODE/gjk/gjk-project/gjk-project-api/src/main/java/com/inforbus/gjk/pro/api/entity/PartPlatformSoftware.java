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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程修改软件框架库保存表
 *
 * @author lijun
 * @date 2019-09-26 16:26:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_app_part_platform_software")
public class PartPlatformSoftware extends Model<PartPlatformSoftware> {
private static final long serialVersionUID = 1L;

    /**
   * 主键id
   */
    @TableId
    private String id;
    /**
     * 软件框架库id
     */
    private String softwareId;
    /**
     * 软件框架库名字
     */
      private String softwareName;
    /**
   * 版本
   */
    private Double softwareVersion;
    /**
   * 文件路径
   */
    private String softwareFilePath;
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
