package com.afan.rpc.remote.protocol;

/**
 * @author AFan
 * @date 2021/6/14 17:46
 */
public class Ping extends Message {

    private static final long serialVersionUID = 4868480897246762469L;

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
