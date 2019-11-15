
package com.inforbus.gjk.pro.controller;

import com.inforbus.gjk.admin.api.dto.SimulationDTO;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.common.log.annotation.SysLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @author geng_hxian
 * @date 2019/4/17
 */
@RestController
@RequestMapping("/simulation")
public class SimulationController {
	/**
	 * 返回Echarts图形所需数据
	 *
	 * @param gjkAlgorithm
	 * @return
	 */
	@SysLog("生成仿真的Echarts数据")
	@GetMapping("/simulationData")
	public R simulation() {
		SimulationDTO simulationDTO = new SimulationDTO();

		// X轴数据
		List<String> xAxisData = new ArrayList<String>();

		// Y轴数据
		List<Integer> yAxisData = new ArrayList<Integer>();

		int yAxis = 0;
		for (int i = 0; i < 50000; i++) {
			yAxis = (int) (1 + Math.random() * (500 - 1 + 1));
			yAxisData.add(yAxis);

			xAxisData.add(String.valueOf(i+1));
		}

		simulationDTO.setYAxisData(yAxisData);
		simulationDTO.setXAxisData(xAxisData);
		return new R<>(simulationDTO);
	}

	/**
	 * 返回Echarts图形所需数据
	 *
	 * @param gjkAlgorithm
	 * @return
	 */
	@SysLog("生成真实的数据的Echarts数据")
	@GetMapping("/realData")
	public R realDate() {
		SimulationDTO simulationDTO = new SimulationDTO();

		// X轴数据
		List<String> xAxisData = new ArrayList<String>();

		// Y轴数据
		List<Integer> yAxisData = new ArrayList<Integer>();

		int yAxis = 0;
		for (int i = 0; i < 50000; i++) {
			yAxis = (int) (1 + Math.random() * (500 - 1 + 1));
			yAxisData.add(yAxis);

			xAxisData.add(String.valueOf(i+1));
		}

		simulationDTO.setYAxisData(yAxisData);
		simulationDTO.setXAxisData(xAxisData);
		return new R<>(simulationDTO);
	}
}
