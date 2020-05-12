/**
 * @Title: RemoteStructServiceFeignFallbackFactory.java
 * @Package com.inforbus.gjk.libs.api.feign.factory
 * @Desc 
 * Copyright: Copyright (c) 2018
 * Website: www.panzhijie.cn
 * 
 * @Author cvics
 * @DateTime 2020年5月7日
 * @version V1.0
 */

package com.inforbus.gjk.libs.api.feign.factory;

import org.springframework.stereotype.Component;

import com.inforbus.gjk.libs.api.feign.RemoteStructServiceFeign;
import com.inforbus.gjk.libs.api.feign.fallback.RemoteStructServiceFeignFallbackImpl;

import feign.hystrix.FallbackFactory;

/**
 * @ClassName: RemoteStructServiceFeignFallbackFactory
 * @Desc
 * @Author cvics
 * @DateTime 2020年5月7日  
 */
@Component
public class RemoteStructServiceFeignFallbackFactory implements FallbackFactory<RemoteStructServiceFeign> {

	@Override
	public RemoteStructServiceFeign create(Throwable throwable) {
		RemoteStructServiceFeignFallbackImpl remoteStructServiceFeignFallbackImpl = new RemoteStructServiceFeignFallbackImpl();
		remoteStructServiceFeignFallbackImpl.setCause(throwable);
		return remoteStructServiceFeignFallbackImpl;
	}

}
