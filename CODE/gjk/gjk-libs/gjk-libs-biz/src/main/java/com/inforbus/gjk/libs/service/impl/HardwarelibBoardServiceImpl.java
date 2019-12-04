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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.libs.api.entity.HardwarelibBoard;
import com.inforbus.gjk.libs.mapper.HardwarelibBoardMapper;
import com.inforbus.gjk.libs.service.HardwarelibBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        //设置UUID
        hardwarelibBoard.setId(IdGenerate.uuid());
        //设置板卡id
        if (hardwarelibBoard.getBoardId() ==null) {
            long currentTime = System.currentTimeMillis();
            hardwarelibBoard.setBoardId(hardwarelibBoard.getUserId() + currentTime);
        }
        //设置创建时间和更新时间
        hardwarelibBoard.setCreateTime(LocalDateTime.now());
        hardwarelibBoard.setUpdateTime(hardwarelibBoard.getCreateTime());
        hardwarelibBoard.setDelFlag("0");

        //只有入库才有版本
        hardwarelibBoard.setVersion(null);
		baseMapper.insert(hardwarelibBoard);
	}

    @Override
    public void updateBoardById(HardwarelibBoard hardwarelibBoard) {
        if ("2".equals(hardwarelibBoard.getApplyState())) {
            //版本临时变量
            int infVersionTemp;
            //如果版本为空则赋值为0（其实版本都是空）
            if (hardwarelibBoard.getVersion() != null) {
                infVersionTemp = hardwarelibBoard.getVersion();
            } else {
                infVersionTemp = 0;
            }
            //是否进入过for循环
            int ifSetVersionFlag = 1;
            //条件构造器
            QueryWrapper<HardwarelibBoard> wrapper = new QueryWrapper<>();
            //接口id相同条件
            wrapper.eq("board_id", hardwarelibBoard.getBoardId());
            //已入库条件，因为只有入库才有版本
            wrapper.eq("apply_state", hardwarelibBoard.getApplyState());
            //找出所有符合条件的接口
            List<HardwarelibBoard> hardwarelibInfs = baseMapper.selectList(wrapper);
            //循环找出最高版本，如果有更高版本则赋值给临时值
            for (HardwarelibBoard inf : hardwarelibInfs) {
                if (infVersionTemp <= inf.getVersion()) {
                    infVersionTemp = inf.getVersion();
                }
                //是否进入过循环标志赋值为0，说明有更高版本
                ifSetVersionFlag = 0;
            }
            //如果没有最高版本则设置版本为1
            if (ifSetVersionFlag == 1) {
                hardwarelibBoard.setVersion(1);
            } else {
                //有最高版本就在最高版本基础上+1
                hardwarelibBoard.setVersion(infVersionTemp + 1);
            }
        }
        hardwarelibBoard.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(hardwarelibBoard);
    }

}
