package com.afan.rpc.config.spring;

import com.afan.rpc.annotation.ARpcScan;
import com.afan.rpc.annotation.ARpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * 参考 https://github.com/Snailclimb/guide-rpc-framework/blob/master/rpc-framework-simple/src/main/java/github/javaguide/spring/CustomScannerRegistrar.java
 * 自定义扫描规则
 * @author AFan
 * @date 2021/6/16 15:55
 */
@Slf4j
public class RpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final String RPC_BASE_PACKAGE = "com.afan.rpc";
    private static final String BASE_PACKAGE = "basePackage";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ARpcScan.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE);
        }
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{((StandardAnnotationMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName()};
        }

        RpcClassPathScanner rpcClassPathScanner = new RpcClassPathScanner(registry, ARpcService.class);
        RpcClassPathScanner springBeanScanner = new RpcClassPathScanner(registry, Component.class);

        if (resourceLoader != null) {
            rpcClassPathScanner.setResourceLoader(resourceLoader);
            springBeanScanner.setResourceLoader(resourceLoader);
        }

        int springBeanAmount = springBeanScanner.scan(RPC_BASE_PACKAGE);
        log.info("springBeanScanner扫描的数量 [{}]", springBeanAmount);
        int rpcServiceCount = rpcClassPathScanner.scan(rpcScanBasePackages);
        log.info("rpcClassPathScanner扫描的数量 [{}]", rpcServiceCount);
    }

}
