package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteBSPDetailService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteBSPDetailFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteBSPDetailServiceFallbackFactory
 *
 * @author wang
 * @date 2020/5/12
 * @Description gjk_bsp_detail熔断工厂
 */
@Component
public class RemoteBSPDetailServiceFallbackFactory implements FallbackFactory<RemoteBSPDetailService>{
    @Override
    public RemoteBSPDetailService create(Throwable throwable) {
        RemoteBSPDetailFallbackServiceImpl remoteBSPDetailFallbackService = new RemoteBSPDetailFallbackServiceImpl();
        remoteBSPDetailFallbackService.setCause(throwable);
        return remoteBSPDetailFallbackService;
    }
}
