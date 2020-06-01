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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.common.core.util.FileDownload;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import com.inforbus.gjk.pro.api.dto.AppDataDTO;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.service.AppService;
import com.inforbus.gjk.pro.service.impl.ManagerServiceImpl;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ho.yaml.Yaml;
import org.springframework.security.access.prepost.PreAuthorize;
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

	/**
	 * 简单分页查询
	 * 
	 * @param page 分页对象
	 * @param app
	 * @return
	 */
	@PostMapping("/page/{current}/{size}")
	public R<IPage<App>> getAppPage(@PathVariable Long current, @PathVariable Long size,@RequestBody App app) {
		Page<App> page = new Page<App>();
		page.setCurrent(current);
		page.setSize(size);
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
	@GetMapping(value = "/getAppVosPage/{fileName}/{userId}")
	public R getAppVosPage(@PathVariable String fileName, @PathVariable String userId) {
		return new R<>(appService.getAppVosPage(fileName, userId));
	}

	/**
	 * 返回文件路径
	 * @return
	 */
	@PostMapping(value = "/returnFilePath")
	public R returnFilePath() {
		return new R<>(appService.returnFilePath());
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
		return new R<>(appService.appInstall(appDataDTO));
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
		return new R<>(appService.appLoadStart(appDataDTO));
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
		return new R<>(appService.appUnload(appDataDTO));
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
		return new R<>(appService.appTaskRestart(appDataDTO));
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
		return new R<>(appService.appStop(appDataDTO));
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
		return new R<>(appService.appPause(appDataDTO));
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
		return new R<>(appService.appDelete(appDataDTO));
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
	public R appTaskExport(@RequestBody AppDataDTO appDataDTO) {
		return new R<>(appService.appTaskExport(appDataDTO));
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
	 * 通过流程id拿到当前流程建模信息，用于挨app控制的展开功能
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/getProcessByProcessId/{parentId}")
	public ProjectFile getProcessByProcessId(@PathVariable("parentId") String parentId) {
		return appService.getProcessByProcessId(parentId);

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

	/**
	 * 导出文件夹
	 * @param request 
	 * @param response
	 * @param map  需要下载的文件路径，下载到的路径+9
	 */
	@PostMapping("/createZipFile")
	public void createZipFile(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, String> map) {
		appService.createZipFile(request, response, map);
	}

	/**
	 * 根据流程Id查询是否生成app组件工程
	 * 2020年5月29日16点52分晓冬修改，此类型不需要从配置文件中获取
	 * @return
	 */
	@PutMapping(value = "/getAppByProcessId")
	public R getAppByProcessId(@RequestBody Map<String, String> map) {
		String procedureId = map.get("procedureId");
		String fileName = map.get("fileName");
		// 根据流程Id查询
		App app = appService.getAppByProcessId(procedureId);
		// 解析json 获取对应文件夹所在平台
		JSONObject json = JSONObject.parseObject(app.getPartnamePlatform());
		//获取到平台类型
		String plartType = json.get(fileName).toString();
		return new R<>(plartType);
	}
	/**
	 * 通过yml文件获取平台
	 * 2020年5月29日17点26分 晓冬修改，此数据不需要从配置文件中获取，从字典表中获取
	 * @return
	 */
	@PutMapping( "/getPlatformName")
	public R getPlatformNameByYml() {
		Map<String,String> map = appService.getPlatformName();
		return new R<>(map);
	}
}
