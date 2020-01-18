package com.inforbus.gjk.simulation.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.jgit.JGitUtil;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.simulation.dto.SimulationDTO;
import com.inforbus.gjk.simulation.core.Global;
import com.inforbus.gjk.simulation.dto.SimulationTableDataDTO;
import com.inforbus.gjk.simulation.service.ManagerServiceImpl;
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
 * @Auth l_tf
 */
@Service
public class SimulatorServiceImpl implements SimulatorService {

    /**
     * redis连接工具模板
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    ManagerServiceImpl managerServiceImpl;
    /**
     * reidsIP
     */
    @Value("${simulation.jedisHost}")
    private String host;

    /**
     * 队列长度
     */
    @Value("${simulation.queueSize}")
    private String queueSize;
    // gitlu路径
    @Value("${git.local.path}")
    private String gitDetailPath;

    // 集成代码生成结果存放路径
    @Value("${gjk.pro.process.generateCodeResult}")
    private String generateCodeResult;
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
        //放入全局变量
        Global.USERS_SIMULATOR_THREAD.put(username,startMoniRecvDataThread);
        return true;
    }

    @Override
    public boolean stopSimulator(String username) {
        MoniRecvDataThread moniRecvDataThread = (MoniRecvDataThread) Global.USERS_SIMULATOR_THREAD.get(username);
        if(moniRecvDataThread == null){
            return false;
        }
        ExternalIOTransUtils.stopMoniRecvDataThread(moniRecvDataThread);
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
    public Map<String,Object> getData(String username, String projectId,SimulationDTO simulationDTO) {
        //修改队列初始化状态
        redisTemplate.opsForValue().set(username+":initState:"+simulationDTO.getSymbol(),"1");
        //拼接队列key
        String key = "Simulator:" + username+ ":" + simulationDTO.getSymbol();
        //从队列右侧弹出一个元素
        String str = redisTemplate.opsForList().rightPop(key);

        JSONObject objects = JSONUtil.parseObj(str);

        //获取结构体表格数据
        String[] tabNames = ((String) objects.get("tabNameList")).split("\\|");
        List<SimulationTableDataDTO> tableData = Lists.newArrayList();
        //解析表格数据，得到表格对象集合
        tableData.addAll(forEachGetSimulationTableData((Map) objects.get(tabNames[0])));
        tableData.addAll(forEachGetSimulationTableData((Map) objects.get(tabNames[1])));

        //调用客户接口解析 接口待确认 把这组数据扔到接口拿返回值就OK


//        ProjectFile processFile = getOne(
//     Wrappers.<ProjectFile>query().lambda().eq(ProjectFile::getId, this.getById(proDetailId).getParentId()));
        String FilePath = managerServiceImpl.getprocessFile(projectId);
        String packinfoFileName = gitDetailPath + FilePath + generateCodeResult + "/packinfo.xml";

        Map<String, Object> dataMap = Maps.newHashMap();
        //表格数据
        dataMap.put("tableData", tableData);
        return dataMap;
    }

    @Override
    public Map<String, List<String>> suspend(String username,List<String> symbols) {
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
    public List<String> getDataSource(String username, SimulationDTO simulationDto) {

        MoniRecvDataThread moniRecvDataThread = (MoniRecvDataThread) Global.USERS_SIMULATOR_THREAD.get(username);
        //调用客户接口 获取数据源
        return moniRecvDataThread.getArrowIdList(simulationDto.getStartId() + "|" + simulationDto.getEndId());
    }

    /**
     * 解析表格Map
     * @param tableDataMap 表格数据Map 【key: name, value: 值|备注】
     * @return
     */
    public List<SimulationTableDataDTO> forEachGetSimulationTableData(Map<String, String> tableDataMap){
        List<SimulationTableDataDTO> simulationTableDataDTOS = Lists.newArrayList();
        SimulationTableDataDTO simulationTableDataDTO = null;
        for (String key : tableDataMap.keySet()) {
            simulationTableDataDTO = new SimulationTableDataDTO();
            simulationTableDataDTO.setName(key);
            //根据“|”截取值和备注
            String[] valueAndRemake = tableDataMap.get(key).split("\\|");
            simulationTableDataDTO.setValue(valueAndRemake.length > 0 ? valueAndRemake[0] : "");
            //如果截取数组长度超过等于2
            simulationTableDataDTO.setRemark(valueAndRemake.length == 2 ? valueAndRemake[1] : "");
            simulationTableDataDTOS.add(simulationTableDataDTO);
        }
        return simulationTableDataDTOS;
    }
}
