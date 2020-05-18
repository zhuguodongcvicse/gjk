package com.inforbus.gjk.simulation.task;


import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * redis监听对象
 */
public class Subscriber extends JedisPubSub {

    private String username;

    private RedisTemplate<String, String> redisTemplate;

    private Integer queueSize;


    public Subscriber() {}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public void onMessage(String channel, String message) {
        //处理message
        Map jsonObject = JSONUtil.parseObj(message);
        //获取标识
        String symbol = (String)jsonObject.get("id");
        //String key = "Simulator:"+username+":"+symbol;
        String key = "Simulator:"+username+":"+symbol;
        //根据标识放redis list队列
        ListOperations<String, String> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        if(size == null || size < queueSize){
            operations.leftPush(key,message);
        }else{
            if(!redisTemplate.hasKey(username+":initState:"+symbol)){
                operations.rightPop(key);
                operations.leftPush(key,message);
            }
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }


}

