package com.inforbus.gjk.pro.mapper;

import com.inforbus.gjk.admin.api.entity.SysDict;

import java.util.List;

/**
 * DictMapper
 *
 * @author wang
 * @date 2020/5/29
 * @Description 查询sys_dict表的数据
 */
public interface DictMapper {

    /**
     * @Author wang
     * @Description: 根据类型获取字典数据
     * @Param: [platform_type]
     * @Return: com.inforbus.gjk.admin.api.entity.SysDict
     * @Create: 2020/5/29
     */
    List<SysDict> getDictBytype(String type);

}
