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
import com.inforbus.gjk.libs.api.entity.HardwarelibBoard;
import com.inforbus.gjk.libs.mapper.HardwarelibBoardMapper;
import com.inforbus.gjk.libs.service.HardwarelibBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 板子设计
 *
 * @author pig code generator
 * @date 2019-06-01 10:28:01
 */
@Service("hardwarelibBoardService")
public class HardwarelibBoardServiceImpl extends ServiceImpl<HardwarelibBoardMapper, HardwarelibBoard> implements HardwarelibBoardService {

  /**
   * 板子设计简单分页查询
   * @param hardwarelibBoard 板子设计
   * @return
   */
  @Override
  public IPage<HardwarelibBoard> getHardwarelibBoardPage(Page<HardwarelibBoard> page, HardwarelibBoard hardwarelibBoard){
      return baseMapper.getHardwarelibBoardPage(page,hardwarelibBoard);
  }

	@Override
	public void saveBoard(HardwarelibBoard hardwarelibBoard) {
        if (hardwarelibBoard.getId() != null && hardwarelibBoard.getId() != "") {
            hardwarelibBoard.setVersion(hardwarelibBoard.getVersion() + 1);
        } else {
            hardwarelibBoard.setVersion(1);
        }
		hardwarelibBoard.setId(IdGenerate.uuid());
		baseMapper.insert(hardwarelibBoard);
	}

}
