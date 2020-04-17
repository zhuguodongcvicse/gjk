package com.inforbus.gjk.dataCenter.controller;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.service.ExternalInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: gjk
 * @description: 第三方接口
 * @author: SunChao
 * @create: 2020-04-17 14:16
 **/
@RestController
@RequestMapping("/ExternalInfServer")
public class ExternalInfController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private ExternalInfService externalInfService;

    @RequestMapping("/getCmpSysConfig")
    public R<Map<String, List<String>>> getCmpSysConfig(String netWorkConfigFileName, String packinfoPath, String workModeFileName) {
        Map<String, List<String>> cmpSysConfig = externalInfService.getCmpSysConfig(netWorkConfigFileName, packinfoPath, workModeFileName);
        return new R<>(cmpSysConfig);
    }

}
