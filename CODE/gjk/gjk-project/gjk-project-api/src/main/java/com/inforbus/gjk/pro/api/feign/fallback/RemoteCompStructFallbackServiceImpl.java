package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.RemoteCompStructService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteCompStructFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign调用lbs模块中的compStruct接口熔断器
 */
@Component
@Slf4j
public class RemoteCompStructFallbackServiceImpl implements RemoteCompStructService {

    @Setter
    private Throwable cause;

    @Override
    public R<List<CompStruct>> getCompStructByCompIdList(List<String> compIdList) {
        log.error("调用库管理的feign接口getCompStructByCompIdList方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
