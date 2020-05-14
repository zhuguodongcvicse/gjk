package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteStructService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteStructFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteStructServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/13
 * @Description 通过feign调用lbs模块中的struct接口熔断工厂
 */
@Component
public class RemoteStructServiceFallbackFactory  implements FallbackFactory<RemoteStructService>{
    @Override
    public RemoteStructService create(Throwable throwable) {
        RemoteStructFallbackServiceImpl remoteStructFallbackService = new RemoteStructFallbackServiceImpl();
        remoteStructFallbackService.setCause(throwable);
        return remoteStructFallbackService;
    }
}
