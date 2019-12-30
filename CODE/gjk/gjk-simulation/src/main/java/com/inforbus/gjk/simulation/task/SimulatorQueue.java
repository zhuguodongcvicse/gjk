package com.inforbus.gjk.simulation.task;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;

import java.util.*;

public class SimulatorQueue extends Thread {

    private boolean flag;

    private String channelName;

    private String jedisHost;

    private volatile Integer i;


    public SimulatorQueue(String jedisHost, String channelName){
        this.jedisHost = jedisHost;
        this.channelName = channelName;
        flag= true;
        i = 0;
    }
    @Override
    public void run() {
    	int i = 1;
        while (flag) {
        	
            //生成数据
            Jedis jedis = new Jedis(jedisHost);
            Map<String, Object> map = Maps.newHashMap();
            map.put("data",getData());
            map.put("frameNum",i++);
            map.put("id",i%2==0?"symbol1":"sumbol2");
            String s = JSONUtil.toJsonStr(map);
            jedis.publish(channelName,s);
            jedis.close();
            i++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        if(stringStringListOperations.size(key) < queueSize){
//            Map<String, List<String>> data = getData(obj);
//            data.keySet().stream().forEach(mapKey->{
//                String s = new String(data.get(mapKey).toString().getBytes());
//                stringStringListOperations.leftPush(key, s);
//            });
//
//        }

    }

    public void close(){
        this.flag = false;
    }
    public List<String> getData(){
        List<String> list = Lists.newArrayList();
            for (int i = 0; i < 50000; i++) {
                Random random = new Random();
                int i1 = random.nextInt(500);
                list.add(i1 + "");
            }
        return list;
    }
}
