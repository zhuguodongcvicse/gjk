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
 * 公共构件库表
 *
 * @author pig code generator
 * @date 2019-06-10 14:24:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gjk_common_component_detail")
public class CommonComponentDetail extends Model<CommonComponentDetail> {
private static final long serialVersionUID = 1L;

    /**
   * 
   */
    @TableId
    private String id;
    /**
   * 
   */
    private String compId;
    /**
   * 
   */
    private String fileName;
    /**
   * 1、文件夹；2、c文件；3、h文件；4、doc文件;5、待扩展
   */
    private String fileType;
    /**
   * 
   */
    private String filePath;
    /**
   * 
   */
    private String paraentId;
    /**
   * 
   */
    private String version;
    /**
   * 
   */
    private String paraentIds;
    /**
   * 库目录树节点
   */
    private String libsId;
  
}
