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
package com.inforbus.gjk.pro.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.pro.api.dto.AppDataDTO;
import com.inforbus.gjk.pro.api.entity.App;
import com.inforbus.gjk.pro.api.entity.Project;
import com.inforbus.gjk.pro.api.entity.ProjectFile;
import com.inforbus.gjk.pro.api.feign.AppSubassemblyServiceFeign;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;
import com.inforbus.gjk.pro.api.feign.ExternalInfInvokeService;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import com.inforbus.gjk.pro.mapper.AppMapper;
import com.inforbus.gjk.pro.service.AppService;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import feign.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 *
 * @author pig code generator
 * @date 2019-07-22 13:39:45
 */
@Service("appService")
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

	private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);
	@Autowired
	private ExternalInfInvokeService externalInfInvokeService;
	@Autowired
	protected DisposeDataCenterServiceFeign disposeDataCenterServiceFeign;

	@Autowired
	private AppSubassemblyServiceFeign appSubassemblyServiceFeign;

	//git文件路径
	@Value("${git.local.path}")
	private String proDetailPath;
	// packinfo文件路径(客户自存自取)
	@Value("${gjk.pro.process.softToHardResult}")
	private String softToHardResult;
	// 组件划分方案路径(客户自存自取)
	@Value("${gjk.pro.process.generateCodeResult}")
	private String generateCodeResult;
	

	/**
	 * 简单分页查询
	 * 
	 * @param app
	 * @return
	 */
	@Override
	public IPage<App> getAppPage(Page<App> page,@RequestBody App app) {
		return baseMapper.getAppPage(page, app);
	}

	/**
	 * 保存app
	 */
	@Override
	public App saveApp(App app) {
		App a = getAppByProcessId(app.getProcessId());
		if (a != null) {
			this.removeById(a.getId());
		}
		baseMapper.saveApp(app);
		return this.getById(app.getId());
	}

	/**
	 * 获取所有app工程
	 */
	@Override
	public List<App> getAllApp() {
		return baseMapper.getAllApp();
	}

	/**
	 * 获取app工程图片
	 */
	@Override
	public FileInputStream getAppImgFile(String id) {
		App app = baseMapper.getAppImgFile(id);
		String basePath = JGitUtil.getLOCAL_REPO_PATH();
		FileInputStream fis = null;
		try {
			String filePath = app.getBackPath();
			System.out.println("....." + (basePath + filePath));
			fis = new FileInputStream(basePath + filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}

	/**
	 * 获取app页数
	 */
	@Override
	public List<App> getAppVosPage(String fileName, String userId) {
		return baseMapper.getAppVosPage(fileName, userId);
	}

	/**
	 * 通过流程id获取app
	 */
	@Override
	public App getAppByProcessId(String processId) {
		return baseMapper.getAppByProcessId(processId);
	}

	/**
	 * 获取app树
	 */
	@Override
	public List<ProjectFileVO> getAppFileTree(String processId) {
		App app = getAppByProcessId(processId);
		if (app != null) {
			String appPath = proDetailPath + app.getFilePath() + File.separator + app.getFileName();
			List tree = appSubassemblyServiceFeign.createAppTree(appPath, processId).getData();
			return tree;
		} else {
			return Lists.newArrayList();
		}
	}

	/**
	 * 添加app树
	 * @param tree
	 * @param parentId
	 * @param file
	 * @param processId
	 */
	private void addAppFileTree(List<ProjectFileVO> tree, String parentId, File file, String processId) {
		String fileId = IdGenerate.uuid();

		if (file.isDirectory()) {
			tree.add(new ProjectFileVO(fileId, file.getName(), file.getName(), "app",
					file.getParentFile().getAbsolutePath(), parentId, processId, "0"));
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addAppFileTree(tree, fileId, childFile, processId);
			}
		} else {
			tree.add(new ProjectFileVO(fileId, file.getName(), file.getName(), "app",
					file.getParentFile().getAbsolutePath(), parentId, processId, "1"));
		}
	}

	/**
	 * 编辑app运行状态
	 */
	@Override
	public void editAppState(String id, String appState) {
		baseMapper.editAppState(id, appState);
	}

	/**
	 * 通过流程id获取项目
	 */
	@Override
	public ProjectFile getProcessByProjectId(String id) {
		return baseMapper.getProcessByProjectId(id);
	}

	/**
	 * 通过项目id获取项目
	 */
	@Override
	public Project getprojectByProjectId(String id) {
		return baseMapper.getprojectByProjectId(id);
	}

	/**
	 * 通过appid删除app
	 */
	@Override
	public void deleteAppByAPPId(String id) {
		App appData = baseMapper.selectAPPByAPPId(id);
		String appPath = proDetailPath + appData.getFilePath() + File.separator + appData.getFileName();
		UploadFilesUtils.delFolder(appPath);
		baseMapper.deleteAppByAPPId(id);
	}

	/**
	 * 通过appid筛选app
	 */
	@Override
	public App selectAPPByAPPId(String id) {
		return baseMapper.selectAPPByAPPId(id);
	}
	
	/**
	 * 通过流程id获取流程
	 */
	@Override
	public ProjectFile getProcessByProcessId(String parentId) {
		return baseMapper.getProcessByProcessId(parentId);
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
	public boolean appInstall( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		String bb = appDataDTO.getSysconfigPath().replaceAll("\\\\", "/");
		// String aa = bb.substring(0,bb.lastIndexOf("/",bb.indexOf("/")+1));
		String aa = bb.substring(0, bb.lastIndexOf("/"));
		aa = aa.substring(0, aa.lastIndexOf("/"));
		// packinfo文件路径（客户自存自取的路径）
		String selfSoftToHardResult = proDetailPath + aa + File.separator + generateCodeResult + File.separator
				+ "packinfo.xml";
		// 组件划分方案路径（自存自取）
		String selfGenerateCodeResult = proDetailPath + aa + File.separator + softToHardResult + File.separator
				+ "组件划分方案.xml";
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用注册接口
//			returnVal = ExternalIOTransUtils.appInstall(cmpNameToHwType, appDataDTO.getUserName(),
//					appDataDTO.getFlowId(), appDataDTO.getAppName(), selfSoftToHardResult, selfGenerateCodeResult,
//					proDetailPath + appDataDTO.getAppProPath());
			returnVal = externalInfInvokeService.appInstall(cmpNameToHwType, appDataDTO.getUserName(),
					appDataDTO.getFlowId(), appDataDTO.getAppName(), selfSoftToHardResult, selfGenerateCodeResult,
					proDetailPath + appDataDTO.getAppProPath());
		} catch (Exception e) {
			returnVal = false;
			e.printStackTrace();
		}
		return returnVal;
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
	public boolean appLoadStart( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 是否带部署方案(数据库存的字符串0、1，后台需要转换一下)
		boolean existDeployConfig;
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		if (appDataDTO.getExistDeployConfig().equals("1")) {
			existDeployConfig = false;
		} else {
			existDeployConfig = true;
		}
		try {
			// 调用加载、更新加载接口
//			returnVal = ExternalIOTransUtils.appLoad(cmpNameToHwType, appDataDTO.getUserName(), appDataDTO.getFlowId(),
//					appDataDTO.getAppName(), existDeployConfig, proDetailPath + appDataDTO.getSysconfigPath(),
//					proDetailPath + appDataDTO.getAppProPath());
			returnVal = externalInfInvokeService.appLoad(cmpNameToHwType, appDataDTO.getUserName(), appDataDTO.getFlowId(),
					appDataDTO.getAppName(), existDeployConfig, proDetailPath + appDataDTO.getSysconfigPath(),
					proDetailPath + appDataDTO.getAppProPath());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * 卸载
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	public boolean appUnload( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用卸载接口
			returnVal = ExternalIOTransUtils.appUnload(cmpNameToHwType, appDataDTO.getUserName(),
					appDataDTO.getFlowId(), appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * 启动
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	public boolean appTaskRestart( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用启动接口
			returnVal = ExternalIOTransUtils.appRestart(cmpNameToHwType, appDataDTO.getUserName(),
					appDataDTO.getFlowId(), appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * 停止
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	public boolean appStop( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用停止接口
			returnVal = ExternalIOTransUtils.appStop(cmpNameToHwType, appDataDTO.getUserName(), appDataDTO.getFlowId(),
					appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
	}

	/**
	 * 暂停
	 * 
	 * @param cmpNameToHwType APP组件工程生成，<组件名称，对应平台大类属性>
	 * @param appID           APP对应的流程ID
	 * @param appName         APP名称
	 * @return 执行结果(成功或失败)
	 */
	public boolean appPause( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		// 返回值
		boolean returnVal;
		try {
			// 调用暂停接口
			returnVal = ExternalIOTransUtils.appPause(cmpNameToHwType, appDataDTO.getUserName(), appDataDTO.getFlowId(),
					appDataDTO.getAppName());
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
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
	public boolean appDelete( AppDataDTO appDataDTO) {
		// 前台以json串的形式传到后台，后台解析成map
		Map<String, String> cmpNameToHwType = JSONUtil.toBean(appDataDTO.getCmpNameToHwType(), Map.class);
		String bb = appDataDTO.getSysconfigPath().replaceAll("\\\\", "/");
		// String aa = bb.substring(0,bb.lastIndexOf("/",bb.indexOf("/")+1));
		String aa = bb.substring(0, bb.lastIndexOf("/"));
		aa = aa.substring(0, aa.lastIndexOf("/"));
		// packinfo文件路径（客户自存自取的路径）
		String selfSoftToHardResult = proDetailPath + aa + File.separator + generateCodeResult + File.separator
				+ "packinfo.xml";
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
		boolean returnVal;
		try {
			// 调用注销接口
//			returnVal = ExternalIOTransUtils.appUnInstall(cmpNameToHwType, appDataDTO.getUserName(),
//					appDataDTO.getFlowId(), appDataDTO.getAppName(), selfSoftToHardResult);
			returnVal = externalInfInvokeService.appUnInstall(cmpNameToHwType, appDataDTO.getUserName(),
					appDataDTO.getFlowId(), appDataDTO.getAppName(), selfSoftToHardResult);
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		}
		return returnVal;
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

	public boolean appTaskExport( AppDataDTO appDataDTO) {
		String bb = appDataDTO.getSysconfigPath().replaceAll("\\\\", "/");
		// String aa = bb.substring(0,bb.lastIndexOf("/",bb.indexOf("/")+1));
		String aa = bb.substring(0, bb.lastIndexOf("/"));
		aa = aa.substring(0, aa.lastIndexOf("/"));
		// packinfo文件路径（客户自存自取的路径）
		String selfSoftToHardResult = proDetailPath + aa + File.separator + generateCodeResult + File.separator
				+ "packinfo.xml";
		// 组件划分方案路径（自存自取）
		String selfGenerateCodeResult = proDetailPath + aa + File.separator + softToHardResult + File.separator
				+ "组件划分方案.xml";
		String appPath = proDetailPath + File.separator + appDataDTO.getAppProPath();
		// 接口返回值（用于修改app的运行状态 true：改变；false：不改变）
				boolean returnVal;
				try {
					// 调用注销接口
//					ExternalIOTransUtils.appTaskExport(appDataDTO.getUserName(), appDataDTO.getFlowId(), appDataDTO.getAppName(),
//							appPath, proDetailPath + appDataDTO.getSysconfigPath(), selfSoftToHardResult, selfGenerateCodeResult);
					externalInfInvokeService.appTaskExport(appDataDTO.getUserName(), appDataDTO.getFlowId(), appDataDTO.getAppName(),
							appPath, proDetailPath + appDataDTO.getSysconfigPath(), selfSoftToHardResult, selfGenerateCodeResult);
					returnVal = true;
				} catch (Exception e) {
					e.printStackTrace();
					returnVal = false;
				}
		return returnVal;
	}
	
	/**
	 * 返回文件路径
	 */
	public String returnFilePath() {
		return proDetailPath;
	}
	/**
	 * 导出压缩文件
	 */
	public void createZipFile(HttpServletRequest request, HttpServletResponse response,
			 Map<String, String> map) {
		InputStream inputStream = null;
		ByteArrayOutputStream outStream = null;
		String filePath = proDetailPath + map.get("oriFilePath");
//		String filePath = "D:\\14S_GJK_GIT\\gjk\\gjk\\project\\Project1234\\Flow12\\app\\AppTaskInfo.xml";
		String zipFileName = map.get("downloadAPPFileName") + ".zip";
		String zipFilePath = proDetailPath + "gjk" + File.separator + "APPDownload" + File.separator + zipFileName;
		String [] strArr = {filePath};
		try {
			// feign文件下载
			Response respon = disposeDataCenterServiceFeign.downloadStreamFiles(strArr);
			Response.Body body = respon.body();
			inputStream = body.asInputStream();
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			in = new BufferedInputStream(inputStream);
			out = new BufferedOutputStream(new FileOutputStream(zipFilePath));
			outStream = new ByteArrayOutputStream();
			int len = -1;
			byte[] b = new byte[1024];
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
				outStream.write(b, 0, len);
			}
			in.close();
			out.close();
			
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
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
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
	 * 导出压缩文件
	 */
	public void createZipFiles(HttpServletRequest request, HttpServletResponse response,
			 Map<String, String> map) {
		FileInputStream inputStream = null;
		ByteArrayOutputStream outStream = null;
		String zipFileName = map.get("downloadAPPFileName") + ".zip";
		String zipFilePath = proDetailPath + "gjk" + File.separator + "APPDownload" + File.separator + zipFileName;
		try {
			org.apache.tools.ant.Project prj = new org.apache.tools.ant.Project();
			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(new File(zipFilePath));
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);

			File srcFile = null;
			srcFile = new File(proDetailPath + map.get("oriFilePath"));
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
