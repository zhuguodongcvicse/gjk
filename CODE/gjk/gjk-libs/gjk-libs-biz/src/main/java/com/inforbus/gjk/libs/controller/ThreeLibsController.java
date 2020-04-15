/*
 *    Copyright (c) 2018-2025, inforbus All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the inforbus.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: inforbus
 */
package com.inforbus.gjk.libs.controller;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.service.CommonComponentDetailService;
import com.inforbus.gjk.libs.service.ThreeLibsService;

import lombok.AllArgsConstructor;

import java.io.*;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@RestController
@AllArgsConstructor
@RequestMapping("/threelibs")
public class ThreeLibsController {

	private final ThreeLibsService threeLibsService;
	private final CommonComponentDetailService commonComponentDetailService;

	/**
	 *
	 * @Title: getAlgorithmByLibsId
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/algorithmLib")
	public R getAlgorithmByLibsId() {
		return new R<>(threeLibsService.getAlgorithmByLibsId());
	}

	/**
	 *
	 * @Title: getAlgorithmFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/algorithmfile")
	public R getAlgorithmFile() {
		return new R<>(threeLibsService.getAlgorithmFile());
	}

	/**
	 *
	 * @Title: getPlatformByLibsId
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/platformLib")
	public R getPlatformByLibsId() {
		return new R<>(threeLibsService.getPlatformByLibsId());
	}

	/**
	 *
	 * @Title: getPlatformFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/platformfile")
	public R getPlatformFile() {
		return new R<>(threeLibsService.getPlatformFile());
	}

	/**
	 *
	 * @Title: getTestByLibsId
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/testLib")
	public R getTestByLibsId() {
		return new R<>(threeLibsService.getTestByLibsId());
	}

	/**
	 *
	 * @Title: getTestFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/testfile")
	public R getTestFile() {
		return new R<>(threeLibsService.getTestFile());
	}

	/**
	 * 根据id获取文件路径
	 *
	 * @Title: getAlgorithmFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月5日 下午5:29:09
	 * @return
	 */
	@GetMapping("/algorithmfilepath/{id}")
	public R getAlgorithmFilePath(@PathVariable("id") String id) {
		return new R<>(threeLibsService.getAlgorithmFilePath(id));
	}

	/**
	 * 根据文件路径取文件内容
	 *
	 * @Title: fileRead
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月10日 上午9:55:30
	 * @throws Exception
	 */

	@PostMapping("/readAlgorithmfile")
	public R fileRead(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		return threeLibsService.fileRead(threeLibsFilePathDTO);
	}

	public String codeString(String fileName) throws Exception {
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();
		bin.close();
		String code = null;

		switch (p) {
		case 298127:
			code = "UTF-8";
			break;
		case 65534:
			code = "Unicode";
			break;
		case 65279:
			code = "UTF-16BE";
			break;
		default:
			code = "GBK";
		}

		return code;
	}

	/**
	 * 得到平台库的文件夹树
	 *
	 * @Title: getAlgorithmFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月5日 下午5:29:09
	 * @return
	 */
	@GetMapping("/getSoftwarePlatformTree")
	public R getSoftwarePlatformTree() {
		System.out.println("ppppppp");
		try {
			List<Software> soft = threeLibsService.getSoftware();
			List<SoftwareDetail> softdetail = threeLibsService.getSoftwarePlatform();
			List<SoftwareFile> softfile = threeLibsService.getSoftwareFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new R<>();
	}

	@PostMapping("/saveFileContext")
	public void saveFileContext(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		System.out.println("uiuiiuiuiui:::" + threeLibsFilePathDTO.getFilePathName());
		threeLibsService.saveFileContext(threeLibsFilePathDTO.getFilePath(), threeLibsFilePathDTO.getFilePathName());
	}

	@GetMapping("/getFileStream/{filePath}")
	public void getFileStream(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("filePath") String filePath) {
		String replace = filePath.replace("!", "\\");
		response.setContentType("application/pdf");
		FileInputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(new File(replace));
			out = response.getOutputStream();
			byte[] b = new byte[512];
			while ((in.read(b)) != -1) {
				out.write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据libsid查询到已入库的表中是否选中三个库
	 * 
	 * @param platfromId
	 * @return
	 */
	@PostMapping("/findThreeLibsId/{platformId}")
	public boolean findThreeLibsId(@PathVariable("platformId") String platformId) {
		boolean bool = false;
		if (((threeLibsService.findBSPDetailByPlatformId(platformId)).size()) > 0) {
			bool = true;
		}
		if (((threeLibsService.findSoftwareDetailByPlatformId(platformId)).size()) > 0) {
			bool = true;
		}
		if (((threeLibsService.findComponentDetailByPlatformId(platformId)).size()) > 0) {
			bool = true;
		}
		if (((threeLibsService.findCompframeDetailByPlatformId(platformId)).size()) > 0) {
			bool = true;
		}
		if (((commonComponentDetailService.findCommonComponentDetailByLibsId(platformId)).size()) > 0) {
			bool = true;
		}
		return bool;
	}

}
