package com.inforbus.gjk.compile.taskThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * StreamManage
 *
 * @author wang
 * @date 2020/4/8
 * @Description 流线程类, 防止线程阻塞
 */
public class StreamManage extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(StreamManage.class);

    private InputStream inputStream;//输入流

    private String chartSet;//字符编码格式

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

    /**
     * @Author wang
     * @Description: 覆写run方法，实现不同流交叉读取控制台打印数据
     * @Param: []
     * @Return: void
     * @Create: 2020/4/8
     */
    @Override
    public void run() {
        logger.debug("run方法开始运行");
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
            logger.error("IO异常");
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();//关流
                } catch (IOException e) {
                    logger.error("IO关闭异常");
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();//关流
                } catch (IOException e) {
                    logger.error("IO关闭异常");
                }
            }
        }
        logger.debug("run方法结束运行");
    }
}
