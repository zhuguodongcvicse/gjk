package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.CommonComponentDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RemoteCommonComponentDetailService
 *
 * @author wang
 * @date 2020/5/12
 * @Description 获取库管理中gjk_commoncomponent_detail表数据feign接口
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,url = "localhost:8080/libs/commoncomponentdetail")
public interface RemoteCommonComponentDetailService {
    /**
     * @Author wang
     * @Description: 根据多个ID查询gjk_CommonComponent_Detail表数据
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.CommonComponentDetail>>
     * @Create: 2020/5/12
     */
    @GetMapping("/getCommonComponentDetailByCompIdIn/{ids}")
    public R<List<CommonComponentDetail>> getCommonComponentDetailByCompIdIn(@PathVariable("ids") String ids);

}
