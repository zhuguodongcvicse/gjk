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
import com.inforbus.gjk.libs.api.entity.HardwarelibChip;

/**
 * 芯片设计
 *
 * @author pig code generator
 * @date 2019-05-30 09:45:53
 */
public interface HardwarelibChipService extends IService<HardwarelibChip> {

  /**
   * 芯片设计简单分页查询
   * @param hardwarelibChip 芯片设计
   * @return
   */
  IPage<HardwarelibChip> getHardwarelibChipPage(Page<HardwarelibChip> page, HardwarelibChip hardwarelibChip);

  	void saveChip(HardwarelibChip hardwarelibChip);


	void saveChipJson(HardwarelibChip hardwarelibChip);

	HardwarelibChip getChipByName(String chipName);
}
