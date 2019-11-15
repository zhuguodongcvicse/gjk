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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.libs.api.entity.HardwarelibChip;
import com.inforbus.gjk.libs.mapper.HardwarelibChipMapper;
import com.inforbus.gjk.libs.service.HardwarelibChipService;
import org.springframework.stereotype.Service;

/**
 * 芯片设计
 *
 * @author pig code generator
 * @date 2019-05-30 09:45:53
 */
@Service("hardwarelibChipService")
public class HardwarelibChipServiceImpl extends ServiceImpl<HardwarelibChipMapper, HardwarelibChip> implements HardwarelibChipService {

  /**
   * 芯片设计简单分页查询
   * @param hardwarelibChip 芯片设计
   * @return
   */
  @Override
  public IPage<HardwarelibChip> getHardwarelibChipPage(Page<HardwarelibChip> page, HardwarelibChip hardwarelibChip){
      return baseMapper.getHardwarelibChipPage(page,hardwarelibChip);
  }

	@Override
	public void saveChip(HardwarelibChip hardwarelibChip) {
		if (hardwarelibChip.getId() != null && hardwarelibChip.getId() != "") {
			hardwarelibChip.setVersion(hardwarelibChip.getVersion() + 1);
		} else {
			hardwarelibChip.setVersion(1);
		}
		hardwarelibChip.setId(IdGenerate.uuid());
		baseMapper.insert(hardwarelibChip);
	}

	@Override
	public void saveChipJson(HardwarelibChip hardwarelibChip) {
		baseMapper.insert(hardwarelibChip);
	}

	@Override
	public HardwarelibChip getChipByName(String chipName) {
		QueryWrapper<HardwarelibChip> wrapper = new QueryWrapper<>();
		wrapper.eq("chip_name",chipName);
		HardwarelibChip chip = baseMapper.selectOne(wrapper);
		System.out.println(chip);
		return chip;
	}

}
