package com.inforbus.gjk.compile.feign.factory;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import com.inforbus.gjk.compile.feign.RemoteTokenService;
import com.inforbus.gjk.compile.feign.fallback.RemoteTokenServiceFallbackImpl;

@Component
public class RemoteTokenServiceFallbackFactory implements FallbackFactory<RemoteTokenService> {

	@Override
	public RemoteTokenService create(Throwable throwable) {
		RemoteTokenServiceFallbackImpl remoteTokenServiceFallback = new RemoteTokenServiceFallbackImpl();
		remoteTokenServiceFallback.setCause(throwable);
		return remoteTokenServiceFallback;
	}
}
