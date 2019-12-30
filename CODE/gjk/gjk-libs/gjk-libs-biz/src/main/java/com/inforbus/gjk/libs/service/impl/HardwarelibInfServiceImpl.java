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
import com.inforbus.gjk.libs.api.entity.HardwarelibInf;
import com.inforbus.gjk.libs.mapper.HardwarelibInfMapper;
import com.inforbus.gjk.libs.service.HardwarelibInfService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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
     *
     * @param hardwarelibInf 接口设计
     * @return
     */
    @Override
    public IPage<HardwarelibInf> getHardwarelibInfPage(Page<HardwarelibInf> page, HardwarelibInf hardwarelibInf) {
        IPage<HardwarelibInf> hardwarelibInfPage = baseMapper.getHardwarelibInfPage(page, hardwarelibInf);
        return hardwarelibInfPage;
    }

    @Override
    public HardwarelibInf saveInf(HardwarelibInf hardwarelibInf) {
        //如果InfId为空，则是新增，用用户名和时间戳新拼一个id
        if (hardwarelibInf.getInfId() == null) {
            long currentTime = System.currentTimeMillis();
            hardwarelibInf.setInfId(hardwarelibInf.getUserId() + currentTime);
        }
        hardwarelibInf.setDelFlag("0");
        //如果审批状态为空，则设置为0，即未处理
        if (hardwarelibInf.getApplyState() == null) {
            hardwarelibInf.setApplyState("0");
        }
        //只有入库才有版本
        hardwarelibInf.setVersion(null);
        //设置创建时间和更新时间
        hardwarelibInf.setCreateTime(LocalDateTime.now());
        hardwarelibInf.setUpdateTime(hardwarelibInf.getCreateTime());
        //设置uuid
        hardwarelibInf.setId(IdGenerate.uuid());
        baseMapper.saveInf(hardwarelibInf);

//		long startTime = System.currentTimeMillis();
//		long endTime = System.currentTimeMillis();
//		System.out.println("startTime - endTime: " + (startTime - endTime) + "/ms");
        return hardwarelibInf;
    }

    @Override
    public List<HardwarelibInf> getInfList() {
        return baseMapper.selectList(null);
    }

    @Override
    public void updateInfById(HardwarelibInf hardwarelibInf) {
        //如果已入库，则进行下列操作
        if ("2".equals(hardwarelibInf.getApplyState())) {
            //版本临时变量
            int infVersionTemp;
            //如果版本为空则赋值为0（其实版本都是空）
            if (hardwarelibInf.getVersion() != null) {
                infVersionTemp = hardwarelibInf.getVersion();
            } else {
                infVersionTemp = 0;
            }
            //是否进入过for循环
            int ifSetVersionFlag = 1;
            //条件构造器
            QueryWrapper<HardwarelibInf> wrapper = new QueryWrapper<>();
            //接口id相同条件
            wrapper.eq("inf_id", hardwarelibInf.getInfId());
            //已入库条件，因为只有入库才有版本
            wrapper.eq("apply_state", hardwarelibInf.getApplyState());
            //找出所有符合条件的接口
            List<HardwarelibInf> hardwarelibInfs = baseMapper.selectList(wrapper);
            //循环找出最高版本，如果有更高版本则赋值给临时值
            for (HardwarelibInf inf : hardwarelibInfs) {
                if (infVersionTemp <= inf.getVersion()) {
                    infVersionTemp = inf.getVersion();
                }
                //是否进入过循环标志赋值为0，说明有更高版本
                ifSetVersionFlag = 0;
            }
            //如果没有最高版本则设置版本为1
            if (ifSetVersionFlag == 1) {
                hardwarelibInf.setVersion(1);
            } else {
                //有最高版本就在最高版本基础上+1
                hardwarelibInf.setVersion(infVersionTemp + 1);
            }
        }
        hardwarelibInf.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(hardwarelibInf);
    }

}
