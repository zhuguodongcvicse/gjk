package com.inforbus.gjk.compile.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.compile.feign.RemoteDevenvService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * RemoteDevenvServiceFallbackImpl
 *
 * @author wang
 * @date 2020/4/17
 * @Description fegin调用失败熔断实现类
 */
@Slf4j
@Component
public class RemoteDevenvServiceFallbackImpl implements RemoteDevenvService {
    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 编译功能熔断方法
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/17
     */
    @Override
    public R command(Map<String, String> map) {
        log.error("Fegin调用数据中心编译功能失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
