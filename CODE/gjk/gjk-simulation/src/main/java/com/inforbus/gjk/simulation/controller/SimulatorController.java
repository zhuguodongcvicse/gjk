package com.inforbus.gjk.simulation.controller;


import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.simulation.dto.SimulationDTO;
import com.inforbus.gjk.simulation.service.SimulatorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	 * @param componentLinks
	 * @param filePath
	 * @return
	 */
	@PostMapping("/startSimulator/{username}")
	public R startSimulator(@PathVariable("username") String username,@RequestParam(value = "componentLinks",required=false) List<String> componentLinks, String filePath){
		return new R<>(simulatorService.startSimulator(username,componentLinks,filePath));
	}

	@PostMapping("/getDataSource")
	public R getDataSource(@RequestBody SimulationDTO simulationDTO){
		return new R<>(simulatorService.getDataSource(simulationDTO));
	}

	@PostMapping("/getData/{username}")
	public R getData(@PathVariable("username") String username, @RequestBody SimulationDTO simulationDTO){
		return new R<>(simulatorService.getData(username, simulationDTO));
	}

	@PostMapping("/stopSimulator/{username}")
	public R stopSimulator(@PathVariable("username") String username){
		return new R<>(simulatorService.stopSimulator(username));
	}
}
