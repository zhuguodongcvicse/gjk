package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.config.FeignSpringFormEncoder;
import feign.codec.Encoder;
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

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.feign.factory.DisposeDataCenterServiceFallbackFactory;

import feign.Response;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE,
		url = "localhost:8080/dataCenter/fileServe",
		fallbackFactory = DisposeDataCenterServiceFallbackFactory.class,
		configuration = DisposeDataCenterServiceFeign.FeignMultipartSupportConfig.class)
public interface DisposeDataCenterServiceFeign {

	public static final String serviceName = "/fileServe";

	/**
	 * @ClassName: FeignMultipartSupportConfig
	 * @Desc Feign 上传文件时用到的配置
	 * @Author wang
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

	/**
	 * @Title: uploadLocalFiles
	 * @Desc 多文件上传 MultipartFile[]
	 * @Author wang
	 * @DateTime 2020年5月6日
	 * @param ufile     MultipartFile[] 文件数组
	 * @param localPath 要上传的文件绝对路径
	 * @return
	 */
	@PostMapping(value = "/uploadMultipartFiles", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> uploadLocalFiles(@RequestPart(value = "file") MultipartFile[] ufile,
								 @RequestParam("filePath") String localPath);

	/**
	 * @param source 源文件路径
	 * @param destin 拷贝文件路径
	 * @return R<Boolean>
	 * @throws Exception
	 * @Title: copylocalFile
	 * @Description: 拷贝文件
	 * @Author wang
	 * @DateTime 2020年5月07日
	 */
	@PostMapping("copylocalFile")
	public R<Boolean> copylocalFile(@RequestParam("source") String source, @RequestParam("destin") String destin);

	/**
	 * @param sourcePath 文件绝对路径
	 * @return R<Boolean>
	 * @Title: delAllFile
	 * @Description: 删除文件或文件夹
	 * @Author wang
	 * @DateTime 2020年5月07日
	 */
	@PostMapping("delAllFile")
	public R<Boolean> delAllFile(@RequestParam("sourcePath") String sourcePath);
}
