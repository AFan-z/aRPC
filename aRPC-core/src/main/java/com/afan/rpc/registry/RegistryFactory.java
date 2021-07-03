package com.afan.rpc.registry;

import com.afan.rpc.extension.SPI;
import com.afan.rpc.loadbalance.LoadBalance;

/**
 * @author AFan
 * @date 2021/7/3 23:12
 */
@SPI
public interface RegistryFactory {
    /**
     * 获得服务注册
     * @return
     */
    ServiceRegistry getServiceRegistry();

    /**
     * 获得服务发现
     * @return
     */
    ServiceDiscovery getServiceDiscovery(LoadBalance loadBalancer);
}
