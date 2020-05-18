package com.inforbus.gjk.simulation.controller;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.simulation.dto.SimulationDTO;
import com.inforbus.gjk.simulation.service.SimulatorService;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @Description: 仿真webAPI
 * @Author: ZhangHongXu
 * @CreateDate: 2020/4/9 9:12
 * @UpdateUser: ZhangHongXu
 * @UpdateDate: 2020/4/9 9:12
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@AllArgsConstructor
@RequestMapping("/simulation")
public class SimulatorController {

    @Autowired
    private SimulatorService simulatorService;

    /**
     * @Description 开始仿真
     * @Author ZhangHongXu
     * @Return
     * @Exception
     * @Date 2020/4/9 9:12
     */
    @PostMapping("/startSimulator/{username}/{projectId}")
    public R startSimulator(@PathVariable("username") String username,@PathVariable("projectId") String projectId, @RequestBody Object obj) throws IOException {
        Map parse = JSON.parseObject(JSONUtil.toJsonStr(obj));
        return new R<>(simulatorService.startSimulator(username,projectId, (List<String>) parse.get("componentLinks"), (String) parse.get("filePath")));
    }

    /**
     * @Description 获取数据源
     * @Author ZhangHongXu
     * @Return
     * @Exception
     * @Date 2020/4/9 9:13
     */
    @PostMapping("/getDataSource/{username}")
    public R getDataSource(@PathVariable("username") String username, @RequestBody SimulationDTO simulationDTO) {
        return new R<>(simulatorService.getDataSource(username, simulationDTO));
    }
	/**
	* @Description 获取曲波图数据
	* @Author ZhangHongXu
	 * @Return
	* @Exception 
	* @Date 2020/4/9 9:14
	*/
    @PostMapping("/getData/{username}/{projectId}")
    public R getData(@PathVariable("username") String username, @PathVariable("projectId") String projectId, @RequestBody SimulationDTO simulationDTO) {
        return new R<>(simulatorService.getData(username, projectId, simulationDTO));
    }
	/**
	* @Description 停止仿真
	* @Author ZhangHongXu
	 * @Return
	* @Exception 
	* @Date 2020/4/9 9:14
	*/
    @PostMapping("/stopSimulator/{username}")
    public R stopSimulator(@PathVariable("username") String username) {
        return new R<>(simulatorService.stopSimulator(username));
    }
	/**
	* @Description 停止
	* @Author ZhangHongXu
	 * @Return
	* @Exception 
	* @Date 2020/4/9 9:14
	*/
    @PostMapping("/stop/{username}")
    public R stop(@PathVariable("username") String username, @RequestBody Object obj) {
        Map parse = JSON.parseObject(JSONUtil.toJsonStr(obj));
        return new R<>(simulatorService.suspend(username, (List<String>) parse.get("symbols")));
    }
	/**
	* @Description 获取帧数得到数据
	* @Author ZhangHongXu
	 * @Return
	* @Exception 
	* @Date 2020/4/9 9:22
	*/
    @PostMapping("/start")
    public R start(@RequestBody SimulationDTO obj) {
        return new R<>(simulatorService.start(obj));
    }
}

