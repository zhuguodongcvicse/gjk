package com.inforbus.gjk.libs.mapper;

import com.inforbus.gjk.libs.api.entity.BSPDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BSPDetailMapper
 *
 * @author wang
 * @date 2020/5/12
 * @Description 查询gjk_bsp_detail表数据
 */
public interface BSPDetailMapper {
    /**
     * 根据多个ID查询数据列表 BSP和平台关系
     * @param ids
     * @return
     */
    List<BSPDetail> getBSPDetailByBSPIdIn(@Param("ids") String ids);
}
