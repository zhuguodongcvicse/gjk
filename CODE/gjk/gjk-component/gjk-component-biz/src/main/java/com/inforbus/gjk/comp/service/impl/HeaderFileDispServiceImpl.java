package com.inforbus.gjk.comp.service.impl;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.common.core.util.HeaderFileAndStructUtils;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.core.util.vo.HeaderFileTransVO;
import com.inforbus.gjk.common.core.util.vo.ParamTreeVO;
import com.inforbus.gjk.comp.api.feign.RemoteDataCenterService;
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
	@Value("${git.local.path}")
	private String serverPath;
	@Autowired
	private RemoteDataCenterService rdcService;

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
	public R<?> parseHeaderFile(String headerFile) {
		R ret = new R<>();
		R<HeaderFileTransVO> rdc = rdcService.getHeader(headerFile);
		if (rdc.getCode() == CommonConstants.SUCCESS) {
			ret.setData(HeaderFileAndStructUtils.headFileToShow(rdc.getData()));
		}
		ret.setCode(rdc.getCode());
		ret.setMsg(rdc.getMsg());
		return ret;
//		return new R(HeaderFileAndStructUtils.headFileToShow(parsefilePath(headerFile)));
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
	public R<?> parsePerformanceTable(String excelPath) {
		return rdcService.getPerformanceTable(excelPath);
//		return ExternalIOTransUtils.parsePerformanceTable(parsefilePath(excelPath));
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
