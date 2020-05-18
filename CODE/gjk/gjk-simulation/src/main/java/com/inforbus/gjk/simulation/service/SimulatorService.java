package com.inforbus.gjk.simulation.service;


import com.inforbus.gjk.simulation.dto.SimulationDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @Description:    仿真业务接口
* @Author:         ZhangHongXu
* @CreateDate:     2020/4/9 9:23
* @UpdateUser:     ZhangHongXu
* @UpdateDate:     2020/4/9 9:23
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public interface SimulatorService {
    /**
     * 开始仿真
     *
     * @param username
     * @param componentLinks
     * @param filePath
     * @return
     */
    boolean startSimulator(String projectId, String username, List<String> componentLinks, String filePath) throws IOException;

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
    Map<String,Object> getData(String username, String projectId,SimulationDTO simulationDTO);

    /**
     * 仿真暂停获取帧号集合
     * @param username 用户名
     * @param symbols 标识集合
     * @return
     */
    List<Object> suspend(String username, List<String> symbols);

    Map<String,Object> getDataSource(String username, SimulationDTO simulationDTO);

    List<Object> start(SimulationDTO obj);
}
