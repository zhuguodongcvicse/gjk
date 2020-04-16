package com.inforbus.gjk.admin.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.inforbus.gjk.admin.api.dto.CompIdDTO;
import com.inforbus.gjk.admin.api.entity.Component;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;

@FeignClient("gjk-component")
public interface FeginComponentService {
	//根据libsId获取库文件
	@PostMapping(value = "/component/getLibsFile/{libsId}")
    public List<ComponentDetail> getLibsFile(@PathVariable("libsId") String libsId);
	
	//根据libsId获取库类型
	@PostMapping(value = "/component/getLibsFileType")
    public List<ComponentDetail> getLibsFileType(String libsId);
	
	//根据id获取构件名
	@PostMapping(value = "/component/getCompNameById/{id}")
    public Component getCompNameById(@PathVariable("id") String id);
	
	//通过分组compId获取CompIds
	@PostMapping(value = "/component/getCompIdsGroupCompId")
    public List<String> getCompIdsGroupCompId();
	
	//根据构件id、文件名获取构件详细信息
	@PostMapping(value = "/component/getCompDetailByComponentId/{componentId}/{fileName}")
    public ComponentDetail getCompDetailByComponentId(@PathVariable("componentId") String componentId, @PathVariable("fileName") String fileName);
	
	//根据compId获取构件信息
	@PostMapping(value = "/component/getCompByCompId")
    public List<Component> getCompByCompId(@RequestBody CompIdDTO compIdDTO);
}
