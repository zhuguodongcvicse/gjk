package com.inforbus.gjk.libs.controller;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.service.BSPDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.BinaryClient;

import java.util.List;

/**
 * BSPDetailController
 *
 * @author wang
 * @date 2020/5/12
 * @Description gjk_bsp_detail表控制器
 */
@RestController
@RequestMapping("bspDetail")
public class BSPDetailController {

    @Autowired
    private BSPDetailService bspDetailService;

    /**
     * @Author wang
     * @Description: 根据多个ID查询数据列表 BSP和平台关系
     * @Param: [ids]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.libs.api.entity.BSPDetail>>
     * @Create: 2020/5/12
     */
    @GetMapping("getBSPDetailByBSPIdIn/{ids}")
    public R<List<BSPDetail>> getBSPDetailByBSPIdIn(@PathVariable("ids") String ids){
        R<List<BSPDetail>> r = new R<>();
        try {
            List<BSPDetail> list = bspDetailService.getBSPDetailByBSPIdIn(ids);
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
