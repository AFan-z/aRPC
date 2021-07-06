package com.afan.rpc.loadbalance.loadbalancer;

import com.afan.rpc.loadbalance.LoadBalance;
import com.afan.rpc.remote.protocol.RpcRequest;

import java.util.List;

/**
 * @author AFan
 * @date 2021/6/15 16:58
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String select(List<String> addresses, RpcRequest rpcRequest) {
        if (addresses == null || addresses.size() == 0) {
            return null;
        }
        if (addresses.size() == 1) {
            return addresses.get(0);
        }
        return doSelect(addresses, rpcRequest);
    }

    /**
     * 在服务列表中选择一个调用者
     * @param addresses 服务列表
     * @param rpcRequest rpc 请求参数
     * @return 返回其中一个调用者地址
     */
    protected abstract String doSelect(List<String> addresses, RpcRequest rpcRequest);
}
