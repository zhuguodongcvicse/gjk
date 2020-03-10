
package com.inforbus.gjk.dataCenter.api.feign.fallback;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.api.feign.RemoteFtpService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RemoteFtpServiceFallbackImpl
 * @Description: 
 * @Author xiaohe
 * @DateTime 2020年2月26日  
 */
@Slf4j
@Component
public class RemoteFtpServiceFallbackImpl implements RemoteFtpService {
	@Setter
	private Throwable cause;

	/**
	 * @Title: fileUpload
	 * @Description:
	 * @Author xiaohe
	 * @DateTime 2020年2月26日
	 * @param file
	 * @param romte
	 * @return 
	 * @see com.inforbus.gjk.dataCenter.api.feign.RemoteFtpService#fileUpload(org.springframework.web.multipart.MultipartFile, java.lang.String) 
	 */
	@Override
	public R<T> fileUpload(MultipartFile file, String romte) {
		log.error("feign 上传文件失败:{}", file.getOriginalFilename(), cause);
		return null;
	}
}
