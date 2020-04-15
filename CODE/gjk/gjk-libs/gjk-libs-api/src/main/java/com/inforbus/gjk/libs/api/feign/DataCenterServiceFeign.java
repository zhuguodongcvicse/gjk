package com.inforbus.gjk.libs.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.feign.factory.DataCenterServiceFallbackFactory;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, fallbackFactory = DataCenterServiceFallbackFactory.class)
public interface DataCenterServiceFeign {

	/**
	 * 三个库读取程序文本编辑器文件
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	@PostMapping("/fileServe/readFiles")
	public R readFiles(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) ;
	
	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 * @param filePath 文件路径
	 * @param textContext 文本内容
	 */
	@PostMapping("/fileServe/saveFileContext")
	public R saveFileContext(@RequestParam("filePath") String filePath,@RequestParam("textContext") String textContext) ;
}
