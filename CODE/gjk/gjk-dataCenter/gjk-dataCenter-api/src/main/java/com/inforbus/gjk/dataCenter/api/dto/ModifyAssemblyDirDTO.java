package com.inforbus.gjk.dataCenter.api.dto;

import com.inforbus.gjk.dataCenter.api.entity.Part;
import lombok.Data;

/**
 * @program: gjk
 * @description: 修改组件工程文件夹数据对象
 * @author: SunChao
 * @create: 2020-05-11 15:39
 **/
@Data
public class ModifyAssemblyDirDTO {
    private String assemblyName;
    private Part part;
    private String makefileType;
    private String integerCodeFilePath;
    private String bspFilePath;
    private String sylixosProjectName;
    private String proDetailPath;
}
