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
package com.inforbus.gjk.libs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.entity.Approval;
import com.inforbus.gjk.libs.api.entity.ApprovalApply;
import com.inforbus.gjk.libs.service.ApprovalApplyService;
import com.inforbus.gjk.libs.service.ApprovalService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 审批管理
 *
 * @author xiaohe
 * @date 2019-04-17 13:57:36
 */
@RestController
@AllArgsConstructor
@RequestMapping("/approval")
public class ApprovalController {

	private final ApprovalService approvalService;
	private final ApprovalApplyService approvalApplyService;

	/**
	 * 简单分页查询
	 * 
	 * @param page     分页对象
	 * @param approval 审批管理
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<Approval>> getApprovalPage(Page<Approval> page, Approval approval) {
		return new R<>(approvalService.getApprovalPage(page, approval));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<Approval> getById(@PathVariable("id") String id) {
		return new R<>(approvalService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param approval
	 * @return R
	 */
	@SysLog("新增审批管理")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('libs_approval_add')")
	public R save(@RequestBody Approval approval) {
		return new R<>(approvalService.save(approval));
	}

	/**
	 * 修改记录
	 * 
	 * @param approval
	 * @return R
	 */
	@SysLog("修改审批管理")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('libs_approval_edit')")
	public R update(@RequestBody Approval approval) {
		return new R<>(approvalService.updateById(approval));
	}

	/**
	 * 修改审批相关id的修改
	 * 
	 * @param approvalApply
	 * @return R
	 */
	@SysLog("修改审批管理相关ID记录")
	@PutMapping("/updateApprovalApplyById")
	public R update(@RequestBody List<ApprovalApply> approvalApplys) {
		try {
			for (ApprovalApply approvalApply : approvalService.getApprovalApplyIdByList(approvalApplys)) {
				approvalApplyService.updateById(approvalApply);
			}
			return new R<>(true);
		} catch (Exception e) {
			return new R<>(false);
		}
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除审批管理")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('libs_approval_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(approvalService.removeById(id));
	}

	/**
	 * 新增审批记录
	 * 
	 * @param approval
	 * @return R
	 */
	@PutMapping
	@RequestMapping("/saveApproval")
	public R saveApproval(@RequestBody Approval approval) {
		return new R<>(approvalService.saveApproval(approval));
	}

	/**
	 * @Title: getIdByApplyId
	 * @Description: 获取申请构件的申请记录ID
	 * @Author cvics
	 * @DateTime 2019年6月14日 下午4:44:11
	 * @param applyId
	 * @return
	 */
	@GetMapping
	@RequestMapping("/getIdByApplyId/{applyId}")
	public R getIdByApplyId(@PathVariable String applyId) {
		return new R<>(approvalService.getIdByApplyId(applyId));
	}

	@PutMapping
	@RequestMapping("/saveApprovalApply/{approvalId}")
	public R saveApprovalApply(@PathVariable String approvalId, @RequestBody List<String> applyIds) {
		return new R<>(approvalService.saveApprovalApply(approvalId, applyIds));
	}

	@GetMapping
	@RequestMapping("/getAllApprovalApplyByApprovalId/{approvalId}")
	public R getAllApprovalApplyByApprovalId(@PathVariable String approvalId) {
		return new R<>(approvalService.getAllApprovalApplyByApprovalId(approvalId));
	}

	@RequestMapping("/getUnprocessedRecord")
	public R getUnprocessedRecord(@RequestBody Approval approval) {
		return new R<>(approvalService.getUnprocessedRecord(approval));
	}

	@GetMapping("/getPassCompByProId/{approvalId}")
	public R getPassCompByProId(@PathVariable String approvalId) {
		return new R<>(approvalService.getPassCompByProId(approvalId));
	}

}
