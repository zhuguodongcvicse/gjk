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
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.libs.api.entity.HardwarelibCase;
import com.inforbus.gjk.libs.mapper.HardwarelibCaseMapper;
import com.inforbus.gjk.libs.service.HardwarelibCaseService;
import org.springframework.stereotype.Service;

/**
 * 机箱设计
 *
 * @author sun
 * @date 2019-06-18 17:00:04
 */
@Service("hardwarelibCaseService")
public class HardwarelibCaseServiceImpl extends ServiceImpl<HardwarelibCaseMapper, HardwarelibCase> implements HardwarelibCaseService {

  /**
   * 机箱设计简单分页查询
   * @param hardwarelibCase 机箱设计
   * @return
   */
  @Override
  public IPage<HardwarelibCase> getHardwarelibCasePage(Page<HardwarelibCase> page, HardwarelibCase hardwarelibCase){
      return baseMapper.getHardwarelibCasePage(page,hardwarelibCase);
  }

	@Override
	public void saveCase(HardwarelibCase hardwarelibCase) {
        if (hardwarelibCase.getId() != null && hardwarelibCase.getId() != "") {
            hardwarelibCase.setVersion(hardwarelibCase.getVersion() + 1);
        } else {
            hardwarelibCase.setVersion(1);
        }
		hardwarelibCase.setId(IdGenerate.uuid());
		baseMapper.insert(hardwarelibCase);
	}
}
