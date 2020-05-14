package com.inforbus.gjk.libs.mapper;

import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SoftwareDetailMapper
 *
 * @author wang
 * @date 2020/5/12
 * @Description 查询software_detail表数据
 */
public interface SoftwareDetailMapper {
    /**
     * 根据多个ID查询数据列表 软件框架和平台关系
     * @param ids
     * @return
     */
    List<SoftwareDetail> getSoftwareDetailBySoftwareIdIn(@Param("ids") String ids);
}