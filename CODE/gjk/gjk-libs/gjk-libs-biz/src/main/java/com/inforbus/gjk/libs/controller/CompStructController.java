package com.inforbus.gjk.libs.controller;

import com.inforbus.gjk.common.core.constant.CommonConstants;
import com.inforbus.gjk.common.core.entity.CompStruct;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.libs.service.CompStructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CompStructController
 *
 * @author wang
 * @date 2020/5/12
 * @Description TODO
 */
@RestController
@RequestMapping("/compStruct")
public class CompStructController {

    @Autowired
    private CompStructService compStructService;

    /**
     * @Author wang
     * @Description: 根据构件库id集合，获取构件库和结构体关系数据
     * @Param: [compIdList]
     * @Return: com.inforbus.gjk.common.core.util.R<java.util.List<com.inforbus.gjk.common.core.entity.CompStruct>>
     * @Create: 2020/5/12
     */
    @PostMapping("/getCompStructByCompIdList")
    public R<List<CompStruct>> getCompStructByCompIdList(@RequestBody List<String> compIdList){
        R<List<CompStruct>> r = new R<>();
        try {
            List<CompStruct> list = compStructService.getCompStructByCompIdList(compIdList);
            r.setData(list);
            r.setMsg("查询成功");
        }catch (Exception e){
            r.setData(null);
            r.setMsg("查询失败");
            r.setCode(CommonConstants.FAIL);
        }
        return r;
    }
}
