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
package com.inforbus.gjk.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.dto.BaseTemplateDTO;
import com.inforbus.gjk.admin.api.entity.BaseTemplate;
import com.inforbus.gjk.admin.api.feign.RemoteBaseTemplateService;
import com.inforbus.gjk.admin.mapper.BaseTemplateMapper;
import com.inforbus.gjk.admin.service.BaseTemplateService;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BaseTemplateServiceImpl
 *
 * @author wang
 * @date 2020/4/15
 * @Description 基础模板
 */
@Service("baseTemplateService")
public class BaseTemplateServiceImpl extends ServiceImpl<BaseTemplateMapper, BaseTemplate> implements BaseTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(BaseTemplateServiceImpl.class);

    @Value("${git.local.path}")
    private String LOCALPATH;

    @Autowired
    private RemoteBaseTemplateService remoteBaseTemplateService;

    /**
     * @Author wang
     * @Description: 基础模板简单分页查询
     * @Param: [page, baseTemplate]
     * @Return: com.baomidou.mybatisplus.core.metadata.IPage<com.inforbus.gjk.admin.api.entity.BaseTemplate>
     * @Create: 2020/4/14
     */
    @Override
    public IPage<BaseTemplate> getBaseTemplatePage(Page<BaseTemplate> page, BaseTemplate baseTemplate) {
        return baseMapper.getBaseTemplatePage(page, baseTemplate);
    }

    /**
     * @Author wang
     * @Description: 基础模板编辑功能解析xml文件, 从数据库读取一条数据, 根据路径解析xml文件, 返回XmlEntityMap对象至页面
     * @Param: [baseTemplate]
     * @Return: com.inforbus.gjk.common.core.util.R<com.inforbus.gjk.common.core.entity.XmlEntityMap>
     * @Create: 2020/4/14
     */
    @Override
    public R<XmlEntityMap> editParseXml(BaseTemplate baseTemplate) {
        String path = LOCALPATH + baseTemplate.getTempPath();
        R r = remoteBaseTemplateService.analysisXmlFileToXMLEntityMap(path);
        return r;
    }

    /**
     * @Author wang
     * @Description: 基础模板编辑功能保存数据, 编辑页面保存数据, 保存xml文件至指定位置
     * @Param: [baseTemplateDTO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
    @Override
    public boolean editBaseTemplate(BaseTemplateDTO baseTemplateDTO) {
        logger.debug("editBaseTemplate方法开始运行");
        BaseTemplate baseTemplate = baseTemplateDTO.getBaseTemplate();
        XmlEntityMap xmlEntityMap = baseTemplateDTO.getXmlEntityMap();
        try {
            //创建xmlEntityMapVO对象
            XMlEntityMapVO xMlEntityMapVO = new XMlEntityMapVO();
            xMlEntityMapVO.setLocalPath(LOCALPATH + baseTemplate.getTempPath());
            xMlEntityMapVO.setXmlEntityMap(xmlEntityMap);
            //远程调用dataCenter中的生成xml文件的方法
            R r = remoteBaseTemplateService.createXMLFile(xMlEntityMapVO);
            //强转数据为boolean类型，
            if ((Boolean) r.getData()) {
                //更新数据库更新时间
                baseTemplate.setUpdateTime(LocalDateTime.now());
                baseMapper.updateById(baseTemplate);
                return true;
            }
        } catch (Exception e) {
            logger.error("数据保存失败,请联系管理员");
        }
        logger.debug("editBaseTemplate方法运行结束");
        //保存成功返回true
        return false;
    }

    /**
     * @Author wang
     * @Description: 基础模板新增功能保存数据, 新增页面保存数据, 保存xml文件至指定位置
     * @Param: [baseTemplateDTO]
     * @Return: boolean
     * @Create: 2020/4/14
     */
    @Override
    public boolean saveBaseTemplate(BaseTemplateDTO baseTemplateDTO) throws Exception {
        logger.debug("saveBaseTemplate方法开始运行");
        BaseTemplate baseTemplate = baseTemplateDTO.getBaseTemplate();
        XmlEntityMap xmlEntityMap = baseTemplateDTO.getXmlEntityMap();
        //获取日期毫秒值
        long millis = System.currentTimeMillis();
        //保存的绝对路径,文件名称由模板名称+时间毫秒值组成
        String dataBasePath = "gjk" + File.separator + "baseTemplate" + File.separator + baseTemplate.getTempName() + String.valueOf(millis) + ".xml";
        try {
            XMlEntityMapVO xMlEntityMapVO = new XMlEntityMapVO();
            xMlEntityMapVO.setXmlEntityMap(xmlEntityMap);
            xMlEntityMapVO.setLocalPath(LOCALPATH + dataBasePath);
            //先生成xml模板文件至指定位置
            R r = remoteBaseTemplateService.createXMLFile(xMlEntityMapVO);
            if ((Boolean) r.getData()) {
                //设置id
                baseTemplate.setTempId(IdGenerate.uuid());
                //设置创建时间
                baseTemplate.setCreateTime(LocalDateTime.now());
                baseTemplate.setUpdateTime(null);
                baseTemplate.setDelFlag("0");
                baseTemplate.setTempPath(dataBasePath);
                //设置初始版本号
                Integer version = baseMapper.getMaxVersion(baseTemplate.getTempType());
                if (version == null) {
                    baseTemplate.setTempVersion(1);
                } else {
                    baseTemplate.setTempVersion(version + 1);
                }
                //xml文件生成成功后,数据库存储一条数据
                baseMapper.insert(baseTemplate);
                return true;
            }
        } catch (Exception e) {
            logger.error("模板保存失败,请检查相关配置是否正确");
            throw new Exception();
        }
        logger.debug("saveBaseTemplate方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: 基础模板新增功能根据路径解析xml文件, 新增时选择指定位置的xml文件
     * @Param: [baseTemplatePath]
     * @Return: com.inforbus.gjk.common.core.entity.XmlEntityMap
     * @Create: 2020/4/14
     */
    @Override
    public XmlEntityMap parseXml(String baseTemplatePath) {
        //根据指定路径解析xml文件
        R<XmlEntityMap> r = remoteBaseTemplateService.analysisXmlFileToXMLEntityMap(baseTemplatePath);
        return r.getData();
    }

    /**
     * @Author wang
     * @Description: 基础模板修改功能
     * @Param: [baseTemplate]
     * @Return: boolean
     * @Create: 2020/4/14
     */
    @Override
    public boolean update(BaseTemplate baseTemplate) throws Exception {
        logger.debug("update方法开始运行");
        try {
            //保存的位置,文件名称由模板名称+时间毫秒值组成
            File oldPath = new File(LOCALPATH + baseTemplate.getTempPath());
            if (oldPath.exists()) {
                long millis = System.currentTimeMillis();
                //生成新文件的名称
                String path = "gjk" + File.separator + "baseTemplate" + File.separator + baseTemplate.getTempName() + String.valueOf(millis) + ".xml";
                //调用fegin接口解析dataCenter数据中心中的原有的模板
                R<XmlEntityMap> r = remoteBaseTemplateService.analysisXmlFileToXMLEntityMap(LOCALPATH + baseTemplate.getTempPath());
                XmlEntityMap xmlData = r.getData();
                XMlEntityMapVO xMlEntityMapVO = new XMlEntityMapVO();
                xMlEntityMapVO.setXmlEntityMap(xmlData);
                xMlEntityMapVO.setLocalPath(LOCALPATH + path);
                //调用fegin接口在dataCenter数据中心生成新的模板
                R result = remoteBaseTemplateService.createXMLFile(xMlEntityMapVO);
                boolean flag = (boolean) result.getData();
                //根据模板id查询模板数据
                BaseTemplate oldBT = baseMapper.selectById(baseTemplate.getTempId());
                if (flag && oldBT != null) {
                    if (!oldBT.getTempType().equals(baseTemplate.getTempType())) {
                        //获取最新版本
                        Integer version = baseMapper.getMaxVersion(baseTemplate.getTempType());
                        if (version == null) {
                            baseTemplate.setTempVersion(1);
                        } else {
                            baseTemplate.setTempVersion(version + 1);
                        }
                    }
                }
                //更改新的路径
                baseTemplate.setTempPath(path);
                baseTemplate.setUpdateTime(LocalDateTime.now());
                //把新的数据更细至数据库
                baseMapper.updateById(baseTemplate);
                remoteBaseTemplateService.delFile(oldPath.getAbsolutePath());
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        logger.debug("update方法运行结束");
        return false;
    }

    /**
     * @Author wang
     * @Description: 查询模板数据
     * @Param: []
     * @Return: java.util.List<com.inforbus.gjk.admin.api.entity.BaseTemplate>
     * @Create: 2020/4/15
     */
    @Override
    public List<BaseTemplate> getBaseTemplate() {
        return baseMapper.selectList(null);
    }

    /**
     * @Author wang
     * @Description: 根据模板类型获取基础模板数据
     * @Param: [tempType]
     * @Return: java.util.List<com.inforbus.gjk.admin.api.entity.BaseTemplate>
     * @Create: 2020/4/15
     */
    @Override
    public List<BaseTemplate> getBaseTemplateByTempType(String tempType) {
        BaseTemplate baseTemplate = new BaseTemplate();
        baseTemplate.setTempType(tempType);
        return baseMapper.selectList(new QueryWrapper<>(baseTemplate).orderByDesc("temp_version"));
    }

    /**
     * @Author wang
     * @Description: 基础模板获取本地地址
     * @Param: []
     * @Return: java.lang.String
     * @Create: 2020/4/15
     */
    @Override
    public String getLocalPath() {
        return LOCALPATH;
    }

    /**
     * @Author wang
     * @Description: 根据模板id删除模板
     * @Param: [tempId]
     * @Return: boolean
     * @Create: 2020/4/15
     */
    @Override
    public boolean removeById(String tempId) {
        BaseTemplate baseTemplate = baseMapper.selectById(tempId);
        int b = baseMapper.deleteById(tempId);
        if (b != -1) {
            //调用fegin接口，根据文件的绝对路径删除文件
            R<Boolean> r = remoteBaseTemplateService.delFile(LOCALPATH + baseTemplate.getTempPath());
            return r.getData();
        }
        return false;
    }
}
