package com.inforbus.gjk.simulation.global;

import com.google.common.collect.Maps;

import java.util.Map;

public class Global {

    public static Map<String, Map<String, Thread>> USERS_SIMULATOR_QUEUES = Maps.newHashMap();

}
