package com.inforbus.gjk.compile;

import com.inforbus.gjk.common.security.annotation.EnableGjkFeignClients;
import com.inforbus.gjk.common.security.annotation.EnableGjkResourceServer;
import com.inforbus.gjk.compile.taskThread.TaskThread;
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
public class GjkCompileApplication {
	public static ConfigurableApplicationContext applicationContext;
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(GjkCompileApplication.class, args);
		TaskThread taskThread = (TaskThread) applicationContext.getBean("taskThread");
		taskThread.start();//开启编译线程
	}

}