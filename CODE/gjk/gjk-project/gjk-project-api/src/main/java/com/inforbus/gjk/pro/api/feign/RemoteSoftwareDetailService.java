package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.SoftwareDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RemoteSoftwareDetailService
 *
 * @author wang
 * @date 2020/5/12
 * @Description 查询libs服务中softwareDetail数据
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,url = "localhost:8080/libs/softwareDetail")
public interface RemoteSoftwareDetailService {

    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表 软件框架和平台关系
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.SoftwareDetail>>
     * @Create: 2020/5/12
     */
    @GetMapping("getSoftwareDetail/{ids}")
    public R<List<SoftwareDetail>> getSoftwareDetailBySoftwareIdIn(@PathVariable("ids") String ids);
}
