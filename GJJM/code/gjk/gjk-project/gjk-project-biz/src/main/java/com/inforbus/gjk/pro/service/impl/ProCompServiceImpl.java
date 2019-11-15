package com.inforbus.gjk.pro.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.pro.api.entity.ProComp;
import com.inforbus.gjk.pro.mapper.ProCompMapper;
import com.inforbus.gjk.pro.service.ProCompService;

@Service("proCompService")
public class ProCompServiceImpl extends ServiceImpl<ProCompMapper, ProComp> implements ProCompService {

	public boolean updateAllProComp(List<ProComp> proComps) {
		try {
			for (ProComp proComp : proComps) {
				this.updateById(proComp);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
