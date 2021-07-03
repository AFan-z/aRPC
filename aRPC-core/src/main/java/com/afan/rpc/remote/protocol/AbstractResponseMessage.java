package com.afan.rpc.remote.protocol;

import lombok.Data;
import lombok.ToString;
/**
 * @author AFan
 * @date 2021/6/14 17:46
 */
@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    private boolean success;
    private String reason;

    public AbstractResponseMessage() {}
    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
