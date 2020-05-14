package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteSoftwareDetailService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteSoftwareDetailFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteSoftwareDetailServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description 获取gjk_software_detail表数据熔断工厂
 */
@Component
public class RemoteSoftwareDetailServiceFallbackFactory implements FallbackFactory<RemoteSoftwareDetailService> {
    @Override
    public RemoteSoftwareDetailService create(Throwable throwable) {
        RemoteSoftwareDetailFallbackServiceImpl remoteSoftwareDetailFallbackService = new RemoteSoftwareDetailFallbackServiceImpl();
        remoteSoftwareDetailFallbackService.setCause(throwable);
        return remoteSoftwareDetailFallbackService;
    }
}
