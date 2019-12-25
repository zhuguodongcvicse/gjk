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
package com.inforbus.gjk.libs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.entity.HardwarelibInf;
import com.inforbus.gjk.libs.service.HardwarelibInfService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 接口设计
 *
 * @author pig code generator
 * @date 2019-05-25 15:15:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hardwarelibinf")
public class HardwarelibInfController {

  private final  HardwarelibInfService hardwarelibInfService;

  /**
   * 简单分页查询
   * @param page 分页对象
   * @param hardwarelibInf 接口设计
   * @return
   */
  @GetMapping("/page/{userName}")
  public R<IPage<HardwarelibInf>> getHardwarelibInfPage(Page<HardwarelibInf> page, HardwarelibInf hardwarelibInf, @PathVariable(value = "userName") String userName) {
    System.out.println("page" + page);
    return  new R<>(hardwarelibInfService.getHardwarelibInfPage(page, userName, hardwarelibInf));
  }


  /**
   * 通过id查询单条记录
   * @param id
   * @return R
   */
  @GetMapping("/{id}")
  public R<HardwarelibInf> getById(@PathVariable("id") String id){
    return new R<>(hardwarelibInfService.getById(id));
  }

  /**
   * 新增记录
   * @param hardwarelibInf
   * @return R
   */
  @SysLog("新增接口设计")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('libs_hardwarelibinf_add')")
  public R save(@RequestBody HardwarelibInf hardwarelibInf){
    return new R<>(hardwarelibInfService.save(hardwarelibInf));
  }

  /**
   * 修改记录
   * @param hardwarelibInf
   * @return R
   */
  @SysLog("修改接口设计")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('libs_hardwarelibinf_edit')")
  public R update(@RequestBody HardwarelibInf hardwarelibInf){
    return new R<>(hardwarelibInfService.saveInf(hardwarelibInf));
  }

  /**
   * 通过id删除一条记录
   * @param id
   * @return R
   */
  @SysLog("删除接口设计")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('libs_hardwarelibinf_del')")
  public R removeById(@PathVariable String id){
    return new R<>(hardwarelibInfService.removeById(id));
  }

/**
 * 新增接口
 * @param hardwarelibInf
 * @return
 */
  @PostMapping("/saveInf")
  public void saveInf(@RequestBody HardwarelibInf hardwarelibInf) {
    hardwarelibInfService.saveInf(hardwarelibInf);
  }

  /**
   * 编辑接口
   * @param hardwarelibInf
   */
  @PostMapping("updateInf")
  public void updateInf(@RequestBody HardwarelibInf hardwarelibInf) {
    hardwarelibInfService.updateInfById(hardwarelibInf);
  }

  /**
   * 左侧芯片回显
   * @return
   */
  @GetMapping("getInfList")
  public List<HardwarelibInf> getInfList() {
    return hardwarelibInfService.getInfList();
  }

}
