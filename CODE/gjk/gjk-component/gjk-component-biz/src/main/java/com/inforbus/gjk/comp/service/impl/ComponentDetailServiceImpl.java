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
package com.inforbus.gjk.comp.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.common.core.util.XmlFileToken;
import com.inforbus.gjk.comp.api.entity.CompImg;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;
import com.inforbus.gjk.comp.mapper.CompImgMapper;
import com.inforbus.gjk.comp.mapper.ComponentDetailMapper;
import com.inforbus.gjk.comp.mapper.ComponentMapper;
import com.inforbus.gjk.comp.service.ComponentDetailService;
import com.inforbus.gjk.comp.service.ComponentService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import java.io.IOException;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@Service("componentDetailService")
public class ComponentDetailServiceImpl extends ServiceImpl<ComponentDetailMapper, ComponentDetail>
		implements ComponentDetailService {

	private static final String compDetailPath = JGitUtil.getLOCAL_REPO_PATH();
	private static final String compUserFilePath = "gjk" + File.separator + "component";
	@Autowired
	private ComponentMapper compMapper;
	@Autowired
	private CompImgMapper compImgMapper;
	@Autowired
	private ComponentService componentService;
	@Autowired
	protected ComponentDetailMapper compDetailMapper;

	/**
	 * 构件明细简单分页查询
	 * 
	 * @param componentDetail 构件明细
	 * @return
	 */
	@Override
	public IPage<ComponentDetail> getComponentDetailPage(Page<ComponentDetail> page, ComponentDetail componentDetail) {
		return baseMapper.getComponentDetailPage(page, componentDetail);
	}

	/**
	 * @upload
	 * @Description: 对前台传回detail数据进行处理
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:19:51
	 * @param request
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#upload(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ComponentDetail upload(HttpServletRequest request) {
		List<ComponentDetail> list = new ArrayList<ComponentDetail>();
		// 保存上传文件时创建父级记录
		ComponentDetail detail = saveCompDetail(request);
		// 查询此父级记录在数据库中是否存在
		ComponentDetail componentDetail = fileParentCompDetail(detail);

		ComponentDetail returnCompDetail = new ComponentDetail();
		returnCompDetail.setCompId(detail.getCompId());
		returnCompDetail.setFilePath(detail.getFilePath());
		returnCompDetail.setVersion(detail.getVersion());
		if (componentDetail != null) {
			detail = componentDetail;
		} else {
			list.add(detail);
		}
		// 根据传入的上传文件创建实体类存入数据库
		List<ComponentDetail> details = saveFileAndCompDetile(request, detail.getId(), detail.getCompId(),
				detail.getFileType(), detail.getFilePath(), detail.getId(), detail.getVersion());
		// 如果没有传入文件，删除父级记录
		if (details.size() != 0) {
			// 将父级与子级detail一同存入数据库
			list.addAll(details);
			for (ComponentDetail cd : list) {
				saveCompDetail(cd);
			}
		}
		return returnCompDetail;
	}

	/**
	 * @Title: saveCompDetail
	 * @Description: 解析前台传入的携带data相关信息，创建父级实体类
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:24:46
	 * @param request
	 * @return
	 */
	private ComponentDetail saveCompDetail(HttpServletRequest request) {
		ComponentDetail detail = null;
		InputStream is = null;
		String compName = null;
		String version = null;
		try {
			Collection<Part> parts = request.getParts();
			Iterator<Part> iterator = parts.iterator();
			detail = new ComponentDetail();
			detail.setId(IdGenerate.uuid());
			while (iterator.hasNext()) {
				Part part = iterator.next();
				String partName = part.getName();
				if (part.getContentType() == null) {
					is = part.getInputStream();
					byte[] bytes = new byte[1024];
					int nRead = 1;
					int nTotalRead = 0;
					while (nRead > 0) {
						nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
						if (nRead > 0)
							nTotalRead = nTotalRead + nRead;
					}
					String str = new String(bytes, 0, nTotalRead, "utf-8");
					if (partName.equals("compValue")) {
						detail.setCompId(str);
						detail.setParaentId(str);
					}
					if (partName.equals("compName")) {
						compName = str;
					}
					if (partName.equals("fileType")) {
						detail.setFileType(str + "file");
						if (str.equals("algorithm")) {
							detail.setFileName("算法文件");
						}
						if (str.equals("test")) {
							detail.setFileName("测试文件");
						}
						if (str.equals("platform")) {
							detail.setFileName("平台文件");
						}
					}
					if (partName.equals("libsID")) {
						detail.setLibsId(str);
					}
					if (partName.equals("version")) {
						version = str;
						detail.setVersion(str);
					}
				}
			}
			String path = compUserFilePath + File.separator + compName + File.separator + version + File.separator;
			detail.setFilePath(path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return detail;
	}

	/**
	 * @Title: saveFileAndCompDetile
	 * @Description: 根据传入的相关comp信息创建子级实体类
	 * @Author cvics
	 * @DateTime 2019年5月7日 下午3:26:58
	 * @param request
	 * @param parentId
	 * @param compId
	 * @param fileType
	 * @param path
	 * @param libsId
	 * @return
	 */
	private List<ComponentDetail> saveFileAndCompDetile(HttpServletRequest request, String parentId, String compId,
			String fileType, String path, String libsId, String version) {
		List<ComponentDetail> list = new ArrayList<ComponentDetail>();
		if (fileType.startsWith("algorithm")) {
			path += "算法文件/";
		} else if (fileType.startsWith("test")) {
			path += "测试文件/";
		} else if (fileType.startsWith("platform")) {
			path += "平台文件/";
		}
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				ComponentDetail detail = new ComponentDetail();
				detail.setId(IdGenerate.uuid());
				detail.setCompId(compId);
				detail.setFilePath(path);
				detail.setParaentId(parentId);
				detail.setLibsId(libsId);
				detail.setVersion(version);
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					detail.setFileName(fileName);
					detail.setFileType(fileName.substring(fileName.lastIndexOf('.')));
					// git本地库绝对路径
					System.out.println("1111111111111111111111" + compDetailPath);
					String absolutePath = compDetailPath + path + fileName;
					// 绝对路径的路径file，判断路径是否存在
					File filePath = new File(compDetailPath + path);
					// 绝对路径文件file，直接写入文件
					File absoluteFile = new File(absolutePath);
					// 上传
					try {
						if (!filePath.exists()) {
							filePath.mkdirs();
						}
//						D:/14S_GJK_GIT/gjk/gjk/component/123/2.0/算法文件/系统配置.xml
						file.transferTo(absoluteFile);
						JGitUtil.commitAndPush(path + fileName, "上传构件相关文件");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				list.add(detail);
			}
		}
		return list;
	}

	/**
	 * @saveCompDetail
	 * @Description: 保存构件详细信息
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:26:38
	 * @param detail
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#saveCompDetail(com.inforbus.gjk.comp.api.entity.ComponentDetail)
	 */
	@Override
	public ComponentDetail saveCompDetail(ComponentDetail detail) {
		baseMapper.saveCompDetail(detail);
		return detail;
	}

	@Override
	public ComponentDetail fileParentCompDetail(ComponentDetail detail) {
		return baseMapper.fileParentCompDetail(detail);
	}

	/**
	 * @Title: createFile
	 * @Description: 根据构件编号保存构件明细
	 * @Author xiaohe
	 * @DateTime 2019年8月30日 上午10:39:14
	 * @param token
	 * @param compId
	 * @return 空文件文件
	 */
	private File createFile(String token, String compId) {
		Component component = compMapper.getCompById(compId);
		String absolutePath = compDetailPath + compUserFilePath + File.separator + component.getCompName()
				+ File.separator + component.getVersion() + File.separator;
		String gitRelativePath = compUserFilePath + File.separator + component.getCompName() + File.separator
				+ component.getVersion() + File.separator;
		File f = new File(absolutePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		ComponentDetail detail = baseMapper.findCompXml(compId, "xml");
		String time = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
		String xmlFileName = XmlFileToken.COMPONENT + time + ".xml";
		if (!token.equals(XmlFileToken.COMPONENT)) {

		}
		if (detail == null) {
			detail = new ComponentDetail();
			detail.setId(IdGenerate.uuid());
			detail.setCompId(component.getId());
			detail.setParaentId(component.getId());
			detail.setVersion(component.getVersion());
			detail.setFileName(xmlFileName);
			detail.setFilePath(gitRelativePath);
			detail.setFileType("xml");
			this.saveCompDetail(detail);// 保存构件XML
			return new File(absolutePath + xmlFileName);
		} else {
			String pathc = absolutePath + detail.getFileName();
			f = new File(pathc);
			if (f.exists()) {
				f.delete();
			}
			detail.setFilePath(gitRelativePath);
			detail.setFileName(xmlFileName);
			baseMapper.editCompDetail(detail);
			File file = new File(absolutePath + xmlFileName);
			return file;
		}
	}

	@Override
	public boolean createXmlFile(XmlEntity entity, String token, String compId) {
		File file = this.createFile(token, compId);
		return XmlFileHandleUtil.createXmlFile(entity, file);
//		Component component = compMapper.getCompById(compId);
//
//		String absolutePath = compDetailPath + compUserFilePath + File.separator + component.getCompName()
//				+ File.separator + component.getVersion() + File.separator;
//		String gitRelativePath = compUserFilePath + File.separator + component.getCompName() + File.separator
//				+ component.getVersion() + File.separator;
//		File f = new File(absolutePath);
//		if (!f.exists()) {
//			f.mkdirs();
//		}
//		ComponentDetail detail = baseMapper.findCompXml(compId, "xml");
//		String time = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
//		String xmlFileName = XmlFileToken.COMPONENT + time + ".xml";
//		if (!token.equals(XmlFileToken.COMPONENT)) {
//			return false;
//		}
//		if (detail == null) {
//			detail = new ComponentDetail();
//			detail.setId(IdGenerate.uuid());
//			detail.setCompId(component.getId());
//			detail.setParaentId(component.getId());
//			detail.setVersion(component.getVersion());
//			detail.setFileName(xmlFileName);
//			detail.setFilePath(gitRelativePath);
//			detail.setFileType("xml");
//			this.saveCompDetail(detail);// 保存构件XML
//			File file = new File(absolutePath + xmlFileName);
//			boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
//			JGitUtil.commitAndPush(gitRelativePath + xmlFileName, "上传构件xml文件");
//			return flag;
//		} else {
//			String pathc = absolutePath + detail.getFileName();
//			f = new File(pathc);
//			if (f.exists()) {
//				f.delete();
//			}
//			detail.setFilePath(gitRelativePath);
//			detail.setFileName(xmlFileName);
//			baseMapper.editCompDetail(detail);
//			File file = new File(absolutePath + xmlFileName);
//			boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
//			JGitUtil.commitAndPush(gitRelativePath + xmlFileName, "上传构件xml文件");
//			return flag;
//		}
	}

	private void getStructIds(XmlEntityMap xmlEnt, Set<String> Ids) {
		if (ObjectUtils.isNotEmpty(xmlEnt)) {
			// 判断有没有属性Attribute
			if (ObjectUtils.isNotEmpty(xmlEnt.getAttributeMap())) {
				if (xmlEnt.getLableName() == "选择变量" || xmlEnt.getLableName() == "variable") {
					System.out.println("带有structId 的项：" + JSONUtil.toJsonPrettyStr(xmlEnt));
				}
				String val = xmlEnt.getAttributeMap().get("structId");
				if (StringUtils.isNotEmpty(val)) {
					Ids.add(val.replace("_*", ""));
				}
			}
			// 判断有没有下级标签list
			if (ObjectUtils.isNotEmpty(xmlEnt.getXmlEntityMaps())) {
				for (XmlEntityMap chXmlEnt : xmlEnt.getXmlEntityMaps()) {
					try {
						this.getStructIds(chXmlEnt, Ids);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * @Title: createXmlFile
	 * @Description: 根据xmlMap 生成文件
	 * @Author xiaohe
	 * @DateTime 2019年8月30日 上午10:33:47
	 * @param xmlMap
	 * @param token
	 * @param compId
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#createXmlFile(com.inforbus.gjk.common.core.entity.XmlEntityMap,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean createXmlFile(XmlEntityMap entity, String token, String compId) {
		File file = this.createFile(token, compId);
		Set<String> Ids = Sets.newHashSet();
		this.getStructIds(entity, Ids);
		// 删除已有的当前构件所对应的结构体
		compMapper.deleteCompAndStruct(compId);
		for (String structId : Ids) {
			compMapper.saveCompAndStruct(IdGenerate.uuid(), compId, structId);
		}
		// 创建文件
		return XmlFileHandleUtil.createXmlFile(entity, file);
//		Component component = compMapper.getCompById(compId);
//
//		String absolutePath = compDetailPath + compUserFilePath + File.separator + component.getCompName()
//				+ File.separator + component.getVersion() + File.separator;
//		String gitRelativePath = compUserFilePath + File.separator + component.getCompName() + File.separator
//				+ component.getVersion() + File.separator;
//		File f = new File(absolutePath);
//		if (!f.exists()) {
//			f.mkdirs();
//		}
//		ComponentDetail detail = baseMapper.findCompXml(compId, "xml");
//		String time = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
//		String xmlFileName = XmlFileToken.COMPONENT + time + ".xml";
//		if (!token.equals(XmlFileToken.COMPONENT)) {
//			return false;
//		}
//		if (detail == null) {
//			detail = new ComponentDetail();
//			detail.setId(IdGenerate.uuid());
//			detail.setCompId(component.getId());
//			detail.setParaentId(component.getId());
//			detail.setVersion(component.getVersion());
//			detail.setFileName(xmlFileName);
//			detail.setFilePath(gitRelativePath);
//			detail.setFileType("xml");
//			this.saveCompDetail(detail);// 保存构件XML
//			File file = new File(absolutePath + xmlFileName);
//			boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
//			JGitUtil.commitAndPush(gitRelativePath + xmlFileName, "上传构件xml文件");
//			return flag;
//		} else {
//			String pathc = absolutePath + detail.getFileName();
//			f = new File(pathc);
//			if (f.exists()) {
//				f.delete();
//			}
//			detail.setFilePath(gitRelativePath);
//			detail.setFileName(xmlFileName);
//			baseMapper.editCompDetail(detail);
//			File file = new File(absolutePath + xmlFileName);
//			boolean flag = XmlFileHandleUtil.createXmlFile(entity, file);
//			JGitUtil.commitAndPush(gitRelativePath + xmlFileName, "上传构件xml文件");
//			return flag;
//		}
	}

	/**
	 * 将map装换为javabean对象
	 *
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> map, T bean) {
		BeanMap beanMap = BeanMap.create(bean);
		for (Object key : beanMap.keySet()) {
			Object value = beanMap.get(key);

			// 排除空数据
			if (value == null) {
				continue;
			}

			beanMap.put(key + "", String.valueOf(value));
		}
		return bean;
	}

	/**
	 * @saveCompImg 保存构件图片
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年5月28日 下午8:29:14
	 * @param map
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#saveCompImg(java.util.Map)
	 */
	public boolean saveCompImg(MultipartFile file, Map<String, Object> map) {
		String originalFilename = "";
		// 用于临时转换的map
		Map<String, Object> resMap = Maps.newHashMap();
		resMap.putAll(JSONUtil.parseObj(map.get("comp")));
		// map转成Component
		Component comp = mapToEntity(resMap, Component.class);
		resMap.clear();
		resMap.putAll(JSONUtil.parseObj(map.get("compImg")));
		// map转成CompImg
		CompImg img = new JSONObject(map.get("compImg")).toBean(CompImg.class);
		// 设置默认图片名称 格式为 '构件名称.png'
		originalFilename = StringUtils.isEmpty(img.getImgShowName()) ? "" : img.getImgShowName() + ".png";
		// 远程路径
		String gitRelativePath = compUserFilePath + File.separator + comp.getCompName() + File.separator
				+ comp.getVersion() + File.separator + "图标文件";
		// 选择文件后进入的判断
		if (ObjectUtils.isNotEmpty(file)) {
			try {
				File uploadFile = UploadFilesUtils.createFile(gitRelativePath + File.separator + originalFilename);
				// 将上传文件保存到路径
				if (uploadFile.exists()) {
					try {
						uploadFile.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 上传图标文件
				file.transferTo(uploadFile);
				String base64 = UploadFilesUtils.fileToEncodeBase64(uploadFile);
				img.setImgPath("data:image/png;base64," + base64);
				img.setImgHtml(elReplace(img.getImgHtml(), "'/comp/component/comImg/(.*?)'",
						"data:image/png;base64," + base64));
				// 上传图标文件到 GIT
				JGitUtil.commitAndPush(gitRelativePath + File.separator + originalFilename, "上传构件xml文件");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 没有选择文件进入的判断
			StringBuilder strb = new StringBuilder();
			String imgId = elReplace(img.getImgPath(), "/comp/component/comImg/", "");
			File uploadFile = new File(
					JGitUtil.getLOCAL_REPO_PATH() + gitRelativePath + File.separator + originalFilename);
			OutputStream os = null;
			try {
				if (!uploadFile.exists()) {
					uploadFile = UploadFilesUtils.createFile(gitRelativePath + File.separator + originalFilename);
				}
				if (imgId.equals("1")) {
					// 得到默认图标的文件
					InputStream fis = componentService.getImgFile(imgId, strb);
					// 得到BASE64字符串
					String base64 =  UploadFilesUtils.getBase64ByInputStream(fis);
					img.setImgPath("data:image/png;base64," + base64);
					img.setImgHtml(elReplace(img.getImgHtml(), "'/comp/component/comImg/(.*?)'",
							"data:image/png;base64," + base64));
				} else {
					 UploadFilesUtils.base64ToFile(uploadFile, elReplace(imgId, "data:image/(.*?);base64,", ""));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (ObjectUtils.isNotEmpty(os)) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		// 保存目录数据
		ComponentDetail detdirs = new ComponentDetail();
		detdirs.setId(IdGenerate.uuid());
//			detdirs.setId(IdGenerate.uuid());
		detdirs.setCompId(comp.getId());
		detdirs.setFileName("图标文件");
		detdirs.setFileType("imgfile");
		detdirs.setFilePath(compUserFilePath + File.separator + comp.getCompName() + File.separator + comp.getVersion()
				+ File.separator);
		detdirs.setParaentId(comp.getId());
		detdirs.setVersion(comp.getVersion());
		// 保存文件数据
		ComponentDetail detfiles = new ComponentDetail();
		detfiles.setId(IdGenerate.uuid());
		detfiles.setCompId(comp.getId());
		detfiles.setFileName(originalFilename);
		detfiles.setFileType("img");
		detfiles.setFilePath(gitRelativePath);
		detfiles.setParaentId(detdirs.getId());
		detfiles.setVersion(comp.getVersion());
		// 保存图标文件
		img.setId(IdGenerate.uuid());
		img.setCompDetid(detfiles.getId());
//		String imgStr = img.getImgHtml();
//		img.setImgPath("/comp/component/comImg/" + detdirs.getId());
//		System.out.println(img.getImgPath());
//		String html = elReplace(img.getImgHtml(), "'data:image/(.*?)'", "'" + img.getImgPath() + "'");
//		img.setImgHtml(elReplace(html, "'/comp/component/comImg/(.*?)'", "'" + img.getImgPath() + "'"));
//		System.out.println(imgStr);
		System.out.println(img.getImgHtml());
		// 删除包含图片的信息
		compDetailMapper.delete(Wrappers.<ComponentDetail>query().lambda().eq(ComponentDetail::getCompId, comp.getId())
				.likeRight(ComponentDetail::getFileType, "img%"));
		baseMapper.saveCompDetail(detdirs);
		baseMapper.saveCompDetail(detfiles);
		// 保存包含图片的信息
		compImgMapper.saveCompImg(img);
		// 修改主表数据
		comp.setCompImg(img.getImgHtml());
		compMapper.editComp(comp);
		return true;
	}

	@Override
	public ComponentDetail findCompXml(String compId, String fileType) {
		return null;
	}

	@Override
	public void editCompDetail(ComponentDetail detail) {

	}

	@Override
	public List<ComponentDetail> listCompDetailByCompId(String compId) {
		return baseMapper.listCompDetailByCompId(compId);
	}

	/**
	 * @Title: delFilePath
	 * @Description: 删除文件
	 * @Author xiaohe
	 * @DateTime 2019年7月17日 下午4:50:43
	 * @param lists 文件路径
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#delFilePath(java.util.List)
	 */
	@Override
	public void delFilePath(List<String> lists) {
		for (String path : lists) {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	@Override
	public String getFilePathById(String id) {
		ComponentDetail des = this.getById(id);
//		return baseMapper.getFilePathById(id);
		System.out.println("ddddd:::" + compDetailPath + File.separator + des.getFilePath() + des.getFileName());
		return compDetailPath + File.separator + des.getFilePath() + des.getFileName();
	}

	/**
	 * @Title: getUploadFiles 多文件上传
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年7月9日 下午4:24:11
	 * @param file
	 * @return
	 */
	public List<CompFilesVO> getUploadFilesUrl(MultipartFile[] files, Map<String, String> mapPath) {
		String url = "";
		List<CompFilesVO> listvos = Lists.newArrayList();
		try {
			for (MultipartFile file : files) {
				Map<String, String> map = Maps.newHashMap();
				// 获取上传文件名,包含后缀
				String fileName = file.getOriginalFilename();
				url = new String(mapPath.get("path") + File.separator + fileName);
				File uploadFile = null;
				String path = compDetailPath + "/" + url;
				if (StringUtils.isNotEmpty(path)) {
					uploadFile = new File(path);
					if (!uploadFile.getParentFile().exists()) {
						uploadFile.getParentFile().mkdirs();
					}
					uploadFile.createNewFile();
				}
				// 将上传文件保存到路径
				if (uploadFile.exists()) {
					uploadFile.delete();
				}
				file.transferTo(uploadFile);
				JGitUtil.commitAndPush(url, "多个文件上传");
				listvos.add(new CompFilesVO(fileName, uploadFile.getPath()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listvos;

	}

	/**
	 * @Title: saveCompfiles
	 * @Description: 保存构件文件信息
	 * @Author xiaohe
	 * @DateTime 2019年7月18日 下午9:19:27
	 * @param maps
	 * @param paths
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#saveCompfiles(java.util.Map,
	 *      java.util.List)
	 */
	@Override
	public String saveCompfiles(Map<String, String> maps, List<CompFilesVO> paths) {
		// 用于保存文件夹时产生的中间文件夹的名字（保存中间文件夹不重复）
//		Map<String, String> keyMap = Maps.newHashMap();
		// 存储要保存的文件明细对象
//		List<ComponentDetail> saveDetdirs = Lists.newArrayList();
		// 文件类型
		String strType = maps.get("fileType").toString() + "file";
		String fileType = ("algorithmfile").equals(strType) ? "算法文件"
				: ("testfile").equals(strType) ? "测试文件" : ("platformfile").equals(strType) ? "平台文件" : "";
		String newPath = compUserFilePath + File.separator + maps.get("compName").toString() + File.separator
				+ maps.get("version").toString() + File.separator;

		ComponentDetail detdirs = baseMapper.selectOne(Wrappers.<ComponentDetail>query().lambda()
				.eq(ComponentDetail::getCompId, maps.get("compValue").toString())
				.eq(ComponentDetail::getFileName, fileType));
		List<File> files = Lists.newArrayList();
		Boolean isUpdate = false;
		String oldPath = "";
		if (null == detdirs) {
			isUpdate = false;
			// 保存根目录对象
			detdirs = new ComponentDetail(IdGenerate.uuid(), maps.get("compValue").toString(), fileType, strType,
					newPath, maps.get("version").toString(), maps.get("compValue").toString(),
					maps.get("libsID").toString());
		} else {
			isUpdate = true;
			detdirs.setLibsId(maps.get("libsID").toString());
			oldPath = detdirs.getFilePath() + detdirs.getFileName();
			File file = new File(compDetailPath + oldPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 得到所有文件
			showDirectory(file, files);
			detdirs.setFilePath(newPath);
		}
		String detfilesPath = newPath + fileType;
		for (int i = 0; i < paths.size(); i++) {
			// 这里是要保存的文件数据
			CompFilesVO vo = JSONUtil.parseObj(paths.get(i)).toBean(CompFilesVO.class);
			// 将临时文件考到对应目录下
			File uploadFile = new File(vo.getRelativePath());//
			File file = null;
			String tmpPath = "";
			try {
				// 判断路径是否相等
				if (detfilesPath.equals(oldPath)) {
					// 如果文件不存在
					if (!files.contains(uploadFile)) {
						tmpPath = compDetailPath + File.separator + detfilesPath + File.separator + vo.getName();// 要上传的文件路径
						if (StringUtils.isNotEmpty(tmpPath)) {
							file = new File(tmpPath);
							if (!file.getParentFile().exists()) {
								file.getParentFile().mkdirs();
							}
							file.createNewFile();
						}
						FileUtil.copyFilesFromDir(uploadFile, file, true);
						// 上传文件到 GIT
						JGitUtil.commitAndPush(file.getPath(), "上传构件xml文件");
					}
				} else {
					String path = uploadFile.getPath();
					if (StringUtils.isNotEmpty(oldPath) && path.indexOf(oldPath) != -1) {
						tmpPath = path.replace("\\", "/").replaceAll(oldPath.replace("\\", "/"),
								detfilesPath.replace("\\", "/"));
					} else {
						tmpPath = compDetailPath + File.separator + detfilesPath + File.separator + vo.getName();// 要上传的文件路径
					}
					// 删除以前的文件及文件夹
					if (isUpdate) {
						UploadFilesUtils.delFolder(compDetailPath + File.separator + oldPath);
					}
					if (StringUtils.isNotEmpty(tmpPath)) {
						file = new File(tmpPath);
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						file.createNewFile();
					}
					FileUtil.copyFilesFromDir(uploadFile, file, true);
					// 上传文件到 GIT
					JGitUtil.commitAndPush(file.getPath(), "上传构件xml文件");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		if (isUpdate) {
			baseMapper.updateById(detdirs);
		} else {
			baseMapper.saveCompDetail(detdirs);
		}

		return detdirs.getFilePath() + detdirs.getFileName();

	}

	@Override
	public CompImg getCompDefaultImg() {
		return compImgMapper.selectById("1");
	}

	/**
	 * @Title: elReplace
	 * @Description: 通过正则查找替换
	 * @Author cvics
	 * @DateTime 2019年8月5日 下午2:34:23
	 * @param target
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String elReplace(String target, String regex, String replacement) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(target);
		StringBuffer operatorStr = new StringBuffer(target);
		while (m.find()) {
			// find()尝试查找与该模式匹配的输入序列的下一个子序列
			operatorStr.replace(m.start(), m.end(), replacement);
		}
		return operatorStr.toString();
	}

	/**
	 * Map转实体类
	 * 
	 * @param map    需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
	 * @param entity 需要转化成的实体类
	 * @return
	 */
	public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
		T t = null;
		try {
			t = entity.newInstance();
			for (Field field : entity.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
					field.setAccessible(true);
					Object object = map.get(field.getName());
					if (object != null && field.getType().isAssignableFrom(object.getClass())) {
						field.set(t, object);
					}
					field.setAccessible(flag);
				}
			}
			return t;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}

	@Test
	public void test() {
//		String str = "{\"compFuncname\":\"\",\"applyState\":\"0\",\"description\":null,\"updateTime\":\"2019-07-24 21:28:37\",\"delFlag\":\"0\",\"compName\":\"sdfghm\",\"userId\":\"1\",\"version\":\"1.0\",\"compImg\":\"<div style='text-align:center;height:80px;width:160px;border:1px solid;border-radius:5px;background-color:'';display: block;'><img  src='' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'><\\/i><div class='desc' id='\\\" + i + \\\"'><\\/div> <\\/div>\",\"compId\":\"ghjklgh\",\"createTime\":\"2019-07-24 21:28:33\",\"id\":\"35b7341e248a4072815aa3eba1577c2c\",\"applyDesc\":\"未申请入库\"}";
//		JSONObject jsonObject = JSONUtil.parseObj(str);
//		System.out.println("jsonObject" + jsonObject);
//		Component component = jsonObject.toBean(Component.class);
//		System.out.println(component);
//		String s = "";
	//	String path = "data:image/jpeg;base64,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";
//		String path = "<div style='text-align:center;height:80px;width:110px;border:1px solid #000;border-radius:5px;background-color: null;display: block;'><img src='data:image/jpeg;base64,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' style='vertical-align: middle;width: 110px; height:80px;border-radius:5px;'><div class='desc' id='" + 1 + "'>测试构件02-V1.0</div></div>";
//		new ComponentDetailServiceImpl().base64ToFile(new File("D:\\111.png"), path);
		try {
			String base64 = UploadFilesUtils.fileToEncodeBase64(new File("D:\\111.png"));
//			System.out.println(base64);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String str = "<div style='text-align:center;height:80px;width:110px;border:1px solid #000;border-radius:5px;background-color: null;display: block;'><img  src='data:image/png;base64ZPjf//3/AZMfZZynZiCHAAAAAElFTkSuQmCC' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>fdg</div></div>";
//		String regex = "'data:image/(.*?)'";
//		System.out.println(elReplace(str, regex, "'00'"));
//		byte[] b = path.getBytes();

//		System.out.println("字节：" + b.length);
	}

	public static void showDirectory(File file, List<File> paths) {
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				showDirectory(f, paths);
			} else {
				paths.add(f);
			}
		}
	}

}
