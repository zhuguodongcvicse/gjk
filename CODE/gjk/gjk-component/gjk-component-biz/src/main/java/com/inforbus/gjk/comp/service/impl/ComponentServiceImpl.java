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

import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.inforbus.gjk.comp.api.entity.Components;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ComponentConstant;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.comp.api.dto.CompTree;
import com.inforbus.gjk.comp.api.dto.ComponentDTO;
import com.inforbus.gjk.comp.api.dto.ComponentInput;
import com.inforbus.gjk.comp.api.dto.ComponentOutput;
import com.inforbus.gjk.comp.api.entity.CompImg;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.feign.RemoteDataCenterService;
import com.inforbus.gjk.comp.api.util.CompTreeUtil;
import com.inforbus.gjk.comp.api.util.XmlAnalysisUtil;
import com.inforbus.gjk.comp.api.vo.CompDetailVO;
import com.inforbus.gjk.comp.api.vo.CompDictVO;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;
import com.inforbus.gjk.comp.api.vo.CompVO;
import com.inforbus.gjk.comp.mapper.CompImgMapper;
import com.inforbus.gjk.comp.mapper.ComponentDetailMapper;
import com.inforbus.gjk.comp.mapper.ComponentMapper;
import com.inforbus.gjk.comp.service.CommonComponentServiceFeign;
import com.inforbus.gjk.comp.service.ComponentDetailService;
import com.inforbus.gjk.comp.service.ComponentService;
import com.inforbus.gjk.comp.service.SysUserService;
import com.inforbus.gjk.libs.api.entity.CommonComponent;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;

/**
 * 构件
 *
 * @author xiaohe
 * @date 2019-04-17 16:01:51
 */

/**
 * @ClassName: ComponentServiceImpl
 * @Description:
 * @Author cvics
 * @DateTime 2019年5月23日 上午11:36:23
 */
@Service("componentService")
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	protected ComponentDetailMapper compDetailMapper;
	@Autowired
	private CompImgMapper compImgMapper;
	@Autowired
	private ComponentDetailService componentDetailService;
	@Autowired
	private AmqpTemplate rabbitmqTemplate;
	@Autowired
	private CommonComponentServiceFeign commonComponentServiceFeign;
	@Value("${git.local.path}")
	private String compDetailPath;
	private static final Logger logger = LoggerFactory.getLogger(ComponentServiceImpl.class);
	@Autowired
	private RemoteDataCenterService rdcService;

	/**
	 * 构件简单分页查询
	 *
	 * @param component 构件
	 * @return
	 */
	@Override
	public IPage<Component> getComponentPage(Page<Component> page, Component component) {
		return baseMapper.getComponentPage(page, component);
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean deleteCompAndCompDetail(String compId) {
		List<ComponentDetail> details = compDetailMapper.listCompDetailByCompId(compId);
		try {
			Component comp = this.getById(compId);
			// 获取当前用户信息
//			R<SysUser> su=	sysUserService.user(comp.getUserId());
			String filePath = this.compDetailPath + "gjk" + File.separator + ComponentConstant.COMP + File.separator
					+ comp.getCompId() + File.separator
					+ comp.getCreateTime().toString().replaceAll("[[\\s-T:punct:]]", "") + File.separator;

			// 删除构件
			this.removeById(compId);
			// 删除构件详细信息
			for (ComponentDetail detail : details) {
				// 当文件详细信息是图片时删除图片
				if ("img".equals(detail.getFileType())) {
					compImgMapper.delete(Wrappers.<CompImg>query().lambda().eq(CompImg::getCompDetid, detail.getId()));
				}
				componentDetailService.removeById(detail.getId());
			}
			// 删除构件相关文件
//			cn.hutool.core.io.FileUtil.del(filePath);
			UploadFilesUtils.delFolder(filePath);
			return true;
		} catch (Exception e) {
			logger.error("删除构件 ", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动开启事务回滚
			return false;
		}
	}

	/**
	 * @param userId 用户编号
	 * @return
	 * @getCompByUserId
	 * @Description: 根据用户编号查询构件菜单
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午11:37:04
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompByUserId(java.lang.String)
	 */
	@Override
	public List<CompDetailVO> getCompByUserId(Integer userId) {
		List<Component> comps = baseMapper.listCompByUserId(userId);
		List<CompDetailVO> tree = Lists.newArrayList();
		for (Component comp : comps) {
//			将构件转成树
			tree.add(new CompDetailVO(comp.getCompId(), comp.getCompName(), "", "", "-1", comp.getVersion()));
			List<ComponentDetail> vos = compDetailMapper.listCompDetailByCompId(comp.getCompId());
			for (ComponentDetail v : vos) {
				if (!v.getFileType().equalsIgnoreCase("xml")) {
					addTree(tree, v);
				}
			}
		}
		return tree;
	}

	/**
	 * getCompByCompId
	 *
	 * @param compId 用户编号
	 * @return
	 * @Description: 根据用户编号查询构件菜单(无构件xml文件)
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午11:37:04
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompByCompId(java.lang.String,
	 *      boolean)
	 */
	@Override
	public List<CompDetailVO> getCompByCompId(String compId, boolean isShowCompXml) {
		Component comp = baseMapper.listCompByCompId(compId);
		List<CompDetailVO> tree = Lists.newArrayList();
//			将构件转成树
		tree.add(new CompDetailVO(comp.getId(), comp.getCompName(), ComponentConstant.COMP, "", "-1",
				comp.getVersion()));
		List<ComponentDetail> vos = compDetailMapper.listCompDetailByCompId(comp.getId());
		// 如果为true,则显示comp的xml文件，不走if内
		for (ComponentDetail v : vos) {
			if (!isShowCompXml) {
				if (!v.getFileType().equalsIgnoreCase("xml")) {
				}
				addTree(tree, v);
			} else {
				addTree(tree, v);
			}
			if ("algorithmfile".equals(v.getFileType()) || "testfile".equals(v.getFileType())
					|| "platformfile".equals(v.getFileType())) {
				File file = new File(this.compDetailPath + v.getFilePath() + File.separator + v.getFileName());
				if (file.isDirectory()) {
					File[] childFileList = file.listFiles();
					for (File childFile : childFileList) {
						addCompDetailTree(tree, v.getId(), childFile);
					}
				}
			}
		}
		return tree;
	}

	/**
	 * 递归生成文件树的vo并保存到List<CompDetailVO>中
	 *
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addCompDetailTree(List<CompDetailVO> tree, String parentId, File file) {
		String fileId = IdGenerate.uuid();
		CompDetailVO vo = new CompDetailVO(fileId, file.getName(), "", file.getParentFile().getAbsolutePath(), parentId,
				"");
		if (file.isDirectory()) {
			vo.setFileType("dirs");
			tree.add(vo);
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addCompDetailTree(tree, fileId, childFile);
			}
		} else {
			vo.setFileType("file");
			tree.add(vo);
		}
	}

	private void addCompDetailFiles(List<CompFilesVO> files, File file) {
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addCompDetailFiles(files, childFile);
			}
		} else {
			files.add(new CompFilesVO(file.getName(),
					file.getParentFile().getAbsolutePath() + File.separator + file.getName()));
		}
	}

	private void addTree(List<CompDetailVO> tree, ComponentDetail v) {
		tree.add(new CompDetailVO(v.getId(), v.getFileName(), v.getFileType(), this.compDetailPath + v.getFilePath(),
				v.getParaentId(), v.getVersion()));
	}

	/**
	 * @param proId 模型编号
	 * @return
	 * @getCompAndDetailMap
	 * @Description: 根据项目查询申请的构件及明细
	 * @Author xiaohe
	 * @DateTime 2019年5月5日 下午2:44:33
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompAndDetailMap(java.lang.String)
	 *      2020年5月7日15:43:23 更改解析xml文件的调用方式 xiaohe
	 */
	@Override
	public Map<String, Object> getCompAndDetailMap(String proId) {
		Map<String,String> filePathMap = new HashMap<String,String>();
		List<Component> comps = baseMapper.selectByComms(proId);
		List<Components> componentsList = new ArrayList<Components>();
		for(Component comp : comps){
			ComponentDetail vo = compDetailMapper.getCommCompXMl(comp.getId());
			Components components = new Components();
			components.setComponent(comp);
			components.setComponentDetail(vo);
			componentsList.add(components);
			if (vo != null) {
				String fileName = vo.getFileName();
				if (fileName.startsWith(ComponentConstant.COMP)
						&& fileName.toUpperCase().endsWith(ComponentConstant.COMP_XML)) {
					String filePath = this.compDetailPath + File.separator + vo.getFilePath() + File.separator
							+ fileName;
					filePathMap.put(vo.getCompId(),filePath);
				}
			}
		}
		Map<String, XmlEntityMap> xmlEntityMap = rdcService.getCompData(filePathMap).getData();
		List<ComponentDTO> dtos = rdcService.getCompDetailData(componentsList).getData();

		Map<String, Object> maps = Maps.newHashMap();
//		List<ComponentDTO> dtos = Lists.newArrayList();
//		Map<String, XmlEntityMap> xmlEntityMap = Maps.newHashMap();
//		// 查询公共构件的
//		List<Component> comps = baseMapper.selectByComms(proId);
//		for (Component comp : comps) {
//			// 查询公共构件中的明细
//			ComponentDetail vo = compDetailMapper.getCommCompXMl(comp.getId());
//			if (vo != null) {
//				String fileName = vo.getFileName();
//				// p判断是否是满足条件的xml文件
//				if (fileName.startsWith(ComponentConstant.COMP)
//						&& fileName.toUpperCase().endsWith(ComponentConstant.COMP_XML)) {
//					String filePath = this.compDetailPath + File.separator + vo.getFilePath() + File.separator
//							+ fileName;
//					R<XmlEntityMap> rdc = rdcService.analysisXmlFileToXMLEntityMap(filePath);
//					if (rdc.getCode() == CommonConstants.SUCCESS) {
//						Response response = rdcService.downloadStreamFiles(new String[] { filePath });
//						InputStream is = null;
//						String tmpPath = "./";
//						try {
//							is = response.body().asInputStream();
//							// 解压文件
//							UploadFilesUtils.decompression(is, tmpPath);
//							// 将构件文件放入map
//							dtos.add(XmlAnalysisUtil.xmlFileToComponentDTO(comp, vo, new File(tmpPath+ File.separator
//									+ fileName)));
//							// 将构件所对应的xml存入xmlEntityMap中
//							xmlEntityMap.put(vo.getCompId(), rdc.getData());
//							// 删除文件
//							cn.hutool.core.io.FileUtil.del(tmpPath);
//						} catch (IOException e) {
//							logger.error("文件解析异常", e);
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//		// 构件编号集合
		List<String> compIdList = new ArrayList<String>();
		// 构件id、编号map
		Map<String, String> compIdMap = new HashMap<String, String>();
		// 构件id、版本map
		Map<String, String> versionMap = new HashMap<String, String>();
		for (ComponentDTO dto : dtos) {
			compIdList.add(dto.getCompId());
			compIdMap.put(dto.getId(), dto.getCompId());
			versionMap.put(dto.getId(), dto.getVersion());
			for (int i = 0; i < dto.getInputList().size(); i++) {
				ComponentInput input = dto.getInputList().get(i);
				if (!input.getCategoryName().toUpperCase().equals("DATA")) {
					dto.getInputList().remove(i);
					i--;
				}
			}
			for (int i = 0; i < dto.getOutputList().size(); i++) {
				ComponentOutput output = dto.getOutputList().get(i);
				if (!output.getCategoryName().toUpperCase().equals("DATA")) {
					dto.getOutputList().remove(i);
					i--;
				}
			}
			System.out.println("dto.getInputList()=>" + dto.getInputList());
		}
		maps.put("dtos", dtos);
//		maps.put("xmls", xmlMap);
		maps.put("xmlMaps", xmlEntityMap);
		return maps;
	}

	/**
	 * @param component
	 * @return
	 * @saveComp
	 * @Description: 保存comp或者
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:07:37
	 * @see com.inforbus.gjk.comp.service.ComponentService#saveComp(com.inforbus.gjk.comp.api.entity.Component)
	 */
	@Override
	public Component saveComp(Component component) {
		component.setDelFlag("0");
		if (StringUtils.isNotEmpty(component.getId())) {
//			String version = ("0").equals(component.getApplyState()) ? component.getVersion()
//					: Double.toString(Double.parseDouble(component.getVersion()) + 1);
//			component.setVersion(version);
			// 修改主表信息
			baseMapper.editComp(component);
		} else {
			component.setId(IdGenerate.uuid());
//			component.setVersion(getVersion(component));
			component.setCreateTime(LocalDateTime.now());
			baseMapper.saveComp(component);
		}
		return this.getById(component.getId());
	}

	@Override
	public List<String> getVersionByCompId(Component component) {
		List<String> list = new ArrayList<String>();
		for (Component comp : baseMapper.getVersionByCompId(component)) {
			list.add(comp.getVersion());
		}
		return list;
	}

	/**
	 * 设置构件版本号，若是第一次，设为1.0，否则设为最高版本加1
	 *
	 * @param component
	 * @return
	 */
	private String getVersion(Component component) {
		List<Double> versions = new ArrayList<Double>();
		for (String version : getVersionByCompId(component)) {
			versions.add(Double.parseDouble(version));
		}
		// 对版本号List进行排序
		Collections.sort(versions);
		if (versions.size() != 0) {
			return versions.get(versions.size() - 1) + 1 + "";
		} else {
			return 1.0 + "";
		}
	}

	private List<CompVO> getAllComp() {
		List<Component> comps = this.list(Wrappers.<Component>query().lambda());
//		List<Component> comps = baseMapper.getAllComp();
		List<CompVO> tree = Lists.newArrayList();
		// 获取所有构件名,避免重名
		Set<String> strings = new HashSet<>();
		for (Component comp : comps) {
			strings.add(comp.getCompId());
		}
		// 创建构件选择树的所有父节点
		for (String s : strings) {
			tree.add(new CompVO("0", s, null, "-1"));
		}
		// 添加构件选择树的所有子节点
		for (Component comp : comps) {
			tree.add(new CompVO(comp.getId(), comp.getCompId() + "-" + comp.getVersion(), comp.getVersion(),
					comp.getCompId()));
		}
		return tree;
	}

	public List<CompTree> getCompSelectTree() {
		List<CompTree> list = CompTreeUtil.buildCompTree(getAllComp(), "-1");
		return list;
	}

	/**
	 * @param compId
	 * @return
	 * @getCompFiles
	 * @Description:
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:36:26
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompFiles(java.lang.String)
	 *      2020年5月6日13:34:22 xiaohe 更改返回类型和解析文件改为数据中心调取
	 */
	public R getCompFiles(String compId) {
		R ret = new R<T>();
		Map<String, Object> fileMap = Maps.newHashMap();
		Component comp = baseMapper.listCompByCompId(compId);
		fileMap.put("comp", comp);
		List<ComponentDetail> vos = compDetailMapper.listCompDetailByCompId(comp.getId());
		for (ComponentDetail parent : vos) {
			// 存文件目录及文件列表
			Map<String, Object> map = Maps.newHashMap();
			List<CompFilesVO> filesVOs = Lists.newArrayList();
//            if ("algorithmfile".equals(parent.getFileType()) || "testfile".equals(parent.getFileType())
//                    || "platformfile".equals(parent.getFileType())) {
			if (ComponentConstant.COMP_ALGORITHM.equals(parent.getFileType())
					|| ComponentConstant.COMP_TEST.equals(parent.getFileType())
					|| ComponentConstant.COMP_PLATFORM.equals(parent.getFileType())) {
				File file = new File(
						this.compDetailPath + parent.getFilePath() + File.separator + parent.getFileName());
				if (file.isDirectory()) {
					File[] childFileList = file.listFiles();
					for (File childFile : childFileList) {
						addCompDetailFiles(filesVOs, childFile);
					}
				}
				if (parent.getLibsId() != null)
					map.put("filePath", baseMapper.findLibs(parent.getLibsId(), parent.getFileType()));
			} else {
				for (ComponentDetail children : vos) {
					// 存文件列表
					CompFilesVO vo = new CompFilesVO();
					if (parent.getId().equals(children.getParaentId())) {
						vo.setName(children.getFileName());
						if (ComponentConstant.COMP_IMG.equals(parent.getFileType())) {
							CompImg img = compImgMapper.selectOne(
									Wrappers.<CompImg>query().lambda().eq(CompImg::getCompDetid, children.getId()));
							vo.setRelativePath(img.getImgPath());
							vo.setCompImg(img);
							filesVOs.add(vo);
						}

					}
				}
			}
			if (filesVOs.size() > 0) {
				// 文件列表
				map.put("filevo", filesVOs);
				// 文件类型
				fileMap.put(parent.getFileType(), map);
			}
			// 解析文件
			if (parent.getParaentId().equals(compId)) {
				String path = parent.getFileName();
				String filePath = "";
//				File file = null;
				if (path.startsWith(ComponentConstant.COMP)
						&& path.toUpperCase().endsWith(ComponentConstant.COMP_XML)) {
					filePath = this.compDetailPath + File.separator + parent.getFilePath() + File.separator + path;
					R<XmlEntityMap> rdc = rdcService.analysisXmlFileToXMLEntityMap(filePath);
					if (rdc.getCode() == CommonConstants.SUCCESS) {
						fileMap.put("compBasicMap", rdc.getData());
					} else {
						ret.setCode(CommonConstants.FAIL);
						fileMap.put("compBasicMap", "");
					}
					ret.setMsg(rdc.getMsg());
//					file = new File(this.compDetailPath + File.separator + parent.getFilePath() + "/" + path);
//					fileMap.put("compBasic", XmlFileHandleUtil.analysisXmlFile(file));
//					fileMap.put("compBasicMap", XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file));
				}
			}
		}
		ret.setData(fileMap);
		return ret;
	}

	@Override
	public FileInputStream getImgFile(String imgId, StringBuilder sBName) {
		ComponentDetail detail = null;
		if (imgId.equals("1")) {
			detail = compDetailMapper.selectById(imgId);
		} else {
			detail = baseMapper.getImgFile(imgId);
		}
		String basePath = this.compDetailPath;
		FileInputStream fis = null;
		try {
			String fileName = detail.getFileName();
			sBName.delete(0, sBName.length());
			sBName.append(fileName);
			String filePath = detail.getFilePath();
			fis = new FileInputStream(basePath + filePath + File.separator + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}

	public List<CompDictVO> getCompDictList() {
		List<Component> components = this.list(Wrappers.<Component>query().lambda());
		List<CompDictVO> compDictVOs = new ArrayList<>();
		for (Component comp : components) {
			CompDictVO dictVO = new CompDictVO(comp);
			compDictVOs.add(dictVO);
		}
		return compDictVOs;
	}

	/**
	 * @Title: analysisXmlFile
	 * @Desc 解析基础模板文件
	 * @Author xiaohe
	 * @DateTime 2020年5月6日
	 * @param filePath 文件路径（绝对路径）
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentService#analysisXmlFile(java.lang.String)
	 */
	@Override
	public R analysisXmlFile(String filePath) {
//		File file = null;
//		XmlEntityMap xmlEntityMap = null;
//		if (StringUtils.isNotEmpty(filePath)) {
//			file = new File(this.compDetailPath + filePath);
//		}
//		if (file.exists()) {
//			xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);
//		}
		// 调用数据中心解析文件
		return rdcService.analysisXmlFileToXMLEntityMap(this.compDetailPath + File.separator + filePath);
	}

	/**
	 * @Title: analysisZipFile
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年5月6日
	 * @param ufile
	 * @param userId
	 * @param userName
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentService#analysisZipFile(org.springframework.web.multipart.MultipartFile,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public List<Component> analysisZipFile(MultipartFile ufile, String userId, String userName) {
		String filePath = this.compDetailPath + "gjk" + File.separator + "zipFile" + File.separator
				+ (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + "-" + ufile.getOriginalFilename();
		try {
			String descDirPath = filePath.substring(0, filePath.lastIndexOf("."));
			// 解压zip文件夹
			UploadFilesUtils.decompression(ufile.getInputStream(), descDirPath);
			List<Component> components = null;
			File[] fs = new File(descDirPath).listFiles();
			for (File f : fs) {
				if (ComponentConstant.COMP.equals(f.getName())) {
					String excelFilePath = descDirPath + File.separator + ComponentConstant.COMP + File.separator
							+ "MySQL.xlsx";
					components = readExcel(descDirPath, excelFilePath, userId, userName);
					// 删除压缩包
					cn.hutool.core.io.FileUtil.del(filePath);
					// 删除解压包
					cn.hutool.core.io.FileUtil.del(descDirPath);
				}
			}
			return components;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解压文件到指定目录
	 *
	 * @param zipPath 压缩文件地址
	 * @param descDir 指定目录
	 * @throws IOException
	 */
	private static void unZipFiles(File zipFile, String descDir) throws IOException {
		try {
			File pathFile = new File(descDir);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			ZipFile zip = new ZipFile(zipFile);
			for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				String outPath = descDir + File.separator + zipEntryName;

				// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (entry.isDirectory()) {
					continue;
				}

				// 判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
				}
				InputStream in = zip.getInputStream(entry);
				// 输出文件路径信息
				OutputStream out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
			}
			zip.close();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * 解析excel文件
	 *
	 * @param path
	 * @throws Exception
	 */
	public List<Component> readExcel(String unZipFilePath, String path, String userId, String userName)
			throws Exception {
		List<Component> compList = new ArrayList<Component>();

		// 获取整个表格文件对象
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));

		List<Component> comps = new ArrayList<Component>();
		List<ComponentDetail> compDetails = new ArrayList<ComponentDetail>();
		List<Structlibs> structlibsList = new ArrayList<>();
		List<CompStruct> compStructList = new ArrayList<>();

		// 获取所有sheet对象
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while (sheetIterator.hasNext()) {
			Sheet sheet = sheetIterator.next();
			String tableName = sheet.getSheetName();
			String className = null;
			if ("gjk_common_component".equals(tableName)) {
				className = "com.inforbus.gjk.comp.api.entity.Component";
				for (Object obj : parseSheet(sheet, className)) {
					comps.add((Component) obj);
				}
			} else if ("gjk_common_component_detail".equals(tableName)) {
				className = "com.inforbus.gjk.comp.api.entity.ComponentDetail";
				for (Object obj : parseSheet(sheet, className)) {
					compDetails.add((ComponentDetail) obj);
				}
			} else if ("gjk_structlibs".equals(tableName)) {
				className = "com.inforbus.gjk.common.core.entity.Structlibs";
				for (Object obj : parseSheet(sheet, className)) {
					structlibsList.add((Structlibs) obj);
				}
			} else if ("gjk_comp_struct".equals(tableName)) {
				className = "com.inforbus.gjk.common.core.entity.CompStruct";
				for (Object obj : parseSheet(sheet, className)) {
					compStructList.add((CompStruct) obj);
				}
			}
		}
		workbook.close();

		// 遍历comp
		for (Component comp : comps) {
			// 判断数据库中是否已存此构件，若已存，则跳过
			if (this.getById(comp.getId()) != null) {
				continue;
			}

			List<ComponentDetail> componentDetails = new ArrayList<ComponentDetail>();
			for (ComponentDetail detail : compDetails) {
				if (detail.getCompId().equals(comp.getId())) {
					componentDetails.add(detail);
				}
			}

			String beforeFilePath = unZipFilePath + File.separator + ComponentConstant.COMP + File.separator
					+ comp.getCompId() + File.separator + comp.getVersion() + File.separator;
			String beforeDetailFilePath = "gjk" + File.separator + "common" + File.separator + ComponentConstant.COMP
					+ File.separator + comp.getCompId() + File.separator + comp.getVersion() + File.separator;
			String compVersion = comp.getVersion();

			comp.setUserId(Integer.parseInt(userId));
			comp.setApplyState("0");
			comp.setApplyDesc("未申请入库");
			comp.setCreateTime(LocalDateTime.now());
			comp.setUpdateTime(null);
			comp.setVersion(null);
			// 保存构件
			baseMapper.saveComp(comp);

			String timeStr = comp.getCreateTime().toString().replaceAll("[[\\s-T:punct:]]", "");
			if (timeStr.contains(".")) {
				timeStr = timeStr.substring(0, timeStr.indexOf("."));
			}
			String afterFilePath = "gjk" + File.separator + ComponentConstant.COMP + File.separator + userName
					+ File.separator + comp.getCompId() + File.separator + timeStr + File.separator;
			for (ComponentDetail detail : componentDetails) {
				String filePath = detail.getFilePath().substring(beforeDetailFilePath.length());
				detail.setFilePath(afterFilePath + filePath);
				compDetailMapper.saveCompDetail(detail);
			}

			// 保存构件和结构体关系
			List<CompStruct> compStructSubList = new ArrayList<>();
			for (CompStruct item : compStructList) {
				if (item.getCompId().equals(comp.getId())) {
					compStructSubList.add(item);
				}
			}
			for (CompStruct compStruct : compStructSubList) {
				baseMapper.saveCompAndStruct(compStruct.getId(), compStruct.getCompId(), compStruct.getStructId());
			}
//			rdcService.uploadLocalFile(ufile, filePath);
			FileUtil.copyFile(beforeFilePath, this.compDetailPath + afterFilePath);
			compList.add(this.getById(comp.getId()));
		}

		// 遍历结构体structlibs
		for (Structlibs item : structlibsList) {
			// 判断是否存在
			if (baseMapper.getStructlibsById(item.getId()) != null) {
				continue;
			}
			baseMapper.saveStructlibs(item);
		}

		return compList;
	}

	/**
	 * 解析工作簿
	 *
	 * @param sheet
	 * @throws Exception
	 */
	private List<Object> parseSheet(Sheet sheet, String className) throws Exception {
		List<Object> objects = new ArrayList<Object>();

		// 获取所有Row对象
		Iterator<Row> iterator = sheet.iterator();
		int index = 0;
		// 存列信息
		List<String> columnList = new ArrayList<String>();
		while (iterator.hasNext()) {
			if (index == 0) {
				columnList = parseRow(iterator.next());
			} else {
				objects.add(parseRow(iterator.next(), className, columnList));
			}
			index++;
		}
		return objects;
	}

	/**
	 * 解析列
	 *
	 * @param next
	 */
	private List<String> parseRow(Row next) {
		List<String> list = new ArrayList<String>();
		// 获取所有Cell对象取值
		Iterator<Cell> cellIterator = next.cellIterator();
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			cell.setCellType(CellType.STRING);
			list.add(cell.getStringCellValue());
		}
		return list;
	}

	/**
	 * 解析每行数据
	 *
	 * @param next
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private Object parseRow(Row next, String calssName, List<String> columnList) throws Exception {
		Class<?> cla = Class.forName(calssName);
		Object obj = cla.newInstance();

		// 获取所有Cell对象取值
		Iterator<Cell> cellIterator = next.cellIterator();
		int index = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			cell.setCellType(CellType.STRING);
			if (!"".equals(cell.getStringCellValue())) {
				if ("CreateTime".equals(columnList.get(index)) || "UpdateTime".equals(columnList.get(index))) {
					LocalDateTime parse = LocalDateTime.parse(cell.getStringCellValue());
					setFieldValueByName(columnList.get(index), cla, obj, parse);
				} else {
					setFieldValueByName(columnList.get(index), cla, obj, cell.getStringCellValue());
				}
			}
			index++;
		}
		return obj;
	}

	/**
	 * 获取表中的字段名
	 *
	 * @param tableName
	 * @return
	 */
	private List<Map<String, String>> queryColumns(String tableName) {
		return baseMapper.queryColumns(tableName);
	}

	/**
	 * 列名转换成Java属性名
	 */
	private String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
	}

	/**
	 * set属性值
	 *
	 * @param fieldName 字段名称
	 * @param o         对象
	 * @return Object
	 */
	private void setFieldValueByName(String fieldName, Class<?> clazz, Object o, Object value) {
		try {
			String firstLetter = fieldName.substring(0, 1).toLowerCase();
			fieldName = firstLetter + fieldName.substring(1); // 获取方法名
			PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
			Method wM = pd.getWriteMethod();// 获得写方法

			// 判断字段类型 转换类型
			if (pd.getPropertyType().getName().indexOf("Double") > -1) {
				value = Double.valueOf((String) value);
			} else if (pd.getPropertyType().getName().indexOf("Integer") > -1) {
				value = Integer.valueOf((String) value);
			}

			wM.invoke(o, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * 检查更新
	 */
	public Map<String, Object> checkComp(List<Object> obj) {
		Map<String, Object> maps = Maps.newHashMap();
		int compNumber = 0;
		String strJson = "";
		String token = "";
		Map<String, String> compIdMap = new HashMap<String, String>();
		Map<String, String> compNameMap = new HashMap<String, String>();
		Map<String, String> versionMap = new HashMap<String, String>();
		List<String> compIdList = new ArrayList<String>();
		// List<String> idList = new ArrayList<String>();
		JSONArray compJson = JSONUtil.parseArray(obj);
		for (int i = 0; i < compJson.size(); i++) {
			JSONObject job = compJson.getJSONObject(i);
			compIdList.add((String) job.get("compId"));
			// idList.add((String) job.get("id"));
			compIdMap.put((String) job.get("id"), (String) job.get("compId"));
			compNameMap.put((String) job.get("id"), (String) job.get("compName"));
			versionMap.put((String) job.get("id"), (String) job.get("version"));
			token = (String) job.get("token");

		}
		List<Component> compList = baseMapper.checkComp(compIdList);
		List<Double> versions = new ArrayList<Double>();
		Map<String, String> compUpdate = Maps.newHashMap();
		for (Map.Entry<String, String> entry : compIdMap.entrySet()) {
			for (Component comp : compList) {
				if (entry.getValue().equals(comp.getCompId())) {
					versions.add(Double.parseDouble(comp.getVersion()));
				}
			}
			if (versions.size() > 1) {
				compNumber += 1;
			}
			Collections.sort(versions);
			versions.get(versions.size() - 1);
			strJson += compNameMap.get(entry.getKey()) + "：当前版本" + versionMap.get(entry.getKey()) + ";  最高版本为"
					+ versions.get(versions.size() - 1) + "\n";
			if ((Double.valueOf(versionMap.get(entry.getKey()))) < (Double
					.valueOf(versions.get(versions.size() - 1)))) {
				compUpdate.put(entry.getKey(), ComponentConstant.COMP_NEWVERSION);// 已更新
			} else {
				compUpdate.put(entry.getKey(), ComponentConstant.COMP_NO_NEWVERSION);// 未更新
			}
			versions.clear();
		}
		// System.out.println(strJson);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String consoleStr = "<<" + sdf.format(new Date()) + "  total  " + compJson.size() + "  updated  " + compNumber
				+ "  Not updated  " + (compJson.size() - compNumber) + "  >>\n" + strJson;
		this.rabbitmqTemplate.convertAndSend(token, "lcjm" + "===@@@===" + consoleStr);
		maps.put("compUpdate", compUpdate);
		return maps;
	}

	/**
	 * @param userId 用户Id
	 * @Title: getImgFile
	 * @Description: listCompByUserId
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 上午11:34:43
	 */
	@Override
	public List<Component> listCompByUserId(Integer userId) {
		return baseMapper.listCompByUserId(userId);
	}

	/**
	 * 判断选择的库目录文件是否存在
	 *
	 * @param id
	 * @return
	 */
	@Override
	public String isSelectLibs(String id) {
		String res = "";
		List<ComponentDetail> componentDetailList = compDetailMapper.listCompDetailByCompId(id);
		for (ComponentDetail componentDetail : componentDetailList) {
			// 过虑库目录id为空的数据
			if (StringUtils.isBlank(componentDetail.getLibsId())) {
				continue;
			}
			// 判断平台库数据是否存在
			if (componentDetail.getFileType().equals("platformfile")) {
				String platformId = componentDetail.getLibsId();
				if (!"-1".equals(platformId)) {
					GjkPlatform platform = baseMapper.getPlatformByIdNotDelete(platformId);
					if (platform == null) {
						res += "平台文件、";
					}
				}
			}
			// 判断算法库数据是否存在
			else if (componentDetail.getFileType().equals("algorithmfile")) {
				String algorithmId = componentDetail.getLibsId();
				if (!"-1".equals(algorithmId)) {
					GjkAlgorithm algorithm = baseMapper.getAlgorithmByIdNotDelete(algorithmId);
					if (algorithm == null) {
						res += "算法文件、";
					}
				}
			}
			// 判断测试库数据是否存在
			else if (componentDetail.getFileType().equals("testfile")) {
				String testId = componentDetail.getLibsId();
				if (!"-1".equals(testId)) {
					GjkTest test = baseMapper.getTestByIdNotDelete(testId);
					if (test == null) {
						res += "测试文件、";
					}
				}
			}
		}

		if (res.length() > 0) {
			res = res.substring(0, res.length() - 1);
			res = "选择的" + res + "不存在,请重新配置相应的库节点";
		}
		return res;
	}

	// **********************************************************//
	// upms模块用到的东西，用于展示库管理模块的树级关系

	@Override
	public List<ComponentDetail> getLibsInfo() {
		// TODO Auto-generated method stub
		return baseMapper.getLibsInfo();
	}

	@Override
	public List<ComponentDetail> getLibsFile(String libsId) {
		// TODO Auto-generated method stub
		return baseMapper.getLibsFile(libsId);
	}

	@Override
	public List<ComponentDetail> getLibsFileType(String libsId) {
		// TODO Auto-generated method stub
		return baseMapper.getLibsFileType(libsId);
	}

	@Override
	public Component getCompNameById(String id) {
		// TODO Auto-generated method stub
		return baseMapper.getCompNameById(id);
	}

	@Override
	public List<String> getCompIdsGroupCompId() {
		// TODO Auto-generated method stub
		return baseMapper.getCompIdsGroupCompId();
	}

	@Override
	public ComponentDetail getCompDetailByComponentId(String componentId, String fileName) {
		// TODO Auto-generated method stub
		return baseMapper.getCompDetailByComponentId(componentId, fileName);
	}

	@Override
	public List<Component> getCompByCompId(String compId) {
		// TODO Auto-generated method stub
		return baseMapper.getCompByCompId(compId);
	}
	// **********************************************************//

	/**
	 * @return
	 * @Title: compJsplumbToGojsImg
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年3月27日
	 * @see com.inforbus.gjk.comp.service.ComponentService#compJsplumbToGojsImg()
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<T> compJsplumbToGojsImg() {
		// 第一步 备份数据库
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String format = dateTimeFormatter.format((LocalDateTime.now()));
		try {

			// 要替换的src 的 格式
			String rgex_src = "src=['|\"|]?(.*?)['|\"| ]";
			// 要替换 名字的格式<div class='desc' id='" + i + "'>构件B-V1.0</div></div>
			String rgex_Name = "<div class='desc' id='\" \\+ i \\+ \"'>(.*?)</div>";

			String baseformat = "<div data-v-6ff96f48=\"\" id=\"%s\" class=\"avatar-uploader\"\r\n"
					+ "style=\"margin:10px auto 10px auto;text-align: center; border: 1px none rgb(0, 0, 0); display: flex; justify-content: center; align-items: center; height: 100px; width: 110px; border-radius: 5px; background-color: rgb(173, 210, 183);\">\r\n"
					+ "    <div data-v-6ff96f48=\"\"><img data-v-6ff96f48=\"\" src=\"%s\" style=\"width: 100px; height: 70px;\"><span data-v-6ff96f48=\"\" style=\"display: inline-block; line-height: 18px; font-size:14px;\">%s</span></div>\r\n"
					+ "</div>";
			baseMapper.copyCompAndCommCompDb(format);
			// 第二步查询数据库
			// 查询你构件所有数据
			List<Component> compPages = baseMapper.getComponentPage(new Page<>(), new Component()).getRecords();
			for (Component component : compPages) {
				String oldDivStyle = component.getCompImg();
//				System.out.println("oldDivStyle:   "+oldDivStyle);
				String imgSrc = getSubUtilSimple(oldDivStyle, rgex_src);
				String imgName = getSubUtilSimple(oldDivStyle, rgex_Name);
//				System.out.println("imgSrc : "+imgSrc+"    ");
//				System.out.println("imgName : "+imgName+"    ");
				if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(imgName)) {
					// System.out.println("String.format(baseformat, imgSrc, imgName):" +
					// String.format(baseformat, imgSrc, imgName));
					// 替换字符串
					component.setCompImg(String.format(baseformat, "$divId$", imgSrc, imgName));
//					System.out.println("component.getCompImg(): "+component.getCompImg());
					// 修改数据
					baseMapper.updateById(component);
				}
			}

			List<CommonComponent> commonCompPages = commonComponentServiceFeign.getCommonComponentPage();
			for (CommonComponent commonComponent : commonCompPages) {
				String oldDivStyle = commonComponent.getCompImg();
				String imgSrc = getSubUtilSimple(oldDivStyle, rgex_src);
				String imgName = getSubUtilSimple(oldDivStyle, rgex_Name);
//				System.out.println("imgSrc : "+imgSrc+"    ");
//				System.out.println("imgName : "+imgName+"    ");
				if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(imgName)) {
//					System.out.println("String.format(baseformat, imgSrc, imgName):"+String.format(baseformat, imgSrc, imgName));
					// 替换字符串
					commonComponent.setCompImg(String.format(baseformat, commonComponent.getId(), imgSrc, imgName));
//					System.out.println("component.getCompImg(): "+commonComponent.getCompImg());
					// 修改数据
					commonComponentServiceFeign.update(commonComponent);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚失误
			return new R<>(e); // 返回你自己的错误信息
		}
		return new R<>();
	}

	/**
	 * @return
	 * @Title: compGojsToJspumbImg
	 * @Desc
	 * @Author zhanghongxu
	 * @DateTime 2020年4月3日
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<T> compGojsToJspumbImg() {
		// 第一步 备份数据库
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String format = dateTimeFormatter.format((LocalDateTime.now())) + "Gojs";
		try {
			// 要替换的src 的 格式
			String rgex_src = "src=['|\"|]?(.*?)['|\"| ]";
			// 要替换 名字的格式">构件B</span>
			String rgex_Name = "<span .*?>(.*?)</span>";
			String baseformat = "<div style='text-align:center;height:80px;width:110px;border:1px solid #000;border-radius:5px;background-color: null;display: block;'><img  src=\"%s\" style='vertical-align: middle;width: 110px; height:80px;border-radius:5px;'><div class='desc' id='\" + i + \"'>%s</div></div>";
			baseMapper.copyCompAndCommCompDb(format);

			// 第二步查询数据库
			// 查询你构件所有数据
			List<Component> compPages = baseMapper.getComponentPage(new Page<>(), new Component()).getRecords();
			for (Component component : compPages) {
				String oldDivStyle = component.getCompImg();
				String imgSrc = getSubUtilSimple(oldDivStyle, rgex_src);
				String imgName = getSubUtilSimple(oldDivStyle, rgex_Name);

				System.out.println("imgSrc:   " + imgSrc);
				System.out.println("imgName:   " + imgName);
				if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(imgName)) {
					System.out.println(
							"String.format(baseformat, imgSrc, imgName):" + String.format(baseformat, imgSrc, imgName));
					// 替换字符串
					component.setCompImg(String.format(baseformat, imgSrc, imgName));
					// 修改数据
					baseMapper.updateById(component);
				}
			}

			List<CommonComponent> commonCompPages = commonComponentServiceFeign.getCommonComponentPage();
			for (CommonComponent commonComponent : commonCompPages) {
				String oldDivStyle = commonComponent.getCompImg();
				String imgSrc = getSubUtilSimple(oldDivStyle, rgex_src);
				String imgName = getSubUtilSimple(oldDivStyle, rgex_Name);
				if (StringUtils.isNotEmpty(imgSrc) && StringUtils.isNotEmpty(imgName)) {
					// 替换字符串
					commonComponent.setCompImg(String.format(baseformat, imgSrc, imgName));
					// 修改数据
					commonComponentServiceFeign.update(commonComponent);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚失误
			return new R<>(e); // 返回你自己的错误信息
		}
		return new R<>();
	}

	/**
	 * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
	 *
	 * @param soap
	 * @param rgex
	 * @return
	 */
	public String getSubUtilSimple(String soap, String rgex) {
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
