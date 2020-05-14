package com.inforbus.gjk.libs.controller;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.service.SoftwareDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SoftwareDetailController
 *
 * @author wang
 * @date 2020/5/12
 * @Description softwaredetail控制器
 */
@RestController
@RequestMapping("/softwareDetail")
public class SoftwareDetailController {

    @Autowired
    private SoftwareDetailService softwareDetailService;

    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表 软件框架和平台关系
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.SoftwareDetail>>
     * @Create: 2020/5/12
     */
    @GetMapping("getSoftwareDetail/{ids}")
    public R<List<SoftwareDetail>> getSoftwareDetailBySoftwareIdIn(@PathVariable("ids") String ids){
        R<List<SoftwareDetail>> r = new R<>();
        try {
            List<SoftwareDetail> list = softwareDetailService.getSoftwareDetailBySoftwareIdIn(ids);
            r.setData(list);
            r.setMsg("查询成功");
        }catch (Exception e){
            r.setData(null);
            r.setMsg("查询失败");
            r.setCode(CommonConstants.FAIL);
        }
        return r;
    }

}
