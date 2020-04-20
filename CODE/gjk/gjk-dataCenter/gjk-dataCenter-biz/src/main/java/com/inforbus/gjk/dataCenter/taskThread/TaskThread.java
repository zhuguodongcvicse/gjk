package com.inforbus.gjk.dataCenter.taskThread;

import com.inforbus.gjk.dataCenter.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TaskThread
 *
 * @author wang
 * @date 2020/4/8
 * @Description 任务线程类，实现编译排队功能
 */
@Component(value = "taskThread")
public class TaskThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(TaskThread.class);

    private ConcurrentLinkedQueue<Task> compileQueue;//非阻塞队列

    private boolean stop = false;//中断，停止标志

    public TaskThread() {
        compileQueue = new ConcurrentLinkedQueue<Task>();
    }

    public void addTask(Task task) {
        compileQueue.add(task);//给队列中添加编译任务
    }

    /**
     * @Author wang
     * @Description: 停止线程方法
     * @Param: []
     * @Return: void
     * @Create: 2020/4/8
     */
    public void stop1() {
        synchronized (this) {
            stop = true;
            compileQueue.notify();
        }
    }

    public ConcurrentLinkedQueue<Task> getCompileQueue() {
        return compileQueue;
    }

    public void setCompileQueue(ConcurrentLinkedQueue<Task> compileQueue) {
        this.compileQueue = compileQueue;
    }

    /**
     * @Author wang
     * @Description: 覆写run方法，使用ConcurrentLinkedQueue阻塞队列对编译请求进行排队
     * @Param: []
     * @Return: void
     * @Create: 2020/4/8
     */
    @Override
    public void run() {
        logger.debug("run方法开始运行，编译排队线程准备就绪...");
        synchronized (compileQueue) {
            while (!Thread.currentThread().isInterrupted() && !stop) {
                Task task = compileQueue.poll();//拿出任务
                if (task != null) {
                    try {
                        task.command();//开始任务
                    } catch (Exception e) {
                        logger.error("编译失败,请检查相关配置");
                    }
                } else {
                    try {
                        compileQueue.wait(1000);
                    } catch (InterruptedException e) {
                        logger.error("线程被中断或进入阻塞状态");
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        logger.debug("run方法运行结束");
    }
}
