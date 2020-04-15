package com.inforbus.gjk.compile.taskThread;

import com.inforbus.gjk.compile.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component(value="taskThread")
public class TaskThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(TaskThread.class);
    private ConcurrentLinkedQueue<Task> compileQueue;//非阻塞队列
    private boolean stop = false;

    public TaskThread() {
        compileQueue = new ConcurrentLinkedQueue<Task>();
    }

    public void addTask(Task task) {
        compileQueue.add(task);//给队列中添加编译任务
    }

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

    @Override
    public void run() {
        synchronized (compileQueue) {
            while (!Thread.currentThread().isInterrupted() && !stop) {
                Task task = compileQueue.poll();//拿出任务
                if (task != null) {
                    try {
                        task.command();//开始任务
                    } catch (Exception e) {
                        logger.error("编译失败,请检查相关配置");
                        e.printStackTrace();
                    }
                }else {
                    try {
                        compileQueue.wait(1000);
                    } catch (InterruptedException e) {
                        logger.error("线程被中断");
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
