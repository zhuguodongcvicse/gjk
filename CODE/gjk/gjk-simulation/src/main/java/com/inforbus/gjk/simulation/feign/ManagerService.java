package com.inforbus.gjk.simulation.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient( value = "gjk-project")
public interface ManagerService {

    @ResponseBody
    @PostMapping(path = "/manager/getFilePath")
    public String getprocessFile(@RequestParam(value = "projectId", required = false) String projectId);
}
