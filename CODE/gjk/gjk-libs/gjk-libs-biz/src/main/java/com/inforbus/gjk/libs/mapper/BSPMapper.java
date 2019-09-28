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
 * this BSP without specific prior written permission.
 * Author: inforbus
 */
package com.inforbus.gjk.libs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.libs.api.entity.BSP;
import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.api.entity.BSPFile;

import org.apache.ibatis.annotations.Param;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
public interface BSPMapper extends BaseMapper<BSP> {
  /**
    * 软件框架库表简单分页查询
    * @param BSP 软件框架库表
    * @return
    */
  IPage<BSP> getBSPPage(Page page, @Param("bsp") BSP bsp);
  
  /**
   * 
   * @Title: saveBSP  保存软件框架库信息
   * @Description: 
   * @Author cvicse
   * @DateTime 2019年6月14日 上午11:40:43
   * @param BSP
   */
  void saveBSP(@Param("bs") BSP bsp);
  
  /**
   * 保存软件框架库子表
   * @Title: saveBSPDetail
   * @Description: 
   * @Author cvicse
   * @DateTime 2019年6月14日 下午4:48:49
   * @param BSPDetail
   */
  void saveBSPDetail(@Param("bspDetail") BSPDetail BSPDetail);
  
  /**
   * 保存软件框架库文件路径子表
   * @Title: saveBSPFile
   * @Description: 
   * @Author cvicse
   * @DateTime 2019年6月14日 下午4:48:49
   * @param BSPDetail
   */
  void saveBSPFile(@Param("bspFile") BSPFile BSPFile);
  
  
  /**
   	* 根据选择的平台文件对版本进行赋值
   * @param platformId
   * @return
   */
//  Integer setVersionSize(@Param("platformId") String platformId);
  BSP setVersionSize();
  
  /**
   * 根据id级联删除bsp文件表
   */
  void removeBspFile(@Param("bspId")String bspId);
  
  /**
   * 根据id级联删除bsp详细表
   */
  void removeBspDetail(@Param("bspId")String bspId);
  
}
