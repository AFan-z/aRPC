package com.afan.rpc.consumer;

import com.afan.rpc.annotation.ARpcScan;
import com.afan.rpc.config.ResourceConfig;
import com.afan.rpc.consumer.controller.HelloController;
import com.afan.rpc.consumer.controller.TestController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @author AFan
 * @date 2021/7/3 20:38
 */
//@Slf4j
@ARpcScan(basePackage = {"com.afan.rpc.consumer"})
public class Client {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Client.class);
        HelloController helloController = (HelloController) context.getBean("helloController");
        TestController testController = (TestController) context.getBean("testController");
        System.out.println(helloController.test("广州"));

        testController.test();

        //回车，程序继续执行
        System.in.read();
        System.out.println(helloController.test("广州"));
    }

}
