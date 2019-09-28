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
import com.inforbus.gjk.libs.api.entity.HardwarelibBoard;
import com.inforbus.gjk.libs.api.entity.HardwarelibChip;
import com.inforbus.gjk.libs.service.HardwarelibBoardService;
import com.inforbus.gjk.libs.service.HardwarelibChipService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 板子设计
 *
 * @author pig code generator
 * @date 2019-06-01 10:28:01
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hardwarelibboard")
public class HardwarelibBoardController {

  private final  HardwarelibBoardService hardwarelibBoardService;

  private final HardwarelibChipService hardwarelibChipService;

  @Autowired
	ApplicationContext applicationContext;

  /**
   * 简单分页查询
   * @param page 分页对象
   * @param hardwarelibBoard 板子设计
   * @return
   */
  @GetMapping("/page")
  public R<IPage<HardwarelibBoard>> getHardwarelibBoardPage(Page<HardwarelibBoard> page, HardwarelibBoard hardwarelibBoard) {
    return  new R<>(hardwarelibBoardService.getHardwarelibBoardPage(page,hardwarelibBoard));
  }


  /**
   * 通过id查询单条记录
   * @param id
   * @return R
   */
  @GetMapping("/{id}")
  public R<HardwarelibBoard> getById(@PathVariable("id") String id){
    return new R<>(hardwarelibBoardService.getById(id));
  }

  /**
   * 新增记录
   * @param hardwarelibBoard
   * @return R
   */
  @SysLog("新增板子设计")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('libs_hardwarelibboard_add')")
  public R save(@RequestBody HardwarelibBoard hardwarelibBoard){
    return new R<>(hardwarelibBoardService.save(hardwarelibBoard));
  }

  /**
   * 修改记录
   * @param hardwarelibBoard
   * @return R
   */
  @SysLog("修改板子设计")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('libs_hardwarelibboard_edit')")
  public R update(@RequestBody HardwarelibBoard hardwarelibBoard){
    return new R<>(hardwarelibBoardService.updateById(hardwarelibBoard));
  }

  /**
   * 通过id删除一条记录
   * @param id
   * @return R
   */
  @SysLog("删除板子设计")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('libs_hardwarelibboard_del')")
  public R removeById(@PathVariable String id){
    return new R<>(hardwarelibBoardService.removeById(id));
  }

  @PostMapping("saveBoard")
	public void saveBoard(@RequestBody(required = false) HardwarelibBoard hardwarelibBoard){
  		hardwarelibBoardService.saveBoard(hardwarelibBoard);
  }

  @GetMapping("getChipData")
	public List<HardwarelibChip> getChipData(){
		return hardwarelibChipService.list(null);
  }

  @PostMapping("updateBoard")
  public void updateBoard(@RequestBody(required = false) HardwarelibBoard hardwarelibBoard){
    hardwarelibBoardService.updateById(hardwarelibBoard);
  }

  @GetMapping("getBoardJson/{id}")
  public HardwarelibBoard getBoardJson(@PathVariable String id){
    return hardwarelibBoardService.getById(id);
  }
}
