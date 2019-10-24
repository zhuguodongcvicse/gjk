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
import com.inforbus.gjk.admin.mapper.BaseTemplateMapper;
import com.inforbus.gjk.admin.service.BaseTemplateService;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.XmlFileHandleUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 基础模板
 *
 * @author wang
 * @date 2019-07-16 08:40:33
 */
@Service("baseTemplateService")
public class BaseTemplateServiceImpl extends ServiceImpl<BaseTemplateMapper, BaseTemplate> implements BaseTemplateService {
    private static String LOCALPATH = JGitUtil.getLOCAL_REPO_PATH();

    /**
     * 基础模板简单分页查询
     *
     * @param baseTemplate 基础模板
     * @return
     */
    @Override
    public IPage<BaseTemplate> getBaseTemplatePage(Page<BaseTemplate> page, BaseTemplate baseTemplate) {
        return baseMapper.getBaseTemplatePage(page, baseTemplate);
    }

    /**
     * 基础模板编辑功能解析xml文件
     * 从数据库读取一条数据,根据路径解析xml文件,返回XmlEntityMap对象至页面
     *
     * @param baseTemplate 基础模板对象
     * @return XmlEntityMap
     */
    @Override
    public XmlEntityMap editParseXml(BaseTemplate baseTemplate) {
        String path = LOCALPATH + baseTemplate.getTempPath();//获取将要解析的xml文件的路径
        File localPath = new File(path);
        if (!localPath.exists()) {//如果xml文件不存在,返回null
            return null;
        }
        return XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(localPath);//通过工具类按照路径解析xml文件,返回XmlEntityMap对象
    }

    /**
     * 基础模板编辑功能保存数据
     * 编辑页面保存数据,保存xml文件至指定位置,
     *
     * @param baseTemplateDTO 基础模板包装对象
     * @return boolean
     */
    @Override
    public boolean editBaseTemplate(BaseTemplateDTO baseTemplateDTO) {
        BaseTemplate baseTemplate = baseTemplateDTO.getBaseTemplate();
        XmlEntityMap xmlEntityMap = baseTemplateDTO.getXmlEntityMap();
        String path = LOCALPATH + baseTemplate.getTempPath();//xml文件被保存的位置
        File localPath = new File(path);
        if (!localPath.exists()) {//判断文件是否存在
            return false;
        }
        return XmlFileHandleUtil.createXmlFile(xmlEntityMap, localPath);//保存成功返回true
    }

    /**
     * 基础模板新增功能保存数据
     * 新增页面保存数据,保存xml文件至指定位置,
     *
     * @param baseTemplateDTO 基础模板包装对象
     * @return boolean
     */
    @Override
    public boolean saveBaseTemplate(BaseTemplateDTO baseTemplateDTO) {
        BaseTemplate baseTemplate = baseTemplateDTO.getBaseTemplate();
        XmlEntityMap xmlEntityMap = baseTemplateDTO.getXmlEntityMap();
        long millis = System.currentTimeMillis();//获取日期毫秒值
        File localPath = new File(LOCALPATH + "gjk"+ File.separator+"baseTemplate"+File.separator + baseTemplate.getTempName() + String.valueOf(millis) + ".xml");//保存的位置,文件名称由模板名称+时间毫秒值组成
        if (!localPath.exists()) {
            try {
                File parentFile = localPath.getParentFile();//文件如果不存在,创建文件
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                    localPath.createNewFile();
                }
                System.out.println("文件已生成");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (XmlFileHandleUtil.createXmlFile(xmlEntityMap, localPath)) {//先生成xml模板文件至指定位置
            baseTemplate.setTempId(IdGenerate.uuid());
            baseTemplate.setCreateTime(LocalDateTime.now());
            baseTemplate.setDelFlag("0");
            baseTemplate.setTempPath("gjk"+File.separator+"baseTemplate" + File.separator+ baseTemplate.getTempName() + String.valueOf(millis) + ".xml");
            baseMapper.insert(baseTemplate);//xml文件生成成功后,数据库存储一条数据
            return true;
        }
        return false;
    }

    /**
     * 基础模板新增功能根据路径解析xml文件
     * 新增时选择指定位置的xml文件
     *
     * @param baseTemplatePath 文件路径对象
     * @return XmlEntityMap
     */
    @Override
    public XmlEntityMap parseXml(String baseTemplatePath) {
        File localPath = new File(baseTemplatePath);//根据文件路径创建file对象
        return XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(localPath);//根据指定路径解析xml文件
    }

    /**
     * 基础模板修改功能
     *
     * @param baseTemplate 将要读取的xml文件的路径
     * @return boolean
     */
    @Override
    public boolean update(BaseTemplate baseTemplate) {
        BaseTemplate template = new BaseTemplate();
        template.setTempName(baseTemplate.getTempName());
        BaseTemplate one = baseMapper.selectOne(new QueryWrapper<>(template));//查找数据库中,模板名称是否存在
        if(one != null){
            baseMapper.deleteById(one.getTempId());//删除模板名称重复的数据
        }
        File oldPath = new File(LOCALPATH + baseTemplate.getTempPath());//保存的位置,文件名称由模板名称+时间毫秒值组成
        if (oldPath.exists()) {
            long millis = System.currentTimeMillis();
            String path = "gjk"+ File.separator+"baseTemplate"+File.separator + baseTemplate.getTempName() + String.valueOf(millis) + ".xml";
            File newPath = new File(LOCALPATH + path);//保存的位置,文件名称由模板名称+时间毫秒值组成
            FileInputStream in = null;//输入流对象
            FileOutputStream out = null;//输出流对象
            try {
                in = new FileInputStream(oldPath);
                out = new FileOutputStream(newPath);
                int len = 0;
                byte[] bytes = new byte[1024];//接收数据的数组
                while ((len = in.read(bytes)) != -1) {//循环解读写数据,复制文件
                    out.write(bytes, 0, len);
                }
                baseTemplate.setTempPath(path);//更改新的路径
                baseTemplate.setUpdateTime(LocalDateTime.now());
                baseMapper.insert(baseTemplate);//把新的数据更细至数据库
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();//关闭输入流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(out!=null){
                    try {
                        out.close();//关闭输出流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        oldPath.delete();
        return false;
    }

    @Override
    public List<BaseTemplate> getBaseTemplate() {
        return baseMapper.selectList(null);
    }

}
