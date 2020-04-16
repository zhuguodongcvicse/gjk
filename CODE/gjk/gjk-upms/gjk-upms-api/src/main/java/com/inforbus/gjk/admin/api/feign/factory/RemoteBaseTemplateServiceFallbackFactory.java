package com.inforbus.gjk.admin.api.feign.factory;

import com.inforbus.gjk.admin.api.feign.RemoteBaseTemplateService;
import com.inforbus.gjk.admin.api.feign.fallback.RemoteBaseTemplateServiceFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteBaseTemplateServiceFallbackFactory
 *
 * @author wang
 * @date 2020/4/14
 * @Description fegin失败回调实现类
 */
@Component
public class RemoteBaseTemplateServiceFallbackFactory implements FallbackFactory<RemoteBaseTemplateService> {

    @Override
    public RemoteBaseTemplateService create(Throwable throwable) {
        RemoteBaseTemplateServiceFallbackImpl remoteBaseTemplateServiceFallback = new RemoteBaseTemplateServiceFallbackImpl();
        remoteBaseTemplateServiceFallback.setCause(throwable);
        return remoteBaseTemplateServiceFallback;
    }
}
