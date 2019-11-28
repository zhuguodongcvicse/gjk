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
package com.inforbus.gjk.libs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.entity.BatchApproval;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-11-27 16:44:41
 */
public interface BatchApprovalService extends IService<BatchApproval> {

  /**
   * 简单分页查询
   * @param batchApproval 
   * @return
   */
  IPage<BatchApproval> getBatchApprovalPage(Page<BatchApproval> page, BatchApproval batchApproval);

  R saveBatchApproval(BatchApproval batchApproval);

  R getComponentByApplyId(String applyId);
}
