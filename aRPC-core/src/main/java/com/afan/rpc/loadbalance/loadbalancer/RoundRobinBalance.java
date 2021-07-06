package com.afan.rpc.loadbalance.loadbalancer;

import com.afan.rpc.remote.protocol.RpcRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询算法
 * @author AFan
 * @date 2021/7/5 13:12
 */
public class RoundRobinBalance extends AbstractLoadBalance{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 防止Integer越界 超过Integer最大值
     *
     * @return
     */
    private final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0 : current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        return next;
    }

    @Override
    protected String doSelect(List<String> addresses, RpcRequest rpcRequest) {
        int index = getAndIncrement() % addresses.size();
        return addresses.get(index);
    }
}
