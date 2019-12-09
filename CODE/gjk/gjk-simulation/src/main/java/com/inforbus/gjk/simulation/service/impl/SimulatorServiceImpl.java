package com.inforbus.gjk.simulation.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.simulation.dto.SimulationDTO;
import com.inforbus.gjk.simulation.global.Global;
import com.inforbus.gjk.simulation.service.SimulatorService;
import com.inforbus.gjk.simulation.task.SimulatorQueue;
import com.inforbus.gjk.simulation.task.Subscriber;
import com.inforbus.gjk.simulation.task.SubscriberThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SimulatorServiceImpl implements SimulatorService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${simulation.jedisHost}")
    private String host;

    @Value("${simulation.queueSize}")
    private String queueSize;

    @Override
    public boolean startSimulator(String username, List<String> componentLinks, String filePath) {
        //启用客户线程，传入参数
        String channelName = username+"SimulatorChannel";
        new SimulatorQueue(host,channelName).start();
        //暂时模拟数据接口
        Subscriber subscriber = new Subscriber();
        subscriber.setQueueSize(Integer.parseInt(queueSize));
        subscriber.setUsername(username);
        subscriber.setRedisTemplate(redisTemplate);
        new SubscriberThread(subscriber,channelName,host).start();
        return true;
    }

    @Override
    public boolean stopSimulator(String username) {
        Map<String, Thread> simulatorQueues = Global.USERS_SIMULATOR_QUEUES.get(username);
        simulatorQueues.keySet().stream().forEach(mapKey->{
            SimulatorQueue simulatorQueue = (SimulatorQueue)simulatorQueues.get(mapKey);
            simulatorQueue.close();
        });
        Global.USERS_SIMULATOR_QUEUES.remove(username);
        return true;
    }

    @Override
    public Map<String,Object> getData(String username, SimulationDTO simulationDTO) {
        String key = "Simulator:" + username+ ":" + simulationDTO.getSymbol();
        String str = redisTemplate.opsForList().rightPop(key);
        //调用客户接口解析
        JSONObject objects = JSONUtil.parseObj(str);
        List<Object> data = (List<Object>) objects.get("data");
        if(data==null){
            return null;
        }
        List<String> xaxisData = Lists.newArrayList();
        List<String> yaxisData = Lists.newArrayList();
        for (int i = 0; i < data.size(); i++) {
            xaxisData.add((i+1)+"");
            yaxisData.add(data.get(i)+"");
        }
        Map<String,Object> res = Maps.newHashMap();
        res.put("xaxisData",xaxisData);
        res.put("yaxisData",yaxisData);
        res.put("select",simulationDTO.getSelect());
        return res;
    }

    @Override
    public List<Map<String, Object>> stop() {
        return null;
    }

    @Override
    public List<String> getDataSource(SimulationDTO simulationDto) {
        //调用用户接口
        return getSymbol(simulationDto.getFlowFilePath(),simulationDto.getStartId()+"|"+simulationDto.getEndId());
    }

    public List<String> getSymbol(String filePath, String componentIds){

        List<String> list = Lists.newArrayList();
        list.add("link1");
        return list;

    }
}
