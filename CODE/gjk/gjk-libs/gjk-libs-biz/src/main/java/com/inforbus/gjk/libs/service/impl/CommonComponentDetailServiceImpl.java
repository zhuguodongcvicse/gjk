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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.FileUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.UploadFilesUtils;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.api.feign.DataCenterServiceFeign;
import com.inforbus.gjk.libs.api.vo.CommCompDetailVO;
import com.inforbus.gjk.libs.mapper.CommonComponentDetailMapper;
import com.inforbus.gjk.libs.service.CommonComponentDetailService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 公共构件库表
 *
 * @author pig code generator
 * @date 2019-06-10 14:24:03
 */
@Service("commonComponentDetailService")
public class CommonComponentDetailServiceImpl extends ServiceImpl<CommonComponentDetailMapper, CommonComponentDetail>
		implements CommonComponentDetailService {

	@Value("${git.local.path}")
	private String gitFilePath;

	@Autowired
	private DataCenterServiceFeign dataCenterServiceFeign;

	/**
	 * 公共构件库表简单分页查询
	 * 
	 * @param commonComponentDetail 公共构件库表
	 * @return
	 */
	@Override
	public IPage<CommonComponentDetail> getCommonComponentDetailPage(Page<CommonComponentDetail> page,
			CommonComponentDetail commonComponentDetail) {
		return baseMapper.getCommonComponentDetailPage(page, commonComponentDetail);
	}

	/**
	 * 保存构件的详细信息并拷贝用户构件的文件
	 */
	@Override
	public boolean saveCommonCompDetailList(List<CommonComponentDetail> commonComponentDetailList,
			CommonComponent component, String userCurrent) {
		try {
			// 格式：gjk/common/component/构件名/构件版本/ createTime.replaceAll("[[\\s-T:punct:]]",
			// "")
			String compPath = "gjk" + File.separator + "common" + File.separator + "component" + File.separator
					+ component.getCompId() + File.separator + component.getVersion() + File.separator;
			String subStr = null;
			for (CommonComponentDetail detail : commonComponentDetailList) {
				if ("xml".equals(detail.getFileType())) {
					subStr = detail.getFilePath();
					break;
				}
			}
			for (CommonComponentDetail detail : commonComponentDetailList) {
				String originalFileName = gitFilePath + detail.getFilePath() + File.separator + detail.getFileName();
				detail.setFilePath(compPath + detail.getFilePath().substring(subStr.length()));

				if (!dataCenterServiceFeign.isFile(originalFileName).getData()) {
					//feign调用复制文件
					dataCenterServiceFeign.copylocalFile(originalFileName,gitFilePath + detail.getFilePath());
				} else {
					//feign调用复制文件
					dataCenterServiceFeign.copylocalFile(originalFileName,gitFilePath + detail.getFilePath() + detail.getFileName());
				}

				if ("xml".equals(detail.getFileType())) {
					String xmlFilePath = gitFilePath + detail.getFilePath() + detail.getFileName();
					//调用feign根据xml文件的绝对路径解析xml文件为XmlEntityMap对象
					R<XmlEntityMap> r = dataCenterServiceFeign.analysisXmlFileToXMLEntityMap(xmlFilePath);
					XmlEntityMap entityMap = r.getData();
					if (entityMap != null){
						XmlEntityMap getFunctionName = getXmlMapByLableName(entityMap, "函数路径");
						if (getFunctionName.getAttributeMap().containsKey("name")) {
							getFunctionName.getAttributeMap().put("name",
									compPath + getFunctionName.getAttributeMap().get("name").substring(subStr.length()));
						}
						XMlEntityMapVO xMlEntityMapVO = new XMlEntityMapVO();
						xMlEntityMapVO.setLocalPath(xmlFilePath);
						xMlEntityMapVO.setXmlEntityMap(entityMap);
						dataCenterServiceFeign.createXMLFile(xMlEntityMapVO);
					}
				}
				baseMapper.saveCommonCompDetail(detail);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递归查询对应的标签名下属性值对应的value
	 * 
	 * @param xmlEntityMap xml结构
	 * @param lableName    标签名
	 * @param attributeKey 属性名
	 * @return
	 */
	private XmlEntityMap getXmlMapByLableName(XmlEntityMap xmlEntityMap, String lableName) {
		XmlEntityMap xmlMap = null;
		if (xmlEntityMap.getLableName().equals(lableName)) {
			xmlMap = xmlEntityMap;
		} else {
			if (xmlEntityMap.getXmlEntityMaps() != null) {
				for (XmlEntityMap entityMap : xmlEntityMap.getXmlEntityMaps()) {
					xmlMap = getXmlMapByLableName(entityMap, lableName);
					if (xmlMap != null) {
						break;
					}
				}
			}
		}
		return xmlMap;
	}

	@Override
	public List<CommonComponentDetail> getAllCompDetailByCompId(List<CommonComponent> compList) {
		return baseMapper.getAllCompDetailByCompId(compList);
	}

	@Override
	public Map<String, Object> getCommCompView(CommonComponent comp) {
		Map<String, Object> fileMap = Maps.newHashMap();

		List<CommonComponentDetail> vos = baseMapper.selectList(
				Wrappers.<CommonComponentDetail>query().lambda().eq(CommonComponentDetail::getCompId, comp.getId()));
		for (CommonComponentDetail parent : vos) {
			// ①查询构件文件解析
			File file = null;
			String path = parent.getFileName();
			if ("xml".equals(parent.getFileType())) {
				file = new File(gitFilePath + File.separator + parent.getFilePath() + File.separator + path);
				fileMap.put("compBasicMap", XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(file));
			}

		}
		return fileMap;
	}

	/**
	 * 递归生成文件树的vo并保存到List<CompDetailVO>中
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addCommCompDetailTree(List<CommCompDetailVO> tree, String parentId, File file) {
		String fileId = IdGenerate.uuid();
		tree.add(
				new CommCompDetailVO(fileId, file.getName(), "", file.getParentFile().getAbsolutePath(), parentId, ""));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addCommCompDetailTree(tree, fileId, childFile);
			}
		}
	}

	@Override
	public List<CommCompDetailVO> getCommCompViewTree(CommonComponent comp) {
		List<CommCompDetailVO> tree = Lists.newArrayList();
//		将构件转成树
		tree.add(new CommCompDetailVO(comp.getId(), comp.getCompName(), "", "", "-1", comp.getVersion()));
		List<CommonComponentDetail> vos = baseMapper.selectList(
				Wrappers.<CommonComponentDetail>query().lambda().eq(CommonComponentDetail::getCompId, comp.getId()));
		for (CommonComponentDetail parent : vos) {
			System.out.println(parent.getFileName() + "   ");

			if (parent.getFileName().endsWith("jpeg") || parent.getFileName().endsWith("png")
					|| parent.getFileName().endsWith("jpg")) {
				String base64 = "";
				try {
					base64 = UploadFilesUtils.fileToEncodeBase64(
							new File(gitFilePath + parent.getFilePath() + File.separator + parent.getFileName()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				tree.add(new CommCompDetailVO(parent.getId(), parent.getFileName(), parent.getFileType(),
						comp.getCompImg(), parent.getParaentId(), parent.getVersion()));
//				tree.add(new CommCompDetailVO(parent.getId(), parent.getFileName(), parent.getFileType(),
//						"data:image/png;base64," + base64, parent.getParaentId(), parent.getVersion()));
			} else {
				tree.add(new CommCompDetailVO(parent.getId(), parent.getFileName(), parent.getFileType(),
						gitFilePath + parent.getFilePath(), parent.getParaentId(), parent.getVersion()));
			}
			// ②查询构件详情转树形
			String[] fileType = new String[] { "algorithmfile", "testfile", "platformfile" };
			File file = null;
			if (Arrays.asList(fileType).contains(parent.getFileType())) {
				file = new File(gitFilePath + parent.getFilePath() + File.separator + parent.getFileName());
				if (file.isDirectory()) {
					File[] childFileList = file.listFiles();
					for (File childFile : childFileList) {
						addCommCompDetailTree(tree, parent.getId(), childFile);
					}
				}
			}
		}
		return tree;
	}

	/**
	 * 根据libsid查询到已入库的表中是否选中三个库
	 * 
	 * @param libsId
	 * @return
	 */
	@Override
	public List<CommonComponentDetail> findCommonComponentDetailByLibsId(String libsId) {
		return baseMapper.selectList(
				Wrappers.<CommonComponentDetail>query().lambda().eq(CommonComponentDetail::getLibsId, libsId));

	}

	/**
	 * @Author wang
	 * @Description: 根据多个ID查询gjk_CommonComponent_Detail表数据
	 * @Param: [ids]
	 * @Return: java.util.List<com.inforbus.gjk.libs.api.entity.CommonComponentDetail>
	 * @Create: 2020/5/12
	 */
	@Override
	public List<CommonComponentDetail> getCommonComponentDetailByCompIdIn(String ids) {
		return baseMapper.getCommonComponentDetailByCompIdIn(ids);
	}
}
