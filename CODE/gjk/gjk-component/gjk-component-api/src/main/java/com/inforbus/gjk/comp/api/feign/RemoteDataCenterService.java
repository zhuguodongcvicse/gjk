package com.inforbus.gjk.comp.api.feign;

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
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.comp.api.dto.ComponentDTO;
import com.inforbus.gjk.comp.api.entity.Components;
import com.inforbus.gjk.comp.api.feign.factory.RemoteDataCenterServiceFallbackFactory;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;

import feign.Response;
import feign.codec.Encoder;

/**
 * @ClassName: RemoteDataCenterService
 * @Desc 构件调用远程数据中
 * @Author xiaohe
 * @DateTime 2020年4月13日
 */
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, configuration = RemoteDataCenterService.FeignMultipartSupportConfig.class, fallbackFactory = RemoteDataCenterServiceFallbackFactory.class)
public interface RemoteDataCenterService {
	public static final String serviceName = "/fileServe";
	public static final String serviceHeaderName = "/ExternalInfServer";

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

	/**
	 * @Title: uploadLocalFile
	 * @Desc 文件上传
	 * @Author xiaohe
	 * @DateTime 2020年4月17日
	 * @param ufile     MultipartFile文件
	 * @param localPath 要上传的全文件路径
	 * @return
	 */
	@PostMapping(value = serviceName + "/uploadMultipartFile", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> uploadLocalFile(@RequestPart(value = "file") MultipartFile ufile,
			@RequestParam("filePath") String filePath);

	/**
	 * @Title: uploadLocalFiles
	 * @Desc 多文件上传 MultipartFile[]
	 * @Author xiaohe
	 * @DateTime 2020年4月17日
	 * @param ufile     MultipartFile[] 文件数组
	 * @param localPath 要上传的全文件路径
	 * @return
	 */
	@PostMapping(value = serviceName + "/uploadMultipartFiles", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> uploadLocalFiles(@RequestPart(value = "file") MultipartFile[] ufile,
			@RequestParam("filePath") String localPath);

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

	/**
	 * @Title: delFolder
	 * @Desc 删除文件或者文件夹，或多个文件和文件夹
	 * @Author xiaohe
	 * @DateTime 2020年4月20日
	 * @param folderPath 文件和文件夹列表
	 * @return
	 */
	@PostMapping(serviceName + "/delFolder")
	public R<Boolean> delFolder(@RequestParam("folderPath") String[] folderPath);
	@PostMapping(serviceName + "/delFile")
	public R<Boolean> delAllFile(@RequestParam("absolutePath") String absolutePath);

	/**
	 * @Title: getHeader
	 * @Desc 解析头文件
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param maps key is path
	 * 
	 * @return
	 */
	@PostMapping(serviceHeaderName + "/parseHeaderFile")
	public R<HeaderFileTransVO> getHeader(@RequestParam("path") String path);

	/**
	 * @Title: getPerformanceTable
	 * @Desc 解析性能测试表
	 * @Author xiaohe
	 * @DateTime 2020年5月7日
	 * @param maps key is excelPath
	 * @return
	 */
	@PostMapping(serviceHeaderName + "/parsePerformanceTable")
	public R<?> getPerformanceTable(@RequestParam("excelPath") String excelPath);

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
	public Response downloadStreamFiles(@RequestPart("file") MultipartFile ufile,
			@RequestParam("fileTarget") String fileTarget, @RequestParam("filePaths") String filePaths);

	/**
	 * 解析流程建模所有构件xml
	 * 
	 * @param filePathMap
	 * @return
	 */
	@PostMapping(value = serviceName + "/getCompData")
	public R<Map<String, XmlEntityMap>> getCompData(@RequestBody Map<String, String> filePathMap);

	/**
	 * 获取流程建模所有构件明细
	 */
	@PostMapping(value = serviceName + "/getCompDetailData")
	public R<List<ComponentDTO>> getCompDetailData(@RequestBody List<Components> componentsList);

	/**
	 * 递归遍历目录以及子目录中的所有文件
	 * 
	 * @param path 当前遍历文件或目录的路径
	 * @return 文件列表
	 */
	@PostMapping(value = serviceName + "/loopFiles", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public R<List<CompFilesVO>> loopFiles(@RequestParam("sourcePath") String filePath);
	
	
	/**
	 * @Title: downloadStreamFile
	 * @Desc 下载单文件流（feign）
	 * @Author cvics
	 * @DateTime 2020年5月31日
	 * @param path
	 * @param response HttpServletResponse
	 * @return Response 其中包含 文件流
	 * 
	 *         <pre>
	 *         Response.Body body = response.body();
	 *         InputStream inputStream = body.asInputStream();
	 * 
	 *         </pre>
	 */
	@PostMapping(value = serviceName + "/getImgFile")
	public Response getImgFile(@RequestParam("path") String path);
	@PostMapping( serviceName +"/copylocalFile")
	public R<Boolean> copylocalFile(@RequestParam("source") String source, @RequestParam("destin") String destin);

}
