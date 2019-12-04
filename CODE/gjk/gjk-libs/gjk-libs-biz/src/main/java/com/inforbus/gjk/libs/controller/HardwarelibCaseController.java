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
import com.inforbus.gjk.libs.api.entity.HardwarelibCase;
import com.inforbus.gjk.libs.api.entity.HardwarelibChip;
import com.inforbus.gjk.libs.service.HardwarelibBoardService;
import com.inforbus.gjk.libs.service.HardwarelibCaseService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 机箱设计
 *
 * @author sun
 * @date 2019-06-18 17:00:04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/hardwarelibcase")
public class HardwarelibCaseController {

    private final HardwarelibCaseService hardwarelibCaseService;

    private final HardwarelibBoardService hardwarelibBoardService;

    /**
     * 简单分页查询
     *
     * @param page            分页对象
     * @param hardwarelibCase 机箱设计
     * @return
     */
    @GetMapping("/page")
    public R<IPage<HardwarelibCase>> getHardwarelibCasePage(Page<HardwarelibCase> page, HardwarelibCase hardwarelibCase) {
        return new R<>(hardwarelibCaseService.getHardwarelibCasePage(page, hardwarelibCase));
    }


    /**
     * 通过id查询单条记录
     *
     * @param id
     * @return R
     */
    @GetMapping("/{id}")
    public R<HardwarelibCase> getById(@PathVariable("id") String id) {
        return new R<>(hardwarelibCaseService.getById(id));
    }

    /**
     * 新增记录
     *
     * @param hardwarelibCase
     * @return R
     */
    @SysLog("新增机箱设计")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('libs_hardwarelibcase_add')")
    public R save(@RequestBody HardwarelibCase hardwarelibCase) {
        return new R<>(hardwarelibCaseService.save(hardwarelibCase));
    }

    /**
     * 修改记录
     *
     * @param hardwarelibCase
     * @return R
     */
    @SysLog("修改机箱设计")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('libs_hardwarelibcase_edit')")
    public R update(@RequestBody HardwarelibCase hardwarelibCase) {
        return new R<>(hardwarelibCaseService.updateById(hardwarelibCase));
    }

    /**
     * 通过id删除一条记录
     *
     * @param id
     * @return R
     */
    @SysLog("删除机箱设计")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('libs_hardwarelibcase_del')")
    public R removeById(@PathVariable String id) {
        return new R<>(hardwarelibCaseService.removeById(id));
    }

    /**
     * 保存机箱
     * @param hardwarelibCase
     */
    @PostMapping("saveCase")
    public void saveCase(@RequestBody(required = false) HardwarelibCase hardwarelibCase) {
        hardwarelibCaseService.saveCase(hardwarelibCase);
    }

    /**
     * 编辑机箱
     * @param hardwarelibCase
     */
    @PostMapping("updateCase")
    public void updateBoard(@RequestBody HardwarelibCase hardwarelibCase) {
        System.out.println(hardwarelibCase);
        hardwarelibCaseService.updateCaseById(hardwarelibCase);
    }

    /**
     * 获取所有机箱
     * @return
     */
    @GetMapping("getCaseData")
    public List<HardwarelibCase> getCase() {
        List<HardwarelibCase> hardwarelibCaseList = hardwarelibCaseService.list(null);
        return hardwarelibCaseList;
    }
}
