package com.inforbus.gjk.pro.api.dto;

import lombok.Data;
/*
* 基础模板id集对象
* */
@Data
public class BaseTemplateIDsDTO {
    /*
    * 系统配置模板id
    * */
    private String sysTempId;
    /*
     * 主题配置模板id
     * */
    private String themeTempId;
    /*
     * 网络配置模板id
     * */
    private String networkTempId;
    /*
     * 软硬件映射配置模板id
     * */
    private String hsmTempId;
}
