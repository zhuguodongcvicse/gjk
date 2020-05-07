package com.inforbus.gjk.comp.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.comp.service.HeaderFileDispService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/headerFileDisp")
public class HeaderFileDispController {

	private final HeaderFileDispService headerFileDispService;

	@PostMapping("/parseHeaderFile")
	public R getHeader(@RequestBody Map<String, String> maps) {
		return headerFileDispService.parseHeaderFile(maps.get("path"));
	}

	@PostMapping("/parsePerformanceTable")
	public R getPerformanceTable(@RequestBody Map<String, String> maps) {
		return headerFileDispService.parsePerformanceTable(maps.get("excelPath"));
	}

}
