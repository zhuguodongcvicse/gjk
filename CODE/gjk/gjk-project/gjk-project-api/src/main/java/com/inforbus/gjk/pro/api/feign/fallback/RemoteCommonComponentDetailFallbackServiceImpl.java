package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.CommonComponentDetail;
import com.inforbus.gjk.pro.api.feign.RemoteCommonComponentDetailService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteCommonComponentDetailFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description RemoteCommonComponentDetailService接口熔断器
 */
@Component
@Slf4j
public class RemoteCommonComponentDetailFallbackServiceImpl implements RemoteCommonComponentDetailService {
    @Setter
    private Throwable cause;

    @Override
    public R<List<CommonComponentDetail>> getCommonComponentDetailByCompIdIn(String ids) {
        log.error("调用库管理的feign接口getCommonComponentDetailByCompIdIn方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
