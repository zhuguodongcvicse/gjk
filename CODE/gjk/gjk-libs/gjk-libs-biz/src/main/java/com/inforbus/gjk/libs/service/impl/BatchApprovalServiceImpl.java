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

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.entity.BatchApproval;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.mapper.BatchApprovalMapper;
import com.inforbus.gjk.libs.service.BatchApprovalService;
import com.inforbus.gjk.libs.service.CommonComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-11-27 16:44:41
 */
@Service("batchApprovalService")
public class BatchApprovalServiceImpl extends ServiceImpl<BatchApprovalMapper, BatchApproval> implements BatchApprovalService {

    @Autowired
    private CommonComponentService CommonComponentService;
  /**
   * 简单分页查询
   * @param batchApproval 
   * @return
   */
  @Override
  public IPage<BatchApproval> getBatchApprovalPage(Page<BatchApproval> page, BatchApproval batchApproval){
      return baseMapper.getBatchApprovalPage(page,batchApproval);
  }

    @Override
    public R saveBatchApproval(BatchApproval batchApproval) {
        batchApproval.setId(IdGenerate.uuid());
        baseMapper.saveBatchApproval(batchApproval);
        return new R<>(CommonConstants.SUCCESS,batchApproval.getId());
    }

    @Override
    public R getComponentByApplyId(String applyId) {
        BatchApproval batchApproval = baseMapper.selectById(applyId);
        ArrayList<CommonComponent> commonComponents = Lists.newArrayList();
        JSONArray objects1 = JSONUtil.parseArray(batchApproval.getIdListJson());
        objects1.stream().forEach(id->{
            CommonComponent commonComponent = CommonComponentService.getById((String) id);
            commonComponents.add(commonComponent);
        });
        return new R<>(CommonConstants.SUCCESS,"",commonComponents);
    }
}
