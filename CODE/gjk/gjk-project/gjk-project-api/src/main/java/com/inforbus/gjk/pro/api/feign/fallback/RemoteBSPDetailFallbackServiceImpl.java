package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.BSPDetail;
import com.inforbus.gjk.pro.api.feign.RemoteBSPDetailService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteBSPDetailFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description bsp_detail熔断器
 */
@Component
@Slf4j
public class RemoteBSPDetailFallbackServiceImpl implements RemoteBSPDetailService {

    @Setter
    private Throwable cause;

    @Override
    public R<List<BSPDetail>> getBSPDetailByBSPIdIn(String ids) {
        log.error("调用项目库的feign接口getBSPDetailByBSPIdIn方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
