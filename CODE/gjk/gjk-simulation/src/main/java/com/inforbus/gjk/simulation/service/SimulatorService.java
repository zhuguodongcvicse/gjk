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

    Map<String, List<String>> stop(String username, List<String> symbols);

    List<String> getDataSource(SimulationDTO simulationDTO);

}
