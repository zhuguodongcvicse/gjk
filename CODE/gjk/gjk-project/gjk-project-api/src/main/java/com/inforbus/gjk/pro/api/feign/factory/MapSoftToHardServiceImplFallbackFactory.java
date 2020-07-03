package com.inforbus.gjk.pro.api.feign.factory;

import com.inforbus.gjk.pro.api.feign.MapSoftToHardService;
import com.inforbus.gjk.pro.api.feign.fallback.MapSoftToHardServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: java类作用描述
 * @Author: zhanghongxu
 * @CreateDate: 2020/7/2$ 17:06$
 * @UpdateUser:
 * @UpdateDate: 2020/7/2$ 17:06$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class MapSoftToHardServiceImplFallbackFactory  implements FallbackFactory<MapSoftToHardService> {
    @Override
    public MapSoftToHardService create(Throwable cause) {
        MapSoftToHardServiceImpl mapSoftToHardServiceImpl = new MapSoftToHardServiceImpl();
        mapSoftToHardServiceImpl.setCause(cause);
        return mapSoftToHardServiceImpl;
    }
}
