package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.entity.Software;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RemoteSoftwareService
 *
 * @author wang
 * @date 2020/5/11
 * @Description 通过feign方式获取库服务中的sofrware数据
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,
        url = "localhost:8080/libs/software")
public interface RemoteSoftwareService {
    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.Software>>
     * @Create: 2020/5/11
     */
    @GetMapping("/getAllSoftwareListByIdIn/{ids}")
    public R<List<Software>> getAllSoftwareListByIdIn(@PathVariable("ids") String ids);
}
