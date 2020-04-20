package com.inforbus.gjk.dataCenter.controller;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.config.ExtractApplicationContext;
import com.inforbus.gjk.dataCenter.task.Task;
import com.inforbus.gjk.dataCenter.task.impl.CompileTask;
import com.inforbus.gjk.dataCenter.taskThread.TaskThread;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


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
        R r = new R();
        try {
            String path = map.get("path");
            String fileName = map.get("fileName");
            String platformType = map.get("platformType");
            String token = map.get("token");
            String vsPath = map.get("vsPath");
            String wbPath = map.get("wbPath");
            String linuxPath = map.get("linuxPath");
            String ip = map.get("ip");
            String username = map.get("username");
            String password = map.get("password");
            String dPath = map.get("dPath");
            // 从spring容器中获取到编译任务类对象
            CompileTask compileTask = (CompileTask) ExtractApplicationContext.getBean("compileTask");
            compileTask.setFileName(fileName);
            compileTask.setPath1(path);
            compileTask.setPlatformType(platformType);
            compileTask.setToken(token);
            compileTask.setVsPath(vsPath);
            compileTask.setWbPath(wbPath);
            compileTask.setLinuxPath(linuxPath);
            compileTask.setIp(ip);
            compileTask.setUsername(username);
            compileTask.setPassword(password);
            compileTask.setDPath(dPath);

            ConcurrentLinkedQueue<Task> compileQueue = taskThread.getCompileQueue();
            //获取到排队人数
            int count = compileQueue.size();
            //使用另一条线程执行编译功能
            taskThread.addTask(compileTask);
            String str = "正在编译...请稍候";
            if (count > 0) {
                str = "前面有" + count + "个组件工程在编译......请等候一会";
            }
            r.setMsg(str);
        } catch (Exception e) {
            r.setCode(CommonConstants.FAIL);
            r.setMsg("编译功能发生异常 ：" + e.getMessage());
        }
        return r;
    }
}
