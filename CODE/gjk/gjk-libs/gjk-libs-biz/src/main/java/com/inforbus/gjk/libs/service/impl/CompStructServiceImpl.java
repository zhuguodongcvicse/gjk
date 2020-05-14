package com.inforbus.gjk.libs.service.impl;

import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.libs.mapper.CompStructMapper;
import com.inforbus.gjk.libs.service.CompStructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CompStructServiceImpl
 *
 * @author wang
 * @date 2020/5/12
 * @Description 询gjk_comp_struct表数据业务逻辑实现类
 */
@Service
public class CompStructServiceImpl implements CompStructService {

    @Autowired
    private CompStructMapper compStructMapper;

    /**
     * @Author wang
     * @Description: 根据构件库id集合，获取构件库和结构体关系数据
     * @Param: [compIdList]
     * @Return: java.util.List<com.inforbus.gjk.common.core.entity.CompStruct>
     * @Create: 2020/5/12
     */
    @Override
    public List<CompStruct> getCompStructByCompIdList(List<String> compIdList) {
        return compStructMapper.getCompStructByCompIdList(compIdList);
    }
}
