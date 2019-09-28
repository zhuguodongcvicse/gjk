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
package com.inforbus.gjk.pro.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.FileDownload;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.pro.api.dto.AppDataDTO;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.service.AppService;

import ch.qos.logback.core.util.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-07-22 13:39:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class AppController {

	private final AppService appService;
//	private static String filePath = AppController.class.getResource("/").getPath().split("WEB-INF")[0] + "upload/";
	private static final String gitFilePath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 简单分页查询
	 * 
	 * @param page 分页对象
	 * @param app
	 * @return
	 */
	@GetMapping("/page")
	public R<IPage<App>> getAppPage(Page<App> page, App app) {
		return new R<>(appService.getAppPage(page, app));
	}

	/**
	 * 通过id查询单条记录
	 * 
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R<App> getById(@PathVariable("id") String id) {
		return new R<>(appService.getById(id));
	}

	/**
	 * 新增记录
	 * 
	 * @param app
	 * @return R
	 */
	@SysLog("新增")
	@PostMapping
	public R save(@RequestBody App app) {
		return new R<>(appService.saveApp(app));
	}

	/**
	 * 修改记录
	 * 
	 * @param app
	 * @return R
	 */
	@SysLog("修改")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('pro_app_edit')")
	public R update(@RequestBody App app) {
		return new R<>(appService.updateById(app));
	}

	/**
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@SysLog("删除")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('pro_app_del')")
	public R removeById(@PathVariable String id) {
		return new R<>(appService.removeById(id));
	}

	/**
	 * 获取所有app组件，用于展示
	 * 
	 * @return
	 */
	@GetMapping(value = "/showApp")
	public R getAllApp() {
		return new R<>(appService.getAllApp());
	}

	/**
	 * @Title getImgFile
	 * @Description: 获取图标文件
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 上午11:32:09
	 * @param compId
	 * @return
	 */
	@GetMapping("/appImg/{id}")
	public void getImgFile(@PathVariable String id, HttpServletRequest req, HttpServletResponse res) {
		OutputStream os = null;
		FileInputStream fis = appService.getAppImgFile(id);
		try {
			os = res.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((count = fis.read(buffer)) != -1) {
				os.write(buffer, 0, count);
				os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据app组件名称，模糊查询组件
	 * 
	 * @return
	 */
	@GetMapping(value = "/getAppVosPage/{fileName}")
	public R getAppVosPage(@PathVariable String fileName) {
		return new R<>(appService.getAppVosPage(fileName));
	}

	/**
	 * 
	 * @param cmpNameToHwType <组件名称，执行类型（平台）>
	 * @param workID          流程ID
	 * @param workname        APP名
	 * @param packinfoPath    packinfo文件路径
	 * @param workModeProPath 工程文件夹路径
	 * @return 执行结果(成功或失败)
	 */
	@GetMapping(value = "/appInstall")
	public R appInstall(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appInstall(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname(), appDataDTO.getPackinfoPath(), appDataDTO.getWorkModeProPath()));
	}

	/**
	 * 加载、更新加载
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appLoadStart")
	public R appLoadStart(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appLoadStart(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname(), appDataDTO.isExistDeployConfig(), appDataDTO.getSysconfigPath()));
	}

	/**
	 * 卸载
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appUnload")
	public R appUnload(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appUnload(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname()));
	}

	/**
	 * 启动
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appTaskRestart")
	public R appTaskRestart(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appTaskRestart(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname()));
	}

	/**
	 * 停止
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appStop")
	public R appStop(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appStop(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname()));
	}

	/**
	 * 暂停
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appPause")
	public R appPause(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appPause(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname()));
	}

	/**
	 * 注销
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appDelete")
	public R appDelete(AppDataDTO appDataDTO) {
		return new R<>(ExternalIOTransUtils.appDelete(appDataDTO.getCmpNameToHwType(), appDataDTO.getWorkID(),
				appDataDTO.getWorkname(), appDataDTO.getPackinfoPath()));
	}

	/**
	 * 导出
	 * 
	 * @param appDataDTO
	 * @return
	 */
	@GetMapping(value = "/appTaskExport")
	public void appTaskExport(AppDataDTO appDataDTO) {
		ExternalIOTransUtils.appTaskExport(appDataDTO.getWorkID(), appDataDTO.getTaskInfoPath(),
				appDataDTO.getAppPath(), appDataDTO.getSysconfigPath(), appDataDTO.getPackinfoPath(),
				appDataDTO.getCmpDeployPlanFilePath());
	}

	/**
	 * 根据appid 修改app的运行状态
	 * 
	 * @param appState
	 * @param id
	 */
	@PostMapping("/editAppState/{id}")
	public void editAppState(@RequestBody String appState, @PathVariable("id") String id) {
		appService.editAppState(id, appState.replace("\"", ""));
	}

	/**
	 * 导出文件夹
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	@RequestMapping(value = "export_zip.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public void zipwordDownAction(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, String> map) throws Exception {
		// 调用文件下载的方法
		FileDownload.zipwordDownAction(request, response, map);
	}

	/**
	 * 通过项目id拿到流程
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getProcessByProjectId/{id}")
	public ProjectFile getProcessByProjectId(@PathVariable("id") String id) {
		return appService.getProcessByProjectId(id);

	}

	/**
	 * 通过项目id拿到当前项目信息
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getprojectByProjectId/{id}")
	public Project getprojectByProjectId(@PathVariable("id") String id) {
		return appService.getprojectByProjectId(id);

	}

	@PostMapping("/createZipFile")
	public	void createZipFile(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, String> map) {
		FileInputStream inputStream = null;
		ByteArrayOutputStream outStream = null;
		String zipFileName = map.get("downloadAPPFileName") + ".zip";
		String zipFilePath = gitFilePath + "gjk" + File.separator + "APPDownload" + File.separator + zipFileName;
		try {
			org.apache.tools.ant.Project prj = new org.apache.tools.ant.Project();
			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(new File(zipFilePath));
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);

			File srcFile = null;
			srcFile = new File(map.get("oriFilePath"));
			if (srcFile.isDirectory()) {
				
				fileSet.setDir(srcFile);
			} else {
				fileSet.setFile(srcFile);
			}
			zip.addFileset(fileSet);
			zip.execute();

			int len = 0;
			inputStream = new FileInputStream(new File(zipFilePath));
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while ((len = inputStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();

			new File(zipFilePath).deleteOnExit();

			String userAgent = request.getHeader("User-Agent");
			String formFileName = new String(new File(zipFilePath).getName().getBytes());
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				formFileName = java.net.URLEncoder.encode(formFileName, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				formFileName = new String(formFileName.getBytes("UTF-8"), "ISO-8859-1");
			}

			response.reset();
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", formFileName));
			response.setHeader("FileName", formFileName);
			response.setHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream; charset=UTF-8");

			IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
		} catch (IORuntimeException | IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
