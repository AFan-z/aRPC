package com.afan.rpc.remote.protocol;

/**
 * @author AFan
 * @date 2021/6/14 17:46
 */
public class Pong extends Message {

    private static final long serialVersionUID = -5491084991984778L;

    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
