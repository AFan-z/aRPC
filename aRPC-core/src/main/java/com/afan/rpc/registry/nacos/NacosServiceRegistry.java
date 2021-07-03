package com.afan.rpc.registry.nacos;

import com.afan.rpc.registry.ServiceRegistry;
import com.afan.rpc.registry.nacos.util.NacosUtils;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author AFan
 * @date 2021/6/15 17:22
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtils.registerServer(serviceName,inetSocketAddress);
            log.debug("注册服务：{}", serviceName);
        } catch (NacosException e) {
            throw new RuntimeException("注册Nacos出现异常");
        }
    }
}
