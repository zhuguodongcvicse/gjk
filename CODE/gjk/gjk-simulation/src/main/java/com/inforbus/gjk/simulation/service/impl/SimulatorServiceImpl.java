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
//        //启用客户线程，传入参数
//        MoniRecvDataThread startMoniRecvDataThread = ExternalIOTransUtils.startMoniRecvDataThread(host, channelName, componentLinks, filePath, JGitUtil.getCTRL_TAB_FILE_PATH());
//        Global.USERS_SIMULATOR_THREAD.put(username,startMoniRecvDataThread);
        //模拟数据
        SimulatorQueue simulatorQueue = new SimulatorQueue(host, channelName);
        simulatorQueue.start();
        Global.USERS_SIMULATOR_THREAD.put(username, simulatorQueue);
        //放入全局变量
        return true;
    }

    @Override
    public boolean stopSimulator(String username) {
    	SimulatorQueue simulatorQueues = (SimulatorQueue) Global.USERS_SIMULATOR_THREAD.get(username);
        if(simulatorQueues == null){
            return false;
        }
//        ExternalIOTransUtils.stopMoniRecvDataThread(simulatorQueues);
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
        List<Object> data = (List<Object>) objects.get("data");
        if(data==null || data.isEmpty()){
            return null;
        }
//        String[] tabNames = ((String) objects.get("tabNameList")).split("\\|");
        List<SimulationTableDataDTO> tableData = Lists.newArrayList();
//        tableData.addAll(tableDataMaptoList((Map)objects.get(tabNames[0])));
//        tableData.addAll(tableDataMaptoList((Map)objects.get(tabNames[1])));
        //模拟结构体数据
        tableData.addAll(tableDataMaptoList(null));
        Map<String, Object> req = Maps.newHashMap();
        req.put("tableData", tableData);
        
        List<String> xaxisData = Lists.newArrayList();
        List<String> yaxisData = Lists.newArrayList();
        for (int i = 0; i < data.size(); i++) {
            xaxisData.add((i+1)+"");
            yaxisData.add(data.get(i)+"");
        }
        req.put("xaxisData", xaxisData);
        req.put("yaxisData", yaxisData);
        req.put("select",simulationDTO.getSelect());
        return req;
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
    public List<String> getDataSource(String username,SimulationDTO simulationDto) {
//    	MoniRecvDataThread thread = (MoniRecvDataThread)Global.USERS_SIMULATOR_THREAD.get(username);
//        //调用用户接口
//        return thread.getArrowIdList(simulationDto.getStartId()+"|"+simulationDto.getEndId());
    	
    	return this.getSymbol(simulationDto.getFlowFilePath(), simulationDto.getStartId()+"|"+simulationDto.getEndId());
    }

    public List<SimulationTableDataDTO> tableDataMaptoList(Map<String, String> tabNameDataMap){
    	List<SimulationTableDataDTO> simulationDTOs = Lists.newArrayList();
    	SimulationTableDataDTO simulationTableDTO = null;
//    	for (String key : tabNameDataMap.keySet()) {
//    		simulationTableDTO = new SimulationTableDataDTO();
//          simulationTableDTO.setName(key);
//          String[] split = tabNameDataMap.get(key).split("\\|");
//          simulationTableDTO.setValue(split.length > 0?split[0]:"");
//          simulationTableDTO.setRemark(split.length == 2 ? split[1]: "");
//          simulationDTOs.add(simulationTableDTO);
//		}
    	
    	String [] strs = {"iC","fC","dC","strC","stC"};
    	//模拟结构体数据
    	for (int i = 0; i < 5; i++) {
    		simulationTableDTO = new SimulationTableDataDTO();
          simulationTableDTO.setName(strs[i]);
          simulationTableDTO.setValue((int)(Math.random()*100)+"");
          simulationTableDTO.setRemark("无");
          simulationDTOs.add(simulationTableDTO);
		}
		return simulationDTOs;
    }
    
    public List<String> getSymbol(String filePath, String componentIds){

        List<String> list = Lists.newArrayList();
        list.add("symbol1");
        list.add("symbol2");
        return list;

    }
}
