package com.inforbus.gjk.pro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inforbus.gjk.pro.api.entity.PartPlatformBSP;
import com.inforbus.gjk.pro.api.entity.PartPlatformSoftware;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程软件构架库管理类
 */
public interface PartPlatformBSPMapper extends BaseMapper<PartPlatformBSP> {

	/**
	 * 根据编号ID删除
	 * 
	 * @param procedureId
	 */
	void deleteByProcedureId(@Param("procedureId") String procedureId);

	/**
	 * 根据流程Id查询所有记录
	 * 
	 * @param procedureId
	 * @return
	 */
	List<PartPlatformBSP> getByProcedureId(@Param("procedureId") String procedureId);

}
