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
package com.inforbus.gjk.libs.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.admin.api.vo.DictVO;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;
import com.inforbus.gjk.libs.api.dto.StructDTO;
import com.inforbus.gjk.libs.api.entity.Structlibs;

/**
 * 结构体库
 *
 * @author hu
 * @date 2019-06-14 10:51:14
 */
public interface StructlibsService extends IService<Structlibs> {

	/**
	 * 结构体文件解析
	 * 
	 * @Title: parseStructFile
	 * @Description: 结构体文件解析
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月7日 下午7:07:42
	 * @param filePath 结构体文件路径
	 * @return 解析结果
	 */
	public Map<String, ParamTreeVO> parseStructFile(String filePath);
	
	/**
	 * 新增一个结构体树
	 * @param paramTreeVO 结构体树
	 * @return
	 */
	public int saveOneStruct(@RequestBody ParamTreeVO paramTreeVO);
	
	/**
	 * 获取指定结构体类型最新版本
	 * @param structType
	 * @return 版本
	 */
	public double getLatestVersionByStruct(String structType);

	/**
	 * 结构体库简单分页查询
	 * 
	 * @param structlibs 结构体库
	 * @return
	 */
	IPage<Structlibs> getStructlibsPage(Page<Structlibs> page, Structlibs structlibs);

	/**
	 * @param  
	 * @Title: saveCompImg
	 * @Description: 获取所有结构体
	 * @Author xiaohe
	 * @DateTime 2019年6月28日 下午8:19:16
	 * @param Structlibs
	 * @return
	 */
	public List<StructDTO> getStructTypeAll();

	/**
	 * @param  
	 * @Title: getStructTree
	 * @Description: 获取所有结构体
	 * @Author xiaohe
	 * @DateTime 2019年6月28日 下午8:19:16
	 * @param Structlibs
	 * @return
	 */
	@Deprecated
	public List<Structlibs>  getStructTree( Structlibs structlibs);
	/**
	 * @param  
	 * @Title: getStructTree
	 * @Description: 获取所有结构体
	 * @Author xiaohe
	 * @DateTime 2019年6月28日 下午8:19:16
	 * @param Structlibs
	 * @return
	 */
	public List<StructDTO>  getStructTreeDto( StructDTO structlibs);

	/**
	 * @Title: saveStructMap
	 * @Description: 保存导入的结构体Maps
	 * @Author xiaohe
	 * @DateTime 2019年7月12日 下午4:42:18
	 * @param structMaps 
	 */
	public void saveStructMap(Map<String, Object> structMaps);

	/**
	 * @Title: getStructIncludePointer
	 * @Description:  获取所有结构体包含指针
	 * @Author xiaohe
	 * @DateTime 2019年8月23日 下午1:50:27
	 * @return 
	 */
	public List<DictVO> getStructIncludePointer();

	/**
	 * 根据ID删除结构体数据
	 * @param id
	 * @return
	 */
	public  R deleteStructLibById(String id);

	/**
	 * 修改结构体库
	 * @param structlibs
	 * @return
	 */
	public  R editStruct(Structlibs structlibs);

	/**
	 * 入库
	 * @param structlibs
	 * @return
	 */
	public  R rKuStruct(Structlibs structlibs);
	/**
	 * 修改一个结构体树
	 * @param paramTreeVO 结构体树
	 * @return
	 */
	public int updateOneStruct(@RequestBody ParamTreeVO paramTreeVO);

	/**
	 * 得到库中所有结构体
	 * @return
	 */
	public R findAllStructs();
}
