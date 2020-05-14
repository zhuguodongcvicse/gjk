package com.inforbus.gjk.pro.api.feign;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.Structlibs;
import com.inforbus.gjk.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * RemoteStructService
 *
 * @author wang
 * @date 2020/5/13
 * @Description 通过feign调用lbs模块中的struct接口
 */
@FeignClient(value = ServiceNameConstants.LIBS_SERVICE,url = "localhost:8080/libs/structlibs")
public interface RemoteStructService {

    /**
     * @Author wang
     * @Description: 根据多个id查询结构体数据
     * @Param: [idList]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.common.core.entity.Structlibs>>
     * @Create: 2020/5/13
     */
    @PostMapping("/getStructlibsByIdList")
    public R<List<Structlibs>> getStructlibsByIdList(@RequestBody List<String> idList);
}
