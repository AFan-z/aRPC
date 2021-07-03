package com.afan.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author AFan
 * @date 2021/6/15 13:47
 */
public interface ServiceDiscovery {

    /**
     * 根据服务名获取服务InetSocketAddress
     * @param serviceName
     * @return
     */
    InetSocketAddress discoveryService(String serviceName);
}
