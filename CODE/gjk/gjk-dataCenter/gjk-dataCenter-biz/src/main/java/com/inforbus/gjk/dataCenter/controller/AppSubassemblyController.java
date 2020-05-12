package com.inforbus.gjk.dataCenter.controller;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.dataCenter.service.AppSubassemblyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appSubassemblyServer")
public class AppSubassemblyController {

    private static final Logger logger = LoggerFactory.getLogger(AppSubassemblyController.class);

    @Autowired
    private AppSubassemblyService appSubassemblyService;
    /**
     * 判断文件是否存在
     * @param filePath
     * @auther sunchao
     * @return
     */
    @PostMapping("/judgeFileExist")
    @ResponseBody
    public R judgeFileExist(@RequestParam("filePath") String filePath) {
        R<Boolean> r = new R<>();
        try {
            r.setData(appSubassemblyService.judgeFileExist(filePath));
        } catch (Exception e) {
            r.setMsg("\"判断文件是否存在失败：\" + e.getMessage()");
            logger.error("判断文件是否存在失败：" + e.getMessage());
        }
        return r;
    }

    @PostMapping("/getAppPath")
    @ResponseBody
    public R getAppPath(@RequestParam("filePath") String filePath, @RequestParam("selectFileName") String selectFileName) {
        R<String> r = new R<>();
        try {
            r.setData(appSubassemblyService.getAppPath(filePath, selectFileName));
        } catch (Exception e) {
            r.setMsg("\"获取APP路径失败：\" + e.getMessage()");
            logger.error("获取APP路径失败：" + e.getMessage());
        }
        return r;
    }
}
