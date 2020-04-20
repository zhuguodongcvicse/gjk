package com.inforbus.gjk.compile.service;

import com.inforbus.gjk.common.core.util.R;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * DevenvService
 *
 * @author wang
 * @date 2020/4/16
 * @Description 编译接口类
 */

public interface DevenvService {

    R command(Map<String, String> map);
}
