package com.inforbus.gjk.dataCenter.taskThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * StreamManage2
 *
 * @author wang
 * @date 2020/5/18
 * @Description dos日志打印出现避免出错阻塞线程
 */
public class StreamManage2 extends Thread {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    InputStream inputStream;
    String type;
    public StreamManage2(InputStream inputStream,String type) {
        this.inputStream = inputStream;
        this.type = type;
    }
    public void run () {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (type.equals("Error")) {
                    logger.error(line);
                } else {
                    logger.debug(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
