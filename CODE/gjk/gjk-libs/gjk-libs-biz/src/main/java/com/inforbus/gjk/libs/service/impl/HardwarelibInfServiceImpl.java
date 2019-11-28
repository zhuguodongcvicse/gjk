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
import com.inforbus.gjk.libs.api.entity.HardwarelibInf;
import com.inforbus.gjk.libs.mapper.HardwarelibInfMapper;
import com.inforbus.gjk.libs.service.HardwarelibInfService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 接口设计
 *
 * @author pig code generator
 * @date 2019-05-25 15:15:23
 */
@Service("hardwarelibInfService")
public class HardwarelibInfServiceImpl extends ServiceImpl<HardwarelibInfMapper, HardwarelibInf> implements HardwarelibInfService {

  /**
   * 接口设计简单分页查询
   * @param hardwarelibInf 接口设计
   * @return
   */
  @Override
  public IPage<HardwarelibInf> getHardwarelibInfPage(Page<HardwarelibInf> page, HardwarelibInf hardwarelibInf){
	  IPage<HardwarelibInf> hardwarelibInfPage = baseMapper.getHardwarelibInfPage(page, hardwarelibInf);
	  return hardwarelibInfPage;
  }

	@Override
	public HardwarelibInf saveInf(HardwarelibInf hardwarelibInf) {
		hardwarelibInf.setDelFlag("0");
		if (hardwarelibInf.getId() != null && hardwarelibInf.getId() != "") {
			hardwarelibInf.setVersion(hardwarelibInf.getVersion() + 1);
		} else {
			hardwarelibInf.setVersion(1);
		}
//		long startTime = System.currentTimeMillis();
//		for (int i = 0; i < 30; i++) {
			hardwarelibInf.setId(IdGenerate.uuid());
			baseMapper.saveInf(hardwarelibInf);
//		}

//		long endTime = System.currentTimeMillis();
//		System.out.println("startTime - endTime: " + (startTime - endTime) + "/ms");
		return hardwarelibInf;
	}

	@Override
	public List<HardwarelibInf> getInfList() {
		List<HardwarelibInf> infList = baseMapper.selectList(null);
		return infList;
	}

}
