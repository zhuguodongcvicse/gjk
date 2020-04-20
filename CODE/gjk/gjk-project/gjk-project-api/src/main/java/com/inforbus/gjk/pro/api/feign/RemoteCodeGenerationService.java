package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.factory.RemoteCodeGenerationServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * RemoteCodeGenerationService
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成feign接口
 */
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE,url = "localhost:8080/dataCenter/codeGeneration",fallbackFactory = RemoteCodeGenerationServiceFallbackFactory.class)
public interface RemoteCodeGenerationService {

    /**
     * @Author wang
     * @Description: 集成代码生成方法
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
     * @Create: 2020/4/20
     */
    @PutMapping("/codeGenerate")
    public R<Boolean> codeGeneration(@RequestBody Map<String, String> map);
}
