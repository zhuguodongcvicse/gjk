package com.inforbus.gjk.simulation.dto;

import lombok.Data;

/**
 * 页面参数封装
 */
@Data
public class SimulationDTO {
    private  String startName;

    private  String endName;

    private String flowFilePath;

    private String startId;

    private String endId;

    private String symbol;

    private String x;

    private String y;

    private String z;

    private String select;

    private String dataProcessingType;

    private  Object  frameId;

    private String username;

    private  String projectId;
}
