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
import com.inforbus.gjk.admin.api.vo.DictVO;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.HeaderFileAndStructUtils;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;
import com.inforbus.gjk.libs.api.dto.StructDTO;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.libs.mapper.StructlibsMapper;
import com.inforbus.gjk.libs.service.StructlibsService;

import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 结构体库
 *
 * @author hu
 * @date 2019-06-14 10:51:14
 */
@Service("structlibsService")
public class StructlibsServiceImpl extends ServiceImpl<StructlibsMapper, Structlibs> implements StructlibsService {

    /**
     * 结构体库简单分页查询
     *
     * @param structlibs 结构体库
     * @return
     */
    @Override
    public IPage<Structlibs> getStructlibsPage(Page<Structlibs> page, Structlibs structlibs) {
        IPage<Structlibs> obj = baseMapper.getStructlibsPage(page, structlibs);
        return baseMapper.getStructlibsPage(page, structlibs);
    }

    /**
     * 解析结构体文件
     *
     * @param filePath 结构体文件路径
     * @return
     */
    @Override
    public Map<String, ParamTreeVO> parseStructFile(String filePath) {
//		String url = new String(JGitUtil.getLOCAL_REPO_PATH() + File.separator + filePath);
        String url = new String(filePath);
        if (StringUtils.isNotEmpty(url)) {
            File file = new File(url);
            if (!file.exists()) {
                return null;
            }
        }
        return HeaderFileAndStructUtils.convertStruct(ExternalIOTransUtils.parseStruct(url));
        //return null;
    }

    /**
     * 结构体保存
     *
     * @param paramTreeVO 数据
     */
    @Override
    public int saveOneStruct(ParamTreeVO paramTreeVO) {
        // 获取结构体版本
        Double ver = getLatestVersionByStruct(paramTreeVO.getFparamType());
        paramTreeVO.setVersion(ver);
        saveFirstChildren(paramTreeVO, true);
        return 0;
    }

    /**
     * 将树形数据转化为集合数据方便数据库保存
     *
     * @param paramTreeVO 树形数据
     * @param rootId      树形数据的rootid
     * @param version     树形数据版本
     * @param flag        是修改还是增加标识
     */
    private void saveFirstChildren(ParamTreeVO paramTreeVO, Boolean flag) {
        if (paramTreeVO == null)
            return;

        saveStructToDb(paramTreeVO, flag);
        if (paramTreeVO.getChildren().size() <= 0)
            return;
        for (ParamTreeVO entity : paramTreeVO.getChildren()) {
            entity.setVersion(paramTreeVO.getVersion());
            saveStructToDb(entity, flag);
        }
    }

    /**
     * 用于更新
     * @param paramTreeVO
     * @param flag
     */
    private void updateFirstChildren(ParamTreeVO paramTreeVO, Boolean flag) {
        if (paramTreeVO == null)
            return;

        saveStructToDb(paramTreeVO, flag);
        if (paramTreeVO.getChildren().size() <= 0)
            return;
        List<String> delList=new ArrayList<>();
        for (ParamTreeVO entity : paramTreeVO.getChildren()) {
            delList.add(entity.getDbId());
//            entity.setVersion(paramTreeVO.getVersion());
            Structlibs getStruct = baseMapper.selectById(entity.getDbId());
            if (getStruct!=null){
                entity.setVersion(paramTreeVO.getVersion());
                flag=false;
            }else {
                entity.setVersion(1.0);
                flag=true;
            }
            saveStructToDb(entity, flag);
        }
        //删除
        String ids="'"+StringUtils.join(delList,"','")+"'";
        System.out.println("ids："+ids);
        baseMapper.delStructByIds(paramTreeVO.getDbId(),ids);
    }

    /**
     * 将结构体数据保存到数据库
     *
     * @param paramTreeVO
     * @param rootId
     * @param version
     * @param flag        修改还是新增标识
     */
    private void saveStructToDb(ParamTreeVO paramTreeVO, Boolean flag) {

        // 类型转换
        Structlibs entity = new Structlibs();
        entity.setId(paramTreeVO.getDbId());
        entity.setParentId(paramTreeVO.getParentId());
        entity.setChildrenIds(paramTreeVO.getChildrenIds());
        entity.setName(paramTreeVO.getFparamName());
        entity.setDataType(paramTreeVO.getFparamType());
        entity.setSort(paramTreeVO.getSort());
        entity.setVersion(paramTreeVO.getVersion());
        entity.setType("2");
        entity.setStructType(paramTreeVO.getStructClassify());
        entity.setDataLength(0);
        entity.setPermission("");
        entity.setParamRemarks(paramTreeVO.getParamRemarks());

        // 调用mapper层方法保存到数据库
        if (flag) {
            baseMapper.saveStruct(entity);
        } else {
            baseMapper.updateStruct(entity);
        }

    }

    /**
     * 获取指定结构体类型最新版本
     *
     * @param structType
     * @return 版本
     */
    @Override
    public double getLatestVersionByStruct(String structType) {
        Double ver = 0.0;
        // 调用mapper层方法获取指定结构体类型最新版本
        Structlibs structlibs = baseMapper.getVersionByStruct(structType);
        return structlibs == null ? 1.0 : structlibs.getVersion() + 1.0;
    }

    /**
     * @param
     * @return Structlibs
     * @saveCompImg 获取结构体列表
     * @Description:
     * @Author xiaohe
     * @DateTime 2019年5月28日 下午8:29:14
     * @see com.inforbus.gjk.libs.service.StructlibsService#getStructTypeAll()
     */
    @Override
    public List<StructDTO> getStructTypeAll() {
        List<StructDTO> dto = Lists.newArrayList();
        for (Structlibs list : baseMapper
        		.selectList(Wrappers.<Structlibs>query().lambda().eq(Structlibs::getParentId, "0").eq(Structlibs::getStorageFlag, "2"))) {
            dto.add(new StructDTO(list));
        }
        return dto;
    }

    @Override
    @Deprecated
    public List<Structlibs> getStructTree(Structlibs structlibs) {
        List<Structlibs> lists = baseMapper
                .selectList(Wrappers.<Structlibs>query().lambda().eq(Structlibs::getParentId, structlibs.getId()));
        List<Structlibs> retList = Lists.newArrayList();
        for (Structlibs strs : lists) {
            strs.setRootId(IdGenerate.uuid());
            if (StringUtils.isNotBlank(strs.getChildrenIds())) {
                this.getStructlibs(retList, strs);
            }
            retList.add(strs);
        }
        return retList;
    }

    /**
     * 递归处理结构体树
     *
     * @param retList
     * @param struct
     */
    @Deprecated
    private void getStructlibs(List<Structlibs> retList, Structlibs struct) {
        if (StringUtils.isNotBlank(struct.getChildrenIds())) {
            List<Structlibs> lists = baseMapper.selectList(
                    Wrappers.<Structlibs>query().lambda().eq(Structlibs::getParentId, struct.getChildrenIds()));

            struct.setChildrenIds(IdGenerate.uuid());
            for (Structlibs strs : lists) {
                strs.setRootId(IdGenerate.uuid());
                strs.setParentId(struct.getChildrenIds());
                if (StringUtils.isNotBlank(strs.getChildrenIds())) {
                    System.out.println("strs: " + strs.getId() + "   " + strs.getParentId());
                    this.getStructlibs(retList, strs);
                }
                retList.add(strs);
            }
        }
    }

    /**
     * @param structlibs
     * @return 满足条件的树形数据
     * @Title: getStructTreeDto
     * @Description:根据根结构体查询树形结构体
     * @Author xiaohe
     * @DateTime 2019年7月6日 上午8:15:42
     * @see com.inforbus.gjk.libs.service.StructlibsService#getStructTreeDto(Structlibs)
     */
    @Override
    public List<StructDTO> getStructTreeDto(StructDTO structlibs) {
        List<Structlibs> lists = baseMapper
                .selectList(Wrappers.<Structlibs>query().lambda().eq(Structlibs::getParentId, structlibs.getDbId())
                        .like(Structlibs::getName, structlibs.getQueryParam()).orderByAsc(Structlibs::getSort));
        List<StructDTO> retList = Lists.newArrayList();
        for (Structlibs strs : lists) {
            strs.setRootId(IdGenerate.uuid());
            // 将查询数据转换成树上所需数据
            StructDTO dto = new StructDTO(strs);
            dto.setAssigParamName(strs.getName());// 设置根结构体的下属性的名字.like(Structlibs::getDataType, "",SqlLike.DEFAULT)
            if (StringUtils.isNotBlank(strs.getChildrenIds())) {
                this.getStructlibsDto(retList, dto, structlibs.getQueryParam());
            }
            retList.add(dto);
        }
        return retList;

    }

    /**
     * @param retList 返回
     * @param struct  传入
     * @Title: getStructlibsDto
     * @Description: 递归处理树形数据并处理赋值参数
     * @Author xiaohe
     * @DateTime 2019年7月6日 上午8:07:54
     */
    private void getStructlibsDto(List<StructDTO> retList, StructDTO struct, String... param) {
        if (StringUtils.isNotBlank(struct.getChildrenIds())) {
            // 根据结构体主键查询数据
            List<Structlibs> lists = baseMapper.selectList(
                    Wrappers.<Structlibs>query().lambda().eq(Structlibs::getParentId, struct.getChildrenIds())
                            .like(Structlibs::getName, param[0]).orderByAsc(Structlibs::getSort));
            // 将ChildrenIds处理，用于重新赋值关系 ①
            struct.setChildrenIds(IdGenerate.uuid());
            for (Structlibs strs : lists) {
                strs.setRootId(IdGenerate.uuid());// 将ChildrenIds处理，用于重新赋值关系 ②
                strs.setParentId(struct.getChildrenIds());// 将ChildrenIds处理，用于重新赋值关系 ③
                StructDTO dto = new StructDTO(strs);
                String separator = struct.getFparamType().indexOf("*") == -1 ? "." : "->";
                // 设置结构体赋值名称
                dto.setAssigParamName(struct.getAssigParamName() + separator + dto.getFparamName());
                if (StringUtils.isNotBlank(strs.getChildrenIds())) {
                    System.out.println("strs: " + strs.getId() + "   " + strs.getParentId());
                    this.getStructlibsDto(retList, dto, param);
                }
                retList.add(dto);
            }
        }
    }

    /**
     * @param structMaps
     * @Title: saveStructMap
     * @Description: 循环保存导入的结构体maps
     * @Author xiaohe
     * @DateTime 2019年7月12日 下午4:48:46
     * @see com.inforbus.gjk.libs.service.StructlibsService#saveStructMap(java.util.Map)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStructMap(Map<String, Object> structMaps) {
        for (Map.Entry<String, Object> entry : structMaps.entrySet()) {
            this.saveOneStruct(JSONUtil.parseObj(entry.getValue()).toBean(ParamTreeVO.class));
        }
    }

    @Override
    public List<DictVO> getStructIncludePointer() {

        List<DictVO> dto = Lists.newArrayList();
        for (Structlibs list : baseMapper
                .selectList(Wrappers.<Structlibs>query().lambda().eq(Structlibs::getParentId, "0"))) {
            DictVO vo = new DictVO();
            vo.setValue(list.getId());
            vo.setLabel(list.getDataType());
            dto.add(vo);
            DictVO vo1 = new DictVO();
            vo1.setValue(list.getId() + "_*");
            vo1.setLabel(list.getDataType() + "*");
            dto.add(vo1);
        }
        return dto;
    }

    @Override
    public R deleteStructLibById(String id) {
        Structlibs structLib = baseMapper.selectById(id);
        //未入库允许删除
        if ("0".equals(structLib.getDelFlag())) {
            baseMapper.deleteStructByParentId(id);
            baseMapper.deleteStructById(id);
            return new R<>(true);
        }
        return new R<>(false);
    }

    @Override
    public R editStruct(Structlibs structlibs) {
        return new R<>(baseMapper.getStructByParentId(structlibs.getId()));
    }

    @Override
    public R rKuStruct(Structlibs structlibs) {
        baseMapper.rKuStructById(structlibs.getId());
        baseMapper.rKuStructByParentId(structlibs.getId());
        return new R<>(true);
    }

    @Override
    public int updateOneStruct(ParamTreeVO paramTreeVO) {
        // 获取结构体版本
        System.out.println("获取入库标志：" + paramTreeVO.getStorageFlag());
        if ("1".equals(paramTreeVO.getStorageFlag())) {
            saveOneStruct(paramTreeVO);
        } else {
//            Double ver = getLatestVersionByStruct(paramTreeVO.getFparamType());
//            paramTreeVO.setVersion(ver);
//            Structlibs getStruct = baseMapper.selectById(paramTreeVO.getDbId());
//            if (getStruct!=null){
//                System.out.println("进入编辑");
                updateFirstChildren(paramTreeVO, false);
//            }else {
//                System.out.println("进入保存");
//                saveOneStruct(paramTreeVO);
//            }

        }
        return 0;
    }

    @Override
    public R findAllStructs() {
        return new R<>(baseMapper.findAllStructs());
    }

}
