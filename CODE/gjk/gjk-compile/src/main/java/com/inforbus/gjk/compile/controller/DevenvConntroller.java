package com.inforbus.gjk.compile.controller;

import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.compile.service.DevenvService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/devenv")
public class DevenvConntroller {
	private final DevenvService devenvService;
	//@Autowired
	//private AmqpTemplate rabbitmqTemplate;
	@PutMapping(value = "/Command")
	public R Command(@RequestBody Map<String, String> map) {
		String path =map.get("path");
		String fileName =map.get("fileName");
		String platformType = map.get("platformType");
		String str = devenvService.Command(path,fileName,platformType);
		//推送消息到rabbitmq中
		//this.rabbitmqTemplate.convertAndSend("gjkmq" , "test===@@@==="+str);
		//System.out.println(str);
		return new R<>(str);
	}
}
