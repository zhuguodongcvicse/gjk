package com.inforbus.gjk.simulation.service;
import com.inforbus.gjk.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("gjk-project")
public interface ManagerServiceImpl {

    @ResponseBody
    @PostMapping(path = "/manager/getFilePath")
    public String getprocessFile(@RequestParam(value = "projectId", required = false) String projectId);
}
