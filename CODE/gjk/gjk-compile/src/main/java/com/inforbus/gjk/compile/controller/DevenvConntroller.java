package com.inforbus.gjk.compile.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.inforbus.gjk.compile.config.ExtractApplicationContext;
import com.inforbus.gjk.compile.task.Task;
import com.inforbus.gjk.compile.task.impl.CompileTask;
import com.inforbus.gjk.compile.taskThread.TaskThread;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inforbus.gjk.common.core.util.R;
import lombok.AllArgsConstructor;

import javax.annotation.Resource;


/**
 * DevenvConntroller
 *
 * @author wang
 * @date 2019/10/20
 * @Description 编译功能控制器，实现编译任务排队
 */
@RestController
@AllArgsConstructor
@RequestMapping("/devenv")
public class DevenvConntroller {

    @Resource(name = "taskThread")
    private TaskThread taskThread;
    //任务线程类

    /**
     * @Author wang
     * @Description: 编译功能调用层
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/8
     */
    @PutMapping(value = "/Command")
    public R command(@RequestBody Map<String, String> map) {
        String path = map.get("path");
        String fileName = map.get("fileName");
        String platformType = map.get("platformType");
        String token = map.get("token");
        // 从spring容器中获取到编译任务类对象
        CompileTask compileTask = (CompileTask) ExtractApplicationContext.getBean("compileTask");
        compileTask.setFileName(fileName);
        compileTask.setPath1(path);
        compileTask.setPlatformType(platformType);
        compileTask.setToken(token);
        ConcurrentLinkedQueue<Task> compileQueue = taskThread.getCompileQueue();
        //获取到排队人数
        int count = compileQueue.size();
        //使用另一条线程执行编译功能
        taskThread.addTask(compileTask);
        String str = "正在编译...请稍候";
        if (count > 0) {
            str = "前面有" + count + "个组件工程在编译......请等候一会";
        }
        return new R<>(str);
    }
}
