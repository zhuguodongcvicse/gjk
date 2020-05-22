package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;

import com.inforbus.gjk.pro.api.entity.BSP;
import com.inforbus.gjk.pro.api.feign.factory.RemoteBSPServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RemoteBSPService
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign调用lbs模块中的bsp接口
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,fallbackFactory = RemoteBSPServiceFallbackFactory.class)
public interface RemoteBSPService {

    String url = "/bsp";

    /**
     * @Author wang
     * @Description: 根据多个id查询bsp数据
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.BSP>>
     * @Create: 2020/5/12
     */
    @GetMapping(url + "/getAllBSPListByIdIn/{ids}")
    public R<List<BSP>> getAllBSPListByIdIn(@PathVariable("ids") String ids);
}
