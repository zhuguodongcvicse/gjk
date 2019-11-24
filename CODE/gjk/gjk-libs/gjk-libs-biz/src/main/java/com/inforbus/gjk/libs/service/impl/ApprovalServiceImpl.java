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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.entity.Approval;
import com.inforbus.gjk.libs.api.entity.ApprovalApply;
import com.inforbus.gjk.libs.mapper.ApprovalMapper;
import com.inforbus.gjk.libs.service.ApprovalService;
import org.springframework.stereotype.Service;

/**
 * 审批管理
 *
 * @author xiaohe
 * @date 2019-04-17 13:57:36
 */
@Service("approvalService")
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, Approval> implements ApprovalService {

	private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);

	/**
	 * 审批管理简单分页查询
	 * 
	 * @param approval 审批管理
	 * @return
	 */
	@Override
	public IPage<Approval> getApprovalPage(Page<Approval> page, Approval approval) {
		return baseMapper.getApprovalPage(page, approval);
	}

	/**
	 * @saveApproval
	 * @Description: 保存审批记录
	 * @Author cvics
	 * @DateTime 2019年6月5日 上午9:26:18
	 * @param approval
	 * @return
	 * @see com.inforbus.gjk.libs.service.ApprovalService#saveApproval(com.inforbus.gjk.libs.api.entity.Approval)
	 */
	@Override
	public Approval saveApproval(Approval approval) {
		approval.setId(IdGenerate.uuid());
		approval.setApplyTime(LocalDateTime.now());
		approval.setCreateTime(approval.getApplyTime());
		try {
			baseMapper.saveApproval(approval);
			return this.getById(approval.getId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存记录错误");
			return null;
		}
	}

	/**
	 * @getApprovalById
	 * @Description: 根据id查询记录
	 * @Author cvics
	 * @DateTime 2019年6月5日 上午9:26:41
	 * @param id
	 * @return
	 * @see com.inforbus.gjk.libs.service.ApprovalService#getApprovalById(java.lang.String)
	 */
	@Override
	public Approval getApprovalById(String id) {
		return baseMapper.getApprovalById(id);
	}

	@Override
	public Approval getIdByApplyId(String applyId) {
		Approval approval = baseMapper.getIdByApplyId(applyId);
		if (approval != null) {
			return approval;
		}
		return null;
	}

	private ApprovalApply isApprovalIdApplyIdExist(ApprovalApply approvalApply) {
		return baseMapper.getIdByApprovalIdApplyId(approvalApply);
	}

	@Override
	public List<ApprovalApply> saveApprovalApply(String approvalId, List<String> applyIds) {
		List<ApprovalApply> approvalApplies = new ArrayList<ApprovalApply>();
		for (String applyId : applyIds) {
			ApprovalApply approvalApply = new ApprovalApply();
			approvalApply.setApprovalId(approvalId);
			approvalApply.setApplyId(applyId);
			ApprovalApply apply = isApprovalIdApplyIdExist(approvalApply);
			if (apply != null) {
				approvalApply = apply;
			} else {
				approvalApply.setId(IdGenerate.uuid());
				baseMapper.saveApprovalApply(approvalApply);
			}
			approvalApplies.add(approvalApply);
		}
		return approvalApplies;
	}

	@Override
	public List<ApprovalApply> getAllApprovalApplyByApprovalId(String approvalId) {
		return baseMapper.getAllApprovalApplyByApprovalId(approvalId);
	}

	@Override
	public List<Approval> getUnprocessedRecord(Approval approval) {
		return baseMapper.getUnprocessedRecord(approval);
	}

	@Override
	public List<String> getPassCompByProId(String proId) {
		List<ApprovalApply> approvalApplies = baseMapper.getPassCompByProId(proId);
		List<String> idList = new ArrayList<String>();
		for (ApprovalApply approvalApply : approvalApplies) {
			idList.add(approvalApply.getApplyId());
		}
		return idList;
	}

	@Override
	public List<ApprovalApply> getApprovalApplyIdByList(List<ApprovalApply> approvalApplies) {
		List<ApprovalApply> list = new ArrayList<ApprovalApply>();
		for (ApprovalApply item : approvalApplies) {
			String approvalState = item.getApprovalState();
			ApprovalApply approvalApply = isApprovalIdApplyIdExist(item);
			approvalApply.setApprovalState(approvalState);
			list.add(approvalApply);
		}
		return list;
	}

	@Override
	public void removeCompApproval(String compId, String projectId) {
		List<Approval> approvalApp = baseMapper.getByApplyId(projectId);
		List<String> idList = new ArrayList<String>();
		for(Approval approval:approvalApp) {
			idList.add(approval.getId());
		}
		baseMapper.removeCompApproval(idList,compId);	
	}

	@Override
	public String checkApproval(String projectId) {
		System.out.println(projectId);
		String checkApprovalState = "0";
		List<Approval> approvalApp = baseMapper.getByApplyId(projectId);
		for(Approval approval : approvalApp) {
			if("0".equals(approval.getApprovalState())) {
				checkApprovalState = "1";
			}
		}
		return checkApprovalState;
	}

}
