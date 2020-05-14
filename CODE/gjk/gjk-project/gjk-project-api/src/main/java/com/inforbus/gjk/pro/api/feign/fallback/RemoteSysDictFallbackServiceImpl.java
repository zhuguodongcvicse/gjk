package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.RemoteSysDictService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RemoteSysDictFallbackServiceImpl
 *
 * @author wang
 * @date 2020/5/13
 * @Description 通过feign调用admin模块中的dict接口熔断器
 */
@Component
@Slf4j
public class RemoteSysDictFallbackServiceImpl implements RemoteSysDictService {

    @Setter
    private Throwable cause;

    /**
     * @Author wang
     * @Description: 根据remarks查找字典数据熔断方法
     * @Param: [remarks]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.pro.api.entity.SysDict>>
     * @Create: 2020/5/13
     */
    @Override
    public R<List<SysDict>> getSysDictByRemarksIn(List<String> remarksList) {
        log.error("调用admin管理的feign接口getSysDictByRemarksIn方法失败", cause);
        R r = new R();
        r.setData(null);
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.UMPS_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
