package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteBSPService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteBSPFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteBSPServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description 获取项目库中bsp数据熔断工厂
 */
@Component
public class RemoteBSPServiceFallbackFactory implements FallbackFactory<RemoteBSPService>{

    @Override
    public RemoteBSPService create(Throwable throwable) {
        RemoteBSPFallbackServiceImpl remoteBSPFallbackService = new RemoteBSPFallbackServiceImpl();
        remoteBSPFallbackService.setCause(throwable);
        return remoteBSPFallbackService;
    }
}
