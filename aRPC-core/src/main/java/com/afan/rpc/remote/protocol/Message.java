package com.afan.rpc.remote.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * @author AFan
 * @date 2021/6/14 17:46
 */
@Data
public abstract class Message implements Serializable {

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();


    /**
     * 请求类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_REQUEST = 1;
    /**
     * 响应类型 byte 值
     */
    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 2;

    /**
     * 心跳类型
     */
    public static final int PingMessage = 3;
    public static final int PongMessage = 4;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequest.class);
        messageClasses.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponse.class);
        messageClasses.put(PingMessage, Ping.class);
        messageClasses.put(PongMessage, Pong.class);
    }

}
