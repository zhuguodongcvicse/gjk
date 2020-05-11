
package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.entity.XmlEntityMap;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.XMlEntityMapVO;
import com.inforbus.gjk.pro.api.feign.AppSubassemblyServiceFeign;
import com.inforbus.gjk.pro.api.feign.DisposeDataCenterServiceFeign;
import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sunchao
 * @date 2019/5/11
 * feign token  fallback
 */
@Slf4j
@Component
public class AppSubassemblyServiceFallbackImpl implements AppSubassemblyServiceFeign {
    @Setter
    private Throwable cause;

    @Override
    public R judgeFileExist(@RequestParam("judgeFileExist") String filePath) {
        log.error("调用数据中心的feign接口judgeFileExist方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }

    @Override
    public R getAppPath(@RequestParam("filePath") String filePath, @RequestParam("selectFileName") String selectFileName) {
        log.error("调用数据中心的feign接口getAppPath方法失败", cause);
        R r = new R();
        r.setCode(CommonConstants.FAIL);
        r.setMsg(ServiceNameConstants.DATACENDER_SERVICE + "服务器异常，请联系管理员");
        return r;
    }
}
