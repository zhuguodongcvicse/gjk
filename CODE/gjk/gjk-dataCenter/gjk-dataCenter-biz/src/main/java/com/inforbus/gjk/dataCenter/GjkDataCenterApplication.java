package com.inforbus.gjk.dataCenter;

import com.inforbus.gjk.common.security.annotation.EnableGjkFeignClients;
import com.inforbus.gjk.common.security.annotation.EnableGjkResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dong
 * @date 2019年05月21日 数据中心系统
 */
@EnableGjkResourceServer
@EnableGjkFeignClients
@SpringCloudApplication
@EnableDiscoveryClient
public class GjkDataCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(GjkDataCenterApplication.class, args);
	}

}
