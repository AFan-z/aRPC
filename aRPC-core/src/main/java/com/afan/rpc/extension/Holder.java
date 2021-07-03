package com.afan.rpc.extension;

/**
 * Helper Class for hold a value.
 * @author AFan
 * @date 2021/7/3 13:48
 */
public class Holder<T> {
    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}
