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
import com.inforbus.gjk.libs.api.entity.HardwarelibBoard;

/**
 * 板子设计
 *
 * @author pig code generator
 * @date 2019-06-01 10:28:01
 */
public interface HardwarelibBoardService extends IService<HardwarelibBoard> {

  /**
   * 板子设计简单分页查询
   * @param hardwarelibBoard 板子设计
   * @return
   */
  IPage<HardwarelibBoard> getHardwarelibBoardPage(Page<HardwarelibBoard> page, HardwarelibBoard hardwarelibBoard);

	void saveBoard(HardwarelibBoard hardwarelibBoard);

    void updateBoardById(HardwarelibBoard hardwarelibBoard);
}
