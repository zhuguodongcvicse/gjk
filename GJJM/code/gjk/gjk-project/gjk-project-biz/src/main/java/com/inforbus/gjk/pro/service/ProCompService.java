package com.inforbus.gjk.pro.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inforbus.gjk.pro.api.entity.ProComp;

public interface ProCompService extends IService<ProComp> {

	/**
	 * 将所有传入的记录修改
	 * 
	 * @param proComps
	 * @return
	 */
	boolean updateAllProComp(List<ProComp> proComps);

}
