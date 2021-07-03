package com.afan.rpc.config.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * 参考 https://github.com/Snailclimb/guide-rpc-framework/blob/master/rpc-framework-simple/src/main/java/github/javaguide/spring/CustomScanner.java
 * 自定义扫描规则
 * @author AFan
 * @date 2021/6/16 16:00
 */
public class RpcClassPathScanner extends ClassPathBeanDefinitionScanner {

    public RpcClassPathScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annoType) {
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annoType));
    }

    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }

}
