package com.inforbus.gjk.pro.api.feign;


import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.factory.RemoteSysDictServiceFallbackFatory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * RemoteSysDictService
 *
 * @author wang
 * @date 2020/5/13
 * @Description 通过feign调用admin模块中的dict接口
 */
@FeignClient(value = ServiceNameConstants.UMPS_SERVICE,fallbackFactory = RemoteSysDictServiceFallbackFatory.class)
public interface RemoteSysDictService {

    String url = "/dict";

    /**
     * @Author wang
     * @Description: 根据remarks查找字典数据
     * @Param: [remarks]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.admin.api.entity.SysDict>>
     * @Create: 2020/5/13
     */
    @PostMapping(url + "/getSysDictByRemarksIn")
    public R<List<SysDict>> getSysDictByRemarksIn(@RequestBody List<String> remarksList);
}
