package com.afan.rpc.loadbalance;

import com.afan.rpc.extension.SPI;
import com.afan.rpc.remote.protocol.RpcRequest;

import java.util.List;

/**
 * 负载均衡算法
 * @author AFan
 * @date 2021/6/15 16:57
 */
@SPI
public interface LoadBalance {

    /**
     * 选择服务获取具体服务url
     * @param addresses
     * @param rpcRequest
     * @return
     */
    String select(List<String> addresses, RpcRequest rpcRequest);
}
