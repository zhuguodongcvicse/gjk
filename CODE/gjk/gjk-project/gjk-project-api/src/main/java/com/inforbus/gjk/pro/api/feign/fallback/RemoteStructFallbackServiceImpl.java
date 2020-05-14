package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.RemoteStructService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteStructFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/13
 * @Description 通过feign调用lbs模块中的struct接口熔断器
 */
@Component
@Slf4j
public class RemoteStructFallbackServiceImpl implements RemoteStructService {

    @Setter
    private Throwable cause;

    @Override
    public R<List<Structlibs>> getStructlibsByIdList(List<String> idList) {
        log.error("调用库管理的feign接口getStructlibsByIdList方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
