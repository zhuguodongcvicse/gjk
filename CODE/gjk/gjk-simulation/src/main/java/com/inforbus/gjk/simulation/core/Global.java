package com.inforbus.gjk.simulation.core;

import com.google.common.collect.Maps;
import com.inforbus.gjk.simulation.task.Subscriber;

import java.util.Map;

/**
 * 存放接收数据线程对象
 */
public class Global {

    public static Map<String, Thread> USERS_SIMULATOR_THREAD = Maps.newHashMap();

    public static Map<String, Subscriber> USERS_SIMULATOR_SUBSCRIBER = Maps.newHashMap();

}
