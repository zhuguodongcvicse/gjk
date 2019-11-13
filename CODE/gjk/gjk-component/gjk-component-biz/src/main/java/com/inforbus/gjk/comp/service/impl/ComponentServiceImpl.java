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
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.FileUtil;
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
import com.inforbus.gjk.comp.service.ComponentDetailService;
import com.inforbus.gjk.comp.service.ComponentService;

import cn.hutool.db.sql.Wrapper;
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
import java.time.LocalDateTime;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	protected ComponentDetailMapper compDetailMapper;
	@Autowired
	private CompImgMapper compImgMapper;
	@Autowired
	private ComponentDetailService componentDetailService;
	@Autowired
	private AmqpTemplate rabbitmqTemplate;

	private static final String compDetailPath = JGitUtil.getLOCAL_REPO_PATH();

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
	public boolean deleteCompAndCompDetail(String compId) {
		List<ComponentDetail> details = compDetailMapper.listCompDetailByCompId(compId);
		try {
			Component comp = this.getById(compId);
			String filePath = compDetailPath + "gjk" + File.separator + "component" + File.separator + comp.getCompId()
					+ File.separator + comp.getVersion() + File.separator;

			// 删除构件
			this.removeById(compId);
			// 删除构件详细信息
			for (ComponentDetail detail : details) {
				componentDetailService.removeById(detail.getId());
			}
			// 删除构件相关文件
			cn.hutool.core.io.FileUtil.del(filePath);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
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
	public List<CompDetailVO> getCompByUserId(String userId) {
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
	 * @see com.inforbus.gjk.comp.service.ComponentService#getCompByCompId(java.lang.String)
	 */
	@Override
	public List<CompDetailVO> getCompByCompId(String compId, boolean isShowCompXml) {
		Component comp = baseMapper.listCompByCompId(compId);
		List<CompDetailVO> tree = Lists.newArrayList();
//			将构件转成树
		tree.add(new CompDetailVO(comp.getId(), comp.getCompName(), "", "", "-1", comp.getVersion()));
		List<ComponentDetail> vos = compDetailMapper.listCompDetailByCompId(comp.getId());
		// 如果为true,则显示comp的xml文件，不走if内
		for (ComponentDetail v : vos) {
			if (!isShowCompXml) {
				if (!v.getFileType().equalsIgnoreCase("xml")) {
					addTree(tree, v);
				}
			} else {
				addTree(tree, v);
			}
			if ("algorithmfile".equals(v.getFileType()) || "testfile".equals(v.getFileType())
					|| "platformfile".equals(v.getFileType())) {
				File file = new File(compDetailPath + v.getFilePath() + File.separator + v.getFileName());
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
		tree.add(new CompDetailVO(fileId, file.getName(), "", file.getParentFile().getAbsolutePath(), parentId, ""));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addCompDetailTree(tree, fileId, childFile);
			}
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
		tree.add(new CompDetailVO(v.getId(), v.getFileName(), v.getFileType(), compDetailPath + v.getFilePath(),
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
					file = new File(JGitUtil.getLOCAL_REPO_PATH() + "/" + vo.getFilePath() + "/" + path);
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
		if(compIdList.size()>0) {
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
			String version = ("0").equals(component.getApplyState()) ? component.getVersion()
					: Double.toString(Double.parseDouble(component.getVersion()) + 1);
			component.setVersion(version);
			// 修改主表信息
			baseMapper.editComp(component);
		} else {
			component.setId(IdGenerate.uuid());
			component.setVersion(getVersion(component));
			baseMapper.saveComp(component);
		}
		return component;
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
				File file = new File(compDetailPath + parent.getFilePath() + File.separator + parent.getFileName());
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
					file = new File(JGitUtil.getLOCAL_REPO_PATH() + "/" + parent.getFilePath() + "/" + path);
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
		String basePath = JGitUtil.getLOCAL_REPO_PATH();
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
			file = new File(compDetailPath + filePath);
		}
		if (file.exists()) {
			xmlEntityMap = XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file);
		}
		return xmlEntityMap;
	}

	@Override
	public List<Component> analysisZipFile(MultipartFile ufile) {
		String filePath = compDetailPath + "gjk" + File.separator + "zipFile" + File.separator
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
				components = readExcel(descDirPath, excelFilePath);
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
	public List<Component> readExcel(String unZipFilePath, String path) throws Exception {
		List<Component> compList = new ArrayList<Component>();

		// 获取整个表格文件对象
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));

		List<Component> comps = new ArrayList<Component>();
		List<ComponentDetail> compDetails = new ArrayList<ComponentDetail>();

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

			String beforeFilePath = unZipFilePath + File.separator + "component" + File.separator + comp.getCompName()
					+ File.separator + comp.getVersion() + File.separator;
			String beforeDetailFilePath = "gjk" + File.separator + "component" + File.separator + comp.getCompName()
					+ File.separator + comp.getVersion() + File.separator;

			comp.setVersion(getVersion(comp));
			comp.setApplyState("0");
			comp.setApplyDesc("未申请入库");
			comp.setCreateTime(LocalDateTime.now());
			comp.setUpdateTime(null);
			// 保存构件
			baseMapper.saveComp(comp);

			for (ComponentDetail detail : componentDetails) {
				String filePath = detail.getFilePath().substring(beforeDetailFilePath.length());
				detail.setFilePath("gjk" + File.separator + "component" + File.separator + comp.getCompName()
						+ File.separator + comp.getVersion() + File.separator + filePath);
				detail.setVersion(comp.getVersion());
				detail.setId(IdGenerate.uuid());
				compDetailMapper.saveCompDetail(detail);
			}

			String newFilePath = compDetailPath + "gjk" + File.separator + "component" + File.separator
					+ comp.getCompName() + File.separator + comp.getVersion() + File.separator;

			FileUtil.copyDir(beforeFilePath, newFilePath);
			compList.add(this.getById(comp.getId()));
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

}
