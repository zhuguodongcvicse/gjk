package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.dto.CopySoftwareAndBspDTO;
import com.inforbus.gjk.pro.api.dto.ModifyAssemblyDirDTO;
import com.inforbus.gjk.pro.api.feign.factory.AppSubassemblyServiceFallbackFactory;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
	 * 获取文件属性
	 * @param filePath
	 * @auther sunchao
	 * @return String
	 */
	@PostMapping("/getFileProperty")
	R getFileProperty(@RequestParam("filePath") String filePath, @RequestParam("fileProperty") String fileProperty);

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

	@PostMapping("/modifyAssemblyDir")
	R modifyAssemblyDir(@RequestBody ModifyAssemblyDirDTO modifyAssemblyDirDTO);

	@PostMapping("/copySoftwareAndBsp")
	R copySoftwareAndBsp(@RequestBody CopySoftwareAndBspDTO copySoftwareAndBspDTO);

	@PostMapping(value = "/transferFileToDestination", produces = {
			MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	R transferFileToDestination(@RequestParam("appDirPath") String appDirPath, @RequestPart("file")  MultipartFile file) throws IOException;

	@PostMapping("/createAppTree")
	R<List<ProjectFileVO>> createAppTree(@RequestParam("appPath") String appPath, @RequestParam("processId") String processId);
}
