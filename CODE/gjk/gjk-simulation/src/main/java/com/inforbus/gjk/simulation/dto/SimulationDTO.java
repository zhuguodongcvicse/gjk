package com.inforbus.gjk.simulation.dto;

import lombok.Data;

/**
 * 页面参数封装
 */
@Data
public class SimulationDTO {

    private String flowFilePath;

    private String startId;

    private String endId;

    private String symbol;

    private String attr1;

    private String attr2;

    private String attr3;

    private String select;

}
