package com.afan.rpc.consumer.controller;

import com.afan.rpc.annotation.ARpcReference;
import com.afan.rpc.provider.api.TestService;
import org.springframework.stereotype.Controller;

/**
 * @author AFan
 * @date 2021/7/3 21:54
 */
@Controller
public class TestController {

    @ARpcReference
    TestService testService;

    public void test() {
        System.out.println("TestController 正在调用 TestService ... ...");
        testService.test();
    }
}
