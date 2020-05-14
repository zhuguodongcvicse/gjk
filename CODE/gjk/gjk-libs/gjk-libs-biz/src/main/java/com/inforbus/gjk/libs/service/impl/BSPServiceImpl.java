
package com.inforbus.gjk.libs.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.constant.DataCenterConstants;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.libs.api.dto.BSPDTO;
import com.inforbus.gjk.libs.api.dto.BSPTree;
import com.inforbus.gjk.libs.api.dto.SelectFolderDTO;
import com.inforbus.gjk.libs.api.entity.BSP;
import com.inforbus.gjk.libs.api.entity.BSPDetail;
import com.inforbus.gjk.libs.api.entity.BSPFile;
import com.inforbus.gjk.libs.api.feign.RemoteStructServiceFeign;
import com.inforbus.gjk.libs.api.util.BSPTreeUtil;
import com.inforbus.gjk.libs.mapper.BSPMapper;
import com.inforbus.gjk.libs.service.BSPService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 软件框架库表
 *
 * @author pig code generator
 * @date 2019-06-14 09:46:48
 */
@Service("BSPService")
public class BSPServiceImpl extends ServiceImpl<BSPMapper, BSP> implements BSPService {

	@Value("${git.local.path}")
	private String gitFilePath;
	@Autowired
	private RemoteStructServiceFeign rdcService;

	/**
	 * 软件框架库表简单分页查询
	 * 
	 * // * @param BSP 软件框架库表
	 * 
	 * @return
	 */
	@Override
	public IPage<BSP> getBSPPage(Page<BSP> page, @RequestBody BSP bsp) {
		return baseMapper.getBSPPage(page, bsp);
	}

	/**
	 * 保存软件框架库信息
	 */
	@Override
	public BSP saveBSP(BSP bsp) {
		// 删除状态为0
		bsp.setDelFlag(CommonConstants.STATUS_NORMAL);
		bsp.setId(IdGenerate.uuid());
		bsp.setCreateTime(LocalDateTime.now());
		bsp.setUpdateTime(LocalDateTime.now());
		baseMapper.saveBSP(bsp);
		return bsp;
	}

	/**
	 * 保存软件框架库详细信息
	 */
	@Override
	public BSPDetail saveBSPDetail(BSPDetail BSPDetail) {
		baseMapper.saveBSPDetail(BSPDetail);
		return BSPDetail;
	}

	/**
	 * 保存软件框架库文件路径子表信息
	 */
	@Override
	public BSPFile saveBSPFile(BSPFile bspFile) {
		baseMapper.saveBSPFile(bspFile);
		return bspFile;
	}

	/**
	 * 根据选择的平台文件对版本进行赋值
	 */
	@Override
	public BSP setVersionSize() {
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
			System.out.println("selectFolder:" + selectFolder);

		}
	}

	/**
	 * 获取BSP库表封装后的DTO分页查询
	 * 
	 * @param page
	 * @param software
	 * @return
	 */

	@Override
	public IPage<BSPDTO> getBSPDTOPage(Page<BSP> bspPage, BSP bsp) {
		IPage<BSP> iPage = getBSPPage(bspPage, bsp);
		List<BSP> bsps = iPage.getRecords();

		List<BSPDTO> bspDTOs = new ArrayList<>();
		for (BSP soft : bsps) {
			BSPDTO dto = new BSPDTO(soft);
			bspDTOs.add(dto);

		}
		Page<BSPDTO> bspDTOPage = new Page<BSPDTO>(bspPage.getCurrent(), bspPage.getSize());
		bspDTOPage.setRecords(bspDTOs);
		bspDTOPage.setPages(iPage.getPages());
		bspDTOPage.setTotal(iPage.getTotal());
		return bspDTOPage;
	}

	/**
	 * 获取BSP的树
	 * 
	 * @return
	 */
	@Override
	public List<BSPTree> getBSPTree() {
		List<BSP> list = this.list(Wrappers.<BSP>query().lambda());
		// 树节点为-1
		List<BSPTree> trees = BSPTreeUtil.buildTree(list, CommonConstants.STATUS_TREE);
		return trees;
	}

	/**
	 * 根据id级联删除bsp文件表
	 */
	@Override
	public void removeBspFile(String bspId) {
		baseMapper.removeBspFile(bspId);
	}

	/**
	 * 根据id级联删除bsp详细表
	 */
	@Override
	public void removeBspDetail(String bspId) {
		baseMapper.removeBspDetail(bspId);
	}

	/**
	 * 根据ID获取BSP的树
	 * 
	 * @return
	 */
	@Override
	public List<BSPTree> getTreeById(String id) {
		List<BSPTree> tree = Lists.newArrayList();
		BSP bsp = this.getById(id);
		// 树节点为-1
		tree.add(new BSPTree(bsp, CommonConstants.STATUS_TREE));

		File file = new File(gitFilePath + bsp.getFilePath());
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addBSPTree(tree, childFile, id);
			}
		} else {
			tree.add(new BSPTree(file, IdGenerate.uuid(), id));
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
	private void addBSPTree(List<BSPTree> tree, File file, String parentId) {
		String fileId = IdGenerate.uuid();
		tree.add(new BSPTree(file, fileId, parentId));
		if (file.isDirectory()) {
			File[] childFileList = file.listFiles();
			for (File childFile : childFileList) {
				addBSPTree(tree, childFile, fileId);
			}
		}
	}

	/**
	 * 文件上传
	 * 
	 * @param files       文件夹流
	 * @param versionDisc 版本号 * @param userName 用户名
	 * @return
	 */
	@Override
	public String uploadFiles(MultipartFile files, String versionDisc, String userName) {
		String path = gitFilePath;
		// 文件上传的状态：上传成功！！！
		String res = "上传成功！！！";
		// DataCenterConstants.BSP_FILEPATH : gjk/bsp/
		String ss = (path + DataCenterConstants.BSP_FILEPATH + userName + File.separator + versionDisc + ".0"
				+ File.separator).replaceAll("\\\\", "/");
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
	 * 通过文件路径删除文件夹
	 */
	@Override
	public void deleteFolderByFilePath(String filePath) {
		String folderPath = gitFilePath + filePath;
		rdcService.delAllFile(folderPath);
	}

}
