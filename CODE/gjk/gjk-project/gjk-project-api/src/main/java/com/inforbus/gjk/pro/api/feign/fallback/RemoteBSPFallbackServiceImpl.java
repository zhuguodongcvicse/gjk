package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.BSP;
import com.inforbus.gjk.pro.api.feign.RemoteBSPService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteBSPFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign方式获取库服务中的bsp数据熔断器
 */
@Component
@Slf4j
public class RemoteBSPFallbackServiceImpl implements RemoteBSPService {

    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 根据多个id查询bsp数据
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.BSP>>
     * @Create: 2020/5/12
     */
    @Override
    public R<List<BSP>> getAllBSPListByIdIn(String ids) {
        log.error("调用项目库的feign接口getAllBSPListByIdIn方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
