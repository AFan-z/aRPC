package com.afan.rpc.utils;

import com.afan.rpc.annotation.ARpcService;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author AFan
 * @date 2021/6/16 12:41
 */
@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class RpcServiceUtil {

    public final static String PROVIDE_PREFIX = "provides:";

    public final static String CONSUMER_PREFIX = "consumers:";

    private String version = "";

    private Class<?> interfaces;

    private Object service;

    /**
     * 获取第一个接口名字
     * @return
     */
    public String getServiceDefaultInterfaceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

    /**
     * 获取服务名
     * @param annotation
     * @return
     */
    public String getServiceName(Class<?> annotation) {
        String serviceName = "";

        if (annotation == ARpcService.class) {
            serviceName += PROVIDE_PREFIX;
        }else {
            serviceName += CONSUMER_PREFIX;
        }

        if (interfaces != null) {
            serviceName += interfaces.getName();
        } else {
            serviceName += getServiceDefaultInterfaceName();
        }

        if (StringUtils.hasLength(version)) {
            serviceName += ":" + version;
        }

        return serviceName;
    }

    /**
     * 获取服务名
     * @return
     */
    public String getServiceName(){
        String serviceName = "";

        if (interfaces != null) {
            serviceName += interfaces.getName();
        } else {
            serviceName += getServiceDefaultInterfaceName();
        }

        if (StringUtils.hasLength(version)) {
            serviceName += ":" + version;
        }

        return serviceName;
    }

}
