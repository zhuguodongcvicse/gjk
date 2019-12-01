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
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.entity.BatchApproval;
import com.inforbus.gjk.libs.service.BatchApprovalService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 
 *
 * @author pig code generator
 * @date 2019-11-27 16:44:41
 */
@RestController
@AllArgsConstructor
@RequestMapping("/batchapproval")
public class BatchApprovalController {

  private final  BatchApprovalService batchApprovalService;

  /**
   * 简单分页查询
   * @param page 分页对象
   * @param batchApproval 
   * @return
   */
  @GetMapping("/page")
  public R<IPage<BatchApproval>> getBatchApprovalPage(Page<BatchApproval> page, BatchApproval batchApproval) {
    return  new R<>(batchApprovalService.getBatchApprovalPage(page,batchApproval));
  }


  /**
   * 通过id查询单条记录
   * @param id
   * @return R
   */
  @GetMapping("/{id}")
  public R<BatchApproval> getById(@PathVariable("id") String id){
    return new R<>(batchApprovalService.getById(id));
  }

  /**
   * 新增记录
   * @param batchApproval
   * @return R
   */
  @SysLog("新增")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('libs_batchapproval_add')")
  public R save(@RequestBody BatchApproval batchApproval){
    return new R<>(batchApprovalService.save(batchApproval));
  }

  /**
   * 修改记录
   * @param batchApproval
   * @return R
   */
  @SysLog("修改")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('libs_batchapproval_edit')")
  public R update(@RequestBody BatchApproval batchApproval){
    return new R<>(batchApprovalService.updateById(batchApproval));
  }

  /**
   * 通过id删除一条记录
   * @param id
   * @return R
   */
  @SysLog("删除")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('libs_batchapproval_del')")
  public R removeById(@PathVariable String id){
    return new R<>(batchApprovalService.removeById(id));
  }

  @PutMapping("/saveBatchApproval")
  public R saveBatchApproval(@RequestBody BatchApproval batchApproval){
    return batchApprovalService.saveBatchApproval(batchApproval);
  }

  @GetMapping("/getComponentByApplyId/{id}")
  public R getComponentByApplyId(@PathVariable("id") String applyId){
    return batchApprovalService.getComponentByApplyId(applyId);
  }
}
