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
package com.inforbus.gjk.libs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;


import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
public interface SoftwareMapper extends BaseMapper<Software> {
  /**
    * 软件框架库表简单分页查询
    * @param software 软件框架库表
    * @return
    */
  IPage<Software> getSoftwarePage(Page page, @Param("software") Software software);
  
  /**
   * 
   * @Title: saveSoftware  保存软件框架库信息
   * @Description: 
   * @Author cvicse
   * @DateTime 2019年6月14日 上午11:40:43
   * @param software
   */
  void saveSoftware(@Param("soft") Software software);
  
  /**
   * 保存软件框架库子表
   * @Title: saveSoftwareDetail
   * @Description: 
   * @Author cvicse
   * @DateTime 2019年6月14日 下午4:48:49
   * @param softwareDetail
   */
  void saveSoftwareDetail(@Param("softDetail") SoftwareDetail softwareDetail);
  
  /**
   * 保存软件框架库文件路径子表
   * @Title: saveSoftwareFile
   * @Description: 
   * @Author cvicse
   * @DateTime 2019年6月14日 下午4:48:49
   * @param softwareDetail
   */
  void saveSoftwareFile(@Param("softFile") SoftwareFile softwareFile);
  
  
  /**
   	* 根据选择的平台文件对版本进行赋值
   * @param platformId
   * @return
   */
//  Integer setVersionSize(@Param("platformId") String platformId);
  Software setVersionSize();
  
  /**
   * 解析文件夹路径
   * @param selectFolderDTO
   * @return
   */
//  void selectFolder(List<SelectFolderDTO> selectFolderDTO);removeSoftwareFile
  
  /**
   * 根据id级联删除software文件表
   * @param softwareId
   */
  void removeSoftwareFile(@Param("softwareId") String softwareId);
  
  /**
   * 根据id级联删除software详细表
   * @param softwareId
   */
  void removeSoftwareDetail(@Param("softwareId") String softwareId);

  /**
   * 根据多个ID查询数据列表
   * @param ids
   */
  List<Software> getAllSoftwareListByIdIn(@Param("ids") String ids);
}
