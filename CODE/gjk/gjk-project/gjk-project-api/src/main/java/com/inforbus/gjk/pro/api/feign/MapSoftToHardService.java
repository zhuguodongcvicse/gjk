package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RequestMapping("/mapSoftToHardInf")
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE)
public interface MapSoftToHardService {

	/**
	 * 软硬件映射调用c接口
	 * 
	 * @param exe
	 * @param hardWareFilePath 硬件模型文件
	 * @param mapConfigPath    软硬件映射配置文件
	 * @param sysParamFilePath 系统参数文件
	 * @param userName         用户名（登录）
	 * @return
	 * @throws IOException
	 */
	@PutMapping("/mapSoftToHard")
	public void mapSoftToHard(@RequestParam("exe") String exe,
			@RequestParam("hardWareFilePath") String hardWareFilePath,
			@RequestParam("mapConfigPath") String mapConfigPath,
			@RequestParam("sysParamFilePath") String sysParamFilePath, @RequestParam("userName") String userName);
	
}
