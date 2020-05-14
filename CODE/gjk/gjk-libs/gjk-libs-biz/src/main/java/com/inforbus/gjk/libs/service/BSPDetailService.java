package com.inforbus.gjk.libs.service;


import com.inforbus.gjk.libs.api.entity.BSPDetail;
import java.util.List;

/**
 * BSPDetailService
 *
 * @author wang
 * @date 2020/5/12
 * @Description gjk_bsp_detail表业务逻辑接口
 */
public interface BSPDetailService {
    /**
     * 根据多个ID查询数据列表 BSP和平台关系
     * @param ids
     * @return
     */
    List<BSPDetail> getBSPDetailByBSPIdIn(String ids);
}
