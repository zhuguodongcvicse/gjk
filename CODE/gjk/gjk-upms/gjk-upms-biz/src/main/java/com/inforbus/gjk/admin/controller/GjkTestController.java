
package com.inforbus.gjk.admin.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.inforbus.gjk.admin.api.entity.GjkAlgorithm;
import com.inforbus.gjk.admin.api.entity.GjkPlatform;
import com.inforbus.gjk.admin.api.entity.GjkTest;
import com.inforbus.gjk.admin.api.vo.TestVO;
import com.inforbus.gjk.admin.api.vo.TreeUtil;
import com.inforbus.gjk.admin.api.vo.TreeUtilAl;
import com.inforbus.gjk.admin.api.vo.TreeUtilTest;
import com.inforbus.gjk.admin.service.GjkPlatformService;
import com.inforbus.gjk.admin.service.GjkTestService;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author geng_hxian
 * @date 2019/4/17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class GjkTestController {
	private final GjkTestService gjkTestService;
	private final GjkPlatformService gjkPlatformService;

	/**
	 * 返回树形测试库集合
	 *
	 * @return 树形测试库
	 */
	@GetMapping(value = "/tree")
	public R getTree() {
		return new R<>(TreeUtilTest.buildTreess(gjkTestService.list(Wrappers.emptyWrapper()), "-1"));
	}
	
	/**
	 * 返回树形平台库集合
	 *
	 * @return 树形平台库
	 */
	@GetMapping(value = "/trees")
	public R getTrees() {
		List<GjkTest> testList = gjkTestService.list(Wrappers.emptyWrapper());
		List<GjkTest> gjkTests = Lists.newArrayList();
		for (GjkTest gjkTest : testList) {
			GjkTest gjkTest1 = new GjkTest();
			gjkTest1.setParentId(gjkTest.getTestId());
			gjkTest1.setTestId("component"+gjkTest.getTestId());
			gjkTest1.setName("构件库");
			gjkTests.add(gjkTest1);
		}
		testList.addAll(gjkTests);
		List<GjkTest> softwareList = gjkPlatformService.getTestTree();
		if (CollectionUtils.isNotEmpty(testList) && CollectionUtils.isNotEmpty(softwareList)) {
			testList.addAll(softwareList);
		}

		return new R<>(TreeUtilTest.buildTreess(testList, "-1"));
	}

	/**
	 * 返回角色的测试库集合
	 *
	 * @param roleId 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/tree/{roleId}")
	public List getRoleTree() {
		return gjkTestService.getTestByRoleId()
			.stream()
			.map(TestVO::getTestId)
			.collect(Collectors.toList());
	}

	/**
	 * 通过ID查询测试库的详细信息
	 *
	 * @param id 测试库ID
	 * @return 测试库详细信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable String id) {
		return new R<>(gjkTestService.getById(id));
	}

	/**
	 * 新增测试库
	 *
	 * @param gjkTest 测试库信息
	 * @return success/false
	 */
	@SysLog("新增测试库")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_test_add')")
	public R save(@RequestBody GjkTest gjkTest) {
		gjkTest.setTestId(IdGenerate.uuid());
		return new R<>(gjkTestService.save(gjkTest));
	}

	/**
	 * 删除测试库
	 *
	 * @param id 测试ID
	 * @return success/false
	 */
	@SysLog("删除测试库")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_test_del')")
	public R removeById(@PathVariable String id) {
		return gjkTestService.removeTestById(id);
	}

	/**
	 * 更新测试库
	 *
	 * @param gjkTest
	 * @return
	 */
	@SysLog("更新测试库")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_test_edit')")
	public R update(@Valid @RequestBody GjkTest gjkTest) {
		return new R<>(gjkTestService.updateTestById(gjkTest));
	}

	/**
	 * 导出测试库、平台库、算法库
	 * @param request
	 * @param response
	 * @param libs
	 */
	@PostMapping("/createZipFile")
	@PreAuthorize("@pms.hasPermission('sys_test_export')")
	public void createZipFile(HttpServletRequest request, HttpServletResponse response,
							  @RequestBody List<String> libs) {
		try {
			byte[] data = gjkTestService.createZip(libs);

			String zipFileName = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".zip";
			response.reset();
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", zipFileName));
			response.setHeader("FileName", zipFileName);
			response.setHeader("Content-Length", "" + data.length);
			response.setContentType("application/octet-stream; charset=UTF-8");

			IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: uploadReturnUrll
	 * @Description: 单文件上传
	 * @Author xiaohe
	 * @DateTime 2019年5月13日 下午3:41:10
	 * @param ufile
	 * @return
	 */
	@ResponseBody
	@PostMapping(path = "/importLibsZipUpload", consumes = { "multipart/mixed", "multipart/form-data" })
	@PreAuthorize("@pms.hasPermission('sys_test_import')")
	public R importLibsZipUpload(@RequestParam(value = "file", required = false) MultipartFile ufile,
							@RequestParam(value = "importType", required = false) String importType) {
		return new R<>(gjkTestService.analysisZipFile(ufile, importType));
	}

}
