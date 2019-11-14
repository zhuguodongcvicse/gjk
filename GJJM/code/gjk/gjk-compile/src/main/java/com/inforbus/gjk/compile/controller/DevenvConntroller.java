package com.inforbus.gjk.compile.controller;


import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.inforbus.gjk.compile.config.ExtractApplicationContext;
import com.inforbus.gjk.compile.task.Task;
import com.inforbus.gjk.compile.task.impl.ComplieTask;
import com.inforbus.gjk.compile.taskThread.TaskThread;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inforbus.gjk.common.core.util.R;
import lombok.AllArgsConstructor;
import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/devenv")
public class DevenvConntroller {
	//private final DevenvService devenvService;
	//@Autowired
	//private AmqpTemplate rabbitmqTemplate;
	@Resource(name = "taskThread")
	private TaskThread taskThread;//任务线程类

	@PutMapping(value = "/Command")
	public R Command(@RequestBody Map<String, String> map) {
		String path =map.get("path");
		String fileName =map.get("fileName");
		String platformType = map.get("platformType");
		String token = map.get("token");
		ComplieTask complieTask = (ComplieTask) ExtractApplicationContext.getBean("complieTask");//获取到编译任务类对象
		complieTask.setFileName(fileName);
		complieTask.setPath1(path);
		complieTask.setPlatformType(platformType);
		complieTask.setToken(token);
		ConcurrentLinkedQueue<Task> complieQueue = taskThread.getComplieQueue();
		int count = complieQueue.size();//获取到排队人数
		taskThread.addTask(complieTask);//使用另一条线程执行编译功能
		String str = "";
		str = "前面有"+count+"个组件工程在编译......请等候一会";
		return new R<>(str);
	}
}
