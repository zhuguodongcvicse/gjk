package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteSoftwareService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteSoftwareFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteSoftwareServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description RemoteSoftwareServiceFallback熔断工厂
 */
@Component
public class RemoteSoftwareServiceFallbackFactory implements FallbackFactory<RemoteSoftwareService> {
    @Override
    public RemoteSoftwareService create(Throwable throwable) {
        RemoteSoftwareFallbackServiceImpl remoteSoftwareFallbackService = new RemoteSoftwareFallbackServiceImpl();
        remoteSoftwareFallbackService.setCause(throwable);
        return remoteSoftwareFallbackService;
    }
}
