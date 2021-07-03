package com.afan.rpc.proxy;

import com.afan.rpc.remote.protocol.RpcRequest;
import com.afan.rpc.remote.RpcClient;
import com.afan.rpc.utils.RpcServiceUtil;
import com.afan.rpc.utils.SequenceIdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理类
 *
 * @author AFan
 * @date 2021/6/14 19:23
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private final RpcClient CLIENT;
    private final RpcServiceUtil rpcService;

    public RpcClientProxy(RpcClient CLIENT, RpcServiceUtil rpcServiceUtil) {
        this.CLIENT = CLIENT;
        this.rpcService = rpcServiceUtil;
    }

    public <T> T create(Class<?> clazz) {
        Class<?>[] interfaces = clazz.isInterface()
                ? new Class[]{clazz}
                : clazz.getInterfaces();

        T result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, this);
        return result;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1. 将方法调用转换为 消息对象
        int sequenceId = SequenceIdGenerator.nextId();
        RpcRequest msg = new RpcRequest(
                sequenceId,
                rpcService.getServiceName(),
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes(),
                args
        );
        return CLIENT.sendRequestMsg(msg);
    }

}
