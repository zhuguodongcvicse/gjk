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
package com.inforbus.gjk.comp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.comp.api.entity.ComponentDetail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
public interface ComponentDetailMapper extends BaseMapper<ComponentDetail> {
	/**
	 * 构件明细简单分页查询
	 * 
	 * @param componentDetail 构件明细
	 * @return
	 */
	IPage<ComponentDetail> getComponentDetailPage(Page page, @Param("componentDetail") ComponentDetail componentDetail);

	/**
	 * @Title: listCompDetailByCompId
	 * @Description: 通过构件编号查询文件
	 * @Author cvics
	 * @DateTime 2019年4月29日 上午9:45:30
	 * @param compId
	 * @return
	 */
	List<ComponentDetail> listCompDetailByCompId(@Param("compId") String compId);

	/**
	 * @Title: getCompXMl
	 * @Description:  获取对应构件的XML
	 * @Author xiaohe
	 * @DateTime 2019年5月5日 下午4:30:00
	 * @param compId 构件编号
	 * @return 
	 */
	ComponentDetail getCompXMl(@Param("compId") String compId);
	/**
	 * @Title: getCompXMl
	 * @Description:  获取对应构件的XML
	 * @Author xiaohe
	 * @DateTime 2019年5月5日 下午4:30:00
	 * @param compId 构件编号
	 * @return 
	 */
	ComponentDetail getCommCompXMl(@Param("compId") String compId);
	
	/**
	 * @Title: saveCompDetail
	 * @Description: 保存构件详细信息
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午2:15:54
	 * @param detail
	 * @return
	 */
	void saveCompDetail(@Param("compDetail") ComponentDetail detail);

	/**
	 * @Title: fileParentCompDetail
	 * @Description: 查询构件详细信息父级记录是否存在
	 * @Author cvics
	 * @DateTime 2019年5月6日 下午4:22:14
	 * @param detail
	 * @return
	 */
	ComponentDetail fileParentCompDetail(@Param("compDetail") ComponentDetail detail);

	/**
	 * @Title: getCompFiles
	 * @Description: 
	 * @Author xiaohe
	 * @DateTime 2019年5月23日 下午1:37:31
	 * @param compId 构件ID
	 * @return 
	 */
	List<ComponentDetail> getCompFiles(@Param("compId")String compId);

	/**
	 * @Title: deleteByCompId
	 * @Description:  根据构件编号删除所有构件文件
	 * @Author xiaohe
	 * @DateTime 2019年5月24日 下午2:04:44
	 * @param id 构件编号
	 */
	void deleteByCompId(@Param("compId") String compId);

	/**
	 * @param compId
	 * @param fileType
	 * @return
	 */
	ComponentDetail findCompXml(@Param("compId")String compId, @Param("fileType")String fileType);

	/**
	 * @Title: editCompDetail
	 * @Description:  修改构架XML
	 * @Author xiaohe
	 * @DateTime 2019年5月24日 下午2:04:44
	 * @param detail 构件
	 */
	void editCompDetail(@Param("compDetail")ComponentDetail detail);
	
	/**
	 * 根据树节点的构件库详细信息的id查找文件路径
	 * @param id
	 * @return
	 */
	ComponentDetail getFilePathById(@Param("id")String id);

}
