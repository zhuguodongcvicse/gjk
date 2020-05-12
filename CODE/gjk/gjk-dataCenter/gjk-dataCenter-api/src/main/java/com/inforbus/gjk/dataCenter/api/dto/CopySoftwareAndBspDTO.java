package com.inforbus.gjk.dataCenter.api.dto;

import lombok.Data;

/**
 * @program: gjk
 * @description: 复制软件工程和bsp数据对象
 * @author: SunChao
 * @create: 2020-05-11 15:39
 **/
@Data
public class CopySoftwareAndBspDTO {
    private String appFilePath;
    private String partName;
    private String libsType;
    private String platformType;
    private String softwareFilePath;
    private String softwareName;
    private String bspFilePath;
    private String proDetailPath;
}
