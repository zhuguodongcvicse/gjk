package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteCommonComponentService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteCommonComponentFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteCommonComponentServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description RemoteCommonComponentService 熔断器工厂
 */
@Component
public class RemoteCommonComponentServiceFallbackFactory implements FallbackFactory<RemoteCommonComponentService> {
    @Override
    public RemoteCommonComponentService create(Throwable throwable) {
        RemoteCommonComponentFallbackServiceImpl remoteCommonComponentFallbackService = new RemoteCommonComponentFallbackServiceImpl();
        remoteCommonComponentFallbackService.setCause(throwable);
        return remoteCommonComponentFallbackService;
    }
}
