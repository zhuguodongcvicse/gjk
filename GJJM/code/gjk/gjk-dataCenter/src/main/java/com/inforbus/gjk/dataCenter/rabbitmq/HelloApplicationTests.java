package com.inforbus.gjk.dataCenter.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.inforbus.gjk.dataCenter.GjkDataCenterApplication;

/**
 * Created by asus1 on 2018/6/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GjkDataCenterApplication.class)
public class HelloApplicationTests {

    @Autowired
    private Sender sender;

    @Test
    public void hello(){
        sender.send();
    }
}