package com.afan.rpc.consumer.controller;

import com.afan.rpc.annotation.ARpcReference;
import com.afan.rpc.provider.api.HelloService;
import org.springframework.stereotype.Controller;

/**
 * @author AFan
 * @date 2021/7/3 20:36
 */
@Controller
public class HelloController {

    @ARpcReference(version = "v1.0.0")
    HelloService helloService;

    public String test(String user) {
        System.out.println(helloService.sayHello("广州"));
        return user + " say hello";
    }

}
