package com.inforbus.gjk.dataCenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.api.utils.FtpClientUtil;
import com.inforbus.gjk.dataCenter.api.vo.DownloadFtpVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 说明：
 * 
 * Created by luojie on 2019/04/16.
 * 
 */

@RestController

@RequestMapping("/ftp/pturl")
public class FtpServerController {
	@Autowired
	private FtpClientUtil clientUtil;

	/**
	 * 上传
	 *
	 * @param file
	 * @param romte
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R fileUpload(@RequestPart("file") MultipartFile file, @RequestParam("romte") String romte) {
		// 上传
		return clientUtil.uploadMultipartFile(file, romte, false);
	}

	@PostMapping(value = "/download")
	public R<?> download(@RequestBody DownloadFtpVo ftpVo) {
		return clientUtil.download(ftpVo);
	}
}
