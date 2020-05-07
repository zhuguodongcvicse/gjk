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
package com.inforbus.gjk.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.admin.api.dto.BaseTemplateDTO;
import com.inforbus.gjk.admin.api.dto.BaseTemplatePathDTO;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.admin.api.entity.BaseTemplate;
import com.inforbus.gjk.admin.service.BaseTemplateService;

import lombok.AllArgsConstructor;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BaseTemplateController
 *
 * @author wang
 * @date 2020/4/15
 * @Description 基础模板功能控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basetemplate")
public class BaseTemplateController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseTemplateController.class);

    @Autowired
    private final BaseTemplateService baseTemplateService;

    /**
     * @Author wang
     * @Description: 简单分页查询
     * @Param: [page, baseTemplate]
     * @Return: com.inforbus.gjk.common.core.util.R<com.baomidou.mybatisplus.core.metadata.IPage                               <                               com.inforbus.gjk.admin.api.entity.BaseTemplate>>
     * @Create: 2020/4/15
     */
    @GetMapping("/page")
    public R<IPage<BaseTemplate>> getBaseTemplatePage(Page<BaseTemplate> page, BaseTemplate baseTemplate) {
        return new R<>(baseTemplateService.getBaseTemplatePage(page, baseTemplate));
    }

    /**
     * @Author wang
     * @Description: 通过id查询单条记录
     * @Param: [tempId]
     * @Return: com.inforbus.gjk.common.core.util.R<com.inforbus.gjk.admin.api.entity.BaseTemplate>
     * @Create: 2020/4/15
     */
    @GetMapping("/{tempId}")
    public R<BaseTemplate> getById(@PathVariable("tempId") String tempId) {
        return new R<>(baseTemplateService.getById(tempId));
    }

    /**
     * @Author wang
     * @Description: 新增基础模板,
     * @Param: [baseTemplateDTO]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @SysLog("新增基础模板")
    @PostMapping("addBaseTemplate")
    @PreAuthorize("@pms.hasPermission('admin_basetemplate_add')")
    public R save(@RequestBody BaseTemplateDTO baseTemplateDTO) {
        boolean b = false;
        R r = new R();
        try {
            b = baseTemplateService.saveBaseTemplate(baseTemplateDTO);
            if (!b) {
                r.setCode(CommonConstants.FAIL);
                r.setMsg("基础模板保存失败,请检查模板文件是否正确");
            }
        } catch (Exception e) {
            r.setCode(CommonConstants.FAIL);
            r.setMsg("基础模板保存失败,请检查模板文件是否正确" + e.getMessage());
            logger.error("基础模板保存失败,请检查模板文件是否正确" + e.getMessage());
        }
        r.setData(b);
        return r;
    }

    /**
     * @Author wang
     * @Description: 编辑功能
     * @Param: [baseTemplate]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @SysLog("修改基础模板")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_basetemplate_edit')")
    public R update(@RequestBody BaseTemplate baseTemplate) {
        boolean b = false;
        R r = new R();
        try {
            b = baseTemplateService.update(baseTemplate);
            r.setMsg("修改成功");
            if (!b) {
                r.setMsg("修改失败,请检查相关信息");
                r.setCode(CommonConstants.FAIL);
            }
        } catch (Exception e) {
            r.setCode(CommonConstants.FAIL);
            r.setMsg("修改失败,请检查相关信息" + e.getMessage());
            logger.error("修改失败,请检查相关信息" + e.getMessage());
        }
        r.setData(b);
        return r;
    }

    /**
     * @Author wang
     * @Description: 通过id删除一条记录
     * @Param: [tempId]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @SysLog("删除基础模板")
    @DeleteMapping("/{tempId}")
    @PreAuthorize("@pms.hasPermission('admin_basetemplate_del')")
    public R removeById(@PathVariable String tempId) {
        R r = new R();
        boolean b = baseTemplateService.removeById(tempId);
        try {
            if (!b) {
                r.setMsg("删除失败");
                r.setCode(CommonConstants.FAIL);
            }
        } catch (Exception e) {
            r.setMsg("删除失败,请联系管理员");
            r.setCode(CommonConstants.FAIL);
            logger.error("删除失败" + e.getMessage());
        }
        r.setData(b);
        return r;
    }

    /**
     * @Author wang
     * @Description: 解析xml功能
     * @Param: [baseTemplatePathDTO]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @SysLog("解析xml")
    @PostMapping("/parseXml")
    public R parseXml(@RequestBody BaseTemplatePathDTO baseTemplatePathDTO) {
        R r = new R();
        try {
            r.setMsg("解析成功");
            XmlEntityMap xmlEntityMap = baseTemplateService.parseXml(baseTemplatePathDTO.getPath());
            r.setData(xmlEntityMap);
        } catch (Exception e) {
            r.setCode(CommonConstants.FAIL);
            r.setMsg(baseTemplatePathDTO.getPath() + "文件不存在");
            logger.error("解析xml文件失败" + e.getMessage());
        }
        return r;
    }

    /**
     * @Author wang
     * @Description: 编辑基础模板的解析xml功能
     * @Param: [baseTemplate]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @SysLog("编辑功能解析xml文件")
    @PreAuthorize("@pms.hasPermission('admin_basetemplate_edit')")
    @PostMapping("/editParseXml")
    public R editParseXml(@RequestBody BaseTemplate baseTemplate) {
        R r = new R();
        try {
            r.setMsg("解析xml文件成功");
            r = baseTemplateService.editParseXml(baseTemplate);
        } catch (Exception e) {
            r.setCode(CommonConstants.FAIL);
            r.setMsg("解析xml文件失败" + e.getMessage());
        }
        return r;
    }

    /**
     * @Author wang
     * @Description: 基础模板保存编辑
     * @Param: [baseTemplateDTO]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @SysLog("编辑模板")
    @PreAuthorize("@pms.hasPermission('admin_basetemplate_edit')")
    @PutMapping("/editBaseTemplate")
    public R editBaseTemplate(@RequestBody BaseTemplateDTO baseTemplateDTO) {
        boolean b = false;
        R r = new R();
        try {
            r.setMsg("模板修改成功");
            b = baseTemplateService.editBaseTemplate(baseTemplateDTO);
            if (!b) {
                r.setMsg("模板修改失败,请联系管理员");
                r.setCode(CommonConstants.FAIL);
            }
        } catch (Exception e) {
            r.setMsg("模板修改失败,请联系管理员");
            r.setCode(CommonConstants.FAIL);
            logger.error("模板修改失败,请联系管理员" + e.getMessage());
        }
        r.setData(b);
        return r;
    }

    /**
     * @Author wang
     * @Description: 获取模板数据
     * @Param: []
     * @Return: java.util.List<com.inforbus.gjk.admin.api.entity.BaseTemplate>
     * @Create: 2020/4/15
     */
    @GetMapping("/getBaseTemplate")
    public List<BaseTemplate> getBaseTemplate() {
        return baseTemplateService.getBaseTemplate();
    }

    /**
     * @Author wang
     * @Description: 根据模板类型获取模板数据
     * @Param: [tempType]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @GetMapping("/getBaseTemplates/{tempType}")
    public R getBaseTemplates(@PathVariable String tempType) {
        return new R<>(baseTemplateService.getBaseTemplateByTempType(tempType));
    }

    /**
     * @Author wang
     * @Description: 根据模板类型获取模板数据
     * @Param: []
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/15
     */
    @GetMapping("/getLocalPath")
    public R getLocalPath() {
        return new R<>(baseTemplateService.getLocalPath());
    }
}
