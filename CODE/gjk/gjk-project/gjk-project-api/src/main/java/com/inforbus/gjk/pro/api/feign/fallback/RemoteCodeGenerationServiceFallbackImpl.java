package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.RemoteCodeGenerationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RemoteCodeGenerationServiceFallbackImpl
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成熔断器实现类
 */
@Slf4j
@Component
public class RemoteCodeGenerationServiceFallbackImpl implements RemoteCodeGenerationService {

    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 集成代码生成熔断方法
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
     * @Create: 2020/4/20
     */
    @Override
    public R<Boolean> codeGeneration(Map<String, String> map) {
        log.error("调用数据中心的feign接口codeGeneration方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
