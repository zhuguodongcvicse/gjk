package com.inforbus.gjk.dataCenter.service;

import com.inforbus.gjk.common.core.util.R;

import java.io.IOException;
import java.util.Map;

/**
 * CodeGenerationService
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成接口
 */
public interface CodeGenerationService {
    /**
     * @Author wang
     * @Description: 集成代码生成功能方法
     * @Param: [map]
     * @Return: boolean
     * @Create: 2020/4/20
     */
    boolean codeGeneration(Map<String, String> map) throws IOException;

    /**
     * @Author wang
     * @Description: 静态检查功能
     * @Param: [filePath, fileName]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/5/18
     */
    R staticInspect(String filePath, String fileName);
}

