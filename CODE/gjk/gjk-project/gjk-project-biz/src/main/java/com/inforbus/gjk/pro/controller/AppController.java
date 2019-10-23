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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.inforbus.gjk.pro.service.impl.ManagerServiceImpl;

import ch.qos.logback.core.util.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.ho.yaml.Yaml;
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
	//packinfo文件路径(客户自存自取)
	private static final String softToHardResult = JGitUtil.getSoftToHardResult();
	//组件划分方案路径(客户自存自取)
	private static final String generateCodeResult = JGitUtil.getGenerateCodeResult();
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
	 * 通过id删除一条记录
	 * 
	 * @param id
	 * @return R
	 */
	@PostMapping("/deleteAppByAPPId/{id}")
	public void deleteAppByAPPId(@PathVariable("id") String id) {
		appService.deleteAppByAPPId(id);
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
	 * 注册
	 * 
	 * @param cmpNameToHwType <组件名称，执行类型（平台）>
	 * @param appID           流程ID(Int类型)
	 * @param AppName         APP名
	 * @param packinfoPath    packinfo文件路径（客户自存自取）
	 * @param cmpResFilePath  组件划分方案路径文件路径（客户自存自取）
	 * @param AppProPath      app工程文件夹路径
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appInstall")
	public R appInstall(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		String bb = appDataDTO.getSysconfigPath().replaceAll("\\\\", "/");
		//String aa = bb.substring(0,bb.lastIndexOf("/",bb.indexOf("/")+1));
		String aa = bb.substring(0,bb.lastIndexOf("/"));
		aa = aa.substring(0,aa.lastIndexOf("/"));
		//packinfo文件路径（客户自存自取的路径）
		String selfSoftToHardResult = gitFilePath + aa + File.separator + generateCodeResult  + File.separator + "packinfo.xml";
		//组件划分方案路径（自存自取）
		String selfGenerateCodeResult = gitFilePath + aa + File.separator + softToHardResult   + File.separator + "组件划分方案.xml";
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal ;
		try {
			// 调用注册接口
			returnVal = ExternalIOTransUtils.appInstall(cmpNameToHwType, appDataDTO.getFlowId(), appDataDTO.getAppName(),
					selfSoftToHardResult, selfGenerateCodeResult, gitFilePath + appDataDTO.getAppProPath());
		} catch (Exception e) {
			returnVal = false;
			e.printStackTrace();
		}
		return new R<>(returnVal);
	}

	/**
	 * 加载、更新加载
	 * 
	 * @param cmpNameToHwType   APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID             APP对应的流程ID
	 * @param appName           APP名称
	 * @param existDeployConfig APP组件工程配置
	 * @param sysconfigPath     系统配置模块XML路径
	 * @param appProPath        APP工程文件夹路径
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appLoadStart")
	public R appLoadStart(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 是否带部署方案(数据库存的字符串0、1，后台需要转换一下)
		boolean existDeployConfig;
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal ;
		if (appDataDTO.getExistDeployConfig().equals("1")) {
			existDeployConfig = false;
		} else {
			existDeployConfig = true;
		}
		try {
			// 调用加载、更新加载接口
			returnVal = ExternalIOTransUtils.appLoad(cmpNameToHwType, appDataDTO.getFlowId(), appDataDTO.getAppName(),
					existDeployConfig, appDataDTO.getSysconfigPath(), gitFilePath + appDataDTO.getAppProPath());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return new R<>(returnVal);
	}

	/**
	 * 卸载
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appUnload")
	public R appUnload(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用卸载接口
			returnVal = ExternalIOTransUtils.appUnload(cmpNameToHwType, appDataDTO.getFlowId(), appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return new R<>(returnVal);
	}

	/**
	 * 启动
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appTaskRestart")
	public R appTaskRestart(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用启动接口
			returnVal = ExternalIOTransUtils.appRestart(cmpNameToHwType, appDataDTO.getFlowId(),
					appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return new R<>(returnVal);
	}

	/**
	 * 停止
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appStop")
	public R appStop(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用停止接口
			returnVal = ExternalIOTransUtils.appStop(cmpNameToHwType, appDataDTO.getFlowId(), appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return new R<>(returnVal);
	}

	/**
	 * 暂停
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appPause")
	public R appPause(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 返回值
		boolean returnVal;
		try {
			// 调用暂停接口
			returnVal = ExternalIOTransUtils.appPause(cmpNameToHwType, appDataDTO.getFlowId(), appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return new R<>(returnVal);
	}

	/**
	 * 注销
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @param packinfoPath    客户自存自取路径
	 * @return 执行结果(成功或失败)
	 */
	@PostMapping(value = "/appDelete")
	public R appDelete(@RequestBody AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		String bb = appDataDTO.getSysconfigPath().replaceAll("\\\\", "/");
		//String aa = bb.substring(0,bb.lastIndexOf("/",bb.indexOf("/")+1));
		String aa = bb.substring(0,bb.lastIndexOf("/"));
		aa = aa.substring(0,aa.lastIndexOf("/"));
		//packinfo文件路径（客户自存自取的路径）
		String selfSoftToHardResult = gitFilePath + aa + File.separator + generateCodeResult  + File.separator + "packinfo.xml";
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal ;
		try {
			// 调用注销接口
			returnVal = ExternalIOTransUtils.appUnInstall(cmpNameToHwType, appDataDTO.getFlowId(),
					appDataDTO.getAppName(), selfSoftToHardResult);
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return new R<>(returnVal);
	}

	/**
	 * 导出
	 * 
	 * @param appId                 APP对应的流程ID
	 * @param appName               APP名称
	 * @param appPath               APP工程文件夹路径
	 * @param sysconfigPath         系统配置模块XML路径
	 * @param packinfoPath          客户自存自取路径
	 * @param cmpDeployPlanFilePath 客户自存自取路径
	 */

	@PostMapping(value = "/appTaskExport")
	public void appTaskExport(@RequestBody AppDataDTO appDataDTO) {
		String bb = appDataDTO.getSysconfigPath().replaceAll("\\\\", "/");
		//String aa = bb.substring(0,bb.lastIndexOf("/",bb.indexOf("/")+1));
		String aa = bb.substring(0,bb.lastIndexOf("/"));
		aa = aa.substring(0,aa.lastIndexOf("/"));
		//packinfo文件路径（客户自存自取的路径）
		String selfSoftToHardResult = gitFilePath + aa + File.separator + generateCodeResult  + File.separator + "packinfo.xml";
		//组件划分方案路径（自存自取）
		String selfGenerateCodeResult = gitFilePath + aa + File.separator +  softToHardResult + File.separator + "组件划分方案.xml";
		String appPath = gitFilePath + aa + File.separator + appDataDTO.getAppProPath();
		// 还未给接口，自己模拟的接口  + File.separator +
		ExternalIOTransUtils.appTaskExport(appDataDTO.getFlowId(), appDataDTO.getAppName(), appPath, appDataDTO.getSysconfigPath(), selfSoftToHardResult, selfGenerateCodeResult);
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
	public void createZipFile(HttpServletRequest request, HttpServletResponse response,
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
			srcFile = new File(gitFilePath + map.get("oriFilePath"));
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
	
	/**
	 * 根据流程Id查询是否生成app组件工程
	 * 
	 * @return
	 */
	@PutMapping(value = "/getAppByProcessId")
	public R getAppByProcessId(@RequestBody Map<String, String> map) {
		String procedureId =map.get("procedureId");
		String fileName =map.get("fileName");
		//根据流程Id查询
		App app = appService.getAppByProcessId(procedureId);
		//解析json 获取对应文件夹所在平台
		JSONObject json = JSONObject.parseObject(app.getPartnamePlatform());
		// 获取当前类的路径
		String filePath = ManagerServiceImpl.class.getResource("").getPath();
		try {
			// 中文乱码问题
			filePath = URLDecoder.decode(filePath, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 找到bootstrap.properties的地址
		filePath = filePath.substring(0, filePath.indexOf("target/classes/") + "target/classes/".length())
				+ "platformType.yml";
		File dumpFile = new File(filePath);

		Map father;
		String makefileType = "";
		try {
			father = Yaml.loadType(dumpFile, HashMap.class);
			makefileType = father.get(json.get(fileName)).toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new R<>(makefileType);
	}

}
