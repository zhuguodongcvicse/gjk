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
package com.inforbus.gjk.libs.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.constant.ComponentConstant;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.libs.api.dto.CompTree;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.api.feign.RemoteStructServiceFeign;
import com.inforbus.gjk.libs.api.vo.CompDictVO;
import com.inforbus.gjk.libs.api.vo.CompVO;
import com.inforbus.gjk.libs.api.vo.TreeUtil;
import com.inforbus.gjk.libs.mapper.CommonComponentDetailMapper;
import com.inforbus.gjk.libs.mapper.CommonComponentMapper;
import com.inforbus.gjk.libs.mapper.StructlibsMapper;
import com.inforbus.gjk.libs.service.BatchApprovalService;
import com.inforbus.gjk.libs.service.CommonComponentService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;

/**
 * 公共构件库详细表
 *
 * @author pig code generator
 * @date 2019-06-10 14:25:55
 */
@Service("commonComponentService")
public class CommonComponentServiceImpl extends ServiceImpl<CommonComponentMapper, CommonComponent>
		implements CommonComponentService {

	@Value("${git.local.path}")
	private String gitFilePath;

	@Autowired
	private StructlibsMapper structlibsMapper;

	@Autowired
	private CommonComponentDetailMapper componentDetailMapper;

	@Autowired
	private BatchApprovalService batchApprovalService;
	@Autowired
	private RemoteStructServiceFeign rsService;

	/**
	 * 公共构件库详细表简单分页查询
	 * 
	 * @param commonComponent 公共构件库详细表
	 * @return
	 */
	@Override
	public IPage<CommonComponent> getCommonComponentPage(Page<CommonComponent> page, CommonComponent commonComponent) {
		return baseMapper.getCommonComponentPage(page, commonComponent);
	}

	public List<CommonComponentDetail> deleteCompById(String id) {
		List<CommonComponent> components = new ArrayList<CommonComponent>();
		components.add(new CommonComponent(id));
		List<CommonComponentDetail> commonComponentDetails = componentDetailMapper.getAllCompDetailByCompId(components);
		for (CommonComponentDetail detail : commonComponentDetails) {
			if ("xml".equals(detail.getFileType())) {
				String compPath = gitFilePath + detail.getFilePath();
				cn.hutool.core.io.FileUtil.del(compPath);
				break;
			}
		}
		return commonComponentDetails;
	}

	@Override
	public boolean saveCommonComp(CommonComponent commonComponent) {
		List<Double> versions = getVersionByCompId(commonComponent);
		// 对版本号List进行排序
		Collections.sort(versions);
		if (versions.size() != 0) {
			commonComponent.setVersion((versions.get(versions.size() - 1) + 1 + ""));
		} else {
			commonComponent.setVersion(1.0 + "");
		}
		try {
			String divId = " id='" + commonComponent.getId() + "'";
			String img = commonComponent.getCompImg();// >测试构件01-Vnull<
			img = img.replace("id=\"$divId$\"", divId);
			String showName = commonComponent.getCompName() + "_V" + commonComponent.getVersion();
			System.out.println("showName:  " + showName);
			img = img.replaceAll(">" + commonComponent.getCompName() + "([^<>]*)<", ">" + showName + "<");
			System.out.println("img:  " + img);

			commonComponent.setCompImg(img);
			baseMapper.saveCommonComp(commonComponent);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回此构件的所有版本
	 * 
	 * @param commonComponent
	 * @return
	 */
	private List<Double> getVersionByCompId(CommonComponent commonComponent) {
		List<Double> list = new ArrayList<Double>();
		for (CommonComponent comp : baseMapper.getVersionByCompId(commonComponent)) {
			list.add(Double.parseDouble(comp.getVersion()));
		}
		return list;
	}

	@Override
	public List<CommonComponent> getAllVersionByCompId(CommonComponent commonComponent) {
		return baseMapper.getVersionByCompId(commonComponent);
	}

	@Override
	public IPage<CommonComponent> getScreenComp(Page page, List<String> libsList) {
		return baseMapper.getScreenComp(page, libsList);
	}

	@Override
	public List<CompTree> getScreenComp(List<String> libsList) {
		return TreeUtil.buildCompTree(getAllComp(baseMapper.getScreenComp(libsList)), "-1");
	}

	/**
	 * @Title: createZip
	 * @Desc 创建压缩流数据
	 * @Author xiaohe
	 * @DateTime 2020年5月12日
	 * @param compList 构件列表
	 * @param details  构件文件列表
	 * @return 字节流数据
	 * @throws Exception
	 * @see com.inforbus.gjk.libs.service.CommonComponentService#createZip(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public byte[] createZip(List<CommonComponent> compList, List<CommonComponentDetail> details) throws Exception {
		InputStream inputStream = null;
		Map<String, String> filePaths = Maps.newHashMap();
		// 将目标文件打包成zip导出
		for (CommonComponent comp : compList) {
			String compFilePath = null;
			for (CommonComponentDetail detail : details) {
				if (detail.getCompId().equals(comp.getId()) && "xml".equals(detail.getFileType())) {
					compFilePath = detail.getFilePath();
					break;
				}
			}
			if (StringUtils.isNotEmpty(compFilePath)) {
				String target = ComponentConstant.COMP + File.separator + comp.getCompId() + File.separator;
				// 设置需要下载的文件
				filePaths.put(this.gitFilePath + File.separator + compFilePath, target);
			}
		}
		try {
			MultipartFile mf = this.createExcelFileToZipIO(compList, details);
			// feign文件下载
			JSONObject json = JSONUtil.parseFromMap(filePaths);
			Response response = rsService.downloadStreamFiles(new MultipartFile[] { mf },
					new String[] { ComponentConstant.COMP + File.separator }, json.toJSONString(0));
			Response.Body body = response.body();
			inputStream = body.asInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 数据中心返回的ZIP流
		return IOUtils.toByteArray(inputStream);
	}

	/**
	 * 根据打包路径打包文件或文件夹
	 * 
	 * @param zip  打包压缩文件流
	 * @param file 需要打包的文件或文件夹
	 * @param dir  打包路径
	 * @throws Exception
	 */
	private void zipDirOrFile(ZipOutputStream zip, File file, String dir) throws Exception {
		// 如果当前的是文件夹，则进行进一步处理
		if (file.isDirectory()) {
			// 得到文件列表信息
			File[] files = file.listFiles();
			// 将文件夹添加到下一级打包目录
			zip.putNextEntry(new ZipEntry(dir + "/"));
			dir = dir.length() == 0 ? "" : dir + "/";
			// 循环将文件夹中的文件打包
			for (int i = 0; i < files.length; i++) {
				zipDirOrFile(zip, files[i], dir + files[i].getName()); // 递归处理
			}
		} else { // 当前的是文件，打包处理
			// 文件输入流
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			ZipEntry entry = new ZipEntry(dir);
			zip.putNextEntry(entry);
			zip.write(FileUtils.readFileToByteArray(file));
			IOUtils.closeQuietly(bis);
			zip.flush();
			zip.closeEntry();
		}
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
	 * 获取属性值
	 * 
	 * @param fieldName 字段名称
	 * @param o         对象
	 * @return Object
	 */
	private static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1); // 获取方法名
			Method method = o.getClass().getMethod(getter, new Class[] {}); // 获取方法对象
			Object value = method.invoke(o, new Object[] {}); // 用invoke调用此对象的get字段方法
			return value; // 返回值
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private MultipartFile createExcelFileToZipIO(List<CommonComponent> compList, List<CommonComponentDetail> details)
			throws Exception {
		// 创建表格工作空间
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 构件库页
		createSheet(workbook, compList, "gjk_common_component");
		// 构件库详情页
		createSheet(workbook, details, "gjk_common_component_detail");

		// 构件库和结构体表关系页
		List<String> compIdList = compList.stream().map(CommonComponent::getId).collect(Collectors.toList());
		List<CompStruct> compStructList = baseMapper.getCompStructByCompIdList(compIdList);
		createSheet(workbook, compStructList, "gjk_comp_struct");

		// 结构体页
		List<Structlibs> structlibsList = new ArrayList<>();
		List<String> structIdList = compStructList.stream().map(CompStruct::getStructId).collect(Collectors.toList());
		List<Structlibs> structlibsListSub = new ArrayList<>();
		if (structIdList.size() > 0) {
			structlibsListSub = structlibsMapper.getStructlibsByIdList(structIdList);
		}

		structlibsList.addAll(structlibsListSub);
		findStructlibsRecursion(structlibsListSub, structlibsList);
		createSheet(workbook, structlibsList, "gjk_structlibs");
		File file = new File(gitFilePath + "gjk" + File.separator + "testExcel" + File.separator + "MySQL.xlsx");
		FileItem fileItem = UploadFilesUtils.createFileItem(file.getPath(), file.getName());
		MultipartFile mfile = new CommonsMultipartFile(fileItem);
		return mfile;
	}

	/**
	 * 根据构件和构件详细信息表生成Excel文件并添加到压缩文件流中
	 * 
	 * @param compList
	 * @param details
	 * @param zip
	 * @throws Exception
	 * @throws IOException
	 */
	private void createExcelFileToZipIO(List<CommonComponent> compList, List<CommonComponentDetail> details,
			ZipOutputStream zip) throws Exception, IOException {
		// 创建表格工作空间
		XSSFWorkbook workbook = new XSSFWorkbook();

		// 创建一个新表格
//		XSSFSheet compXssfSheet = workbook.createSheet("gjk_common_component");
//		// set Sheet页头部
//		List<String> compColumnList = setSheetHeader(workbook, compXssfSheet, "gjk_common_component");
//		// set Sheet页内容
//		List<Object> list = new ArrayList<Object>();
//		list.addAll(compList);
//		setSheetContent(workbook, compXssfSheet, list, compColumnList);
		// 构件库页
		createSheet(workbook, compList, "gjk_common_component");

//		XSSFSheet compDetailSheet = workbook.createSheet("gjk_common_component_detail");
//		// set Sheet页头部
//		List<String> compDetailColumnList = setSheetHeader(workbook, compDetailSheet, "gjk_common_component_detail");
//		// set Sheet页内容
//		list = new ArrayList<Object>();
//		list.addAll(details);
//		setSheetContent(workbook, compDetailSheet, list, compDetailColumnList);
		// 构件库详情页
		createSheet(workbook, details, "gjk_common_component_detail");

		// 构件库和结构体表关系页
		List<String> compIdList = compList.stream().map(CommonComponent::getId).collect(Collectors.toList());
		List<CompStruct> compStructList = baseMapper.getCompStructByCompIdList(compIdList);
		createSheet(workbook, compStructList, "gjk_comp_struct");

		// 结构体页
		List<Structlibs> structlibsList = new ArrayList<>();
		List<String> structIdList = compStructList.stream().map(CompStruct::getStructId).collect(Collectors.toList());
		List<Structlibs> structlibsListSub = new ArrayList<>();
		if (structIdList.size() > 0) {
			structlibsListSub = structlibsMapper.getStructlibsByIdList(structIdList);
		}

		structlibsList.addAll(structlibsListSub);
		findStructlibsRecursion(structlibsListSub, structlibsList);
		createSheet(workbook, structlibsList, "gjk_structlibs");

		// 创建Excel文件保存的临时地址
		File file = new File(gitFilePath + "gjk" + File.separator + "testExcel" + File.separator + "MySQL.xlsx");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		// 创建Excel文件压缩目录
		ZipEntry entry = new ZipEntry("component" + File.separator + "MySQL.xlsx");
		zip.putNextEntry(entry);
		// 将Excel文件内容写入临时文件
		OutputStream op = new FileOutputStream(file);
		workbook.write(op);
		// 将Excel文件读入压缩文件流
		zip.write(FileUtils.readFileToByteArray(file));
		FileItem fileItem = UploadFilesUtils.createFileItem(file.getPath(), file.getName());
		MultipartFile mfile = new CommonsMultipartFile(fileItem);

//		// 将临时文件删除
//		file.delete();
		zip.flush();
		zip.closeEntry();
	}

	/**
	 * 创建一个sheep标签 并处理数据
	 * 
	 * @param workbook
	 * @param dataList
	 * @param tableName
	 * @throws Exception
	 */
	private void createSheet(XSSFWorkbook workbook, List<?> dataList, String tableName) throws Exception {
		XSSFSheet compDetailSheet = workbook.createSheet(tableName);
		// set Sheet页头部
		List<String> compDetailColumnList = setSheetHeader(workbook, compDetailSheet, tableName);
		// set Sheet页内容
		List<Object> list = new ArrayList<Object>();
		list.addAll(dataList);
		setSheetContent(workbook, compDetailSheet, list, compDetailColumnList);
	}

	/**
	 * 递归获取结构体数据
	 * 
	 * @param structlibsList
	 * @param resList
	 */
	private void findStructlibsRecursion(List<Structlibs> structlibsList, List<Structlibs> resList) {
		List<String> structIdList = structlibsList.stream().map(Structlibs::getId).collect(Collectors.toList());
		List<Structlibs> list = new ArrayList<>();
		if (structIdList.size() > 0) {
			list = structlibsMapper.getStructlibsByParentIdList(structIdList);
		}
		if (list.size() > 0) {
			resList.addAll(list);
			findStructlibsRecursion(list, resList);
		}
	}

	/**
	 * 配置Excel表格的顶部信息，如：学号 姓名 年龄 出生年月
	 * 
	 * @param xWorkbook
	 * @param xSheet
	 */
	private List<String> setSheetHeader(XSSFWorkbook xWorkbook, XSSFSheet xSheet, String tableName) {
		List<String> columnList = new ArrayList<>();

		// 设置表格的宽度 xSheet.setColumnWidth(0, 20 * 256); 中的数字 20 自行设置为自己适用的
		xSheet.setColumnWidth(0, 20 * 256);
		xSheet.setColumnWidth(1, 15 * 256);
		xSheet.setColumnWidth(2, 15 * 256);
		xSheet.setColumnWidth(3, 20 * 256);

		// 创建表格的样式
		XSSFCellStyle cs = xWorkbook.createCellStyle();
		// 设置居中
		cs.setAlignment(HorizontalAlignment.CENTER);
		// 设置字体
		Font headerFont = xWorkbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);
//		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("宋体");
		cs.setFont(headerFont);
		cs.setWrapText(true);// 是否自动换行

		// 创建一行
		XSSFRow row = xSheet.createRow(0);

		int columnNum = 0;
		// 设置每一列
		for (Map<String, String> column : queryColumns(tableName)) {
			XSSFCell cell = row.createCell(columnNum);
			cell.setCellStyle(cs);
			cell.setCellValue(columnToJava(column.get("columnName")));
			columnList.add(columnToJava(column.get("columnName")));
			columnNum++;
		}
		return columnList;
	}

	/**
	 * 配置(赋值)表格内容部分
	 * 
	 * @param xWorkbook
	 * @param xSheet
	 * @throws Exception
	 */
	private void setSheetContent(XSSFWorkbook xWorkbook, XSSFSheet xSheet, List<Object> objList,
			List<String> columnList) throws Exception {
		// 创建内容样式（头部以下的样式）
		CellStyle cs = xWorkbook.createCellStyle();
		cs.setWrapText(true);
		cs.setAlignment(HorizontalAlignment.CENTER);

		int rowIndex = 1;
		if (null != objList && objList.size() > 0) {
			for (Object obj : objList) {
				XSSFRow xRow = xSheet.createRow(rowIndex);
				int columnIndex = 0;
				for (String key : columnList) {
					Object temp = getFieldValueByName(key, obj);
					String strTemp = "";
					if (temp != null) {
						strTemp = temp.toString();
					}
					XSSFCell cell = xRow.createCell(columnIndex);
					cell.setCellStyle(cs);
					// 把每个对象此字段的属性写入这一列excel中
					cell.setCellValue(strTemp);

					columnIndex++;
				}
				rowIndex++;
			}
		}
	}

	public List<CompTree> getCompSelectTree() {
		List<CommonComponent> comps = this.list(Wrappers.<CommonComponent>query().lambda());
		List<CompTree> list = TreeUtil.buildCompTree(getAllComp(comps), "-1");
		return list;
	}

	private List<CompVO> getAllComp(List<CommonComponent> comps) {
		List<CompVO> tree = Lists.newArrayList();
		// 获取所有构件名,避免重名
		Set<String> strings = new HashSet<>();
		for (CommonComponent comp : comps) {
			strings.add(comp.getCompId());
		}
		// 创建构件选择树的所有父节点
		for (String s : strings) {
			tree.add(new CompVO("0", s, null, "-1"));
		}
		// 添加构件选择树的所有子节点
		for (CommonComponent comp : comps) {
			tree.add(new CompVO(comp.getId(), comp.getCompName() + "-" + comp.getVersion(), comp.getVersion(),
					comp.getCompId()));
		}
		return tree;
	}

	public List<CompDictVO> getCompDictList(List<String> compIdList) {
		List<CommonComponent> components = new ArrayList<CommonComponent>();
		for (String id : compIdList) {
			components.add(this.getById(id));
		}
		List<CompDictVO> compDictVOs = new ArrayList<>();
		for (CommonComponent comp : components) {
			CompDictVO dictVO = new CompDictVO(comp);
			compDictVOs.add(dictVO);
		}
		return compDictVOs;
	}

	@Override
	public IPage<CommonComponent> getCompListByString(Page page, List<String> selectStringList) {
		List<String> strings = new ArrayList<String>();
		for (String selectStr : selectStringList) {
			strings.add(selectStr.toLowerCase());
		}
		return baseMapper.getCompListByString(page, strings);
	}

	@Override
	public IPage<CommonComponent> getCompListByStringAndLibsId(Page page, List<String> libsList,
			List<String> selectStringList) {
		List<String> strings = new ArrayList<String>();
		for (String selectStr : selectStringList) {
			strings.add(selectStr.toLowerCase());
		}
		return baseMapper.getCompListByStringAndLibsId(page, libsList, strings);
	}

	@Override
	public IPage<CommonComponent> findPageByBatchApprovalId(Page page, String applyId) {
		R componentByApplyId = batchApprovalService.getComponentByApplyId(applyId);
		List<CommonComponent> data = (List<CommonComponent>) componentByApplyId.getData();
		page.setTotal(data.size());
		ArrayList<CommonComponent> commonComponents = Lists.newArrayList();
		for (int i = 0; i < page.getSize(); i++) {
			if (data.size() > (int) ((page.getCurrent() - 1) * page.getSize()) + i) {
				commonComponents.add(data.get((int) ((page.getCurrent() - 1) * page.getSize()) + i));
			}
		}
		page.setRecords(commonComponents);
		return page;
	}

	@Override
	public boolean saveCommonCompList(List<CommonComponent> commonComponents) {
		commonComponents.forEach(comm -> {
			this.saveCommonComp(comm);
		});
		return true;
	}

	@Override
	public List<String> getRemoveCompIdList(List<String> compIdList) {
		List<String> removeCompIdList = new ArrayList<String>();
		for (String id : compIdList) {
			CommonComponent component = this.getById(id);
			if (component == null) {
				removeCompIdList.add(id);
			}
		}
		return removeCompIdList;
	}

}
