package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.SoftwareDetail;
import com.inforbus.gjk.pro.api.feign.RemoteSoftwareDetailService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteSoftwareDetailFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description 查询libs服务中softwareDetail数据熔断器
 */
@Slf4j
@Component
public class RemoteSoftwareDetailFallbackServiceImpl implements RemoteSoftwareDetailService {

    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表 软件框架和平台关系
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.SoftwareDetail>>
     * @Create: 2020/5/12
     */
    @Override
    public R<List<SoftwareDetail>> getSoftwareDetailBySoftwareIdIn(String ids) {
        log.error("调用项目库的feign接口getSoftwareDetailBySoftwareIdIn方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.LIBS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
