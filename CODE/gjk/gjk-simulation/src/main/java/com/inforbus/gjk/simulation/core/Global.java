package com.inforbus.gjk.simulation.core;

import com.google.common.collect.Maps;
import com.inforbus.gjk.simulation.task.Subscriber;

import java.util.Map;

/**
* @Description:    存放接收数据线程对象
* @Author:         ZhangHongXu
* @CreateDate:     2020/4/9 9:15
* @UpdateUser:     ZhangHongXu
* @UpdateDate:     2020/4/9 9:15
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class Global {

    /**
    * @Description 存放客户线程
    * @Author ZhangHongXu
     * @param null
    * @Return
    * @Exception
    * @Date 2020/4/9 9:15
    */
    public static Map<String, Thread> USERS_SIMULATOR_THREAD = Maps.newHashMap();

}
