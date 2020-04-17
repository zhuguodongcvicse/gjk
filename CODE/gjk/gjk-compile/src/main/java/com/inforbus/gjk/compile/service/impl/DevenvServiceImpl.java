package com.inforbus.gjk.compile.service.impl;

import com.inforbus.gjk.common.core.util.R;
import com.inforbus.gjk.compile.feign.RemoteDevenvService;
import com.inforbus.gjk.compile.service.DevenvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * DevenvServiceImpl
 *
 * @author wang
 * @date 2020/4/16
 * @Description 编译功能实现类
 */
@Service
public class DevenvServiceImpl implements DevenvService {

    @Autowired
    private RemoteDevenvService remoteDevenvService;

    //配置文件读取路径
    @Value("${VS2010.path}")
    private String vsPath;

    @Value("${Workbench.path}")
    private String wbPath;

    @Value("${Linux.path}")
    private String linuxPath;

    //读取配置信息
    @Value("${Linux.ip}")
    private String ip;

    @Value("${Linux.username}")
    private String username;

    @Value("${Linux.password}")
    private String password;

    @Value("${downPath.path}")
    private String dPath;

    @Override
    public R command(Map<String, String> map) {
        map.put("vsPath",this.vsPath);
        map.put("wbPath",this.wbPath);
        map.put("linuxPath",this.linuxPath);
        map.put("ip",this.ip);
        map.put("username",this.username);
        map.put("password",this.password);
        map.put("dPath",this.dPath);
        return remoteDevenvService.command(map);
    }
}
