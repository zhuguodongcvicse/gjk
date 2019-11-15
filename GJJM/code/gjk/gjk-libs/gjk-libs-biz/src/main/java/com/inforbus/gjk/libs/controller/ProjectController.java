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
import com.inforbus.gjk.libs.api.entity.Project;
import com.inforbus.gjk.libs.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 项目库
 *
 * @author pig code generator
 * @date 2019-07-18 11:26:19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectController {

  private final  ProjectService projectService;

  /**
   * 简单分页查询
   * @param page 分页对象
   * @param project 项目库
   * @return
   */
  @GetMapping("/page")
  public R<IPage<Project>> getProjectPage(Page<Project> page, Project project) {
    return  new R<>(projectService.getProjectPage(page,project));
  }


  /**
   * 通过id查询单条记录
   * @param id
   * @return R
   */
  @GetMapping("/{id}")
  public R<Project> getById(@PathVariable("id") String id){
    return new R<>(projectService.getById(id));
  }

  /**
   * 新增记录
   * @param project
   * @return R
   */
  @SysLog("新增项目库")
  @PostMapping
  @PreAuthorize("@pms.hasPermission('libs_project_add')")
  public R save(@RequestBody Project project){
    return new R<>(projectService.save(project));
  }

  /**
   * 修改记录
   * @param project
   * @return R
   */
  @SysLog("修改项目库")
  @PutMapping
  @PreAuthorize("@pms.hasPermission('libs_project_edit')")
  public R update(@RequestBody Project project){
    return new R<>(projectService.updateById(project));
  }

  /**
   * 通过id删除一条记录
   * @param id
   * @return R
   */
  @SysLog("删除项目库")
  @DeleteMapping("/{id}")
  @PreAuthorize("@pms.hasPermission('libs_project_del')")
  public R removeById(@PathVariable String id){
    return new R<>(projectService.removeById(id));
  }

}
