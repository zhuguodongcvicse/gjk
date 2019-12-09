package com.inforbus.gjk.simulation;

import com.inforbus.gjk.common.security.annotation.EnableGjkFeignClients;
import com.inforbus.gjk.common.security.annotation.EnableGjkResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author dong
 * @date 2019年05月21日 数据中心系统
 */
@EnableGjkResourceServer
@EnableGjkFeignClients
@SpringCloudApplication
public class GjkSimulationApplication {
	public static ConfigurableApplicationContext applicationContext;
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(GjkSimulationApplication.class, args);
	}

}
