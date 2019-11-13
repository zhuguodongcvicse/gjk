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

import java.io.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	private static final String defaultEncoding = JGitUtil.getDefaultEncoding();

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

	@PostMapping("/readAlgorithmfile")
	public R fileRead(@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO) throws Exception {
		String fileName = threeLibsFilePathDTO.getFilePathName();
		//获取文件编码格式
		String code = "";
		if(StringUtils.isEmpty(threeLibsFilePathDTO.getCode())) {
			code = defaultEncoding;
		}else {
			code = threeLibsFilePathDTO.getCode();
		}
		if(StringUtils.isEmpty(fileName)) {
			return new R<>();
		}
		File isFile = new File(fileName);
		//获取文件编码格式
		//获得文件编码
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
			// 读取//.h .m .c .o等客户 文件
			else {
				// 获得文件编码
//				String fileEncode=EncodingDetect.getJavaEncode(fileName);
				//String encoding = "utf-8";
				try {
					str = FileUtils.readFileToString(new File(fileName), code);
					System.out.println("sss:::" + str);
				} catch (Exception e) {
					System.out.println("读取文件内容出错");
					e.printStackTrace();
				}
			}
			System.out.println("**************************code::::"+code);
			dto.setTextContext(prefix + "@%#@*+-+@" + str + "@%#@*+-+@" +  code);
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
        case 298127:
            code = "UTF-8";
            break;
        case 65534:
            code = "Unicode";
            break;
        case 65279:
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

	@PostMapping("/getFileStream")
	public void getFileStream(HttpServletRequest request, HttpServletResponse response,@RequestBody ThreeLibsFilePathDTO threeLibsFilePathDTO){
		response.setContentType("application/pdf");
		FileInputStream in = null;
		OutputStream out = null;
	  	try {
			in = new FileInputStream(new File(threeLibsFilePathDTO.getFilePath()));
			out = response.getOutputStream();
			byte[] b = new byte[512];
			while ((in.read(b)) != -1) {
				out.write(b);
			}
	  	} catch (IOException e) {
			e.printStackTrace();
	  	}finally {
	  		if(out!=null){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
