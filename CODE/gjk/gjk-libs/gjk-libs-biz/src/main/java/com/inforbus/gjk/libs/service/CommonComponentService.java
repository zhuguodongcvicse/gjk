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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.libs.api.dto.CompTree;
import com.inforbus.gjk.libs.api.entity.CommonComponent;
import com.inforbus.gjk.libs.api.entity.CommonComponentDetail;
import com.inforbus.gjk.libs.api.vo.CompDictVO;

/**
 * 公共构件库详细表
 *
 * @author pig code generator
 * @date 2019-06-10 14:25:55
 */
public interface CommonComponentService extends IService<CommonComponent> {

	/**
	 * 公共构件库详细表简单分页查询
	 * 
	 * @param commonComponent 公共构件库详细表
	 * @return
	 */
	IPage<CommonComponent> getCommonComponentPage(Page<CommonComponent> page, CommonComponent commonComponent);

	/**
	 * @Title: saveCommonComp
	 * @Description: 保存公共构件
	 * @Author cvics
	 * @DateTime 2019年6月11日 下午2:27:22
	 * @param commonComponent
	 */
	boolean saveCommonComp(CommonComponent commonComponent);

	/**
	 * 根据compId获取所有版本记录
	 * 
	 * @param compId
	 * @return
	 */
	List<CommonComponent> getAllVersionByCompId(CommonComponent commonComponent);

	/**
	 * 根据算法和测试的树节点筛选构件
	 * 
	 * @param libsList
	 * @return
	 */
	IPage<CommonComponent> getScreenComp(Page page, List<String> libsList);

	/**
	 * 根据算法和测试的树节点筛选构件
	 * 
	 * @param libsList
	 * @return
	 */
	List<CompTree> getScreenComp(List<String> libsList);

	/**
	 * 根据构件集合生成压缩文件流
	 * 
	 * @param compList
	 * @return
	 */
	byte[] createZip(List<CommonComponent> compList, List<CommonComponentDetail> details) throws Exception;

	/**
	 * 返回构件树形选择器的树的节点集合
	 * 
	 * @return
	 */
	List<CompTree> getCompSelectTree();

	/**
	 * 返回构件ID-CompName对应字典
	 * 
	 * @return
	 */
	List<CompDictVO> getCompDictList(List<String> compIdList);

}
