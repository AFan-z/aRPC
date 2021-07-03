package com.afan.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册
 * @author AFan
 * @date 2021/6/15 13:47
 */
public interface ServiceRegistry {
    /**
     * 将服务的名称和地址注册进服务注册中心
     * @param serviceName
     * @param inetSocketAddress
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
