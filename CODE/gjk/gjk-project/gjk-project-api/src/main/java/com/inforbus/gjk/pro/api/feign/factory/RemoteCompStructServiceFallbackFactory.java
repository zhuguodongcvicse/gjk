package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteCompStructService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteCompStructFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteCompStructServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign调用lbs模块中的compStruct接口熔断工厂
 */
@Component
public class RemoteCompStructServiceFallbackFactory implements FallbackFactory<RemoteCompStructService> {
    @Override
    public RemoteCompStructService create(Throwable throwable) {
        RemoteCompStructFallbackServiceImpl remoteCompStructFallbackService = new RemoteCompStructFallbackServiceImpl();
        remoteCompStructFallbackService.setCause(throwable);
        return remoteCompStructFallbackService;
    }
}
