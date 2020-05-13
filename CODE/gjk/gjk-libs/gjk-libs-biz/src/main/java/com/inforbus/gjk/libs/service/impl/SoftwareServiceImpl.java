
package com.inforbus.gjk.libs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.DataCenterConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.libs.api.dto.SelectFolderDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareDTO;
import com.inforbus.gjk.libs.api.dto.SoftwareTree;
import com.inforbus.gjk.libs.api.entity.Software;
import com.inforbus.gjk.libs.api.entity.SoftwareDetail;
import com.inforbus.gjk.libs.api.entity.SoftwareFile;
import com.inforbus.gjk.libs.api.feign.RemoteStructServiceFeign;
import com.inforbus.gjk.libs.api.util.SoftwareTreeUtil;
import com.inforbus.gjk.libs.mapper.SoftwareMapper;
import com.inforbus.gjk.libs.service.SoftwareService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
@Service("softwareService")
public class SoftwareServiceImpl extends ServiceImpl<SoftwareMapper, Software> implements SoftwareService {

	@Value("${git.local.path}")
	private String gitFilePath;

	@Autowired
	private RemoteStructServiceFeign rdcService;

	/**
	 * 软件框架库表简单分页查询
	 * 
	 * @param software 软件框架库表
	 * @return
	 */
	@Override
	public IPage<Software> getSoftwarePage(Page<Software> page, Software software) {
		return baseMapper.getSoftwarePage(page, software);
	}

	/**
	 * 保存软件框架库信息
	 */
	@Override
	public Software saveSoftware(Software software) {
		// 删除状态为0
		software.setDelFlag(CommonConstants.STATUS_NORMAL);
		software.setId(IdGenerate.uuid());
		software.setCreateTime(LocalDateTime.now());
		software.setUpdateTime(LocalDateTime.now());
		baseMapper.saveSoftware(software);
		return software;
	}

	/**
	 * 保存软件框架库详细信息
	 */
	@Override
	public SoftwareDetail saveSoftwareDetail(SoftwareDetail softwareDetail) {
		baseMapper.saveSoftwareDetail(softwareDetail);
		return softwareDetail;
	}

	/**
	 * 保存软件框架库文件路径子表信息
	 */
	@Override
	public SoftwareFile saveSoftwareFile(SoftwareFile softwareFile) {
		baseMapper.saveSoftwareFile(softwareFile);
		return softwareFile;
	}

	/**
	 * 根据选择的平台文件对版本进行赋值
	 */
	@Override
	public Software setVersionSize() {
		return baseMapper.setVersionSize();
	}

	/**
	 * 文件夹的解析存储
	 * 
	 * @param selectFolderDTO
	 * @return
	 */
	@Override
	public void selectFolder(List<SelectFolderDTO> selectFolderDTO) {
		for (SelectFolderDTO selectFolder : selectFolderDTO) {
		}
	}

	/**
	 * 获取软件框架的树
	 * 
	 * @return
	 */
	@Override
	public List<SoftwareTree> getSoftwareTree() {
		List<Software> list = this.list(Wrappers.<Software>query().lambda());
		// 树节点为-1
		List<SoftwareTree> trees = SoftwareTreeUtil.buildTree(list, CommonConstants.STATUS_TREE);
		return trees;
	}

	/**
	 * 获取软件框架库表封装后的DTO分页查询
	 * 
	 * @param page
	 * @param software
	 * @return
	 */
	@Override
	public IPage<SoftwareDTO> getSoftwareDTOPage(Page<Software> softwarePage, Software software) {
		List<Software> softwares = getSoftwarePage(softwarePage, software).getRecords();
		List<SoftwareDTO> softwareDTOs = new ArrayList<>();
		for (Software soft : softwares) {
			SoftwareDTO dto = new SoftwareDTO(soft);
			softwareDTOs.add(dto);
		}
		Page<SoftwareDTO> softwareDTOPage = new Page<SoftwareDTO>(softwarePage.getCurrent(), softwarePage.getSize(),
				softwarePage.getTotal());
		softwareDTOPage.setRecords(softwareDTOs);
		return softwareDTOPage;
	}

	/**
	 * 根据id级联删除software文件表
	 * 
	 * @param softwareId
	 */
	@Override
	public void removeSoftwareFile(String softwareId) {
		baseMapper.removeSoftwareFile(softwareId);
	}

	/**
	 * 根据id级联删除software详细表
	 * 
	 * @param softwareId
	 */
	@Override
	public void removeSoftwareDetail(String softwareId) {
		baseMapper.removeSoftwareDetail(softwareId);
	}

	/**
	 * 根据ID获取软件框架的树
	 * 
	 * @return
	 */
	@Override
	public List<SoftwareTree> getTreeById(String id) {
		List<SoftwareTree> tree = Lists.newArrayList();
		Software software = this.getById(id);
		// 树节点为-1
		tree.add(new SoftwareTree(software, CommonConstants.STATUS_TREE));

		File file = new File(gitFilePath + software.getFilePath());
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addSoftwareTree(tree, childFile, id);
			}
		} else {
			tree.add(new SoftwareTree(file, IdGenerate.uuid(), id));
		}
		return tree;
	}

	/**
	 * 递归生成文件树
	 * 
	 * @param tree
	 * @param parentId
	 * @param file
	 */
	private void addSoftwareTree(List<SoftwareTree> tree, File file, String parentId) {
		String fileId = IdGenerate.uuid();
		tree.add(new SoftwareTree(file, fileId, parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addSoftwareTree(tree, childFile, fileId);
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param files 文件流
	 * @param versionDisc 版本号
	 * @param userName 用户名
	 * @return
	 */
	@Override
	public String uploadFiles(MultipartFile files, String versionDisc, String userName) {
		String path = gitFilePath;
		String res = path + ",";
		String ss = (path +  DataCenterConstants.SOFTWARE_FILEPATH + userName + File.separator + versionDisc + ".0" + File.separator)
				.replaceAll("\\\\", "/");
		File file = new File(ss);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			// 调用解压方法：zipPath 压缩文件地址（全路径） descDir 指定目录（全路径）
			rdcService.uploadDecompression(files, ss);
		} catch (Exception e) {
			res += "文件 " + files.getOriginalFilename() + " 上传失败\n";
			e.printStackTrace();
		}
		res += "文件 " + files.getOriginalFilename() + " 上传成功\n";
		return res;
	}

	/**
	 * 通过id删除本地文件夹 
	 * @param filePath
	 */
	@Override
	public void deleteFolderByFilePath(String filePath) {
		String folderPath = gitFilePath + filePath;
		rdcService.delAllFile(folderPath);
	}

}
