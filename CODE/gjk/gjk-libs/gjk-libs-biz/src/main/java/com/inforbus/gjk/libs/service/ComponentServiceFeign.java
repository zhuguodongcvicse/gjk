package com.inforbus.gjk.libs.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.comp.api.entity.Component;

@FeignClient(ServiceNameConstants.COMP_SERVICE)
public interface ComponentServiceFeign {
	/**
	 * 修改记录
	 * 
	 * @param component
	 * @return R
	 */
	@PutMapping("/component/modifyComp")
	public R modifyComp(@RequestBody Component component);
}
