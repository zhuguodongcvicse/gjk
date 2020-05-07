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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ComponentConstant;
import com.inforbus.gjk.common.core.constant.DataCenterConstants;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.common.core.util.XmlFileToken;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.comp.api.entity.CompImg;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.feign.RemoteDataCenterService;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;
import com.inforbus.gjk.comp.mapper.CompImgMapper;
import com.inforbus.gjk.comp.mapper.ComponentDetailMapper;
import com.inforbus.gjk.comp.mapper.ComponentMapper;
import com.inforbus.gjk.comp.service.ComponentDetailService;
import com.inforbus.gjk.comp.service.ComponentService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@Service("componentDetailService")
public class ComponentDetailServiceImpl extends ServiceImpl<ComponentDetailMapper, ComponentDetail>
		implements ComponentDetailService {
	@Value("${git.local.path}")
	private String compDetailPath;
	private static final String compUserFilePath = "gjk" + File.separator + "component";
	@Autowired
	private ComponentMapper compMapper;
	@Autowired
	private CompImgMapper compImgMapper;
	@Autowired
	private ComponentService componentService;
	@Autowired
	protected ComponentDetailMapper compDetailMapper;
	@Autowired
	private RemoteDataCenterService rdcService;
	private static final Logger logger = LoggerFactory.getLogger(ComponentDetailServiceImpl.class);

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
	 * @return 空文件文件 2020年5月6日10:36:34 xiaohe
	 */
	private String createFile(String token, String compId, String userCurrent) {
		Component component = compMapper.getCompById(compId);
		String absolutePath = this.compDetailPath + compUserFilePath + File.separator + userCurrent + File.separator
				+ component.getCompId() + File.separator
				+ component.getCreateTime().toString().replaceAll("[[\\s-T:punct:]]", "") + File.separator;
		String gitRelativePath = compUserFilePath + File.separator + userCurrent + File.separator
				+ component.getCompId() + File.separator
				+ component.getCreateTime().toString().replaceAll("[[\\s-T:punct:]]", "") + File.separator;
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
			return absolutePath + xmlFileName;
		} else {
			String pathc = absolutePath + detail.getFileName();
			detail.setFilePath(gitRelativePath);
			detail.setFileName(xmlFileName);
			baseMapper.editCompDetail(detail);
			return absolutePath + xmlFileName;
		}
	}

	@Override
	@Deprecated
	public String createXmlFile(XmlEntity entity, String token, String compId) {
		File file = new File(this.createFile(token, compId, null));
		if (XmlFileHandleUtil.createXmlFile(entity, file)) {
			return file.getAbsolutePath();
		} else {
			return "";
		}

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
	 *      java.lang.String, java.lang.String) 2020年5月6日10:26:11 xiaohe 更改返回类型
	 */
	@Override
	public R createXmlFile(XmlEntityMap entity, String token, String compId, String userCurrent) {
		// createFile 的到文件需要的路径
		R ret = new R<String>();
		String filePath = this.createFile(token, compId, userCurrent);
		Set<String> Ids = Sets.newHashSet();
		this.getStructIds(entity, Ids);
		// 删除已有的当前构件所对应的结构体
		compMapper.deleteCompAndStruct(compId);
		for (String structId : Ids) {
			compMapper.saveCompAndStruct(IdGenerate.uuid(), compId, structId);
		}
		// 调用数据中心创建并生成文件 创建文件
		R<Boolean> rdc = rdcService.createXMLFile(new XMlEntityMapVO(filePath, entity));
		ret.setCode(rdc.getCode());
		ret.setMsg(rdc.getMsg());
		if (rdc.getCode() != CommonConstants.SUCCESS) {
			filePath = "";
		}
		ret.setData(filePath);
		return ret;

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
		String userName = (String) map.get("username");
		// map转成Component
		String createTime = resMap.get("createTime").toString();
		Component comp = mapToEntity(resMap, Component.class);
		resMap.clear();
		resMap.putAll(JSONUtil.parseObj(map.get("compImg")));
		// map转成CompImg
		CompImg img = new JSONObject(map.get("compImg")).toBean(CompImg.class);
		// 设置默认图片名称 格式为 '构件名称.png'
		originalFilename = StringUtils.isEmpty(img.getImgShowName()) ? "" : img.getImgShowName() + ".png";
		// 远程路径
		String gitRelativePath = compUserFilePath + File.separator + userName + File.separator + comp.getCompId()
				+ File.separator + createTime.replaceAll("[[\\s-T:punct:]]", "") + File.separator + "图标文件";
		// 选择文件后进入的判断
		if (ObjectUtils.isNotEmpty(file)) {
			try {
				// 2020年5月6日14:32:27 更改保存 构件图标的上传文件方法 xiaohe
				String filePath = this.compDetailPath + File.separator + gitRelativePath + File.separator
						+ originalFilename;
				rdcService.uploadLocalFile(file, filePath);
				String base64 = UploadFilesUtils.fileToEncodeBase64(new File(filePath));
//				File uploadFile = UploadFilesUtils.createFile(
//						this.compDetailPath + File.separator + gitRelativePath + File.separator + originalFilename);
//				// 将上传文件保存到路径
//				if (uploadFile.exists()) {
//					try {
//						uploadFile.delete();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				// 上传图标文件
//				file.transferTo(uploadFile);
//				String base64 = UploadFilesUtils.fileToEncodeBase64(uploadFile);
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
					this.compDetailPath + File.separator + gitRelativePath + File.separator + originalFilename);
			OutputStream os = null;
			try {
				if (!uploadFile.exists()) {
					uploadFile = UploadFilesUtils.createFile(
							this.compDetailPath + File.separator + gitRelativePath + File.separator + originalFilename);
				}
				if (imgId.equals("1")) {
					// 得到默认图标的文件
					InputStream fis = componentService.getImgFile(imgId, strb);
					// 得到BASE64字符串
					String base64 = UploadFilesUtils.getBase64ByInputStream(fis);
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
		detdirs.setFilePath(compUserFilePath + File.separator + userName + File.separator + comp.getCompId()
				+ File.separator + createTime.replaceAll("[[\\s-T:punct:]]", "") + File.separator);
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
		System.out.println("detdirs------------>: " + detdirs);
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
		// toArray==>将list转数组
		String[] folderPath = lists.toArray(new String[lists.size()]);
		// delFolder==>调用远程数据中心的服务删除
		rdcService.delFolder(folderPath);

	}

	/**
	 * @Title: moveNioFile
	 * @Description: 拷贝文件
	 * @Author xiaohe
	 * @DateTime 2019年7月17日 下午4:50:43
	 * @param lists 文件路径
	 */
	@Override
	public R moveNioFile(String source, String destin) {
		try {
			UploadFilesUtils.moveNioFile(source, destin);
		} catch (Exception e) {
			e.printStackTrace();
			return new R<>(e.getMessage());
		}
		return new R<>();

	}

	@Override
	public String getFilePathById(String id) {
		ComponentDetail des = this.getById(id);
//		return baseMapper.getFilePathById(id);
		System.out.println("ddddd:::" + this.compDetailPath + File.separator + des.getFilePath() + des.getFileName());
		return this.compDetailPath + File.separator + des.getFilePath() + des.getFileName();
	}

	/**
	 * @Title: getUploadFiles 多文件上传
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2019年7月9日 下午4:24:11
	 * @param file
	 * @return
	 */
	public List<CompFilesVO> getUploadFilesUrl(MultipartFile[] files, String userName) {

		List<CompFilesVO> listvos = Lists.newArrayList();
		// upload
		String upload = DataCenterConstants.FILE_DIRS_UPLOAD;
		// filePath==>文件全路径（不带文件名）
		String filePath = (compDetailPath + upload + File.separator + userName).replace("/", File.separator);
		// fegin 调用接口 返回调用状态
		R<?> rdc = rdcService.uploadLocalFiles(files, filePath);

		// rdc.getData()判断是否上传成功
		if (rdc.getCode() == CommonConstants.SUCCESS) {
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				// CompFilesVO 组装文件路径以及大小
				listvos.add(new CompFilesVO(fileName, filePath + File.separator + fileName));
			}
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
		// 文件类型
		String createTime = maps.get("createTime").toString().replaceAll("[[\\s-T:punct:]]", "");
		String userCurrent = maps.get("userCurrent").toString();
		String strType = maps.get("fileType").toString() + "file";
//		String fileType = ("algorithmfile").equals(strType) ? "算法文件"
//				: ("testfile").equals(strType) ? "测试文件" : ("platformfile").equals(strType) ? "平台文件" : "";

		String fileType = ComponentConstant.COMP_ALGORITHM.equals(strType) ? "算法文件"
				: ComponentConstant.COMP_TEST.equals(strType) ? "测试文件"
						: ComponentConstant.COMP_PLATFORM.equals(strType) ? "平台文件" : "";

		String newPath = compUserFilePath + File.separator + userCurrent + File.separator
				+ maps.get("compId").toString() + File.separator + createTime + File.separator;

		ComponentDetail detdirs = baseMapper.selectOne(Wrappers.<ComponentDetail>query().lambda()
				.eq(ComponentDetail::getCompId, maps.get("compValue").toString())
				.eq(ComponentDetail::getFileName, fileType));
		List<File> files = Lists.newArrayList();
		Boolean isUpdate = false;
		String oldPath = "";
		String libsId = StringUtils.isEmpty(maps.get("libsID").toString()) ? "-1" : maps.get("libsID").toString();
		if (null == detdirs) {
			isUpdate = false;
			// 保存根目录对象
			detdirs = new ComponentDetail(IdGenerate.uuid(), maps.get("compValue").toString(), fileType, strType,
					newPath, createTime, maps.get("compValue").toString(), libsId);
		} else {
			isUpdate = true;
			detdirs.setLibsId(libsId);
			oldPath = detdirs.getFilePath() + detdirs.getFileName();
			File file = new File(this.compDetailPath + oldPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 得到所有文件
			showDirectory(file, files);
			detdirs.setFilePath(newPath);
		}
		String detfilesPath = newPath + fileType;
		// 调用
		try {
			this.isUpdateFiles(paths, files, isUpdate, oldPath, detfilesPath);
		} catch (IOException e) {
			logger.error("文件上传出错！！！", e.getMessage());
		}
		if (isUpdate) {
			baseMapper.updateById(detdirs);
		} else {
			baseMapper.saveCompDetail(detdirs);
		}
		return detdirs.getFilePath() + detdirs.getFileName();

	}

	/**
	 * @Title: isUpdateFiles
	 * @Desc
	 * @Author xiaohe
	 * @DateTime 2020年5月6日
	 * @param paths        将要保存的文件数据
	 * @param files
	 * @param isUpdate
	 * @param oldPath      旧的文件路劲
	 * @param detfilesPath 新的文件路径
	 * @throws IOException
	 */
	private void isUpdateFiles(List<CompFilesVO> paths, List<File> files, Boolean isUpdate, String oldPath,
			String detfilesPath) throws IOException {
		logger.debug("开始处理文件列表。。。。。");
		for (int i = 0; i < paths.size(); i++) {
			// 这里是要保存的文件数据
			CompFilesVO vo = JSONUtil.parseObj(paths.get(i)).toBean(CompFilesVO.class);
			// 将临时文件考到对应目录下
			File uploadFile = new File(vo.getRelativePath());//
			String fileDirPath = vo.getName().replace("/", File.separator);
			if (fileDirPath.contains(File.separator)) {
				fileDirPath = fileDirPath.substring(0, fileDirPath.lastIndexOf(File.separator));
			} else {
				fileDirPath = "";
			}
			String tmpPath = "";
			// 判断路径是否相等 没有更改构件编号
			if (detfilesPath.equals(oldPath)) {
				// 只考虑文件添加的情况（删除时处理了其他情况）
				if (!files.contains(uploadFile)) {
					tmpPath = this.compDetailPath + File.separator + detfilesPath + File.separator + fileDirPath;// 要上传的文件路径
					FileItem fileItem = UploadFilesUtils.createFileItem(uploadFile.getPath(), uploadFile.getName());
					MultipartFile mfile = new CommonsMultipartFile(fileItem);
					// 调用数据中心上传
					R rdc = rdcService.uploadLocalFile(mfile, tmpPath);
					if (rdc.getCode() == CommonConstants.SUCCESS) {
						logger.debug("{}文件上传成功。。。。。", mfile.getOriginalFilename());
					} else {
						logger.error(rdc.getMsg());
					}
				}
			} else {
				String path = uploadFile.getPath();
				// 是否存在以前的路径
				if (StringUtils.isNotEmpty(oldPath) && path.indexOf(oldPath) != -1) {
					// 把旧路径换成新路径 //文件的路径（包含文件名字）
					tmpPath = path.replace("\\", "/").replaceAll(oldPath.replace("\\", "/"),
							detfilesPath.replace("\\", "/"));
					// 去掉文件名字
					tmpPath = tmpPath.substring(0, tmpPath.lastIndexOf("/"));
				} else {
					tmpPath = this.compDetailPath + File.separator + detfilesPath + File.separator + fileDirPath;// 要上传的文件路径
				}

				FileItem fileItem = UploadFilesUtils.createFileItem(uploadFile.getPath(), uploadFile.getName());
				MultipartFile mfile = new CommonsMultipartFile(fileItem);
				// 调用数据中心上传
				R rdc = rdcService.uploadLocalFile(mfile, tmpPath);
				if (rdc.getCode() == CommonConstants.SUCCESS) {
					logger.debug("{}文件上传成功。。。。。", mfile.getOriginalFilename());
					// 删除以前的文件及文件夹
					if (isUpdate) {
						String[] delPath = { vo.getRelativePath() };
						// 调用数据中心删除文件
						rdcService.delFolder(delPath);
					}
				} else {
					logger.error(rdc.getMsg());
				}

			}
			logger.debug("处理文件列表结束。。。。。");
		}
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
					if (object != null && field.getType().getName().equals("java.time.LocalDateTime")) {
//						field.set(t, LocalDateTime.parse(object.toString(), DateTimeFormatter.ofPattern("yyyy-HH-dd hh:mm:ss").withResolverStyle(ResolverStyle.STRICT)));
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

	/**
	 * @Title: createSpbFrameFile
	 * @Description: 生成构件框架
	 * @Author xiaohe
	 * @DateTime 2019年11月13日 下午4:05:24
	 * @param spbModelXmlFile 构件模型XML文件
	 * @param spbModelDir     保存路径(可以是平台文件路径)
	 * @param frameId         构件框架Id
	 */
	@Override
	public R createSpbFrameFile(String spbModelXmlFile, String saveDir, String frameId) {
		R retR = new R<>();
		List<Map<String, Object>> lists = baseMapper.findCompframe(frameId);
		// 框架路径
		String framePath = "";
		// 框架路径
		String frameName = "";
		for (Map<String, Object> map : lists) {
			framePath = this.compDetailPath + map.get("file_path");
			frameName = map.get("name").toString();
		}
		saveDir = this.compDetailPath + saveDir;
		File file = null;
		try {
			if (StringUtils.isEmpty(spbModelXmlFile)) {
				spbModelXmlFile = this.compDetailPath + JGitUtil.getHeaderTemplateFile();
				logger.error("构件模型XML文件 is null");
				return new R<>(new NullPointerException("构件模型XML文件 is null"));
			} else {
				file = new File(spbModelXmlFile);
				if (!file.exists() || !file.isFile()) {
					logger.error("构件模型XML文件 does not exist");
					return new R<>(new NullPointerException("构件模型XML文件 does not exist"));
				}
			}
			UploadFilesUtils.NioToCopyFile(framePath, saveDir + File.separator + frameName);
			if (StringUtils.isEmpty(saveDir)) {
				logger.error("saveDir is null");
				return new R<>(new NullPointerException("saveDir is null"));
			} else {
				file = new File(saveDir);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
			}
			ExternalIOTransUtils.createSpbFrameFile(spbModelXmlFile, saveDir + File.separator + frameName);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new R<>(e);
		}
		return retR;

	}

	/**
	 * @Title: findSpbFrameFile
	 * @Description: 查询构件框架列表
	 * @Author xiaohe
	 * @DateTime 2019年11月13日 下午4:05:24
	 */
	@Override
	public R findSpbFrameFile() {
		List<Map<String, Object>> lists = baseMapper.findCompframe(null);
		for (Map<String, Object> map : lists) {
			List<String> pfs = baseMapper.findPlatform(map.get("id").toString());
			map.put("platformName", pfs);
			System.out.println(pfs);
		}
		return new R(lists);
	}

	/**
	 * @Title: findPlatform
	 * @Description: 根据构件框架名称查询 的平台信息
	 * @Author xiaohe
	 * @DateTime 2019年11月26日 下午2:04:44
	 * @param frameName 构件框架Name
	 * @return
	 */
	@Override
	public List<String> findPlatformByName(String frameName) {
		return baseMapper.findPlatformByName(frameName);
	}

	/**
	 * @Title: getUploadFilesUrl
	 * @Description: 单文件上传返回路径
	 * @Author xiaohe
	 * @DateTime 2019年11月26日 下午2:04:44
	 * @param ufile    文件流
	 * @param userName 当前用户
	 * @param type     当前用户
	 * @return 路径
	 * @see com.inforbus.gjk.comp.service.ComponentDetailService#getUploadFilesUrl(org.springframework.web.multipart.MultipartFile,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public R getUploadFilesUrl(MultipartFile ufile, String userName, String type) {
		// xiaohe 2020年4月16日14:17:12 更改调用方式 调用fegin接口上传文件
		// fileName==>获取上传文件名,包含后缀
		String fileName = ufile.getOriginalFilename();
		// upload
		// filePath==>文件全路径 （不带文件名）
		String filePath = (compDetailPath + type + File.separator + userName).replace("/", File.separator);
		// fegin 调用接口 返回调用状态
		R<?> rdc = rdcService.uploadLocalFile(ufile, filePath);
		R<String> ret = new R<String>();
		// rdc.getData()判断是否上传成功
		if (rdc.getCode() == CommonConstants.SUCCESS) {
			ret.setData((filePath + File.separator + fileName).replace("/", File.separator));
		}
		ret.setCode(rdc.getCode());
		ret.setMsg(rdc.getMsg());
		return ret;
	}

}
