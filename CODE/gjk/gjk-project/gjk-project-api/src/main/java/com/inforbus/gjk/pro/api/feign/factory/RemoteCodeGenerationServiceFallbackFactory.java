package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteCodeGenerationService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteCodeGenerationServiceFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteCodeGenerationServiceFallbackFactory
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成熔断器
 */
@Component
public class RemoteCodeGenerationServiceFallbackFactory implements FallbackFactory<RemoteCodeGenerationService> {

    @Override
    public RemoteCodeGenerationService create(Throwable throwable) {
        RemoteCodeGenerationServiceFallbackImpl remoteCodeGenerationServiceFallback = new RemoteCodeGenerationServiceFallbackImpl();
        remoteCodeGenerationServiceFallback.setCause(throwable);
        return remoteCodeGenerationServiceFallback;
    }
}
