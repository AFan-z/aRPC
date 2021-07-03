package com.afan.rpc.remote;

import com.afan.rpc.config.ResourceConfig;
import com.afan.rpc.remote.handler.MessageCodecSharable;
import com.afan.rpc.remote.handler.ProcotolFrameDecoder;
import com.afan.rpc.remote.handler.server.HeartBeatServerHandler;
import com.afan.rpc.remote.handler.server.RpcRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author AFan
 */
@Slf4j
@Component
public class RpcServer{

    public void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        HeartBeatServerHandler HEARTBEAT_HANDLER = new HeartBeatServerHandler();
        RpcRequestMessageHandler RPC_HANDLER = new RpcRequestMessageHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                    // 60s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
                    ch.pipeline().addLast(new IdleStateHandler(60, 0, 0));
                    // ChannelDuplexHandler 可以同时作为入站和出站处理器
                    ch.pipeline().addLast(HEARTBEAT_HANDLER);
                    ch.pipeline().addLast(RPC_HANDLER);
                }
            });
            log.info("bind port: {}", ResourceConfig.PORT);
            Channel channel = serverBootstrap.bind(ResourceConfig.PORT).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
