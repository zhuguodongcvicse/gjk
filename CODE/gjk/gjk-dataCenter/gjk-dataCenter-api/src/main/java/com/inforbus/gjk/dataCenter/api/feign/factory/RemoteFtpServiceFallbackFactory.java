package com.inforbus.gjk.dataCenter.api.feign.factory;

import com.inforbus.gjk.dataCenter.api.feign.RemoteFtpService;
import com.inforbus.gjk.dataCenter.api.feign.fallback.RemoteFtpServiceFallbackImpl;

import feign.hystrix.FallbackFactory;

public class RemoteFtpServiceFallbackFactory implements FallbackFactory<RemoteFtpService>  {

	@Override
	public RemoteFtpService create(Throwable throwable) {
		RemoteFtpServiceFallbackImpl remoteFtpServiceFallbackImpl = new RemoteFtpServiceFallbackImpl();
		remoteFtpServiceFallbackImpl.setCause(throwable);
		return remoteFtpServiceFallbackImpl;
		
		
	}
}
