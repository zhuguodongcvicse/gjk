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
package com.inforbus.gjk.libs.controller;

import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.dto.ThreeLibsDTO;
import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.service.ThreeLibsService;

import lombok.AllArgsConstructor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@RestController
@AllArgsConstructor
@RequestMapping("/threelibs")
public class ThreeLibsController {

	private final ThreeLibsService threeLibsService;
	private static final String softwarePath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 
	 * @Title: getAlgorithmByLibsId
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/algorithmLib")
	public R getAlgorithmByLibsId() {
		return new R<>(threeLibsService.getAlgorithmByLibsId());
	}

	/**
	 * 
	 * @Title: getAlgorithmFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/algorithmfile")
	public R getAlgorithmFile() {
		return new R<>(threeLibsService.getAlgorithmFile());
	}

	/**
	 * 
	 * @Title: getPlatformByLibsId
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/platformLib")
	public R getPlatformByLibsId() {
		return new R<>(threeLibsService.getPlatformByLibsId());
	}

	/**
	 * 
	 * @Title: getPlatformFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/platformfile")
	public R getPlatformFile() {
		return new R<>(threeLibsService.getPlatformFile());
	}

	/**
	 * 
	 * @Title: getTestByLibsId
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/testLib")
	public R getTestByLibsId() {
		return new R<>(threeLibsService.getTestByLibsId());
	}

	/**
	 * 
	 * @Title: getTestFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月4日 下午5:40:37
	 * @return
	 */
	@GetMapping("/testfile")
	public R getTestFile() {
		return new R<>(threeLibsService.getTestFile());
	}

	/**
	 * 根据id获取文件路径
	 * 
	 * @Title: getAlgorithmFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月5日 下午5:29:09
	 * @return
	 */
	@GetMapping("/algorithmfilepath/{id}")
	public R getAlgorithmFilePath(@PathVariable("id") String id) {
		return new R<>(threeLibsService.getAlgorithmFilePath(id));
	}

	/**
	 * 根据文件路径取文件内容
	 * 
	 * @Title: fileRead
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月10日 上午9:55:30
	 * @throws Exception
	 */

	@RequestMapping("/readAlgorithmfile")
	public R fileRead(ThreeLibsFilePathDTO threeLibsFilePathDTO) throws Exception {
		String fileName = threeLibsFilePathDTO.getFilePathName();
		if(StringUtils.isEmpty(fileName)) {
			return new R<>();
		}
		File isFile = new File(fileName);
		//获取文件编码格式
		String code = this.codeString(fileName);
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
					System.out.println("读取文件内容出错");
					e.printStackTrace();
				}
				// 读取word文件
			} else if ("doc".equals(prefix) || "docx".equals(prefix)) {
				try {
					
					FileInputStream in = new FileInputStream(fileName);
					WordExtractor extractor = new WordExtractor(in);
					str = extractor.getText();
//				dto.setTextContext(str);
				} catch (Exception e) {
					System.out.println("读取文件内容出错");
					e.printStackTrace();
				}

			}
//		else if ("doc".equals(prefix) ) {
//			WordExtractor wordExtractor;
//			try {
//				FileInputStream file = new FileInputStream(fileName);
//				wordExtractor = new WordExtractor(file);
//				String[] paragraph = wordExtractor.getParagraphText();
//				for(int j=0; j < paragraph.length; j++) {
//					str = str + paragraph[j].toString();
//				}
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
			// 读取//.h .m .c .o 文件
			else if ("h".equals(prefix) || "c".equals(prefix) || "cpp".equals(prefix)) {
				// 获得文件编码
//				String fileEncode=EncodingDetect.getJavaEncode(fileName);   
				//String encoding = "utf-8";
				
				try {
					str = FileUtils.readFileToString(new File(fileName), code);
					System.out.println("sss:::" + str);

					// 之前的老方法
					// 考虑到编码格式
//					InputStreamReader read = new InputStreamReader(new FileInputStream(fileName), encoding);
//					BufferedReader bufferedReader = new BufferedReader(read);
//					// 定义一个字符串缓存，将字符串存放缓存中
//					StringBuilder sb = new StringBuilder();
//					String lineTxt = null;
//					while ((lineTxt = bufferedReader.readLine()) != null) {
//						sb.append(lineTxt + "\n");
//					}
//					read.close();
//					str = sb.toString();
				} catch (Exception e) {
					System.out.println("读取文件内容出错");
					e.printStackTrace();
				}
			}

			dto.setTextContext(prefix + "======" + str);
		} else {
			System.out.println("找不到指定的文件");
		}
		return new R<>(dto);

	}
	
    public String codeString(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        String code = null;
 
        switch (p) {
        case 0xefbb:
            code = "UTF-8";
            break;
        case 0xfffe:
            code = "Unicode";
            break;
        case 0xfeff:
            code = "UTF-16BE";
            break;
        default:
            code = "GBK";
        }
 
        return code;
    }

	/**
	 * 得到平台库的文件夹树
	 * 
	 * @Title: getAlgorithmFile
	 * @Description:
	 * @Author cvicse
	 * @DateTime 2019年6月5日 下午5:29:09
	 * @return
	 */
	@GetMapping("/getSoftwarePlatformTree")
	public R getSoftwarePlatformTree() {
		System.out.println("ppppppp");
		try {
			List<Software> soft = threeLibsService.getSoftware();
			List<SoftwareDetail> softdetail = threeLibsService.getSoftwarePlatform();
			List<SoftwareFile> softfile = threeLibsService.getSoftwareFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new R<>();
	}

	@PostMapping("/saveFileContext")
	public void saveFileContext(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		System.out.println("uiuiiuiuiui:::" + threeLibsFilePathDTO.getFilePathName());
		threeLibsService.saveFileContext(threeLibsFilePathDTO.getFilePath(), threeLibsFilePathDTO.getFilePathName());
	}

}
