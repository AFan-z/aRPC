package com.afan.rpc.registry.nacos;

import com.afan.rpc.loadbalance.LoadBalance;
import com.afan.rpc.loadbalance.loadbalancer.RandomLoadBalance;
import com.afan.rpc.registry.ServiceDiscovery;
import com.afan.rpc.registry.nacos.util.NacosUtils;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author AFan
 * @date 2021/6/15 17:23
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final LoadBalance loadBalancer;


    public NacosServiceDiscovery(LoadBalance loadBalancer) {
        this.loadBalancer = loadBalancer == null ? new RandomLoadBalance() : loadBalancer;
    }

    @SneakyThrows
    @Override
    public InetSocketAddress discoveryService(String serviceName) {
        List<Instance> instanceList = NacosUtils.getAllInstance(serviceName);
        if (instanceList.size() == 0 || instanceList == null) {
            throw new RuntimeException("找不到对应服务");
        }
        List<String> serviceAddresses = new ArrayList<>();
        Iterator<Instance> iterator = instanceList.iterator();
        while (iterator.hasNext()) {
            Instance next = iterator.next();
            serviceAddresses.add(next.getIp() + ":" + next.getPort());
        }

        String select = loadBalancer.select(serviceAddresses, null);
        log.debug("serviceAddresses: {}", select);

        String[] split = select.split(":");
        String host = split[0];
        int port = Integer.parseInt(split[1]);

        return new InetSocketAddress(host, port);
    }
}
