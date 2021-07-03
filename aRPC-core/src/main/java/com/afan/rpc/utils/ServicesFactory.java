package com.afan.rpc.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AFan
 */
@Slf4j
public class ServicesFactory {

    /**
     * 保存所有有注解@RpcService的集合
     */
    static final Map<String, Object> serviceFactory = new ConcurrentHashMap<>();


    /**
     * 添加已注解的类进入工厂
     * @param serviceName
     * @param service
     * @param <T>
     */
    public static <T> void addServiceProvider(String serviceName, T service) {
        if (serviceFactory.containsKey(serviceName)) {
            return;
        }
        serviceFactory.put(serviceName, service);
        log.debug("service: {}, 添加进工厂",serviceName);
    }

    public static <T> T getService(String serviceName) {
        return (T) serviceFactory.get(serviceName);
    }
}
