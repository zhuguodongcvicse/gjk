
package com.inforbus.gjk.admin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.inforbus.gjk.admin.api.entity.BSP;
import com.inforbus.gjk.admin.api.entity.BSPDetail;
import com.inforbus.gjk.admin.api.entity.BSPFile;
import com.inforbus.gjk.admin.api.entity.Component;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.entity.Software;
import com.inforbus.gjk.admin.api.entity.SoftwareDetail;
import com.inforbus.gjk.admin.api.entity.SoftwareFile;
import com.inforbus.gjk.admin.api.vo.PlatformVO;
import com.inforbus.gjk.admin.mapper.GjkPlatformMapper;
import com.inforbus.gjk.admin.service.FeginComponentService;
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;
import com.thoughtworks.xstream.core.ReferenceByIdMarshaller.IDGenerator;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 平台库权限表 服务实现类
 * </p>
 *
 * @author geng_hxian
 * @since 2019/4/17
 */
@Service
@AllArgsConstructor
public class GjkPlatformServiceImpl extends ServiceImpl<GjkPlatformMapper, GjkPlatform> implements GjkPlatformService {

	private static final String libsPath = JGitUtil.getLOCAL_REPO_PATH();


	@Autowired
	private FeginComponentService feginComponentService;
	
	@Override
	@Cacheable(value = "menu_details", key = "#roleId  + '_menu'")
	public List<PlatformVO> getPlatformByRoleId() {
		return baseMapper.listPlatformsByRoleId();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "menu_details", allEntries = true)
	public R removePlatformById(String id) {
		// 查询父节点为当前节点的节点
		List<GjkPlatform> menuList = this.list(Wrappers.<GjkPlatform>query().lambda().eq(GjkPlatform::getParentId, id));
		if (CollUtil.isNotEmpty(menuList)) {
			return R.builder().code(CommonConstants.FAIL).msg("菜单含有下级不能删除").build();
		}

		// 删除当前平台库及其子菜单
		return new R(this.removeById(id));
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean updatePlatformById(GjkPlatform gjkPlatform) {
		return this.updateById(gjkPlatform);
	}

	/**
	 * 组装平台树，包括软件框架库
	 * 
	 * @return
	 */
	public List<GjkPlatform> getPlatformTree(List<GjkPlatform> libsList) {
		List<GjkPlatform> gjkPlatforms = null;

		try {
			// 将软件框架库数据组装成 平台库可挂接的数据
			gjkPlatforms = new ArrayList<GjkPlatform>();
			List<GjkPlatform> platforms = Lists.newArrayList();
			for (GjkPlatform gjkPlatform : libsList) {
//				if ("-1".equals(gjkPlatform.getParentId())) {
				GjkPlatform platform = new GjkPlatform();
				platform.setParentId(gjkPlatform.getPlatformId());
				platform.setPlatformId("BSP" + gjkPlatform.getPlatformId());
				platform.setName("BSP库");
				platforms.add(platform);
				GjkPlatform platform1 = new GjkPlatform();
				platform1.setParentId(gjkPlatform.getPlatformId());
				platform1.setPlatformId("software" + gjkPlatform.getPlatformId());
				platform1.setName("软件框架库");
				platforms.add(platform1);
				GjkPlatform platform2 = new GjkPlatform();
				platform2.setParentId(gjkPlatform.getPlatformId());
				platform2.setPlatformId("component" + gjkPlatform.getPlatformId());
				platform2.setName("构件库");
				platforms.add(platform2);
//				}
			}
			gjkPlatforms.addAll(platforms);
			// 软件框架库主表信息
			List<Software> softList = baseMapper.getSoftware();
			// 软件框架库文件表信息
			List<SoftwareFile> softfileList = baseMapper.getSoftwareFile();
			// 软件框架库平台库关联关系
			List<SoftwareDetail> softdetailList = baseMapper.getSoftwarePlatform();

			// BSP主表信息
			List<BSP> bspList = baseMapper.getBSP();
			// BSP文件表信息
			List<BSPFile> bspfileList = baseMapper.getBSPFile();
			// BSP平台库关联关系
			List<BSPDetail> bspdetailList = baseMapper.getBSPPlatform();

			// 将软件框架库文件数据进行分组，将数据分别保存，便于组装数据
			HashMap<String, List<SoftwareFile>> fileMap = new HashMap<String, List<SoftwareFile>>();
			HashMap<String, List<ComponentDetail>> libsFileMap = new HashMap<String, List<ComponentDetail>>();
			if (CollectionUtils.isNotEmpty(softList) && CollectionUtils.isNotEmpty(softfileList)) {
				for (Software soft : softList) {
					List<SoftwareFile> softfiles = new ArrayList<SoftwareFile>();
					for (SoftwareFile softfile : softfileList) {
						if (soft.getId().equals(softfile.getSoftwareId())) {
							softfiles.add(softfile);
						}
					}
					fileMap.put(soft.getId(), softfiles);
				}
			}

			// 将BSP文件数据进行分组，将数据分别保存，便于组装数据
			HashMap<String, List<BSPFile>> fileMaps = new HashMap<String, List<BSPFile>>();
			if (CollectionUtils.isNotEmpty(bspList) && CollectionUtils.isNotEmpty(bspfileList)) {
				for (BSP bsp : bspList) {
					List<BSPFile> bspfiles = new ArrayList<BSPFile>();
					for (BSPFile bspfile : bspfileList) {
						if (bsp.getId().equals(bspfile.getBspId())) {
							bspfiles.add(bspfile);
						}
					}
					fileMaps.put(bsp.getId(), bspfiles);
				}
			}

			gjkPlatforms.addAll(this.getPlatformTrees());
			if (CollectionUtils.isNotEmpty(softdetailList)) {
				for (SoftwareDetail softwareDetail : softdetailList) {
					Software software = this.getSoftware(softwareDetail.getSoftwareId(), softList);
					gjkPlatforms.addAll(this.getPlatforms(softwareDetail, software, fileMap));
				}
			}
			// 将bsp数据组装成 平台库可挂接的数据
			if (CollectionUtils.isNotEmpty(bspdetailList)) {
				for (BSPDetail bspDetail : bspdetailList) {
					BSP bsp = this.getBSP(bspDetail.getBspId(), bspList);
					gjkPlatforms.addAll(this.getBSPs(bspDetail, bsp, fileMaps));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gjkPlatforms;
	}

	/**
	 * 为每个平台组装对应的软件框架库树
	 * 
	 * @param softwareDetail
	 * @param software
	 * @param fileMap
	 * @return
	 */
	private List<GjkPlatform> getPlatforms(SoftwareDetail softwareDetail, Software software,
			HashMap<String, List<SoftwareFile>> fileMap) {
		List<GjkPlatform> gjkPlatforms = new ArrayList<GjkPlatform>();
		if ("2".equals(software.getApplyState())) {
			GjkPlatform gjkPlatform = new GjkPlatform();
			// 组装第一层 软件框架库
			String firstId = IdGenerate.uuid();
			gjkPlatform.setPlatformId(firstId);
			gjkPlatform.setParentId("software" + softwareDetail.getPlatformId());
			gjkPlatform.setName(String.valueOf(software.getVersion()));
			gjkPlatforms.add(gjkPlatform);
			// 解析文件夹
//			File file = new File(libsPath + File.separator + "gjk/software/admin/" + File.separator
//					+ String.valueOf(software.getVersion()));
			File file = new File(libsPath + File.separator + software.getFilePath());
			if (file.isDirectory()) {
				File[] childFileList = file.listFiles();
				for (File childFile : childFileList) {
					addGjkPlatformTree(gjkPlatforms, firstId, childFile);
				}
			}
		}
		return gjkPlatforms;
	}

	/**
	 * 为每个平台组装对应的bsp
	 * 
	 * @param softwareDetail
	 * @param software
	 * @param fileMap
	 * @return
	 */
	private List<GjkPlatform> getBSPs(BSPDetail softwareDetail, BSP software, HashMap<String, List<BSPFile>> fileMap) {

		List<GjkPlatform> gjkPlatforms = new ArrayList<GjkPlatform>();
		if ("2".equals(software.getApplyState())) {
			GjkPlatform gjkPlatform = new GjkPlatform();
			// 组装第一层 软件框架库
			String firstId = IdGenerate.uuid();
			gjkPlatform.setPlatformId(firstId);
			gjkPlatform.setParentId("BSP" + softwareDetail.getPlatformId());
			gjkPlatform.setName(String.valueOf(software.getVersion()));
			gjkPlatforms.add(gjkPlatform);
			// 解析文件夹
			File file = new File(
					libsPath + File.separator +  software.getFilePath());
			if (file.isDirectory()) {
				File[] childFileList = file.listFiles();
				for (File childFile : childFileList) {
					addGjkPlatformTree(gjkPlatforms, firstId, childFile);
				}
			}
		}
		return gjkPlatforms;
	}

	/**
	 * 判断文件夹是否已存在
	 * 
	 * @param name         文件夹名称
	 * @param gjkPlatforms
	 * @return
	 */
	private GjkPlatform isExist(String name, List<GjkPlatform> gjkPlatforms) {
		GjkPlatform platform = null;
		if (CollectionUtils.isNotEmpty(gjkPlatforms)) {
			// 如果已存在同名文件夹，返回此文件夹信息
			for (GjkPlatform gjkPlatform : gjkPlatforms) {
				if (name.equals(gjkPlatform.getName())) {
					platform = gjkPlatform;
					break;
				}
			}
		}

		return platform;
	}

	private GjkAlgorithm isExists(String name, List<GjkAlgorithm> gjkAlgorithms) {
		GjkAlgorithm platform = null;
		if (CollectionUtils.isNotEmpty(gjkAlgorithms)) {
			// 如果已存在同名文件夹，返回此文件夹信息
			for (GjkAlgorithm gjkAlgorithm : gjkAlgorithms) {
				if (name.equals(gjkAlgorithm.getName())) {
					platform = gjkAlgorithm;
					break;
				}
			}
		}

		return platform;
	}

	private GjkTest isExistss(String name, List<GjkTest> gjkTests) {
		GjkTest platform = null;
		if (CollectionUtils.isNotEmpty(gjkTests)) {
			// 如果已存在同名文件夹，返回此文件夹信息
			for (GjkTest gjkTest : gjkTests) {
				if (name.equals(gjkTest.getName())) {
					platform = gjkTest;
					break;
				}
			}
		}

		return platform;
	}

	/**
	 * 根据ID得到软件框架库主表信息
	 * 
	 * @param id
	 * @param softList
	 * @return
	 */
	private Software getSoftware(String id, List<Software> softList) {
		Software software = null;

		for (Software s : softList) {
			if (s.getId().equals(id)) {
				software = s;
				break;
			}
		}
		return software;
	}

	/**
	 * 根据ID得到bsp主表信息
	 * 
	 * @param id
	 * @param bspList
	 * @return
	 */
	private BSP getBSP(String id, List<BSP> bspList) {
		BSP bsp = null;

		for (BSP s : bspList) {
			if (s.getId().equals(id)) {
				bsp = s;
				break;
			}
		}
		return bsp;
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

	@Override
	public List<BSP> getBSP() {
		return baseMapper.getBSP();
	}

	@Override
	public List<BSPDetail> getBSPPlatform() {
		return baseMapper.getBSPPlatform();
	}

	@Override
	public List<BSPFile> getBSPFile() {
		return baseMapper.getBSPFile();
	}

	/**
	 * 组装算法树
	 * 
	 * @return
	 */
	public List<GjkAlgorithm> getAlgorithmTree() {

		List<GjkAlgorithm> gjkAlgorithms = new ArrayList<GjkAlgorithm>();
		// 分组查询，查出构建类型id，便利构建类型id
//		List<String> compIds = baseMapper.getCompIdsGroupCompId();
		List<String> compIds = feginComponentService.getCompIdsGroupCompId();
		compIds.stream().forEach(compId -> {

			// 定义一个去重集合
			Set<String> parentIds = Sets.newHashSet();
			// 定义一个父级id的map
			Map<String, String> uuidMap = Maps.newHashMap();
			// 通过构件类型id获取所有的同一类型的构件对象，遍历
//			List<Component> Components = baseMapper.getCompByCompId(compId);
			List<Component> Components = feginComponentService.getCompByCompId(compId);
			Components.stream().forEach(component -> {
				// 根据构件id获取构件（算法文件）详情信息
//				ComponentDetail componentDetail = baseMapper.getCompDetailByComponentId(component.getId(), "算法文件");
				ComponentDetail componentDetail = feginComponentService.getCompDetailByComponentId(component.getId(), "算法文件");
				// 找到平台id下的“构件库”id
				if (ObjectUtils.isNotEmpty(componentDetail)) {

					String parentId = "component" + componentDetail.getLibsId();
					// 获取到去重集合的长度
					int size = parentIds.size();
					// 把“构件库”id 添加到去重集合里
					parentIds.add(parentId);
					// 判断是否为新的算法
					if (size < parentIds.size()) {
						String compIdUUID = IdGenerate.uuid();
						// 判断存放父级id的map是否存在
						if (!uuidMap.containsKey(parentId)) {
							uuidMap.put(parentId, compIdUUID);
						} 
						// 设置构件编号节点
						GjkAlgorithm gjkAlgorithm = new GjkAlgorithm();
						gjkAlgorithm.setAlgorithmId(compIdUUID);
						gjkAlgorithm.setName(compId);
						gjkAlgorithm.setParentId(parentId);
						gjkAlgorithms.add(gjkAlgorithm);
					}
					// 设置构件名_版本号节点
					GjkAlgorithm detail = new GjkAlgorithm();
					String detailUUID = IdGenerate.uuid();
					detail.setAlgorithmId(detailUUID);
					detail.setParentId(uuidMap.get(parentId));
					detail.setName(component.getCompName() + "_" + component.getVersion());
					gjkAlgorithms.add(detail);
					// 读取文件，生成文件节点
					File file = new File(libsPath + componentDetail.getFilePath() + File.separator + "算法文件");
					if (file.isDirectory()) {
						File[] childFileList = file.listFiles();
						for (File childFile : childFileList) {
							addGjkAlgorithmTree(gjkAlgorithms, detailUUID, childFile);
						}
					}
				}
			});
		});
		return gjkAlgorithms;
	}

	/**
	 * 递归生成文件树的vo并保存到List<CompDetailVO>中
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addGjkAlgorithmTree(List<GjkAlgorithm> tree, String parentId, File file) {
		String fileId = IdGenerate.uuid();
		tree.add(new GjkAlgorithm(fileId, file.getName(),
				file.getParentFile().getAbsolutePath() + File.separator + file.getName(), parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addGjkAlgorithmTree(tree, fileId, childFile);
			}
		}
	}

	/**
	 * 组装测试树
	 * 
	 * @return
	 */
	public List<GjkTest> getTestTree() {

		List<GjkTest> gjkTests = new ArrayList<GjkTest>();
		// 分组查询，查出构建类型id，便利构建类型id
//		List<String> compIds = baseMapper.getCompIdsGroupCompId();
		List<String> compIds = feginComponentService.getCompIdsGroupCompId();
		compIds.stream().forEach(compId -> {
			// 定义一个去重集合
			Set<String> parentIds = Sets.newHashSet();
			// 定义一个父级id的map
			Map<String, String> uuidMap = Maps.newHashMap();
			// 通过构件类型id获取所有的同一类型的构件对象，遍历
//			List<Component> Components = baseMapper.getCompByCompId(compId);
			List<Component> Components = feginComponentService.getCompByCompId(compId);
			Components.stream().forEach(component -> {
				// 根据构件id获取构件（测试文件）详情信息
//				ComponentDetail componentDetail = baseMapper.getCompDetailByComponentId(component.getId(), "测试文件");
				ComponentDetail componentDetail = feginComponentService.getCompDetailByComponentId(component.getId(), "测试文件");
				// 找到测试id下的“构件库”id
				if (ObjectUtils.isNotEmpty(componentDetail)) {
					String parentId = "component" + componentDetail.getLibsId();
					// 获取到去重集合的长度
					int size = parentIds.size();
					// 把“构件库”id 添加到去重集合里
					parentIds.add(parentId);
					// 判断是否为新的测试
					if (size < parentIds.size()) {
						String compIdUUID = IdGenerate.uuid();
						// 判断存放父级id的map是否存在
						if (!uuidMap.containsKey(parentId)) {
							uuidMap.put(parentId, compIdUUID);
						}
						// 设置构件编号节点
						GjkTest gjkTest = new GjkTest();
						gjkTest.setTestId(compIdUUID);
						gjkTest.setName(compId);
						gjkTest.setParentId(parentId);
						gjkTests.add(gjkTest);
					}
					// 设置构件名_版本号节点
					GjkTest detail = new GjkTest();
					String detailUUID = IdGenerate.uuid();
					detail.setTestId(detailUUID);
					detail.setParentId(uuidMap.get(parentId));
					detail.setName(component.getCompName() + "_" + component.getVersion());
					gjkTests.add(detail);
					// 读取文件，生成文件节点
					File file = new File(libsPath + componentDetail.getFilePath() + File.separator + "测试文件");
					if (file.isDirectory()) {
						File[] childFileList = file.listFiles();
						for (File childFile : childFileList) {
							addGjkTestTree(gjkTests, detailUUID, childFile);
						}
					}
				}
			});
		});
		return gjkTests;
	}

	/**
	 * 递归生成文件树的vo并保存到List<GjkTest>中
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addGjkTestTree(List<GjkTest> tree, String parentId, File file) {
		String fileId = IdGenerate.uuid();
		tree.add(new GjkTest(fileId, file.getName(), file.getParentFile().getAbsolutePath(), parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addGjkTestTree(tree, fileId, childFile);
			}
		}
	}

	/**
	 * 组装平台树的构建建模里的文件夹树
	 * 
	 * @return
	 */
	public List<GjkPlatform> getPlatformTrees() {
		List<GjkPlatform> gjkPlatforms = new ArrayList<GjkPlatform>();
		// 分组查询，查出构建类型id，便利构建类型id
//		List<String> compIds = baseMapper.getCompIdsGroupCompId();
		List<String> compIds = feginComponentService.getCompIdsGroupCompId();
		if(CollectionUtils.isNotEmpty(compIds)) {
		compIds.stream().forEach(compId -> {
			// 定义一个去重集合
			Set<String> parentIds = Sets.newHashSet();
			// 定义一个父级id的map
			Map<String, String> uuidMap = Maps.newHashMap();
			// 通过构件类型id获取所有的同一类型的构件对象，遍历
//			List<Component> Components = baseMapper.getCompByCompId(compId);
			List<Component> Components = feginComponentService.getCompByCompId(compId);
			Components.stream().forEach(component -> {
				// 根据构件id获取构件（平台文件）详情信息
//				ComponentDetail componentDetail = baseMapper.getCompDetailByComponentId(component.getId(), "平台文件");
				ComponentDetail componentDetail = feginComponentService.getCompDetailByComponentId(component.getId(), "平台文件");
				// 找到平台id下的“构件库”id
				if (ObjectUtils.isNotEmpty(componentDetail)) {
					String parentId = "component" + componentDetail.getLibsId();
					// 获取到去重集合的长度
					int size = parentIds.size();
					// 把“构件库”id 添加到去重集合里
					parentIds.add(parentId);
					// 判断是否为新的平台
					if (size < parentIds.size()) {
						String compIdUUID = IdGenerate.uuid();
						// 判断存放父级id的map是否存在
						if (!uuidMap.containsKey(parentId)) {
							uuidMap.put(parentId, compIdUUID);
						}
						// 设置构件编号节点
						GjkPlatform gjkPlatform = new GjkPlatform();
						gjkPlatform.setPlatformId(compIdUUID);
						gjkPlatform.setName(compId);
						gjkPlatform.setParentId(parentId);
						gjkPlatforms.add(gjkPlatform);
					}
					// 设置构件名_版本号节点
					GjkPlatform detail = new GjkPlatform();
					String detailUUID = IdGenerate.uuid();
					detail.setPlatformId(detailUUID);
					detail.setParentId(uuidMap.get(parentId));
					detail.setName(component.getCompName() + "_" + component.getVersion());
					gjkPlatforms.add(detail);
					// 读取文件，生成文件节点
					File file = new File(libsPath + componentDetail.getFilePath() + File.separator + "平台文件");
					if (file.isDirectory()) {
						File[] childFileList = file.listFiles();
						for (File childFile : childFileList) {
							addGjkPlatformTree(gjkPlatforms, detailUUID, childFile);
						}
					}
				}
			});
		});
	}
		return gjkPlatforms;
	}

	/**
	 * 递归生成文件树的vo并保存到List<GjkTest>中
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addGjkPlatformTree(List<GjkPlatform> tree, String parentId, File file) {
		String fileId = IdGenerate.uuid();
		tree.add(new GjkPlatform(fileId, file.getName(),
				file.getParentFile().getAbsolutePath() + File.separator + file.getName(), parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addGjkPlatformTree(tree, fileId, childFile);
			}
		}
	}

	/**
	 * @Title: 获取所属平台
	 * @Description: 查询所属平台用于下拉框显示
	 * @Author xiaohe
	 * @DateTime 2019年9月28日 下午4:33:08
	 * @return
	 * @see com.inforbus.gjk.admin.service.GjkPlatformService#selectOwnPlatform()
	 */
	@Override
	public List<GjkPlatform> selectOwnPlatform() {
		return baseMapper.selectList(Wrappers.<GjkPlatform>query().lambda().eq(GjkPlatform::getParentId, "-1"));
	}
}
