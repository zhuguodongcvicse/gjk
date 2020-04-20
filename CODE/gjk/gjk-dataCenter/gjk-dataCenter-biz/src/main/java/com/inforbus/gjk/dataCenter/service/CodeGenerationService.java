package com.inforbus.gjk.dataCenter.service;

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
}
