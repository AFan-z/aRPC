package com.afan.rpc.provider;

import com.afan.rpc.annotation.ARpcScan;
import com.afan.rpc.remote.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @author AFan
 * @date 2021/7/3 20:39
 */
@ARpcScan(basePackage = {"com.afan.rpc.provider"})
public class Server {
    public static void main(String[] args) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Server.class);
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");
        rpcServer.start();
    }
}
