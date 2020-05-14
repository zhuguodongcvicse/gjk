package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.CommonComponent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RemoteCommonComponentService
 *
 * @author wang
 * @date 2020/5/12
 * @Description 获取项目管理中构件库数据feign接口
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,url = "localhost:8080/libs/commoncomponent")
public interface RemoteCommonComponentService {

    /**
     * @Author wang
     * @Description: 根据多个id查询构件库数据
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.CommonComponent>>
     * @Create: 2020/5/12
     */
    @GetMapping("getCommonComponentByIdIn/{ids}")
    public R<List<CommonComponent>> getCommonComponentByIdIn(@PathVariable("ids")String ids);
}
