package com.inforbus.gjk.pro.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.feign.factory.DisposeDataCenterServiceFallbackFactory;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, fallbackFactory = DisposeDataCenterServiceFallbackFactory.class)
public interface DisposeDataCenterServiceFeign {

	/**
	 * 解析xml文件为xmlMap对象
	 * @param [localPath] 文件的绝对路径
	 * @return
	 */
	@PostMapping("/fileServe/analysisXmlFile")
	public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath) ;
	
	/**
     * @Author wang
     * @Description: 在指定位置生成xml文件
     * @Param: [xMlEntityMapVO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
	 @PostMapping("/fileServe/createXMLFile")
	    public R<Boolean> createXMLFile(@RequestBody XMlEntityMapVO xMlEntityMapVO) ;
}
