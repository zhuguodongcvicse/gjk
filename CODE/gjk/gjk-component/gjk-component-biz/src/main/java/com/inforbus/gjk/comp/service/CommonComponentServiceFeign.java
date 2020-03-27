package com.inforbus.gjk.comp.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.libs.api.entity.CommonComponent;

@FeignClient(ServiceNameConstants.LIBS_SERVICE)
public interface CommonComponentServiceFeign {
	/**
	 * 简单分页查询
	 * 
	 * @param page            分页对象
	 * @param commonComponent 公共构件库详细表
	 * @return
	 */
	@GetMapping("/commoncomponent/page")
	public R<IPage<CommonComponent>> getCommonComponentPage(@RequestParam("page")  Page<CommonComponent> page,
			@RequestParam("commonComponent") CommonComponent commonComponent);

	/**
	 * 修改记录
	 * 
	 * @param commonComponent
	 * @return R
	 */
	@SysLog("修改公共构件库详细表")
	@PutMapping("/commoncomponent")
	public R update(@RequestBody CommonComponent commonComponent);
	
}
