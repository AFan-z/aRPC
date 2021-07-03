package com.afan.rpc.annotation;

import com.afan.rpc.config.spring.RpcScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AFan
 * @date 2021/6/16 15:58
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcScannerRegistrar.class)
@Documented
public @interface ARpcScan {
    String[] basePackage();
}
