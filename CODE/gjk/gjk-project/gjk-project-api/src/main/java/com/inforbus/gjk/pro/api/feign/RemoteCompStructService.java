package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.factory.RemoteCompStructServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * RemoteCompStructService
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign调用lbs模块中的compStruct接口
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,fallbackFactory = RemoteCompStructServiceFallbackFactory.class)
public interface RemoteCompStructService {

    String url = "compStruct";

    /**
     * @Author wang
     * @Description: 根据构件库id集合，获取构件库和结构体关系数据
     * @Param: [compIdList]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.common.core.entity.CompStruct>>
     * @Create: 2020/5/12
     */
    @PostMapping(url + "/getCompStructByCompIdList")
    public R<List<CompStruct>> getCompStructByCompIdList(@RequestBody List<String> compIdList);
}
