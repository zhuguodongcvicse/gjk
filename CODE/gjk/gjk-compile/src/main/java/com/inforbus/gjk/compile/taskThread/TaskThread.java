package com.inforbus.gjk.compile.taskThread;

import com.inforbus.gjk.compile.task.Task;
import com.inforbus.gjk.compile.task.impl.ComplieTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component(value="taskThread")
public class TaskThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ComplieTask.class);
    private ConcurrentLinkedQueue<Task> complieQueue;//非阻塞队列
    private boolean stop = false;

    public TaskThread() {
        complieQueue = new ConcurrentLinkedQueue<Task>();
    }

    public void addTask(Task task) {
        complieQueue.add(task);//给队列中添加编译任务
    }

    public void stop1() {
        synchronized (this) {
            stop = true;
            complieQueue.notify();
        }
    }

    public ConcurrentLinkedQueue<Task> getComplieQueue() {
        return complieQueue;
    }

    public void setComplieQueue(ConcurrentLinkedQueue<Task> complieQueue) {
        this.complieQueue = complieQueue;
    }

    @Override
    public void run() {
        synchronized (complieQueue) {
            while (!stop) {
                Task task = complieQueue.poll();
                if (task != null) {
                    try {
                        task.Command();
                    } catch (Exception e) {
                        logger.error("编译失败,请检查相关配置");
                        e.printStackTrace();
                    }
                }else {

                    try {
                        complieQueue.wait(1000);
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
