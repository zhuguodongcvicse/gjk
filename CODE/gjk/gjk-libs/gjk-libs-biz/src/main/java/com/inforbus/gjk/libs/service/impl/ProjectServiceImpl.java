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
package com.inforbus.gjk.libs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.libs.api.entity.Project;
import com.inforbus.gjk.libs.mapper.ProjectMapper;
import com.inforbus.gjk.libs.service.ProjectService;
import org.springframework.stereotype.Service;

/**
 * 项目库
 *
 * @author pig code generator
 * @date 2019-07-18 11:26:19
 */
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

  /**
   * 项目库简单分页查询
   * @param project 项目库
   * @return
   */
  @Override
  public IPage<Project> getProjectPage(Page<Project> page, Project project){
      return baseMapper.getProjectPage(page,project);
  }

}
