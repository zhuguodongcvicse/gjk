package com.inforbus.gjk.compile.feign.factory;

import com.inforbus.gjk.compile.feign.RemoteDevenvService;
import com.inforbus.gjk.compile.feign.fallback.RemoteDevenvServiceFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteDevenvServiceFallbackFactory
 *
 * @author wang
 * @date 2020/4/17
 * @Description fegin调用失败的熔断器
 */
@Component
public class RemoteDevenvServiceFallbackFactory implements FallbackFactory<RemoteDevenvService> {
    @Override
    public RemoteDevenvService create(Throwable throwable) {
        RemoteDevenvServiceFallbackImpl remoteDevenvServiceFallback = new RemoteDevenvServiceFallbackImpl();
        remoteDevenvServiceFallback.setCause(throwable);
        return remoteDevenvServiceFallback;
    }
}
