package com.afan.rpc.registry.nacos;

import com.afan.rpc.loadbalance.LoadBalance;
import com.afan.rpc.registry.RegistryFactory;
import com.afan.rpc.registry.ServiceDiscovery;
import com.afan.rpc.registry.ServiceRegistry;

/**
 * @author AFan
 * @date 2021/7/3 23:15
 */
public class NacosRegistryFactory implements RegistryFactory {
    @Override
    public ServiceRegistry getServiceRegistry() {
        return new NacosServiceRegistry();
    }

    @Override
    public ServiceDiscovery getServiceDiscovery(LoadBalance loadBalancer) {
        return new NacosServiceDiscovery(loadBalancer);
    }
}
