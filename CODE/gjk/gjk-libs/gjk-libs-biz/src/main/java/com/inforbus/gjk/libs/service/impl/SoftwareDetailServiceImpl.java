package com.inforbus.gjk.libs.service.impl;

import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.mapper.SoftwareDetailMapper;
import com.inforbus.gjk.libs.service.SoftwareDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SoftwareDetailServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description softwre_detail也是实现类
 */
@Service
public class SoftwareDetailServiceImpl implements SoftwareDetailService {

    @Autowired
    private SoftwareDetailMapper softwareDetailMapper;

    /**
     * @Author wang
     * @Description: 根据id查询数据
     * @Param: [ids]
     * @Return: java.util.List<com.inforbus.gjk.libs.api.entity.SoftwareDetail>
     * @Create: 2020/5/12
     */
    @Override
    public List<SoftwareDetail> getSoftwareDetailBySoftwareIdIn(String ids) {
        return softwareDetailMapper.getSoftwareDetailBySoftwareIdIn(ids);
    }
}
