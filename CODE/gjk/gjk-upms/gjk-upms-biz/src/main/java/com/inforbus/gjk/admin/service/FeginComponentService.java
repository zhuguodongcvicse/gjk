package com.inforbus.gjk.admin.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.inforbus.gjk.admin.api.entity.Component;
import com.inforbus.gjk.admin.api.entity.ComponentDetail;

@FeignClient("gjk-component")
public interface FeginComponentService {
	@PostMapping(value = "/component/getLibsFile/{libsId}")
    public List<ComponentDetail> getLibsFile(@PathVariable("libsId") String libsId);
	
	@PostMapping(value = "/component/getLibsFileType")
    public List<ComponentDetail> getLibsFileType(String libsId);
	
	@PostMapping(value = "/component/getCompNameById/{id}")
    public Component getCompNameById(@PathVariable("id") String id);
	
	@PostMapping(value = "/component/getCompIdsGroupCompId")
    public List<String> getCompIdsGroupCompId();
	
	@PostMapping(value = "/component/getCompDetailByComponentId/{componentId}/{fileName}")
    public ComponentDetail getCompDetailByComponentId(@PathVariable("componentId") String componentId, @PathVariable("fileName") String fileName);
	
	@PostMapping(value = "/component/getCompByCompId/{compId}")
    public List<Component> getCompByCompId(@PathVariable("compId") String compId);
}
