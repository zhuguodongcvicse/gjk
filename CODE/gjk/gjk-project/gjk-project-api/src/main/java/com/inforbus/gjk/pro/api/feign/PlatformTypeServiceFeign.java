package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.GjkPlatform;
import com.inforbus.gjk.pro.api.feign.factory.PlatformTypeServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = ServiceNameConstants.UMPS_SERVICE, fallbackFactory = PlatformTypeServiceFallbackFactory.class)
public interface PlatformTypeServiceFeign {
    String url = "/platform";

    /**
     * 获取平台类型
     *
     * @return
     */
    @GetMapping(url + "/getPlatFormTypeList")
    List<GjkPlatform> getPlatFormTypeList();
}
