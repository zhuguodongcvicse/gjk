package com.inforbus.gjk.compile.taskThread;

import org.springframework.amqp.core.AmqpTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * wang
 * 流线程类,防止线程阻塞
 * */
public class StreamManage extends Thread {
    private InputStream inputStream;//输入流
    private String chartSet;
    private AmqpTemplate rabbitmqTemplate;//rabbitmq对象
    private String fileName;//项目名称
    private String token;//用户登录的token

    public StreamManage(InputStream inputStream, String chartSet, AmqpTemplate rabbitmqTemplate, String fileName, String token) {
        this.inputStream = inputStream;
        this.chartSet = chartSet;
        this.rabbitmqTemplate = rabbitmqTemplate;
        this.fileName = fileName;
        this.token = token;
    }

    public void run() {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            inputStreamReader = new InputStreamReader(this.inputStream, this.chartSet);//GBK编码格式的输入流
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null) {
                this.rabbitmqTemplate.convertAndSend(this.token, this.fileName + "===@@@===\n" + line);//把执行cmd控制的信息推送到mq中
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();//关流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();//关流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
