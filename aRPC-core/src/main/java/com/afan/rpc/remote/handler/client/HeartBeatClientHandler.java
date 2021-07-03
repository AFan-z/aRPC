package com.afan.rpc.remote.handler.client;

import com.afan.rpc.remote.protocol.Message;
import com.afan.rpc.remote.protocol.Ping;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AFan
 * @date 2021/7/3 15:59
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatClientHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        // 触发了写空闲事件
        if (event.state() == IdleState.WRITER_IDLE) {
            log.debug("用户暂无操作，发送一个心跳包，防止连接断开");
            Ping message = new Ping();
            message.setSequenceId(0);
            message.setMessageType(Message.PingMessage);
            ctx.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("远程调用出错");
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelUnregistered, 远程服务与断开");
        ctx.close();
        super.channelUnregistered(ctx);
    }
}
