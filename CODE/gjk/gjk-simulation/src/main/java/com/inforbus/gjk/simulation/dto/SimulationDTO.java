package com.inforbus.gjk.simulation.dto;

import lombok.Data;

/**
* @Description:    页面参数封装
* @Author:         ZhangHongXu
* @CreateDate:     2020/4/9 9:15
* @UpdateUser:     ZhangHongXu
* @UpdateDate:     2020/4/9 9:15
* @UpdateRemark:   修改内容
* @Version:        1.0
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
