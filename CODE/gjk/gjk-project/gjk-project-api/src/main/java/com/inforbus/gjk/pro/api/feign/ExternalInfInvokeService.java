package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping("/ExternalInfServer")
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE)
public interface ExternalInfInvokeService {


    @RequestMapping("/getCmpSysConfig")
    public R<Map<String, List<String>>> getCmpSysConfig(@RequestParam("netWorkConfigFileName") String netWorkConfigFileName,
                                                        @RequestParam("packinfoPath") String packinfoPath,
                                                        @RequestParam("workModeFileName") String workModeFileName);
	/**
	 * 自定义配置调用客户接口
	 * @param flowFilePath
	 * @param ThemeFilePath
	 * @param filePath
	 * @return
	 */
	@PostMapping("/createUserDefineTopic")
	public R<Boolean> createUserDefineTopic(@RequestParam("flowFilePath")String flowFilePath, 
			@RequestParam("ThemeFilePath")String ThemeFilePath, @RequestParam("filePath")String filePath);
}
