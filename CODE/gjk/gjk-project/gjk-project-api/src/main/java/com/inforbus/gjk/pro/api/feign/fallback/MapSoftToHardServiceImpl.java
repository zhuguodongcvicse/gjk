package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.feign.MapSoftToHardService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: mapSoftohardservice回滚
 * @Author: zhanghongxu
 * @CreateDate: 2020/7/2$ 17:01$
 * @UpdateUser:
 * @UpdateDate: 2020/7/2$ 17:01$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class MapSoftToHardServiceImpl implements MapSoftToHardService {
    @Setter
    private Throwable cause;

    @Override
    public void mapSoftToHard(String exe, String hardWareFilePath, String mapConfigPath, String sysParamFilePath, String userName) {
        log.error("调用数据中心的第三方softhToHard失败", cause);
    }

}
