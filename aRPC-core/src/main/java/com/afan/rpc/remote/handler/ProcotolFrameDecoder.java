package com.afan.rpc.remote.handler;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * 自定义 定长解码器 必须配合MessageCodecSharable使用
 * @author AFan
 */
public class ProcotolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProcotolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProcotolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
