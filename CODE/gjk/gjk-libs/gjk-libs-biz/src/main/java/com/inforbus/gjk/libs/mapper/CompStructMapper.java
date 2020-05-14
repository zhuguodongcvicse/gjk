package com.inforbus.gjk.libs.mapper;

import com.inforbus.gjk.common.core.entity.CompStruct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CompStructMapper
 *
 * @author wang
 * @date 2020/5/12
 * @Description 查询gjk_comp_struct表数据
 */
public interface CompStructMapper {
    /**
     * 根据构件库id集合，获取构件库和结构体关系数据
     * @param compIdList
     * @return
     */
    List<CompStruct> getCompStructByCompIdList(@Param("compIdList") List<String> compIdList);
}
