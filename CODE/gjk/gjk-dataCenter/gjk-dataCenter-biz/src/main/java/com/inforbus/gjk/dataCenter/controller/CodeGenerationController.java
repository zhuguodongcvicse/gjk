package com.inforbus.gjk.dataCenter.controller;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.service.CodeGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * CodeGenerationController
 *
 * @author wang
 * @date 2020/4/20
 * @Description 集成代码生成功能控制器
 */
@RestController
@RequestMapping("/codeGeneration")
public class CodeGenerationController {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerationController.class);

    @Autowired
    private CodeGenerationService codeGenerationService;

    /**
     * @Author wang
     * @Description: 集成代码生成方法
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R<java.lang.Boolean>
     * @Create: 2020/4/20
     */
    @PutMapping("/codeGenerate")
    public R<Boolean> codeGeneration(@RequestBody Map<String, String> map) {
        R<Boolean> r = new R<>();
        boolean b = false;
        try {
            b = codeGenerationService.codeGeneration(map);
            r.setData(b);
        } catch (IOException e) {
            r.setData(b);
            r.setCode(CommonConstants.FAIL);
            r.setMsg("集成代码生成中发生IO异常");
            logger.error("集成代码生成失败，发生异常：" + e.getMessage());
        }
        return r;
    }

    /**
     * @Author wang
     * @Description: 静态检查功能
     * @Param: [filePath, fileName]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/5/18
     */
    @PostMapping("/staticInspect")
    public R staticInspect(@RequestParam("filePath") String filePath, @RequestParam("fileName")String fileName){
        return codeGenerationService.staticInspect(filePath, fileName);
    }
}
