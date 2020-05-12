package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.factory.AppSubassemblyServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE,
		url = "localhost:8080/dataCenter/appSubassemblyServer",
		fallbackFactory = AppSubassemblyServiceFallbackFactory.class)
public interface AppSubassemblyServiceFeign {

	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @auther sunchao
	 * @return
	 */
	@PostMapping("/judgeFileExist")
	R judgeFileExist(@RequestParam("filePath") String filePath);

	/**
	 * 查找App路径
	 * @param filePath
	 * @param selectFileName
	 * @auther sunchao
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/getAppPath")
	R getAppPath(@RequestParam("filePath") String filePath, @RequestParam("selectFileName") String selectFileName);
}
