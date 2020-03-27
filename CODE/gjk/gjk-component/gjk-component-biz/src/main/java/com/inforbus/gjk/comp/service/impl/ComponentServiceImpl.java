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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.entity.SysUser;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.comp.api.dto.CompTree;
import com.inforbus.gjk.comp.api.dto.ComponentDTO;
import com.inforbus.gjk.comp.api.dto.ComponentInput;
import com.inforbus.gjk.comp.api.dto.ComponentOutput;
import com.inforbus.gjk.comp.api.entity.CompImg;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;
import com.inforbus.gjk.comp.api.util.CompTreeUtil;
import com.inforbus.gjk.comp.api.util.XmlAnalysisUtil;
import com.inforbus.gjk.comp.api.vo.CompDetailVO;
import com.inforbus.gjk.comp.api.vo.CompDictVO;
import com.inforbus.gjk.comp.api.vo.CompFilesVO;
import com.inforbus.gjk.comp.api.vo.CompVO;
import com.inforbus.gjk.comp.api.vo.ComponentVO;
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
import sun.misc.BASE64Encoder;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

	private CommonComponentServiceFeign commonComponentServiceFeign;
	@Value("${git.local.path}")
	private String compDetailPath;
	private static final Logger logger = LoggerFactory.getLogger(ComponentServiceImpl.class);

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
			String filePath = this.compDetailPath + "gjk" + File.separator + "component" + File.separator
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

	@Override
	public ComponentVO getComponentCompDetailById(String id) {
		return baseMapper.getComponentCompDetailById(id);
	}

	@Override
	public boolean deleteCompById(String id) {
		boolean flag = true;

		// TODO: 删除构件
		return flag;
	}

	/**
	 * @getCompByUserId
	 * @Description: 根据用户编号查询构件菜单
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午11:37:04
	 * @param userId 用户编号
	 * @return
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
	 * @Description: 根据用户编号查询构件菜单(无构件xml文件)
	 * @Author xiaohe
	 * @DateTime 2019年4月29日 上午11:37:04
	 * @param compId 用户编号
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompByCompId(java.lang.String,
	 *      boolean)
	 */
	@Override
	public List<CompDetailVO> getCompByCompId(String compId, boolean isShowCompXml) {
		Component comp = baseMapper.listCompByCompId(compId);
		List<CompDetailVO> tree = Lists.newArrayList();
//			将构件转成树
		tree.add(new CompDetailVO(comp.getId(), comp.getCompName(), "component", "", "-1", comp.getVersion()));
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
	 * @getCompAndDetailMap
	 * @Description: 根据项目查询申请的构件及明细
	 * @Author xiaohe
	 * @DateTime 2019年5月5日 下午2:44:33
	 * @param proId 模型编号
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompAndDetailMap(java.lang.String)
	 */
	@Override
	public Map<String, Object> getCompAndDetailMap(String proId) {

		Map<String, Object> maps = Maps.newHashMap();

		List<ComponentDTO> dtos = Lists.newArrayList();
//		List<XmlEntity> xmls = Lists.newArrayList();
		Map<String, XmlEntity> xmlMap = Maps.newHashMap();
		Map<String, XmlEntityMap> xmlEntityMap = Maps.newHashMap();
		// 查询流程
//		String pro = compDetailMapper.selectById(proId).getParaentId();
		// 查询公共构件的
		List<Component> comps = baseMapper.selectByComms(proId);
//		List<Component> comps = baseMapper.selectList(Wrappers.<Component>query().lambda().inSql(Component::getId,
//				"SELECT comp_id FROM gjk_project_comp a WHERE  a.project_id='" + proId + "' and a.can_use = 0"));
		System.out.println(comps);
//		List<Component> comps = baseMapper.listCompByUserId(proId);
		for (Component comp : comps) {
			// 查询基本构件中的明细
//			ComponentDetail vo = compDetailMapper.getCompXMl(comp.getId());
			// 查询公共构件中的明细
			ComponentDetail vo = compDetailMapper.getCommCompXMl(comp.getId());
			if (vo != null) {
				String path = vo.getFileName();
				File file = null;
				if (path.startsWith("Component") && path.toUpperCase().endsWith(".XML")) {
					file = new File(this.compDetailPath + File.separator + vo.getFilePath() + "/" + path);
				}
				if (file.exists()) {
					// 将构件文件放入map
					dtos.add(XmlAnalysisUtil.xmlFileToComponentDTO(comp, vo));
//					xmls.add(XmlFileHandleUtil.analysisXmlFile(file));
					xmlMap.put(vo.getCompId(), XmlFileHandleUtil.analysisXmlFile(file));
					xmlEntityMap.put(vo.getCompId(), XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file));
				}

			}
		}
		// 构件编号集合
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
		Map<String, String> compUpdate = Maps.newHashMap();
		// 版本集合
		List<Double> versions = new ArrayList<Double>();
		// 获取所有编号的构件集合
		if (compIdList.size() > 0) {
			List<Component> compList = baseMapper.checkComp(compIdList);
			for (Map.Entry<String, String> entry : compIdMap.entrySet()) {
				for (Component comp : compList) {
					if (entry.getValue().equals(comp.getCompId())) {
						versions.add(Double.parseDouble(comp.getVersion()));
					}
				}
				Collections.sort(versions);
				versions.get(versions.size() - 1);
				// strJson +=
				// compNameMap.get(entry.getKey())+"：当前版本"+versionMap.get(entry.getKey())+";
				// 最高版本为"+versions.get(versions.size()-1)+"\n";
//		    	Double version = Double.valueOf(versionMap.get(entry.getKey()));
//		    	Double versionTmp = Double.valueOf(versions.get(versions.size()-1));
				if ((Double.valueOf(versionMap.get(entry.getKey()))) < (Double
						.valueOf(versions.get(versions.size() - 1)))) {
					compUpdate.put(entry.getKey(), "0");// 已更新
				} else {
					compUpdate.put(entry.getKey(), "1");// 未更新
				}
				versions.clear();
			}
		}

		maps.put("dtos", dtos);
		maps.put("xmls", xmlMap);
		maps.put("xmlMaps", xmlEntityMap);
		maps.put("compUpdate", compUpdate);
		return maps;
	}

	/**
	 * @saveComp
	 * @Description: 保存comp或者
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:07:37
	 * @param component
	 * @return
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
	 * @getCompFiles
	 * @Description:
	 * @Author cvics
	 * @DateTime 2019年5月23日 上午11:36:26
	 * @param compId
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompFiles(java.lang.String)
	 */
	public Map<String, Object> getCompFiles(String compId) {
		Map<String, Object> fileMap = Maps.newHashMap();
		Component comp = baseMapper.listCompByCompId(compId);
		fileMap.put("comp", comp);
		List<ComponentDetail> vos = compDetailMapper.listCompDetailByCompId(comp.getId());
		for (ComponentDetail parent : vos) {
			// 存文件目录及文件列表
			Map<String, Object> map = Maps.newHashMap();
			List<CompFilesVO> filesVOs = Lists.newArrayList();
			if ("algorithmfile".equals(parent.getFileType()) || "testfile".equals(parent.getFileType())
					|| "platformfile".equals(parent.getFileType())) {
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
						if ("imgfile".equals(parent.getFileType())) {
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
				File file = null;
				if (path.startsWith("Component") && path.toUpperCase().endsWith(".XML")) {
					file = new File(this.compDetailPath + File.separator + parent.getFilePath() + "/" + path);
					fileMap.put("compBasic", XmlFileHandleUtil.analysisXmlFile(file));
					fileMap.put("compBasicMap", XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file));
				}
			}

		}

		return fileMap;
	}

	@Override
	public String getImgFileStr(String imgId) {
		String imgPath = "D:/14S_GJK_GIT/gjk/gjk/image/111.png";
		StringBuffer strb = new StringBuffer("data:image/;base64,");
		strb.replace(11, 11, imgPath.substring(imgPath.lastIndexOf(".") + 1));
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgPath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		String Base64 = encoder.encode(Objects.requireNonNull(data));
		strb.append(Base64);
		// 返回Base64编码过的字节数组字符串xx`x`
//		System.out.println(strb.toString());
		return strb.toString();
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

	@Override
	public XmlEntityMap analysisXmlFile(String filePath) {
		File file = null;
		XmlEntityMap xmlEntityMap = null;
		if (StringUtils.isNotEmpty(filePath)) {
			file = new File(this.compDetailPath + filePath);
		}
		if (file.exists()) {
			xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);
		}
		return xmlEntityMap;
	}

	@Override
	public List<Component> analysisZipFile(MultipartFile ufile, String userId, String userName) {
		String filePath = this.compDetailPath + "gjk" + File.separator + "zipFile" + File.separator
				+ (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + "-" + ufile.getOriginalFilename();
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			ufile.transferTo(file);

			String descDirPath = filePath.substring(0, filePath.lastIndexOf("."));
			// 解压zip文件夹
			unZipFiles(filePath, descDirPath);

			List<Component> components = null;
			if ("component".equals(new File(descDirPath).listFiles()[0].getName())) {
				String excelFilePath = descDirPath + File.separator + "component" + File.separator + "MySQL.xls";
				components = readExcel(descDirPath, excelFilePath, userId, userName);
				// 删除压缩包
				cn.hutool.core.io.FileUtil.del(filePath);
				// 删除解压包
				cn.hutool.core.io.FileUtil.del(descDirPath);
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
	private static void unZipFiles(String zipPath, String descDir) throws IOException {
		try {
			File zipFile = new File(zipPath);
			if (!zipFile.exists()) {
				throw new IOException("需解压文件不存在.");
			}
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

			String beforeFilePath = unZipFilePath + File.separator + "component" + File.separator + comp.getCompId()
					+ File.separator + comp.getVersion() + File.separator;
			String beforeDetailFilePath = "gjk" + File.separator + "common" + File.separator + "component"
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
			String afterFilePath = "gjk" + File.separator + "component" + File.separator + userName + File.separator
					+ comp.getCompId() + File.separator + timeStr + File.separator;
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
				compUpdate.put(entry.getKey(), "0");// 已更新
			} else {
				compUpdate.put(entry.getKey(), "1");// 未更新
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
	 * @Title: getImgFile
	 * @Description: listCompByUserId
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 上午11:34:43
	 * @param userId 用户Id
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
	 * @Title: compJsplumbToGojsImg
	 * @Desc
	 * @Author cvics
	 * @DateTime 2020年3月27日
	 * @return
	 * @see com.inforbus.gjk.comp.service.ComponentService#compJsplumbToGojsImg()
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public R<T> compJsplumbToGojsImg() {
		try {
			// 第一步 备份数据库
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			// 要替换的src 的 格式
			String rgex_src = "<img src=['|\"|](.*?)['|\"|] style";
			// 要替换 名字的格式
			String rgex_Name = "<div class='desc' id='\" \\+ i \\+ \"'>(.*?)</div>";
			String format = dateTimeFormatter.format((LocalDateTime.now()));

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
				String imgSrc = getSubUtilSimple(oldDivStyle, rgex_src);
				String imgName = getSubUtilSimple(oldDivStyle, rgex_Name);
				if (StringUtils.isNotEmpty(imgSrc)) {
					// 替换字符串
					component.setCompImg(String.format(baseformat, component.getId(), imgSrc, imgName));
					// 修改数据
					baseMapper.updateById(component);
				}
			}

			List<CommonComponent> commonCompPages = commonComponentServiceFeign.getCommonComponentPage(new Page<>(), new CommonComponent()).getData()
					.getRecords();
			for (CommonComponent commonComponent : commonCompPages) {
				String oldDivStyle = commonComponent.getCompImg();
				String imgSrc = getSubUtilSimple(oldDivStyle, rgex_src);
				String imgName = getSubUtilSimple(oldDivStyle, rgex_Name);
				if (StringUtils.isNotEmpty(imgSrc)) {
					// 替换字符串
					commonComponent.setCompImg(String.format(baseformat, commonComponent.getId(), imgSrc, imgName));
					// 修改数据
					commonComponentServiceFeign.update(commonComponent);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动回滚失误
			return new R<>(e); //返回你自己的错误信息
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
	public  String getSubUtilSimple(String soap, String rgex) {
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(soap);
		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public static void main(String[] args) {
		String strs = "<div style=text-align:center;height:80px;width:110px;border:1px solid #000;border-radius:5px;background-color: null;display: block;'>\r\n"
				+ "        <img src='data:image/png;base64,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' style='vertical-align: middle;width: 110px; height:80px;border-radius:5px;'>\r\n"
				+ "        <div class='desc' id='\" + i + \"'>测试构件0114</div>\r\n" + "    </div>";
		String strss="<div data-v-6ff96f48=\"\" id=\"$divId$\" class=\"avatar-uploader\"\r\n" + 
				"    style=\"margin:10px auto 10px auto;text-align: center; border: 1px none rgb(0, 0, 0); display: flex; justify-content: center; align-items: center; height: 100px; width: 110px; border-radius: 5px; background-color: rgb(173, 210, 183);\">\r\n" + 
				"        <div data-v-6ff96f48=\"\"><img data-v-6ff96f48=\"\"\r\n" + 
				"               src='data:image/png;base64,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'" + 
				"                style=\"width: 100px; height: 70px;\"><span data-v-6ff96f48=\"\"\r\n" + 
				"                style=\"display: inline-block; line-height: 18px; font-size:14px;\">构件02</span></div>\r\n" + 
				"    </div>";
//
		String rgex_src = " src=['|\"|](.*?)['|\"|] style";
		String rgex_Name = "<div class='desc' id='\" \\+ i \\+ \"'>(.*?)</div>";
		String rgex_Name1 = "\">(.*?)</span></div>";
//
////		String str = "<?xml version='1.0' encoding='UTF-8'?><ufinterface billtype='gl' filename='e:\1.xml' isexchange='Y' proc='add' receiver='1060337@1060337-003' replace='Y' roottag='sendresult' sender='01' successful='Y'><sendresult><billpk></billpk><bdocid>w764</bdocid><filename>e:\1.xml</filename><resultcode>1</resultcode><resultdescription>单据w764开始处理...单据w764处理完毕!</resultdescription><content>2017.09-记账凭证-1</content></sendresult><sendresult><billpk></billpk><bdocid>w1007</bdocid><filename>e:\1.xml</filename><resultcode>1</resultcode><resultdescription>单据w1007开始处理...单据w1007处理完毕!</resultdescription><content>2017.10-记账凭证-1</content></sendresult><sendresult><billpk></billpk><bdocid>w516</bdocid><filename>e:\1.xml</filename><resultcode>1</resultcode><resultdescription>单据w516开始处理...单据w516处理完毕!</resultdescription><content>2017.07-记账凭证-50</content></sendresult></ufinterface>";
////
////		//String str = "abc3443abcfgjhgabcgfjabc";  
////
////		String rgex = "<bdocid>(.*?)</bdocid>";
//
//		String sformat = "    <div data-v-6ff96f48=\"\" id=\"%s\" class=\"avatar-uploader\"\r\n"
//				+ "    style=\"margin:10px auto 10px auto;text-align: center; border: 1px none rgb(0, 0, 0); display: flex; justify-content: center; align-items: center; height: 100px; width: 110px; border-radius: 5px; background-color: rgb(173, 210, 183);\">\r\n"
//				+ "        <div data-v-6ff96f48=\"\"><img data-v-6ff96f48=\"\" src=\"%s\" style=\"width: 100px; height: 70px;\"><span data-v-6ff96f48=\"\" style=\"display: inline-block; line-height: 18px; font-size:14px;\">%s</span></div>\r\n"
//				+ "    </div>";
//
//		System.out.println(ComponentServiceImpl.getSubUtilSimple(strs, rgex_src));
//		System.out.println(String.format(sformat, "000", ComponentServiceImpl.getSubUtilSimple(strs, rgex_src),
//				ComponentServiceImpl.getSubUtilSimple(strs, rgex_Name)));
////	    List<String> lists = ComponentServiceImpl.getSubUtil(str,rgex);
////	    for (String string : lists) {
////			System.out.println(string);
////		}
		ComponentServiceImpl ser= new ComponentServiceImpl();
		System.out.println( "000000 "+ser.getSubUtilSimple(strss, rgex_Name1));
		System.out.println( "111111 "+ ser.getSubUtilSimple(strss, rgex_Name));
		System.out.println( "000000 "+ser.getSubUtilSimple(strss, rgex_src));
		System.out.println( "111111 "+ ser.getSubUtilSimple(strs, rgex_src));

	}
}
