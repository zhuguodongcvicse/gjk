package com.inforbus.gjk.pro.api.entity;

import lombok.Data;

@Data
public class Chipsfromhardwarelibs {

    private String id;
    /**
     * 流程id
     */
    private String flowId;
    /**
     * 模型id
     */
    private String modelId;
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 芯片json数据
     */
    private String chips;
}
