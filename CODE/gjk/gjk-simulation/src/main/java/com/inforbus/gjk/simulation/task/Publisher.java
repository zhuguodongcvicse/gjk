package com.inforbus.gjk.simulation.task;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Publisher extends Thread{
    private final JedisPool jedisPool;

    public Publisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private boolean flag;
    private String channelName;
    private volatile Integer i;
    private String jedisHost;
    public Publisher(JedisPool jedisPool, String jedisHost, String channelName){
        this.jedisPool = jedisPool;
        this.jedisHost = jedisHost;
        this.channelName = channelName;
        flag= true;
        i = 0;
    }
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public void run() {
        //生成数据
        Jedis jedis = jedisPool.getResource();   //连接池中取出一个连接
        while (true) {
            for(int i= 0;i<=1500;i++){

                Map<String, Object> map = Maps.newHashMap();
                map.put("data",getData());
                map.put("FrameId",i++);
                map.put("id","A");
                map.put("tabNameList",null);
                String s = JSONUtil.toJsonStr(map);
                if (!"quit".equals(s)) {
                    jedis.publish("adminSimulatorChannel", s);   //从 mychannel 的频道上推送消息
                } else {
                    break;
                }
            }
        }

    }
    public void close(){
        this.flag = false;
    }
    public List<String> getData(){
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < 10000; i++) {
            Random random = new Random();
            int i1 = random.nextInt(500);
            list.add(i1 + "");
        }
        return list;
    }
}
