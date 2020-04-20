package com.inforbus.gjk.pro.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: gjk
 * @description: 系统配置调用数据中心接口
 * @author: SunChao
 * @create: 2020-04-17 10:51
 **/
@RequestMapping("/fileServe")
@FeignClient(ServiceNameConstants.DATACENDER_SERVICE)
public interface SystemSettingsFeign {

    @PostMapping("/analysisXmlFile")
    R<XmlEntityMap> analysisXmlFileToXMLEntityMap(@RequestParam("localPath") String localPath);
}
