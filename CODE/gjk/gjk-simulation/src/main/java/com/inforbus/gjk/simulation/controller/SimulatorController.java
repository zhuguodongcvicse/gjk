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

import java.util.List;
import java.util.Map;

/**
 * 仿真webAPI
 */
@RestController
@AllArgsConstructor
@RequestMapping("/simulation")
public class SimulatorController {

	@Autowired
	private SimulatorService simulatorService;

	/**
	 * 开始仿真
	 * @param username
	 * @param obj
	 * @return
	 */
	@PostMapping("/startSimulator/{username}")
	public R startSimulator(@PathVariable("username") String username,@RequestBody Object obj){
		Map parse = JSON.parseObject(JSONUtil.toJsonStr(obj));
		return new R<>(simulatorService.startSimulator(username,(List<String>)parse.get("componentLinks"),(String)parse.get("filePath")));
	}

	@PostMapping("/getDataSource/{username}")
	public R getDataSource(@PathVariable("username") String username,@RequestBody SimulationDTO simulationDTO){
		return new R<>(simulatorService.getDataSource(username,simulationDTO));
	}

	@PostMapping("/getData/{username}/{projectId}")
	public R getData(@PathVariable("username") String username,@PathVariable("projectId") String projectId, @RequestBody SimulationDTO simulationDTO){
		System.out.println(projectId+simulationDTO);
		return new R<>(simulatorService.getData(username,projectId,simulationDTO));
	}

	@PostMapping("/stopSimulator/{username}")
	public R stopSimulator(@PathVariable("username") String username){
		return new R<>(simulatorService.stopSimulator(username));
	}

	@PostMapping("/stop/{username}")
	public R stop(@PathVariable("username") String username,@RequestBody Object obj){
		Map parse = JSON.parseObject(JSONUtil.toJsonStr(obj));
		return new R<>(simulatorService.suspend(username,(List<String>) parse.get("symbols")));
	}
}

