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

import java.time.LocalDateTime;
import java.util.List;

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
  		//设置UUID
		hardwarelibChip.setId(IdGenerate.uuid());
		//设置芯片id
		if (hardwarelibChip.getChipId() ==null) {
			long currentTime = System.currentTimeMillis();
			hardwarelibChip.setChipId(hardwarelibChip.getUserId().toString() + currentTime);
		}
		//设置创建时间和更新时间
		hardwarelibChip.setCreateTime(LocalDateTime.now());
		hardwarelibChip.setUpdateTime(hardwarelibChip.getCreateTime());
		hardwarelibChip.setDelFlag("0");

		//只有入库才有版本
		hardwarelibChip.setVersion(null);
		baseMapper.insert(hardwarelibChip);
	}

	@Override
	public void updateChipById(HardwarelibChip hardwarelibChip) {
		if ("2".equals(hardwarelibChip.getApplyState())) {
			//版本临时变量
			int infVersionTemp;
			//如果版本为空则赋值为0（其实版本都是空）
			if (hardwarelibChip.getVersion() != null) {
				infVersionTemp = hardwarelibChip.getVersion();
			} else {
				infVersionTemp = 0;
			}
			//是否进入过for循环
			int ifSetVersionFlag = 1;
			//条件构造器
			QueryWrapper<HardwarelibChip> wrapper = new QueryWrapper<>();
			//接口id相同条件
			wrapper.eq("chip_id", hardwarelibChip.getChipId());
			//已入库条件，因为只有入库才有版本
			wrapper.eq("apply_state", hardwarelibChip.getApplyState());
			//找出所有符合条件的接口
			List<HardwarelibChip> hardwarelibInfs = baseMapper.selectList(wrapper);
			//循环找出最高版本，如果有更高版本则赋值给临时值
			for (HardwarelibChip inf : hardwarelibInfs) {
				if (infVersionTemp <= inf.getVersion()) {
					infVersionTemp = inf.getVersion();
				}
				//是否进入过循环标志赋值为0，说明有更高版本
				ifSetVersionFlag = 0;
			}
			//如果没有最高版本则设置版本为1
			if (ifSetVersionFlag == 1) {
				hardwarelibChip.setVersion(1);
			} else {
				//有最高版本就在最高版本基础上+1
				hardwarelibChip.setVersion(infVersionTemp + 1);
			}
		}
		hardwarelibChip.setUpdateTime(LocalDateTime.now());
		baseMapper.updateById(hardwarelibChip);
	}
}
