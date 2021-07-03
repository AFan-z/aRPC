package com.afan.rpc.provider.api;

/**
 * @author AFan
 * @date 2021/7/3 20:30
 */
public interface HelloService {

    /**
     * 服务提供者 api 接口
     *
     * @param msg
     * @return
     */
    String sayHello(String msg);
}
