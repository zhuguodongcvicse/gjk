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
import com.inforbus.gjk.libs.api.entity.HardwarelibChip;
import com.inforbus.gjk.libs.api.entity.HardwarelibInf;
import com.inforbus.gjk.libs.service.HardwarelibChipService;
import com.inforbus.gjk.libs.service.HardwarelibInfService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;


/**
 * 芯片设计
 *
 * @author pig code generator
 * @date 2019-05-30 09:45:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hardwarelibchip")
public class HardwarelibChipController {

    private final HardwarelibChipService hardwarelibChipService;

    /**
     * 简单分页查询
     *
     * @param page            分页对象
     * @param hardwarelibChip 芯片设计
     * @return
     */
    @GetMapping("/page")
    public R<IPage<HardwarelibChip>> getHardwarelibChipPage(Page<HardwarelibChip> page, HardwarelibChip hardwarelibChip) {
        return new R<>(hardwarelibChipService.getHardwarelibChipPage(page, hardwarelibChip));
    }


    /**
     * 通过id查询单条记录
     *
     * @param id
     * @return R
     */
    @GetMapping("/{id}")
    public R<HardwarelibChip> getById(@PathVariable("id") String id) {
        return new R<>(hardwarelibChipService.getById(id));
    }

    /**
     * 新增记录
     *
     * @param hardwarelibChip
     * @return R
     */
    @SysLog("新增芯片设计")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('libs_hardwarelibchip_add')")
    public R save(@RequestBody HardwarelibChip hardwarelibChip) {
        return new R<>(hardwarelibChipService.save(hardwarelibChip));
    }

    /**
     * 修改记录
     *
     * @param hardwarelibChip
     * @return R
     */
    @SysLog("修改芯片设计")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('libs_hardwarelibchip_edit')")
    public R update(@RequestBody HardwarelibChip hardwarelibChip) {
        return new R<>(hardwarelibChipService.updateById(hardwarelibChip));
    }

    /**
     * 通过id删除一条记录
     *
     * @param id
     * @return R
     */
    @SysLog("删除芯片设计")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('libs_hardwarelibchip_del')")
    public R removeById(@PathVariable String id) {
        return new R<>(hardwarelibChipService.removeById(id));
    }

    /**
     * 芯片新增
     *
     * @return
     */
    @PostMapping("/saveChip")
    public void saveChip(@RequestBody(required = false) HardwarelibChip hardwarelibChip) {
        System.out.println("hardwarelibChip: " + hardwarelibChip);
        hardwarelibChipService.saveChip(hardwarelibChip);
    }

    /**
     * 芯片编辑
     * @param hardwarelibChip
     */
    @PostMapping("updateChip")
    public void updateChip(@RequestBody HardwarelibChip hardwarelibChip) {
        System.out.println(hardwarelibChip);
        hardwarelibChipService.updateChipById(hardwarelibChip);
    }

    /**
     * 获取所有芯片
     * @return
     */
    @GetMapping("getChipList")
    public List<HardwarelibChip> getChipList() {
        return hardwarelibChipService.list();
    }
}
