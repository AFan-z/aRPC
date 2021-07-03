package com.afan.rpc.remote.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * @author AFan
 * @date 2021/6/14 17:46
 */
@Data
@ToString(callSuper = true)
public class RpcResponse extends Message {

    private static final long serialVersionUID = 4977168388551841090L;
    /**
     * 返回值
     */
    private Object returnValue;
    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
