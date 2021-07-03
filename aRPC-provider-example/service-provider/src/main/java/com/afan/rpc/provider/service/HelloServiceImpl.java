package com.afan.rpc.provider.service;

import com.afan.rpc.annotation.ARpcService;
import com.afan.rpc.provider.api.HelloService;

/**
 * @author AFan
 * @date 2021/7/3 20:31
 */
@ARpcService(version = "v1.0.0")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        //int i = 1 / 0;
        return "你好, " + msg;
    }
}
