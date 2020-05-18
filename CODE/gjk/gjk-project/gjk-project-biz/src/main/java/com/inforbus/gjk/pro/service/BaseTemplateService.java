package com.inforbus.gjk.pro.service;

import com.inforbus.gjk.admin.api.entity.BaseTemplate;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/*
* 使用Feign调用upms服务
* 使用BaseTemplateController中的方法
* */
@FeignClient(value = ServiceNameConstants.UMPS_SERVICE)
public interface BaseTemplateService {
    @GetMapping(value = "/basetemplate/{tempId}")
    public R<BaseTemplate> getById(@PathVariable("tempId") String tempId);
}
