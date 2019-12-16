package com.inforbus.gjk.simulation.task;

import redis.clients.jedis.Jedis;

/**
 * 监听redis通道线程
 */
public class SubscriberThread extends Thread {

    private Subscriber subscriber;

    private String channelName;

    private String redisHost;

    public SubscriberThread(Subscriber subscriber, String channelName, String redisHost){
        this.subscriber = subscriber;
        this.channelName = channelName;
        this.redisHost = redisHost;
    }

    @Override
    public void run() {
        Jedis jedis = null;
        try {
            jedis = new Jedis(redisHost);
            jedis.subscribe(subscriber, channelName);
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
