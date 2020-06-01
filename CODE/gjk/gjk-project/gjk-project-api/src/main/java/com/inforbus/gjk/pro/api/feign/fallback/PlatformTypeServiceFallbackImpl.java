
package com.inforbus.gjk.pro.api.feign.fallback;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.pro.api.dto.CopySoftwareAndBspDTO;
import com.inforbus.gjk.pro.api.dto.ModifyAssemblyDirDTO;
import com.inforbus.gjk.pro.api.entity.GjkPlatform;
import com.inforbus.gjk.pro.api.feign.AppSubassemblyServiceFeign;
import com.inforbus.gjk.pro.api.feign.PlatformTypeServiceFeign;
import com.inforbus.gjk.pro.api.vo.ProjectFileVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author sunchao
 * @date 2019/5/11
 * feign token  fallback
 */
@Slf4j
@Component
public class PlatformTypeServiceFallbackImpl implements PlatformTypeServiceFeign {
    @Setter
    private Throwable cause;

    @Override
    public List<GjkPlatform> getPlatFormTypeList() {
        log.error("调用数据中心的feign接口getPlatFormTypeList方法失败", cause);
        return null;
    }

}
