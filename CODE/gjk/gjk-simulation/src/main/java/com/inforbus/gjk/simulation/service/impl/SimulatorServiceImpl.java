package com.inforbus.gjk.simulation.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.simulation.dto.SimulationDTO;
import com.inforbus.gjk.simulation.core.Global;
import com.inforbus.gjk.simulation.dto.SimulationTableDataDTO;
import com.inforbus.gjk.simulation.service.SimulatorService;
import com.inforbus.gjk.simulation.task.SimulatorQueue;
import com.inforbus.gjk.simulation.task.Subscriber;
import com.inforbus.gjk.simulation.task.SubscriberThread;
import flowModel.MoniRecvDataThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 仿真业务实现
 */
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
        //定义通道名
        String channelName = username+"SimulatorChannel";
        //队列初始状态
        redisTemplate.opsForValue().set(username+":initState","1");
        //初始化监听对象
        Subscriber subscriber = new Subscriber();
        subscriber.setQueueSize(Integer.parseInt(queueSize));
        subscriber.setUsername(username);
        subscriber.setRedisTemplate(redisTemplate);
        //启动监听线程
        new SubscriberThread(subscriber,channelName,host).start();
        //启用客户线程，传入参数
        MoniRecvDataThread startMoniRecvDataThread = ExternalIOTransUtils.startMoniRecvDataThread(host, channelName, componentLinks, filePath, JGitUtil.getCTRL_TAB_FILE_PATH());
        Global.USERS_SIMULATOR_THREAD.put(username,startMoniRecvDataThread);
        //放入全局变量
        return true;
    }

    @Override
    public boolean stopSimulator(String username) {
        SimulatorQueue simulatorQueues = (SimulatorQueue) Global.USERS_SIMULATOR_THREAD.get(username);
        if(simulatorQueues == null){
            return false;
        }
        simulatorQueues.close();
        Global.USERS_SIMULATOR_THREAD.remove(username);
        Set<String> keys = redisTemplate.keys("Simulator:" + username + ":*");
        for (String key : keys) {
            redisTemplate.delete(key);
        }
        Set<String> keys1 = redisTemplate.keys(username + ":initState:*");
        keys1.stream().forEach(key->{
            redisTemplate.delete(key);
        });
        return true;
    }

    @Override
    public Map<String,Object> getData(String username, SimulationDTO simulationDTO) {
        //修改初始化状态
        redisTemplate.opsForValue().set(username+":initState:"+simulationDTO.getSymbol(),"1");
        String key = "Simulator:" + username+ ":" + simulationDTO.getSymbol();
        String str = redisTemplate.opsForList().rightPop(key);
        //调用客户接口解析
        JSONObject objects = JSONUtil.parseObj(str);
//        List<Object> data = (List<Object>) objects.get("Data");
//        if(data==null){
//            return null;
//        }
        String[] tabNames = ((String) objects.get("tabNameList")).split("\\|");
        List<SimulationTableDataDTO> tableData = Lists.newArrayList();
        Map<String, String> tabNameDataMap = (Map) objects.get(tabNames[0]);
        tabNameDataMap.keySet().stream().forEach(tableDataKey->{
            SimulationTableDataDTO simulationTableDTO = new SimulationTableDataDTO();
            simulationTableDTO.setName(tableDataKey);
            String[] split = tabNameDataMap.get(tableDataKey).split("\\|");
            simulationTableDTO.setValue(split.length > 0?split[0]:"");
            simulationTableDTO.setRemark(split.length == 2 ? split[1]: "");
            tableData.add(simulationTableDTO);
        });
        Map<String, String> tabNameDataMap1 = (Map)objects.get(tabNames[1]);
        tabNameDataMap1.keySet().stream().forEach(tableDataKey->{
            SimulationTableDataDTO simulationTableDTO = new SimulationTableDataDTO();
            simulationTableDTO.setName(tableDataKey);
            String[] split = tabNameDataMap1.get(tableDataKey).split("\\|");
            simulationTableDTO.setValue(split.length > 0?split[0]:"");
            simulationTableDTO.setRemark(split.length == 2 ? split[1]: "");
            tableData.add(simulationTableDTO);
        });
        Map<String, Object> map = Maps.newHashMap();
        map.put("tableData", tableData);
//        List<String> xaxisData = Lists.newArrayList();
//        List<String> yaxisData = Lists.newArrayList();
//        for (int i = 0; i < data.size(); i++) {
//            xaxisData.add((i+1)+"");
//            yaxisData.add(data.get(i)+"");
//        }
//        Map<String,Object> res = Maps.newHashMap();
//        res.put("xaxisData",xaxisData);
//        res.put("yaxisData",yaxisData);
//        res.put("select",simulationDTO.getSelect());
        return map;
    }

    @Override
    public Map<String, List<String>> stop(String username,List<String> symbols) {
        ListOperations<String, String> operations = redisTemplate.opsForList();
        Map<String, List<String>> symbolFrameSelect = Maps.newHashMap();
        List<String> selectData = Lists.newArrayList();
        for (String symbol : symbols) {
            String key = "Simulator:"+username+":"+symbol;
            Long size = operations.size(key);
            List<String> range = operations.range(key, 0, size - 1);
            for (String s : range) {
                JSONObject dataMap = JSONUtil.parseObj(s);
                selectData.add(dataMap.get("frameNum")+"");
            }
            symbolFrameSelect.put(symbol,selectData);
        }
        return symbolFrameSelect;
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
