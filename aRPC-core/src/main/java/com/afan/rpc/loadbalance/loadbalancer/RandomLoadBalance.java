package com.afan.rpc.loadbalance.loadbalancer;

import com.afan.rpc.loadbalance.AbstractLoadBalance;
import com.afan.rpc.remote.protocol.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 *
 * @author AFan
 * @date 2021/6/15 17:11
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    /**
     * 随机获取实例
     * @param addresses 服务列表
     * @param rpcRequest rpc 请求参数
     * @return
     */
    @Override
    protected String doSelect(List<String> addresses, RpcRequest rpcRequest) {
        Random random = new Random();
        return addresses.get(random.nextInt(addresses.size()));
    }
}
