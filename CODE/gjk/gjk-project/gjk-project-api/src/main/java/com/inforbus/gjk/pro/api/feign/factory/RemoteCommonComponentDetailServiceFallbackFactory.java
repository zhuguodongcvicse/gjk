package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteCommonComponentDetailService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteCommonComponentDetailFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteCommonComponentDetailServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description RemoteCommonComponentDetailService接口熔断工厂
 */
@Component
public class RemoteCommonComponentDetailServiceFallbackFactory implements FallbackFactory<RemoteCommonComponentDetailService> {
    @Override
    public RemoteCommonComponentDetailService create(Throwable throwable) {
        RemoteCommonComponentDetailFallbackServiceImpl remoteCommonComponentDetailFallbackService = new RemoteCommonComponentDetailFallbackServiceImpl();
        remoteCommonComponentDetailFallbackService.setCause(throwable);
        return remoteCommonComponentDetailFallbackService;
    }
}
