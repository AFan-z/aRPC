package com.afan.rpc.config.spring;

import com.afan.rpc.config.ResourceConfig;
import com.afan.rpc.extension.ExtensionLoader;
import com.afan.rpc.proxy.RpcClientProxy;
import com.afan.rpc.registry.RegistryFactory;
import com.afan.rpc.remote.RpcClient;
import com.afan.rpc.annotation.ARpcReference;
import com.afan.rpc.annotation.ARpcService;
import com.afan.rpc.registry.ServiceRegistry;
import com.afan.rpc.registry.nacos.NacosServiceRegistry;
import com.afan.rpc.utils.RpcServiceUtil;
import com.afan.rpc.utils.ServicesFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author AFan
 * @date 2021/6/16 12:39
 */
@Slf4j
@Component
public class RpcServiceConfig implements ApplicationContextAware, BeanPostProcessor {
    RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(ResourceConfig.REGISTRY_TYPE);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceRegistry serviceRegistry = registryFactory.getServiceRegistry();
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(ARpcService.class);

        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                ARpcService rpcService = serviceBean.getClass().getAnnotation(ARpcService.class);

                RpcServiceUtil serviceUtil = new RpcServiceUtil();
                if (!"".equals(rpcService.version())) {
                    serviceUtil.setVersion(rpcService.version());
                }

                if (void.class != rpcService.interfaceClass()) {
                    serviceUtil.setInterfaces(rpcService.interfaceClass());
                } else {
                    serviceUtil.setService(serviceBean);
                }

                //注册服务
                try {
                    String host = InetAddress.getLocalHost().getHostAddress();
                    log.info("host: {}", host);
                    ServicesFactory.addServiceProvider(serviceUtil.getServiceName(), serviceBean);
                    serviceRegistry.registerService(serviceUtil.getServiceName(ARpcService.class), new InetSocketAddress(host, ResourceConfig.PORT));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    log.error("occur exception when getHostAddress", e);
                }

            }
        }
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //TODO 这里考虑是否将服务消费者注册到注册中心?
        ServiceRegistry serviceRegistry = new NacosServiceRegistry();

        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ARpcReference rpcReference = declaredField.getAnnotation(ARpcReference.class);
            if (rpcReference != null) {
                RpcServiceUtil serviceUtil = new RpcServiceUtil();
                if (!"".equals(rpcReference.version())) {
                    serviceUtil.setVersion(rpcReference.version());
                }
                serviceUtil.setInterfaces(declaredField.getType());

                RpcClientProxy rpcClientProxy = new RpcClientProxy(new RpcClient(), serviceUtil);
                Object clientProxy = rpcClientProxy.create(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return bean;
    }
}
