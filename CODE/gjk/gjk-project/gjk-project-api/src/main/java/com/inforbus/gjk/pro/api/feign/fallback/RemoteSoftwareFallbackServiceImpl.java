package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.Software;
import com.inforbus.gjk.pro.api.feign.RemoteSoftwareService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteSoftwareFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign方式获取库服务中的sofrware数据熔断器
 */
@Slf4j
@Component
public class RemoteSoftwareFallbackServiceImpl implements RemoteSoftwareService {

    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.Software>>
     * @Create: 2020/5/11
     */
    @Override
    public R<List<Software>> getAllSoftwareListByIdIn(String ids) {
        log.error("调用项目库的feign接口getAllSoftwareListByIdIn方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
