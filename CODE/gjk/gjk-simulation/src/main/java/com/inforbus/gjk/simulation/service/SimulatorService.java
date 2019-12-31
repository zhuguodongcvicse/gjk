package com.inforbus.gjk.simulation.service;


import com.inforbus.gjk.simulation.dto.SimulationDTO;

import java.util.List;
import java.util.Map;

/**
 * 仿真业务接口
 */
public interface SimulatorService {
    /**
     * 开始仿真
     * @param username
     * @param componentLinks
     * @param filePath
     * @return
     */
    boolean startSimulator(String username, List<String> componentLinks, String filePath);

    /**
     * 结束仿真
     * @param username
     * @return
     */
    boolean stopSimulator(String username);

    /**
     * 获取数据
     * @param username
     * @param simulationDTO
     * @return
     */
    Map<String,Object> getData(String username, SimulationDTO simulationDTO);

    /**
     * 仿真暂停获取帧号集合
     * @param username 用户名
     * @param symbols 标识集合
     * @return
     */
    Map<String, List<String>> suspend(String username, List<String> symbols);

    List<String> getDataSource(String username, SimulationDTO simulationDTO);

}
