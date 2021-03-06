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
package com.inforbus.gjk.libs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.libs.api.entity.Approval;
import com.inforbus.gjk.libs.api.entity.ApprovalApply;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 审批管理
 *
 * @author xiaohe
 * @date 2019-04-17 13:57:36
 */
public interface ApprovalMapper extends BaseMapper<Approval> {

	/**
	 * 审批管理简单分页查询
	 * 
	 * @param approval 审批管理
	 * @return
	 */
	IPage<Approval> getApprovalPage(Page page, @Param("approval") Approval approval);

	/**
	 * @Title: saveApproval
	 * @Description: 新增审批记录
	 * @Author cvics
	 * @DateTime 2019年6月4日 下午8:18:15
	 * @param approval
	 * @return
	 */
	void saveApproval(@Param("approval") Approval approval);

	/**
	 * @Title: getApprovalById
	 * @Description: 根据ID查询记录
	 * @Author cvics
	 * @DateTime 2019年6月5日 上午8:52:15
	 * @param id
	 * @return
	 */
	Approval getApprovalById(@Param("id") String id);

	/**
	 * @Title: getIdByApplyId
	 * @Description: 判断是否在审批申请表里有记录
	 * @Author cvics
	 * @DateTime 2019年6月14日 下午4:46:37
	 * @param applyId
	 * @return
	 */
	Approval getIdByApplyId(@Param("applyId") String applyId);

	/**
	 * 新增审批记录和需要审批的id的对应记录
	 * 
	 * @param applovalApply
	 */
	void saveApprovalApply(@Param("approvalApply") ApprovalApply approvalApply);

	/**
	 * 根据id查询审批记录和需要审批的id的关系记录
	 * 
	 * @param applovalApply
	 * @return
	 */
	ApprovalApply getApprovalApplyById(@Param("approvalApply") ApprovalApply approvalApply);

	/**
	 * 根据审批记录的ID和需要审批的id查找在关联表中的ID
	 * 
	 * @param approvalApply
	 * @return
	 */
	ApprovalApply getIdByApprovalIdApplyId(@Param("approvalApply") ApprovalApply approvalApply);

	/**
	 * 根据审批记录ID查找到所有详细信息
	 * 
	 * @param approvalApply
	 * @return
	 */
	List<ApprovalApply> getAllApprovalApplyByApprovalId(@Param("approvalId") String approvalId);

	/**
	 * 查询所有未审批记录
	 * 
	 * @param approval
	 * @return
	 */
	List<Approval> getUnprocessedRecord(@Param("approval") Approval approval);

	void removeCompApproval(@Param("idList") List<String> idList, @Param("compId") String compId);

	List<Approval> getByApplyId(@Param("projectId") String projectId);

}
