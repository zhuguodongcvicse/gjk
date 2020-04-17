package com.inforbus.gjk.compile.controller;

import java.util.Map;


import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.compile.service.DevenvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inforbus.gjk.common.core.util.R;
import lombok.AllArgsConstructor;


/**
 * DevenvConntroller
 *
 * @author wang
 * @date 2019/10/20
 * @Description 编译功能控制器，实现编译任务排队
 */
@RestController
@AllArgsConstructor
@RequestMapping("/devenv")
public class DevenvConntroller {

    @Autowired
    private DevenvService devenvService;
    //任务线程类

    /**
     * @Author wang
     * @Description: 编译功能调用层
     * @Param: [map]
     * @Return: com.inforbus.gjk.common.core.util.R
     * @Create: 2020/4/8
     */
    @PutMapping(value = "/Command")
    public R command(@RequestBody Map<String, String> map) {
        R r = new R();
        try {
            r = devenvService.command(map);
        } catch (Exception e) {
            r.setCode(CommonConstants.FAIL);
            r.setMsg("编译失败 ：" + e.getMessage());
        }
        return r;
    }
}
