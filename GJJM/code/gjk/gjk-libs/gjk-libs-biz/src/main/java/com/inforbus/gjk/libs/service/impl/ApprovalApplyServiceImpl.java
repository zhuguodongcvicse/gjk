package com.inforbus.gjk.libs.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.libs.api.entity.ApprovalApply;
import com.inforbus.gjk.libs.mapper.ApprovalApplyMapper;
import com.inforbus.gjk.libs.service.ApprovalApplyService;

@Service("approvalApplyService")
public class ApprovalApplyServiceImpl extends ServiceImpl<ApprovalApplyMapper, ApprovalApply>
		implements ApprovalApplyService {

}
