package com.inforbus.gjk.libs.api.feign;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.config.FeignSpringFormEncoder;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.feign.factory.RemoteStructServiceFeignFallbackFactory;

import feign.Response;
import feign.codec.Encoder;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, configuration = RemoteStructServiceFeign.FeignMultipartSupportConfig.class, fallbackFactory = RemoteStructServiceFeignFallbackFactory.class)
public interface RemoteStructServiceFeign {
	public static final String serviceName = "/fileServe";
	public static final String serviceHeaderName = "/ExternalInfServer";

	/**
	 * @ClassName: FeignMultipartSupportConfig
	 * @Desc Feign 上传文件时用到的配置
	 * @Author xiaohe
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

	/**
	 * @Title: parseStruct
	 * @Desc 解析结构体（通用：包括系统表、内部表等）
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param filePath 结构体文件路径
	 * @return R<?> <结构体类型名，list<变量类型+空格+变量名>>
	 */
	@PostMapping(serviceHeaderName + "/parseStruct")
	public R<Map<String, List<String>>> parseStruct(@RequestParam("filePath") String filePath);

	/**
	 * @Title: uploadDecompression
	 * @Desc
	 * @Author 解压文件(ZIP)
	 * @DateTime 2020年5月8日
	 * @param file      文件
	 * @param localPath 文件路径
	 * @return
	 */
	@PostMapping(value = serviceName + "/decompression", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<Boolean> uploadDecompression(@RequestPart(value = "files") MultipartFile file,
			@RequestParam("filePath") String localPath);

	/**
	 * @param sourcePath 文件路径
	 * @return
	 * @Title: delAllFile
	 * @Description: 删除指定文件夹下的所有文件
	 * @Author xiaohe
	 * @DateTime 2020年5月9日
	 */
	@PostMapping(value = serviceName + "/delAllFile")
	public R<Boolean> delAllFile(@RequestParam("sourcePath") String sourcePath);

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
	@PostMapping(value = serviceName + "/downloadStreamFilesTarget", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response downloadStreamFiles(@RequestPart("file") MultipartFile[] ufile,
			@RequestParam("fileTarget") String[] fileTarget, @RequestParam("filePaths") String filePaths);
}
