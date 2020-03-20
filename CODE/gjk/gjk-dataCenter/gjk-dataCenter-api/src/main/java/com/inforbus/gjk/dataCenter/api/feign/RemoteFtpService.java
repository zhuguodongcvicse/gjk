package com.inforbus.gjk.dataCenter.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.api.feign.factory.RemoteFtpServiceFallbackFactory;

@FeignClient(value = ServiceNameConstants.DATACENDER_SERVICE, fallbackFactory = RemoteFtpServiceFallbackFactory.class)
public interface RemoteFtpService {
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R<?> fileUpload(@RequestPart("file") MultipartFile file, @RequestParam("romte") String romte);
}
