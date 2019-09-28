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
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.api.entity.ThreeLibs;
import com.inforbus.gjk.libs.mapper.ThreeLibsMapper;
import com.inforbus.gjk.libs.service.ThreeLibsService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@Service("ThreeLibsService")
public class ThreeLibsServiceImpl extends ServiceImpl<ThreeLibsMapper, ThreeLibs> implements ThreeLibsService {

	private static final String softwarePath = JGitUtil.getLOCAL_REPO_PATH();

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

	//文本编辑器的
	public void saveFileContext(String filePath, String textContext) {
//	 public static void main(String[] args) {
		String fileName =filePath;
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

}
