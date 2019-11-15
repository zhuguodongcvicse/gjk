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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 项目库
 *
 * @author pig code generator
 * @date 2019-07-18 11:26:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_project")
public class Project extends Model<Project> {
private static final long serialVersionUID = 1L;

    /**
   * 项目编号
   */
    @TableId
    private String id;
    /**
   * 
   */
    private String projectName;
    /**
   * 
   */
    private String userId;
    /**
   * 项目图标
   */
    private String projectImg;
    /**
   * 创建时间
   */
    private LocalDateTime createTime;
    /**
   * 修改时间
   */
    private LocalDateTime updateTime;
    /**
   * 项目默认对应软件框架
   */
    private String defaultSoftwareId;
    /**
   * 
   */
    private String description;
    /**
   * 0-正常，1-删除
   */
    private String delFlag;
    /**
         * 用户名
     */
    private String userName;
}
