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

/**
 * 基础模板
 *
 * @author xiaohe
 * @date 2019-07-16 08:40:33
 */
@Service("baseTemplateService")
public class BaseTemplateServiceImpl extends ServiceImpl<BaseTemplateMapper, BaseTemplate> implements BaseTemplateService {
    private static String LOCALPATH = JGitUtil.getLOCAL_REPO_PATH();
  /**
   * 基础模板简单分页查询
   * @param baseTemplate 基础模板
   * @return
   */
  @Override
  public IPage<BaseTemplate> getBaseTemplatePage(Page<BaseTemplate> page, BaseTemplate baseTemplate){
      return baseMapper.getBaseTemplatePage(page,baseTemplate);
  }

    @Override
    public XmlEntityMap editParseXml(BaseTemplate baseTemplate) {
        String path = LOCALPATH+baseTemplate.getTempPath();
        File localPath = new File(path);
        if(!localPath.exists()) {
            return null;
        }
        return XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(localPath);
    }

    @Override
    public boolean editBaseTemplate(BaseTemplateDTO baseTemplateDTO) {
        BaseTemplate baseTemplate = baseTemplateDTO.getBaseTemplate();
        XmlEntityMap xmlEntityMap = baseTemplateDTO.getXmlEntityMap();
        String path = LOCALPATH+baseTemplate.getTempPath();
        File localPath = new File(path);
        if(!localPath.exists()) {
            return false;
        }
        return XmlFileHandleUtil.createXmlFile(xmlEntityMap,localPath);
    }

    @Override
    public boolean saveBaseTemplate(BaseTemplateDTO baseTemplateDTO) {
        BaseTemplate baseTemplate = baseTemplateDTO.getBaseTemplate();
        XmlEntityMap xmlEntityMap = baseTemplateDTO.getXmlEntityMap();
        long millis = System.currentTimeMillis();
        File localPath = new File(LOCALPATH+"gjk/baseTemplate/"+baseTemplate.getTempName()+String.valueOf(millis)+".xml");
        if(!localPath.exists()){
            try {
                File parentFile = localPath.getParentFile();
                if (!parentFile.exists()){
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
        if (XmlFileHandleUtil.createXmlFile(xmlEntityMap,localPath)){
            baseTemplate.setTempId(IdGenerate.uuid());
            baseTemplate.setCreateTime(LocalDateTime.now());
            baseTemplate.setDelFlag("0");
            baseTemplate.setTempPath("gjk/baseTemplate/"+baseTemplate.getTempName()+String.valueOf(millis)+".xml");
            baseMapper.insert(baseTemplate);
            return true;
        }
        return false;
    }

    @Override
    public XmlEntityMap parseXml(String baseTemplatePath) {
        File localPath = new File(baseTemplatePath);
        return XmlFileHandleUtil.analysisXmlFileToXMLEntityMap(localPath);
    }

}
