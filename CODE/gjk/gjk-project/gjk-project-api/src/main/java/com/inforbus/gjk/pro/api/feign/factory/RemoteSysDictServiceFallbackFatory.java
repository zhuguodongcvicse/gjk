package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.RemoteSysDictService;
import com.inforbus.gjk.pro.api.feign.fallback.RemoteSysDictFallbackServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteSysDictServiceFallbackFatory
 *
 * @author wang
 * @date 2020/5/13
 * @Description 通过feign调用admin模块中的dict接口熔断工厂
 */
@Component
public class RemoteSysDictServiceFallbackFatory implements FallbackFactory<RemoteSysDictService>{
    @Override
    public RemoteSysDictService create(Throwable throwable) {
        RemoteSysDictFallbackServiceImpl remoteSysDictFallbackService = new RemoteSysDictFallbackServiceImpl();
        remoteSysDictFallbackService.setCause(throwable);
        return remoteSysDictFallbackService;
    }
}
