package com.inforbus.gjk.admin.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 仿真监测数据
 * @author dong
 *
 */
@Data
public class SimulationDTO {

	// X轴数据
	private List<String> xAxisData;
	
	// Y轴数据
	private List<Integer> yAxisData;
	
	
 }
