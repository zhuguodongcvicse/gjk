package com.inforbus.gjk.libs.service.impl;

import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.mapper.BSPDetailMapper;
import com.inforbus.gjk.libs.mapper.BSPMapper;
import com.inforbus.gjk.libs.service.BSPDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BSPDetailServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description gjk_bsp_detail表业务实现类
 */
@Service
public class BSPDetailServiceImpl implements BSPDetailService {

    @Autowired
    private BSPDetailMapper bspDetailMapper;

    /**
     * @Author wang
     * @Description: 根据多个id查询平台类型数据
     * @Param: [ids]
     * @Return: java.util.List<com.inforbus.gjk.libs.api.entity.BSPDetail>
     * @Create: 2020/5/12
     */
    @Override
    public List<BSPDetail> getBSPDetailByBSPIdIn(String ids) {
        return bspDetailMapper.getBSPDetailByBSPIdIn(ids);
    }
}
