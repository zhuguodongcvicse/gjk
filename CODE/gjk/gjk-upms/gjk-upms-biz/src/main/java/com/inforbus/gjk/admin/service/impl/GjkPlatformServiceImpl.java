
package com.inforbus.gjk.admin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.R;

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
	public List<GjkPlatform> getPlatformTree() {
		List<GjkPlatform> gjkPlatforms = null;

		try {
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

			// 将软件框架库数据组装成 平台库可挂接的数据
			gjkPlatforms = new ArrayList<GjkPlatform>();

			if (CollectionUtils.isNotEmpty(softdetailList)) {
				gjkPlatforms.addAll(this.getPlatformTrees());
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
		GjkPlatform gjkPlatform = new GjkPlatform();
		// 组装第一层 软件框架库
		String firstId = IdGenerate.uuid();
		gjkPlatform.setPlatformId(firstId);
		gjkPlatform.setParentId(softwareDetail.getPlatformId());
		gjkPlatform.setName("软件框架库");
		gjkPlatforms.add(gjkPlatform);

		// 组装第二层 软件框架库 版本
		gjkPlatform = new GjkPlatform();
		String secondId = IdGenerate.uuid();
		gjkPlatform.setPlatformId(secondId);
		gjkPlatform.setParentId(firstId);
		gjkPlatform.setName(String.valueOf(software.getVersion()));
		gjkPlatforms.add(gjkPlatform);

		// 解析文件夹
		List<SoftwareFile> softwareFiles = fileMap.get(softwareDetail.getSoftwareId());
		for (SoftwareFile sofware : softwareFiles) {
			// 如果是-1，无文件夹，直接组装数据；否则，解析文件夹
			if (sofware.getFileName().indexOf("/") == -1) {
				gjkPlatform = new GjkPlatform();
				gjkPlatform.setPlatformId(IdGenerate.uuid());
				gjkPlatform.setParentId(secondId);
				gjkPlatform.setName(sofware.getFileName());
				gjkPlatform.setPermission(libsPath + File.separator + sofware.getFilePath());
				gjkPlatforms.add(gjkPlatform);
			} else {
				// 分解文件夹
				String[] strs = sofware.getFileName().split("/");
				String parentId = secondId;
				for (String str : strs) {
					// 如果文件夹实体类已经建立，则修改 parentID
					GjkPlatform isExist = this.isExist(str, gjkPlatforms);
					if (null != isExist) {
						parentId = isExist.getPlatformId();
					} else {
						gjkPlatform = new GjkPlatform();
						gjkPlatform.setPlatformId(IdGenerate.uuid());
						gjkPlatform.setParentId(parentId);
						// 重置parentID
						parentId = gjkPlatform.getPlatformId();
						gjkPlatform.setName(str);
						gjkPlatform.setPermission(libsPath + File.separator + sofware.getFilePath());
						gjkPlatforms.add(gjkPlatform);
					}
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
		GjkPlatform gjkPlatform = new GjkPlatform();
		// 组装第一层 软件框架库
		String firstId = IdGenerate.uuid();
		gjkPlatform.setPlatformId(firstId);
		gjkPlatform.setParentId(softwareDetail.getPlatformId());
		gjkPlatform.setName("BSP");
		gjkPlatforms.add(gjkPlatform);

		// 组装第二层 软件框架库 版本
		gjkPlatform = new GjkPlatform();
		String secondId = IdGenerate.uuid();
		gjkPlatform.setPlatformId(secondId);
		gjkPlatform.setParentId(firstId);
		gjkPlatform.setName(String.valueOf(software.getVersion()));
		gjkPlatforms.add(gjkPlatform);

		// 解析文件夹
		List<BSPFile> softwareFiles = fileMap.get(softwareDetail.getBspId());
		for (BSPFile sofware : softwareFiles) {
			// 如果是-1，无文件夹，直接组装数据；否则，解析文件夹
			if (sofware.getFileName().indexOf("/") == -1) {
				gjkPlatform = new GjkPlatform();
				gjkPlatform.setPlatformId(IdGenerate.uuid());
				gjkPlatform.setParentId(secondId);
				gjkPlatform.setName(sofware.getFileName());
				gjkPlatform.setPermission(libsPath + File.separator + sofware.getFilePath());
				gjkPlatforms.add(gjkPlatform);
			} else {
				// 分解文件夹
				String[] strs = sofware.getFileName().split("/");
				String parentId = secondId;
				for (String str : strs) {
					// 如果文件夹实体类已经建立，则修改 parentID
					GjkPlatform isExist = this.isExist(str, gjkPlatforms);
					if (null != isExist) {
						parentId = isExist.getPlatformId();
					} else {
						gjkPlatform = new GjkPlatform();
						gjkPlatform.setPlatformId(IdGenerate.uuid());
						gjkPlatform.setParentId(parentId);
						// 重置parentID
						parentId = gjkPlatform.getPlatformId();
						gjkPlatform.setName(str);
						gjkPlatform.setPermission(libsPath + File.separator + sofware.getFilePath());
						gjkPlatforms.add(gjkPlatform);
					}
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
	 * 组装测试树
	 * 
	 * @return
	 */
	public List<GjkAlgorithm> getAlgorithmTree() {
		List<GjkAlgorithm> gjkAlgorithms = new ArrayList<GjkAlgorithm>();
		List<ComponentDetail> commInfo = baseMapper.getLibsInfo();
		String firstId = "";
		String parId = "";
		String libsFilePath = "";
		for (ComponentDetail commInfos : commInfo) {
//			List<ComponentDetail> libsFile = new ArrayList<>();
			GjkAlgorithm gjkAlgorithm = new GjkAlgorithm();
			if (commInfos.getFileName().equals("算法文件")) {
				firstId = commInfos.getId();
				String sssId = IdGenerate.uuid();
				gjkAlgorithm.setAlgorithmId(commInfos.getId());
				gjkAlgorithm.setParentId(commInfos.getLibsId());
				gjkAlgorithm.setName(commInfos.getFileName());
				gjkAlgorithms.add(gjkAlgorithm);
				// 展示构件名
				Component compName = baseMapper.getCompNameById(commInfos.getId());
				if (compName != null) {

					gjkAlgorithm = new GjkAlgorithm();
					gjkAlgorithm.setAlgorithmId(sssId);
					gjkAlgorithm.setParentId(firstId);
					// 重置parentID
					parId = gjkAlgorithm.getAlgorithmId();
					gjkAlgorithm.setName(compName.getCompName());
					gjkAlgorithms.add(gjkAlgorithm);

					// 解析文件夹
					libsFilePath = commInfos.getFilePath();
					File file = new File(libsPath + libsFilePath + File.separator + "算法文件");
					if (file.isDirectory()) {
						File[] childFileList = file.listFiles();
						for (File childFile : childFileList) {
							addGjkAlgorithmTree(gjkAlgorithms, parId, childFile);
						}
					}
				}
			}
		}
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
		List<ComponentDetail> commInfo = baseMapper.getLibsInfo();
		String firstId = "";
		String parId = "";
		String libsFilePath = "";
		for (ComponentDetail commInfos : commInfo) {
			GjkTest gjkTest = new GjkTest();
			if (commInfos.getFileName().equals("测试文件")) {
				firstId = commInfos.getId();
				String sssId = IdGenerate.uuid();
				gjkTest.setTestId(commInfos.getId());
				gjkTest.setParentId(commInfos.getLibsId());
				gjkTest.setName(commInfos.getFileName());
				gjkTests.add(gjkTest);
				// 展示构件名
				Component compName = baseMapper.getCompNameById(commInfos.getId());
				if (compName != null) {
					gjkTest = new GjkTest();
					gjkTest.setTestId(sssId);
					gjkTest.setParentId(firstId);
					// 重置parentID
					parId = gjkTest.getTestId();
					gjkTest.setName(compName.getCompName());
					gjkTests.add(gjkTest);

					// 解析文件夹
					libsFilePath = commInfos.getFilePath();
					File file = new File(libsPath + libsFilePath + File.separator + "测试文件");
					if (file.isDirectory()) {
						File[] childFileList = file.listFiles();
						for (File childFile : childFileList) {
							addGjkTestTree(gjkTests, parId, childFile);
						}

					}
				}
			}

		}
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
		List<ComponentDetail> commInfo = baseMapper.getLibsInfo();
		String firstId = "";
		String parId = "";
		String libsFilePath = "";
		for (ComponentDetail commInfos : commInfo) {
			GjkPlatform gjkPlatform = new GjkPlatform();
			if (commInfos.getFileName().equals("平台文件")) {
				firstId = commInfos.getId();
				String sssId = IdGenerate.uuid();
				gjkPlatform.setPlatformId(commInfos.getId());
				gjkPlatform.setParentId(commInfos.getLibsId());
				gjkPlatform.setName(commInfos.getFileName());
				gjkPlatforms.add(gjkPlatform);
				// 展示构件名
				Component compName = baseMapper.getCompNameById(commInfos.getId());
				if (compName != null) {
					gjkPlatform = new GjkPlatform();
					gjkPlatform.setPlatformId(sssId);
					gjkPlatform.setParentId(firstId);
					// 重置parentID
					parId = gjkPlatform.getPlatformId();
					gjkPlatform.setName(compName.getCompName());
					gjkPlatforms.add(gjkPlatform);

					// 解析文件夹
					libsFilePath = commInfos.getFilePath();
					File file = new File(libsPath + libsFilePath + File.separator + "平台文件");
					if (file.isDirectory()) {
						File[] childFileList = file.listFiles();
						for (File childFile : childFileList) {
							addGjkPlatformTree(gjkPlatforms, parId, childFile);
						}

					}
				}

//				for (ComponentDetail commInfoss : commInfo) {
//					if (commInfos.getId().equals(commInfoss.getParaentId())) {
//						libsFile.add(commInfoss);
//					}
//				}
			}
			// 解析文件夹

//			for (ComponentDetail libsFiles : libsFile) {
//				// 如果是-1，无文件夹，直接组装数据；否则，解析文件夹
//				if (libsFiles.getFileName().indexOf("/") == -1) {
//					gjkPlatform = new GjkPlatform();
//					gjkPlatform.setPlatformId(IdGenerate.uuid());
//					gjkPlatform.setParentId(parId);
//					gjkPlatform.setName(libsFiles.getFileName());
//					gjkPlatform.setPermission(libsFiles.getFilePath() + libsFiles.getFileName());
//					gjkPlatforms.add(gjkPlatform);
//				} else {
//					// 分解文件夹
//					String[] strs = libsFiles.getFileName().split("/");
//					String parentId = parId;
//					for (String str : strs) {
//						// 如果文件夹实体类已经建立，则修改 parentID
//						GjkPlatform isExist = this.isExist(str, gjkPlatforms);
//						if (null != isExist) {
//							parentId = isExist.getPlatformId();
//						} else {
//							gjkPlatform = new GjkPlatform();
//							gjkPlatform.setPlatformId(IdGenerate.uuid());
//							gjkPlatform.setParentId(parentId);
//							// 重置parentID
//							parentId = gjkPlatform.getPlatformId();
//							gjkPlatform.setName(str);
//							gjkPlatform.setPermission(libsFiles.getFilePath() + libsFiles.getFileName());
//							gjkPlatforms.add(gjkPlatform);
//						}
//					}
//				}
//			}
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

}
