package com.afan.rpc.remote.handler.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AFan
 * @date 2021/7/3 16:06
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        IdleStateEvent event = (IdleStateEvent) evt;
        // 触发了读空闲事件
        if (event.state() == IdleState.READER_IDLE) {
            log.debug("长时间没有接收到客服端：{}的数据了，关闭连接", ctx.channel());
            ctx.channel().close();
        }
    }
}
