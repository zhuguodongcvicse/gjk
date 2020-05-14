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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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
	 * 删除构件对应的文件夹
	 * 
	 * @param id
	 * @return
	 */
	List<CommonComponentDetail> deleteCompById(String id);

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

	/**
	 * 根据list中的每一项元素对comp_id，comp_name，comp_funcname，description进行模糊查询
	 * 
	 * @param selectStringList
	 * @return
	 */
	IPage<CommonComponent> getCompListByString(Page page, List<String> selectStringList);

	/**
	 * 联合查询 根据list中的每一项元素对comp_id，comp_name，comp_funcname，description进行模糊查询
	 * 根据算法和测试的树节点筛选构件
	 * 
	 * @param page
	 * @param libsList
	 * @param selectStringList
	 * @return
	 */
	IPage<CommonComponent> getCompListByStringAndLibsId(Page page, List<String> libsList,
			List<String> selectStringList);

	IPage<CommonComponent> findPageByBatchApprovalId(Page page, String applyId);

	/**
	 * 批量保存
	 * 
	 * @param commonComponents
	 * @return
	 */
	boolean saveCommonCompList(List<CommonComponent> commonComponents);

	/**
	 * 在构件ID集合中查找所有已被删除的构件
	 * 
	 * @param compIdList
	 * @return
	 */
	List<String> getRemoveCompIdList(List<String> compIdList);

	/**
	 * 根据多个ID查询构件数据
	 * @param ids
	 * @return
	 */
	List<CommonComponent> getCommonComponentByIdIn(String ids);
}
