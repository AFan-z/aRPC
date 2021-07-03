package com.afan.rpc.remote.handler.server;


import com.afan.rpc.remote.protocol.RpcRequest;
import com.afan.rpc.remote.protocol.RpcResponse;
import com.afan.rpc.utils.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest message) {
        RpcResponse response = new RpcResponse();
        response.setSequenceId(message.getSequenceId());
        try {
            Object service = ServicesFactory.getService(message.getInterfaceName());
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
            //执行本地方法
            Object invoke = method.invoke(service, message.getParameterValue());
            //将返回结果返回远程调用
            response.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错:" + msg));
        }
        ctx.writeAndFlush(response);
    }

}
