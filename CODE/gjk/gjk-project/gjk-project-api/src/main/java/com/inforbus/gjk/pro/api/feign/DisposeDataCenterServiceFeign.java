package com.inforbus.gjk.pro.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.feign.factory.DisposeDataCenterServiceFallbackFactory;

import feign.Response;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, fallbackFactory = DisposeDataCenterServiceFallbackFactory.class)
public interface DisposeDataCenterServiceFeign {

	public static final String serviceName = "/fileServe";
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
	 
	 /**
		 * @Title: downloadStreamFiles
		 * @Desc 多文件下载（feign）
		 * @Author cvics
		 * @DateTime 2020年4月15日
		 * @param filePaths
		 * @return Response 其中包含 文件流
		 * 
		 *         <pre>
		 *         Response.Body body = response.body();
		 *         InputStream inputStream = body.asInputStream();
		 * 
		 *         </pre>
		 */
		@PostMapping(value = serviceName + "/downloadStreamFiles", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public Response downloadStreamFiles(@RequestParam("filePaths") String[] filePaths);
}
