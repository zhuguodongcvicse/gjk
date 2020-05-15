package com.inforbus.gjk.simulation.dto;

import lombok.Data;

/**
 * @Description: 仿真下载本地文件路径
 * @Author: zhanghongxu
 * @CreateDate: 2020/5/15$ 14:04$
 * @UpdateUser:
 * @UpdateDate: 2020/5/15$ 14:04$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
public class SimulatorFilePathBO {
    /**
     * 本地流程建模文件路径
     */
    private String processModelNativePath;
    /**
     * 本地packinfo文件路径
     */
    private  String packinfoNativePath;
}
