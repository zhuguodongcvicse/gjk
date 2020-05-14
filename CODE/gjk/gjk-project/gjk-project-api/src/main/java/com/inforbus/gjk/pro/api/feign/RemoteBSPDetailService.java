package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.BSPDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RemoteBSPDetailService
 *
 * @author wang
 * @date 2020/5/12
 * @Description 通过feign调用lbs模块中的bspDetail接口
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,url = "localhost:8080/libs/bspDetail")
public interface RemoteBSPDetailService {

    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表 BSP和平台关系
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.BSPDetail>>
     * @Create: 2020/5/12
     */
    @GetMapping("getBSPDetailByBSPIdIn/{ids}")
    public R<List<BSPDetail>> getBSPDetailByBSPIdIn(@PathVariable("ids") String ids);
}
