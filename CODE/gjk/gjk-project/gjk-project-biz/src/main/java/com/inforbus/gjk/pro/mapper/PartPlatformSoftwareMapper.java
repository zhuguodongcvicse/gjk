package com.inforbus.gjk.pro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inforbus.gjk.pro.api.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 流程软件构架库管理类
 */
public interface PartPlatformSoftwareMapper extends BaseMapper<PartPlatformSoftware> {

	/**
	 * 保存
	 * 
	 * @param partPlatformSoftware
	 */
	void savePartPlatformSoftware(@Param("soft") PartPlatformSoftware partPlatformSoftware);

	/**
	 * 根据编号ID删除
	 * 
	 * @param procedureId
	 */
	void deletePartPlatformSoftware(@Param("procedureId") String procedureId);

	/**
	 * 根据流程Id查询所有记录
	 * 
	 * @param procedureId
	 * @return
	 */
	List<PartPlatformSoftware> getByProcedureId(@Param("procedureId") String procedureId);
	/**
	 * 根据procedureId得到数据列表
	 * @param procedureId
	 * @return
	 */
	List<PartPlatformSoftware> getAllPartPlatformSoftwareByProcedureId(@Param("procedureId") String procedureId);
}
