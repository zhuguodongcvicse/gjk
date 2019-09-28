package com.inforbus.gjk.comp.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.entity.StringRef;
import com.inforbus.gjk.common.core.entity.XmlEntity;
import com.inforbus.gjk.common.core.idgen.IdGenerate;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.HeaderFileAndStructUtils;
import com.inforbus.gjk.common.core.util.vo.HeaderFileShowVO;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;
import com.inforbus.gjk.comp.api.entity.Component;
import com.inforbus.gjk.comp.mapper.ComponentMapper;
import com.inforbus.gjk.comp.service.ComponentService;
import com.inforbus.gjk.comp.service.HeaderFileDispService;

/**
 * 头文件解析服务实现类
 * 
 * @author hu
 *
 */
@Service("headerFileDispService")
public class HeaderFileDispServiceImpl implements HeaderFileDispService {

	@Autowired
	protected HeaderFileDispService headerFileDispService;
	private static String serverPath = JGitUtil.getLOCAL_REPO_PATH();

	/**
	 * 头文件解析
	 * 
	 * @parseHeaderFile
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月1日 下午4:22:33
	 * @param headerFile
	 * @return
	 * @see com.inforbus.gjk.comp.service.HeaderFileDispService#parseHeaderFile(java.lang.String)
	 */
	@Override
	public HeaderFileShowVO parseHeaderFile(String headerFile) {
		return HeaderFileAndStructUtils.headFileToShow(parsefilePath(headerFile));
	}

	/**
	 * 结构体解析
	 * 
	 * @parseStruct
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年5月7日 下午8:19:58
	 * @param filePath
	 * @return
	 * @see com.inforbus.gjk.comp.service.StructsDispService#parseStruct(java.lang.String)
	 */
	@Override
	public Map<String, ParamTreeVO> parseStruct(String filePath) {
		return null;
	}

	/**
	 * 解析性能测试表
	 * 
	 * @Title: parsePerformanceTable
	 * @Description:
	 * @Author CVICSE-COMMON
	 * @DateTime 2019年6月3日 上午10:44:28
	 * @param excelPath
	 * @return
	 */
	@Override
	public Map<Integer, Float> parsePerformanceTable(String excelPath) {
		return ExternalIOTransUtils.parsePerformanceTable(parsefilePath(excelPath));
	}
	/**
	 * @Title: parsefilePath
	 * @Description: 添加路径前缀 （GJK）
	 * @Author xiaohe
	 * @DateTime 2019年7月17日 下午2:02:27
	 * @param filePath
	 * @return 
	 */
	private String parsefilePath(String filePath) {
		String url = new String(filePath);
//		String url = new String(serverPath + File.separator + filePath);
		if (StringUtils.isNotEmpty(url)) {
			File file = new File(url);
			if (!file.exists()) {
				return null;
			}
		}
		return url;
	}
}
