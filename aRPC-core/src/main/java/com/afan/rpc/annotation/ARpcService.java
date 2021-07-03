package com.afan.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AFan
 * @date 2021/6/15 17:46
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Component
public @interface ARpcService {

    /**
     * 服务接口类, 默认 void.class 时，服务的第一个接口这为服务接口类，
     * 服务继承多个接口需要指定具体接口
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务版本，默认为空
     */
    String version() default "";
}
