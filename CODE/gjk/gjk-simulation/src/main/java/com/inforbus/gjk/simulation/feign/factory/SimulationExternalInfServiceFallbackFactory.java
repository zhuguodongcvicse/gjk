package com.inforbus.gjk.simulation.feign.factory;

import com.inforbus.gjk.simulation.feign.SimulationExternalInfService;
import com.inforbus.gjk.simulation.feign.fallback.SimulationExternalInfServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Description: 熔断实现工厂
 * @Author: zhanghongxu
 * @CreateDate: 2020/4/21$ 10:00$
 * @UpdateUser:
 * @UpdateDate: 2020/4/21$ 10:00$
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
public class SimulationExternalInfServiceFallbackFactory implements FallbackFactory<SimulationExternalInfService> {
    @Override
    public SimulationExternalInfService create(Throwable cause) {
        SimulationExternalInfServiceImpl rxternalInfServiceFallback = new SimulationExternalInfServiceImpl();
        rxternalInfServiceFallback.setCause(cause);
        return rxternalInfServiceFallback;
    }
}
