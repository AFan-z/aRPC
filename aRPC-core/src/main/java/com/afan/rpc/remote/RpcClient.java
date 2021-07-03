package com.afan.rpc.remote;

import com.afan.rpc.config.ResourceConfig;
import com.afan.rpc.extension.ExtensionLoader;
import com.afan.rpc.loadbalance.LoadBalance;
import com.afan.rpc.registry.RegistryFactory;
import com.afan.rpc.remote.handler.client.HeartBeatClientHandler;
import com.afan.rpc.remote.protocol.RpcRequest;
import com.afan.rpc.remote.handler.MessageCodecSharable;
import com.afan.rpc.remote.handler.ProcotolFrameDecoder;
import com.afan.rpc.remote.handler.client.RpcResponseMessageHandler;
import com.afan.rpc.registry.ServiceDiscovery;
import com.afan.rpc.utils.RpcServiceUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端代理类
 *
 * @author AFan
 * @date 2021/6/14 19:23
 */
@Slf4j
public class RpcClient {
    private final ServiceDiscovery serviceDiscovery;
    private static final Bootstrap bootstrap;
    private static final NioEventLoopGroup group;
    public static final Map<String, Channel> CHANNELS;

    static {
        CHANNELS = new ConcurrentHashMap<>();
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        initChannel();
    }

    public RpcClient() {
        LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(ResourceConfig.LOAD_BALANCE_TYPE);
        this.serviceDiscovery = ExtensionLoader
                                .getExtensionLoader(RegistryFactory.class)
                                .getExtension(ResourceConfig.REGISTRY_TYPE)
                                .getServiceDiscovery(loadBalance);
    }

    public Object sendRequestMsg(RpcRequest msg) throws InterruptedException {
        //从注册中心获取服务 ip
        InetSocketAddress inetSocketAddress = serviceDiscovery.discoveryService(RpcServiceUtil.PROVIDE_PREFIX + msg.getInterfaceName());

        // 1. 将消息对象发送出去
        getChannel(inetSocketAddress).writeAndFlush(msg);

        // 2. 准备一个空 Promise 对象，来接收结果             指定 promise 对象异步接收结果线程
        DefaultPromise<Object> promise = new DefaultPromise<>(getChannel(inetSocketAddress).eventLoop());
        RpcResponseMessageHandler.PROMISES.put(msg.getSequenceId(), promise);

        // 3. 等待 promise 结果
        promise.await();
        if (promise.isSuccess()) {
            // 调用正常
            return promise.getNow();
        } else {
            // 调用失败
            throw new RuntimeException(promise.cause());
        }
    }

    /**
     * 获取channel, 如果不存在，则新建channel并建立连接
     * @return
     */
    private Channel getChannel(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        //判断是否存在
        if (CHANNELS.containsKey(key)) {
            Channel channel = CHANNELS.get(key);
            if (CHANNELS != null && channel.isActive()) {
                return channel;
            }
            CHANNELS.remove(key);
        }

        Channel channel = null;
        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
            channel.closeFuture().addListener(future -> {
                log.debug("Client close ...");
            });
        } catch (InterruptedException e) {
            channel.close();
            log.error("连接客户端出错" + e);
            return null;
        }
        CHANNELS.put(key, channel);
        return channel;
    }

    /**
     * 初始化 channel 方法
     */
    private static void initChannel() {
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        HeartBeatClientHandler HEARTBEAT_HANDLER = new HeartBeatClientHandler();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();

        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProcotolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
                // 50s 内如果没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
                ch.pipeline().addLast(new IdleStateHandler(0, 50, 0));
                // ChannelDuplexHandler 可以同时作为入站和出站处理器
                ch.pipeline().addLast(HEARTBEAT_HANDLER);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
    }

}
