package com.inforbus.gjk.libs.api.feign.fallback;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.api.feign.RemoteStructServiceFeign;

import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RemoteStructServiceFeignFallbackImpl implements RemoteStructServiceFeign {
	@Setter
	private Throwable cause;

	@Override
	public R<Map<String, List<String>>> parseStruct(String filePath) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心 服务异常，解析文件出错 ;" + cause.getMessage());
		log.error("数据中心 服务异常，解析文件{}出错 ", filePath, cause);
		return ret;
	}

	@Override
	public R uploadDecompression(MultipartFile file, String localPath) {
		R ret = new R();
		ret.setException(cause);
		ret.setMsg("数据中心 服务异常，远程文件解压出错 ;" + cause.getMessage());
		log.error("数据中心 服务异常，远程文件{}解压出错 路径为{}", file.getOriginalFilename(), localPath, cause);
		return ret;
	}

	@Override
	public R<Boolean> delAllFile(String sourcePath) {
		R<Boolean> ret = new R<Boolean>();
		ret.setException(cause);
		ret.setMsg("数据中心 服务异常，删除构件框架的文件 ;" + cause.getMessage());
		log.error("数据中心 服务异常，删除构件框架的文件路径为{}", sourcePath, cause);
		return ret;
	}

	@Override
	public Response downloadStreamFiles(MultipartFile[] ufile, String[] fileTarget, String maps) {
		R<Boolean> ret = new R<Boolean>();
		ret.setException(cause);
		ret.setMsg("数据中心 服务异常，删除构件框架的文件 ;" + cause.getMessage());
		log.error("数据中心 服务异常，删除构件框架的文件路径为{}", cause);
		return null;
	}

}
