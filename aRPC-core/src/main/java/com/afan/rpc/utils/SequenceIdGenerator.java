package com.afan.rpc.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成自增序列号
 * @author AFan
 */
public abstract class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger(1);

    public static int nextId() {
        return id.incrementAndGet();
    }
}
