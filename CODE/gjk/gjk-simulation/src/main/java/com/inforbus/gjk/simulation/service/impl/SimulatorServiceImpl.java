package com.inforbus.gjk.simulation.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.inforbus.gjk.admin.api.entity.SysDict;
import com.inforbus.gjk.common.core.util.ExternalIOTransUtils;
import com.inforbus.gjk.simulation.dto.SimulationDTO;
import com.inforbus.gjk.simulation.core.Global;
import com.inforbus.gjk.simulation.dto.SimulationTableDataDTO;
import com.inforbus.gjk.simulation.mapper.SysDictMapper;
import com.inforbus.gjk.simulation.service.ManagerServiceImpl;
import com.inforbus.gjk.simulation.service.SimulatorService;
import com.inforbus.gjk.simulation.task.*;
import flowModel.MoniRecvDataThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * 仿真业务实现
 *
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
    @Autowired
    SysDictMapper sysDictMapper;
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

    //获取控制表头文件路径
    @Value("${ctrl.tab.file.path}")
    private String tabFilePath;

    //开始仿真客户线程放入全局变量
    @Override
    public boolean startSimulator(String username, List<String> componentLinks, String filePath) {
        //定义通道名
        String channelName = username + "SimulatorChannel";
        //队列初始状态
        redisTemplate.opsForValue().set(username + ":initState", "1");
        //模拟发布
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379);
        Publisher Publisher = new Publisher(jedisPool, host,"Simulator:admin:A");
        Publisher.start();
        //初始化监听对象
        Subscriber subscriber = new Subscriber();
        subscriber.setQueueSize(Integer.parseInt(queueSize));
        subscriber.setUsername(username);
        subscriber.setRedisTemplate(redisTemplate);
        //启动监听线程
        new SubscriberThread(subscriber, channelName, host).start();
        //启用客户线程，传入参数
       MoniRecvDataThread startMoniRecvDataThread = ExternalIOTransUtils.startMoniRecvDataThread(host, channelName, componentLinks, filePath, tabFilePath);

       //放入全局变量
        Global.USERS_SIMULATOR_THREAD.put(username, startMoniRecvDataThread);
        return true;
    }

    //停止仿真
    @Override
    public boolean stopSimulator(String username) {
        MoniRecvDataThread moniRecvDataThread = (MoniRecvDataThread) Global.USERS_SIMULATOR_THREAD.get(username);
        if (moniRecvDataThread == null) {
            return false;

        }
        ExternalIOTransUtils.stopMoniRecvDataThread(moniRecvDataThread);
        Global.USERS_SIMULATOR_THREAD.remove(username);
        Set<String> keys = redisTemplate.keys("Simulator:" + username + ":*");
        for (String key : keys) {
            redisTemplate.delete(key);
        }
        Set<String> keys1 = redisTemplate.keys(username + ":initState:*");
        keys1.stream().forEach(key -> {
            redisTemplate.delete(key);
        });
        return true;
    }

    @Override
    public Map<String, Object> getData(String username, String projectId, SimulationDTO simulationDTO) {

        //修改队列初始化状态
        redisTemplate.opsForValue().set(username + ":initState:" + simulationDTO.getSymbol(), "1");
        //拼接队列key
        String key = "Simulator:" + username + ":" + simulationDTO.getSymbol();
        //从队列右侧弹出一个元素
        String str = redisTemplate.opsForList().rightPop(key);

        JSONObject objects = JSONUtil.parseObj(str);

        //拼接流程模型文件路径,packinfo文件路径
        String FilePath = managerServiceImpl.getprocessFile(projectId);
        String packinfoFileName = gitDetailPath + FilePath + generateCodeResult + "/packinfo.xml";
        String XmlFilePath = simulationDTO.getFlowFilePath();
                String arrowInfo = simulationDTO.getStartId() + ":" + simulationDTO.getStartName() + "|" + simulationDTO.getEndId() + ":" + simulationDTO.getEndName();

      /*  //获取结构体表格数据
        String[] tabNames = ((String) objects.get("tabNameList")).split("\\|");
        List<SimulationTableDataDTO> tableData = Lists.newArrayList();
        //解析表格数据，得到表格对象集合
        tableData.addAll(forEachGetSimulationTableData((Map) objects.get(tabNames[0])));
        tableData.addAll(forEachGetSimulationTableData((Map) objects.get(tabNames[1])));
        //获取最大xyz维度
        Map<String, String> MaxXYZ = ExternalIOTransUtils.getMaxXYZ(XmlFilePath, packinfoFileName, objects, arrowInfo);*/
        if (simulationDTO.getX() == null) {
            //获取最大维度值添加到配置页面数据中
            HashMap<String, Object> packDataMap = new HashMap<>();
            packDataMap.put("data", objects);
            packDataMap.put("x", "10");
            packDataMap.put("y", "9");
            packDataMap.put("z", "8");
            packDataMap.put("symbol", simulationDTO.getSymbol());
            packDataMap.put("dataProecssingType", simulationDTO.getDataProcessingType());
          //  Map<String, Object> dataInfo = ExternalIOTransUtils.parseMoniData(XmlFilePath, packinfoFileName, packDataMap, arrowInfo);
            Map<String, Object> dataMap = Maps.newHashMap();
            //表格数据
            dataMap.put("tableData", "tableData");
            //展示数据
            dataMap.put("data", objects.get("data"));
            //模拟数据
            int maxx = 10;
            int maxy = 9;
            int maxz = 8;
            //重组x维度数据
            ArrayList<Object> xList = new ArrayList<>();
            for(int  i=0; i<=maxx ;i++){
                xList.add(i);
            }
            xList.add("All");
            Collections.reverse(xList);
            //重组y维度数据
            ArrayList<Object> yList = new ArrayList<>();
            for(int  i=0; i<=maxy ;i++){
                yList.add(i);
            }
            yList.add("All");
            Collections.reverse(yList);
            //重组z维度数据
            ArrayList<Object> zList = new ArrayList<>();
            for(int  i=0; i<=maxz ;i++){
                zList.add(i);
            }
            zList.add("All");
            Collections.reverse(zList);
            HashMap<String, Object> x = new HashMap<>();
            x.put("x",xList);
            x.put("y",yList);
            x.put("z",zList);
            dataMap.put("maxXYZ",x);
            return dataMap;
        } else {
            HashMap<String, Object> packDataMap = new HashMap<>();
            //数据包
            packDataMap.put("data", objects);
            //xyz最大值
            packDataMap.put("x", simulationDTO.getX());
            packDataMap.put("y", simulationDTO.getY());
            packDataMap.put("z", simulationDTO.getZ());
            //数据源
            packDataMap.put("symbol", simulationDTO.getSymbol());
            //数据处理 类型
            packDataMap.put("dataProecssingType", simulationDTO.getDataProcessingType());
           //获取展示数据
            //Map<String, Object> dataInfo = ExternalIOTransUtils.parseMoniData(XmlFilePath, packinfoFileName, packDataMap, arrowInfo);

            Map<String, Object> dataMap = Maps.newHashMap();
            //表格数据
            dataMap.put("tableData", "tableData");
            //展示数据
            //dataMap.put("Data",dataInfo);
            dataMap.put("data", objects.get("data"));
            //模拟数据
            int maxx = 10;
            int maxy = 9;
            int maxz = 8;
            //重组x维度数据
            ArrayList<Object> xList = new ArrayList<>();
            for(int  i=0; i<=maxx ;i++){
                xList.add(i);
            }
            xList.add("All");
            Collections.reverse(xList);
            //重组y维度数据
            ArrayList<Object> yList = new ArrayList<>();
            for(int  i=0; i<=maxy ;i++){
                yList.add(i);
            }
            yList.add("All");
            Collections.reverse(yList);
            //重组z维度数据
            ArrayList<Object> zList = new ArrayList<>();
            for(int  i=0; i<=maxz ;i++){
                zList.add(i);
            }
            zList.add("All");
            Collections.reverse(zList);
            HashMap<String, Object> x = new HashMap<>();
            x.put("x",xList);
            x.put("y",yList);
            x.put("z",zList);
            dataMap.put("maxXYZ",x);
            return dataMap;
        }
    }
//暂停获取帧数
    @Override
    public Map<String, List<String>> suspend(String username, List<String> symbols) {
        ListOperations<String, String> operations = redisTemplate.opsForList();
        Map<String, List<String>> symbolFrameSelect = Maps.newHashMap();
        List<String> selectData = Lists.newArrayList();
        for (String symbol : symbols) {
            String key = "Simulator:" + username + ":" + symbol;
            Long size = operations.size(key);
            List<String> range = operations.range(key, 0, size - 1);
            for (String s : range) {
                JSONObject dataMap = JSONUtil.parseObj(s);
                selectData.add(dataMap.get("frameNum") + "");
            }
            symbolFrameSelect.put(symbol, selectData);
        }
        return symbolFrameSelect;
    }

    //点击小图标获取数据源获取
    @Override
    public Map<String, Object> getDataSource(String username, SimulationDTO simulationDto) {

        MoniRecvDataThread moniRecvDataThread = (MoniRecvDataThread) Global.USERS_SIMULATOR_THREAD.get(username);

        //调用客户接口 获取数据源
        HashMap<String, Object> Data = new HashMap<>();
        List<SysDict> dataProcessingType = sysDictMapper.getDictTypes();
       // Data.put("sourceData",moniRecvDataThread.getArrowIdList(simulationDto.getStartId() + "|" + simulationDto.getEndId()));
        Data.put("sourceData","A");
        Data.put("dataProcessingType", dataProcessingType);
        return Data;
    }

    /**
     * 解析表格Map
     *
     * @return
     * @param. 表格数据Map 【key: name, value: 值|备注】
     */
    public List<SimulationTableDataDTO> forEachGetSimulationTableData(Map<String, String> tableDataMap) {
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
