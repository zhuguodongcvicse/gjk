package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.factory.RemoteCodeGenerationServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * RemoteCodeGenerationService
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成feign接口
 */
@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE,fallbackFactory = RemoteCodeGenerationServiceFallbackFactory.class)
public interface RemoteCodeGenerationService {

    String url = "codeGeneration";
    /**
     * @Author wang
     * @Description: 集成代码生成方法
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
     * @Create: 2020/4/20
     */
    @PutMapping(url + "/codeGenerate")
    public R<Boolean> codeGeneration(@RequestBody Map<String, String> map);

    /**
     * @Author wang
     * @Description: 静态检查功能
     * @Param: [filePath, fileName]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/5/18
     */
    @PostMapping(url + "/staticInspect")
    public R staticInspect(@RequestParam("filePath") String filePath, @RequestParam("fileName")String fileName);
}
