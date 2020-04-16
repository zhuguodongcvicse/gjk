package com.inforbus.gjk.libs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.dto.ThreeLibsFilePathDTO;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.api.entity.ThreeLibs;
import com.inforbus.gjk.libs.api.feign.DataCenterServiceFeign;
import com.inforbus.gjk.libs.mapper.ThreeLibsMapper;
import com.inforbus.gjk.libs.service.ThreeLibsService;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 构件明细
 *
 * @author xiaohe
 * @date 2019-04-17 16:05:37
 */
@Service("ThreeLibsService")
public class ThreeLibsServiceImpl extends ServiceImpl<ThreeLibsMapper, ThreeLibs> implements ThreeLibsService {

	//文件路径（D:\14S_GJK_GIT\gjk\）
	@Value("${git.local.path}")
	private String gitFilePath;
	//文件编码格式
	@Value("${gjk.code.encodeing}")
	private String defaultEncoding;
	
	@Autowired
	private DataCenterServiceFeign dataCenterServiceFeign;

	/*
	 * 通过libs_id获取属于哪个算法类下
	 */
	@Override
	public List<ThreeLibs> getAlgorithmByLibsId() {
		return baseMapper.getAlgorithmByLibsId();
	}

	/*
	 * 获取算法库下面的文件
	 */
	@Override
	public List<ThreeLibs> getAlgorithmFile() {
		return baseMapper.getAlgorithmFile();
	}

	/*
	 * 根据树的节点id获取当条数据，得到filePath
	 */
	@Override
	public List<ThreeLibs> getAlgorithmFilePath(String id) {
		return baseMapper.getAlgorithmFilePath(id);
	}

	/*
	 * 根据库id获取库信息
	 */
	@Override
	public List<ThreeLibs> getPlatformByLibsId() {
		return baseMapper.getPlatformByLibsId();
	}

	@Override
	public List<ThreeLibs> getPlatformFile() {
		return baseMapper.getPlatformFile();
	}

	@Override
	public List<ThreeLibs> getTestByLibsId() {
		return baseMapper.getTestByLibsId();
	}

	@Override
	public List<ThreeLibs> getTestFile() {
		return baseMapper.getTestFile();
	}

	@Override
	public List<Software> getSoftware() {
		return baseMapper.getSoftware();
	}

	@Override
	public List<SoftwareDetail> getSoftwarePlatform() {
		return baseMapper.getSoftwarePlatform();
	}

	@Override
	public List<SoftwareFile> getSoftwareFile() {
		return baseMapper.getSoftwareFile();
	}

	/**
	 * 保存文本编辑器修改的内容（文本编辑器的）
	 * @param filePath 文件路径
	 * @param textContext 文本内容
	 */
	@Override
	public R saveFileContext(String filePath, String textContext) {
		return new R<>(dataCenterServiceFeign.saveFileContext(filePath, textContext));
	}

	/**
	 * 删除三个库的时候，如果关联了构件、bsp、软甲框架等文件，不能被删除 根据各个模块用到的三个库的id来查询数据
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, String>> findBSPDetailByPlatformId(String platformId) {
		return baseMapper.findBSPDetailByPlatformId(platformId);
	}

	@Override
	public List<Map<String, String>> findSoftwareDetailByPlatformId(String platformId) {
		return baseMapper.findSoftwareDetailByPlatformId(platformId);
	}

	@Override
	public List<Map<String, String>> findCompframeDetailByPlatformId(String platformId) {
		return baseMapper.findCompframeDetailByPlatformId(platformId);
	}

	@Override
	public List<ComponentDetail> findComponentDetailByPlatformId(String platformId) {
		return baseMapper.findComponentDetailByPlatformId(platformId);
	}

	/**
	 * 三个库读取程序文本编辑器文件
	 * @param threeLibsFilePathDTO 封装了路径（全路径，从D盘开始）及编码格式
	 * @return
	 */
	@Override
	public R fileRead(ThreeLibsFilePathDTO threeLibsFilePathDTO) {
		return dataCenterServiceFeign.readFiles(threeLibsFilePathDTO);
		
	}
}
