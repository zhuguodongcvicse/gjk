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
import com.inforbus.gjk.common.core.entity.Structlibs;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 结构体库
 *
 * @author hu
 * @date 2019-06-14 10:51:14
 */
public interface StructlibsMapper extends BaseMapper<Structlibs> {
  /**
    * 结构体库简单分页查询
    * @param structlibs 结构体库
    * @return
    */
  IPage<Structlibs> getStructlibsPage(Page page, @Param("structlibs") Structlibs structlibs);

  /**
	 * @Title: saveStruct
	 * @Description:
	 * @Author hu
	 * @DateTime 2019年6月24日 下午2:07:01
	 * @param component
	 * @return
	 */
	void saveStruct(@Param("structlibs") Structlibs structlibs);

	/**
	 * @Title: getVersionByStruct
	 * @Description: 根据结构体类型查找版本号
	 * @Author hu
	 * @DateTime 2019年6月24日 上午11:26:54
	 * @param structlibs
	 * @return
	 */
	Structlibs getVersionByStruct(@Param("dataType") String dataType);
	
	/**
	 * @Title: getLatestStructByType
	 * @Description: 根据结构体类型获取整个结构体
	 * @Author hu
	 * @DateTime 2019年6月24日 上午11:26:54
	 * @param dataType 类型
	 * @return
	 */
	List<Structlibs> getLatestStructByType(@Param("dataType") String dataType);

	/**
	 * 根据父ID删除结构体
	 * @param id
	 */
	void deleteStructByParentId(@Param("id") String id);

    /**
     * 根据ID删除结构体
     * @param id
     */
    void deleteStructById(@Param("id") String id);

	/**
	 * 根据父ID查询结构体列表
	 * @param id
	 * @return
	 */
	List<Structlibs> getStructByParentId(@Param("id") String id);

    /**
     * 入库
     * @param id
     * @return
     */
    void rKuStructById(@Param("id") String id);

	/**
	 * 入库子节点
	 * @param id
	 */
	void rKuStructByParentId(@Param("id") String id);

    /**
     * 修改结构体数据
     * @param structlibs
     */
    void updateStruct(@Param("structlibs") Structlibs structlibs);

	/**
	 * 根据多个id值删除（用于更新删除）
	 * @param ids
	 */
	void delStructByIds(@Param("id") String id,@Param("ids") String ids);

	/**
	 * 得到所有结构体数据
	 * @return
	 */
	List<Structlibs> findAllStructs();

	/**
	 * 得到结构体数据，根据id集合
	 * @param idList
	 * @return
	 */
	List<Structlibs> getStructlibsByIdList(@Param("idList") List<String> idList);

	/**
	 * 得到子结构体数据，根据id集合
	 * @param parentIdList
	 * @return
	 */
	List<Structlibs> getStructlibsByParentIdList(@Param("parentIdList") List<String> parentIdList);
}
