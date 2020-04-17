package com.inforbus.gjk.comp.api.feign;

import com.inforbus.gjk.comp.api.feign.factory.RemoteDataCenterServiceFallbackFactory;
import com.inforbus.gjk.comp.api.feign.factory.RemoteTokenServiceFallbackFactory;
import com.inforbus.gjk.dataCenter.api.entity.FileCenter;

import feign.Response;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

import com.inforbus.gjk.common.core.config.FeignSpringFormEncoder;
import com.inforbus.gjk.common.core.config.SpringMultipartEncoder;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: RemoteDataCenterService
 * @Desc 构件调用远程数据中
 * @Author xiaohe
 * @DateTime 2020年4月13日
 */
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, configuration = RemoteDataCenterService.FeignMultipartSupportConfig.class, fallbackFactory = RemoteDataCenterServiceFallbackFactory.class)
public interface RemoteDataCenterService {
	public static final String serviceName = "/fileServe";

	/**
	 * @ClassName: FeignMultipartSupportConfig
	 * @Desc Feign 上传文件时用到的配置
	 * @Author cvics
	 * @DateTime 2020年4月15日
	 */
	@Configuration
	public class FeignMultipartSupportConfig {
		@Autowired
		private ObjectFactory<HttpMessageConverters> messageConverters;

		@Bean
		public Encoder feignFormEncoder() {
			return new FeignSpringFormEncoder(new SpringEncoder(messageConverters));
		}
	}

	@PostMapping(value = serviceName + "/uploadMultipartFile", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> uploadLocalFile(@RequestPart(value = "file") MultipartFile ufile,
			@RequestParam("filePath") String filePath);

	@PostMapping(value = serviceName + "/uploadMultipartFiles", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> uploadLocalFiles(@RequestPart(value = "files") MultipartFile[] ufile);

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

	/**
	 * @Title: downloadStreamFile
	 * @Desc 下载单文件流（feign）
	 * @Author cvics
	 * @DateTime 2020年4月15日
	 * @param filePath
	 * @param response HttpServletResponse
	 * @return Response 其中包含 文件流
	 * 
	 *         <pre>
	 *         Response.Body body = response.body();
	 *         InputStream inputStream = body.asInputStream();
	 * 
	 *         </pre>
	 */
	@PostMapping(value = serviceName + "/downloadStreamFile", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response downloadStreamFile(@RequestParam("filePath") String filePath);

	/**
	 * @Title: analysisXmlFileToXMLEntityMap
	 * @Desc 解析xml文件为xmlEntity对象
	 * @Author xiaohe
	 * @DateTime 2020年4月16日
	 * @param localPath 文件的绝对路径
	 * @return
	 */
	@PostMapping(serviceName + "/analysisXmlFile")
	public R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath);

	/**
	 * @Title: createXMLFile
	 * @Desc 在指定位置生成xml文件
	 * @Author xiaohe
	 * @DateTime 2020年4月16日
	 * @param xMlEntityMapVO
	 * @return
	 */
	@PostMapping(serviceName + "/createXMLFile")
	public R<Boolean> createXMLFile(@RequestBody XMlEntityMapVO xMlEntityMapVO);
}
