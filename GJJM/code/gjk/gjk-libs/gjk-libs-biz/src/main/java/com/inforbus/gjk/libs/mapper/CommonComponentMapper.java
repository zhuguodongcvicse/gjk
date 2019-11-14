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
package com.inforbus.gjk.libs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inforbus.gjk.libs.api.entity.CommonComponent;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 公共构件库详细表
 *
 * @author pig code generator
 * @date 2019-06-10 14:25:55
 */
public interface CommonComponentMapper extends BaseMapper<CommonComponent> {
	/**
	 * 公共构件库详细表简单分页查询
	 * 
	 * @param commonComponent 公共构件库详细表
	 * @return
	 */
	IPage<CommonComponent> getCommonComponentPage(Page page, @Param("commonComponent") CommonComponent commonComponent);

	/**
	 * @Title: saveCommonComp
	 * @Description: 保存公共构件
	 * @Author cvics
	 * @DateTime 2019年6月11日 下午2:27:22
	 * @param commonComponent
	 */
	void saveCommonComp(@Param("comp") CommonComponent commonComponent);

	/**
	 * @Title: getVersionByCompId
	 * @Description: 返回此构件的所有版本
	 * @Author cvics
	 * @DateTime 2019年6月11日 下午2:59:10
	 * @param commonComponent
	 * @return
	 */
	List<CommonComponent> getVersionByCompId(@Param("comp") CommonComponent commonComponent);

	/**
	 * 根据算法和测试的树节点筛选构件
	 * 
	 * @param libsList
	 * @return
	 */
	IPage<CommonComponent> getScreenComp(Page page, @Param("libsList") List<String> libsList);

	/**
	 * 根据算法和测试的树节点筛选构件
	 * 
	 * @param libsList
	 * @return
	 */
	List<CommonComponent> getScreenComp(@Param("libsList") List<String> libsList);

	/**
	 * 查询表信息
	 *
	 * @param tableName 表名称
	 * @return
	 */
	Map<String, String> queryTable(String tableName);

	/**
	 * 查询表列信息
	 *
	 * @param tableName 表名称
	 * @return
	 */
	List<Map<String, String>> queryColumns(String tableName);

	/**
	 * 根据list中的每一项元素对comp_id，comp_name，comp_funcname，description进行模糊查询
	 * 
	 * @param selectStringList
	 * @return
	 */
	IPage<CommonComponent> getCompListByString(Page page, @Param("selectStringList") List<String> selectStringList);

	/**
	 * 联合查询 根据list中的每一项元素对comp_id，comp_name，comp_funcname，description进行模糊查询
	 * 根据算法和测试的树节点筛选构件
	 * 
	 * @param page
	 * @param libsList
	 * @param selectStringList
	 * @return
	 */
	IPage<CommonComponent> getCompListByStringAndLibsId(Page page, @Param("libsList") List<String> libsList,
			@Param("selectStringList") List<String> selectStringList);
}
