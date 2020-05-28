package com.inforbus.gjk.compile.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.compile.feign.factory.RemoteDevenvServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * RemoteDevenvService
 *
 * @author wang
 * @date 2020/4/16
 * @Description 远程调用的fegin接口，调用编译功能
 */
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE,fallbackFactory = RemoteDevenvServiceFallbackFactory.class)
public interface RemoteDevenvService {

    String url = "/devenv";

    @PutMapping(value = url + "/Command")
    public R command(@RequestBody Map<String, String> map);
}
