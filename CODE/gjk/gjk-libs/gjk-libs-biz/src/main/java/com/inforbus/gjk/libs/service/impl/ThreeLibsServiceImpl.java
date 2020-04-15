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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.dto.ThreeLibsDTO;
import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.api.entity.ThreeLibs;
import com.inforbus.gjk.libs.mapper.ThreeLibsMapper;
import com.inforbus.gjk.libs.service.ThreeLibsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@Service("ThreeLibsService")
public class ThreeLibsServiceImpl extends ServiceImpl<ThreeLibsMapper, ThreeLibs> implements ThreeLibsService {

	@Value("${git.local.path}")
	private String gitFilePath;
	@Value("${gjk.code.encodeing}")
	private String defaultEncoding;

	/*
	 * 通过libs_id获取属于哪个算法类下
	 */
	@Override
	public List<ThreeLibs> getAlgorithmByLibsId() {
		return baseMapper.getAlgorithmByLibsId();
	}

	/*
	 * 获取算法库下面的文件
	 */
	@Override
	public List<ThreeLibs> getAlgorithmFile() {
		return baseMapper.getAlgorithmFile();
	}

	/*
	 * 根据树的节点id获取当条数据，得到filePath
	 */
	@Override
	public List<ThreeLibs> getAlgorithmFilePath(String id) {
		List<ThreeLibs> list = baseMapper.getAlgorithmFilePath(id);
		for (ThreeLibs lists : list) {
			System.out.println("ssss" + lists.getFilePath());
		}
		System.out.println(baseMapper.getAlgorithmFilePath(id));
		return baseMapper.getAlgorithmFilePath(id);
	}

	@Override
	public List<ThreeLibs> getPlatformByLibsId() {
		return baseMapper.getPlatformByLibsId();
	}

	@Override
	public List<ThreeLibs> getPlatformFile() {
		return baseMapper.getPlatformFile();
	}

	@Override
	public List<ThreeLibs> getTestByLibsId() {
		return baseMapper.getTestByLibsId();
	}

	@Override
	public List<ThreeLibs> getTestFile() {
		return baseMapper.getTestFile();
	}

	@Override
	public List<Software> getSoftware() {
		return baseMapper.getSoftware();
	}

	@Override
	public List<SoftwareDetail> getSoftwarePlatform() {
		return baseMapper.getSoftwarePlatform();
	}

	@Override
	public List<SoftwareFile> getSoftwareFile() {
		return baseMapper.getSoftwareFile();
	}

	// 文本编辑器的
	public void saveFileContext(String filePath, String textContext) {
//	 public static void main(String[] args) {
		String fileName = filePath;
//		 String fileName = softwarePath +"soft\\func2.h";
		File file = new File(fileName);
		OutputStream outputStream = null;
		if (file.exists()) {
			try {
				// 如果文件找不到，就new一个
				file.delete();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// 定义输出流，写入文件的流
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 定义将要写入文件的数据
//		String textContext = "Hell Java, Hello World, 你好，世界！";
		// 把string转换成byte型的，并存放在数组中
		byte[] bs = textContext.getBytes();
		try {
			// 写入bs中的数据到file中
			outputStream.write(bs);
			System.out.println("ooooo");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除三个库的时候，如果关联了构件、bsp、软甲框架等文件，不能被删除 根据各个模块用到的三个库的id来查询数据
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, String>> findBSPDetailByPlatformId(String platformId) {
		return baseMapper.findBSPDetailByPlatformId(platformId);
	}

	@Override
	public List<Map<String, String>> findSoftwareDetailByPlatformId(String platformId) {
		return baseMapper.findSoftwareDetailByPlatformId(platformId);
	}

	@Override
	public List<Map<String, String>> findCompframeDetailByPlatformId(String platformId) {
		return baseMapper.findCompframeDetailByPlatformId(platformId);
	}

	@Override
	public List<ComponentDetail> findComponentDetailByPlatformId(String platformId) {
		return baseMapper.findComponentDetailByPlatformId(platformId);
	}

	@Override
	public R fileRead(ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		String fileName = threeLibsFilePathDTO.getFilePathName();
		// 获取文件编码格式
		String code = "";
		if (StringUtils.isEmpty(threeLibsFilePathDTO.getCode())) {
			code = defaultEncoding;
		} else {
			code = threeLibsFilePathDTO.getCode();
		}
		if (StringUtils.isEmpty(fileName)) {
			return new R<>();
		}
		File isFile = new File(fileName);
		// 获取文件编码格式
		// 获得文件编码
//		String fileEncode=EncodingDetect.getJavaEncode(filePath);
//		if(threeLibsFilePathDTO.getEditorCode()!=null) {
//			code = threeLibsFilePathDTO.getEditorCode();
//		}else {
//			code = this.codeString(fileName);
//		}
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);

		String str = "";
		ThreeLibsDTO dto = new ThreeLibsDTO();
		if (isFile.exists() && isFile.isFile()) {

			// 读取excel文件
			if ("xlsx".equals(prefix) || "xls".equals(prefix)) {
				try {

					XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileName);
					// 循环工作表sheet
					XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
					int firstRowIndex = xssfSheet.getFirstRowNum(); // 第一行是列名，所以不读
					int lastRowIndex = xssfSheet.getLastRowNum();
					// 循环行row
					XSSFRow xssfRow = xssfSheet.getRow(0);
					// 循环列cell
					// 用stringbuffer得到excel表格一行的内容并用逗号分隔
					StringBuffer sbs = new StringBuffer();
					for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {// 遍历行
						Row row = xssfSheet.getRow(rIndex);
						if (row != null) {
							int firstCellIndex = row.getFirstCellNum();
							int lastCellIndex = row.getLastCellNum();
							for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {// 遍历列
								Cell cell = row.getCell(cIndex);
								sbs.append(cell);
								if (cell != null) {
									sbs.append(",");
									System.out.println(cell.toString());
								}
							}
						}

					}
					str = sbs.toString();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("读取" + fileName + "文件内容出错");
					return new R<>(new Exception("读取" + fileName + "文件内容出错"));
				}
				// 读取word文件
			} else if ("doc".equals(prefix) || "docx".equals(prefix)) {
				try {

					FileInputStream in = new FileInputStream(fileName);
					WordExtractor extractor = new WordExtractor(in);
					str = extractor.getText();
//				dto.setTextContext(str);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("读取" + fileName + "文件内容出错");
					return new R<>(new Exception("读取" + fileName + "文件内容出错"));
				}
			}
			// 读取//.h .m .c .o等客户 文件
			else {
				// 获得文件编码
//				String fileEncode=EncodingDetect.getJavaEncode(fileName);
				// String encoding = "utf-8";
				try {
					str = FileUtils.readFileToString(new File(fileName), code);
					System.out.println("sss:::" + str);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("读取" + fileName + "文件内容出错");
					return new R<>(new Exception("读取" + fileName + "文件内容出错"));
				}
			}
			System.out.println("**************************code::::" + code);
			dto.setTextContext(prefix + "@%#@*+-+@" + str + "@%#@*+-+@" + code);
		} else {
			System.out.println("找不到指定的文件");
			return new R<>(new Exception("找不到指定的文件"));
		}
		return new R<>(dto);
	}

}
