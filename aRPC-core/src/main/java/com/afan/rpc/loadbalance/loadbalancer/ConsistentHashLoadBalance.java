package com.afan.rpc.loadbalance.loadbalancer;

import com.afan.rpc.remote.protocol.RpcRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 参考 https://dubbo.apache.org/zh/blog/2019/05/01/dubbo-%E4%B8%80%E8%87%B4%E6%80%A7hash%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1%E5%AE%9E%E7%8E%B0%E5%89%96%E6%9E%90/
 *      2.7.8 org.apache.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance
 * 哈希一致性算法
 * @author AFan
 * @date 2021/7/6 12:59
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance{

    private final ConcurrentMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    protected String doSelect(List<String> addresses, RpcRequest request) {
        String methodName = request.getMethodName();
        // key格式：接口名.方法名
        String key = request.getInterfaceName()+ "." + methodName;

        // addressesHashCode 用来识别invokers是否发生过变更
        // using the hashcode of list to compute the hash only pay attention to the elements in the list
        int addressesHashCode = addresses.hashCode();

        ConsistentHashSelector selector = (ConsistentHashSelector) selectors.get(key);
        if (selector == null || selector.identityHashCode != addressesHashCode) {
            selectors.put(key, new ConsistentHashSelector(addresses, addressesHashCode));
            selector = (ConsistentHashSelector) selectors.get(key);
        }
        return selector.select(request);
    }

    private static final class ConsistentHashSelector {

        /**
         * 存储Hash值与节点映射关系的TreeMap
         */
        private final TreeMap<Long, String> virtualInvokers;

        /**
         * 节点数目 默认 160 TODO 之后可改成配置文件获取值
         */
        private final int replicaNumber = 160;

        /**
         * 用来识别Invoker列表是否发生变更的Hash码
         */
        private final int identityHashCode;

        /**
         * 请求中用来作Hash映射的参数的索引
         */
//        private final int[] argumentIndex;

        ConsistentHashSelector(List<String> invokers, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;

//            argumentIndex = new int[index.length];
//            for (int i = 0; i < index.length; i++) {
//                argumentIndex[i] = Integer.parseInt(index[i]);
//            }
            for (String invoker : invokers) {
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(invoker + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }


        public String select(RpcRequest invocation) {
            //默认以所有参数值作为 key
            String key = invocation.getInterfaceName() + toKey(invocation.getParameterValue());
//            String key = invocation.getInterfaceName() + ":" + invocation.getParameterValue();
            byte[] digest = md5(key);
            return selectForKey(hash(digest, 0));
        }

        /**
         * 根据参数索引获取参数，并将所有参数拼接成字符串
         */
//        private String toKey(Object[] args) {
//            StringBuilder buf = new StringBuilder();
//            for (int i : argumentIndex) {
//                if (i >= 0 && i < args.length) {
//                    buf.append(args[i]);
//                }
//            }
//            return buf.toString();
//        }
        private String toKey(Object[] args) {
            if (args == null) {
                return null;
            }
            StringBuilder buf = new StringBuilder();
            for (Object arg : args) {
                buf.append(arg).append(",");
            }
            return buf.toString();
        }

        /**
         * 根据参数字符串的md5编码找出Invoker
         * @param hash
         * @return
         */
        private String selectForKey(long hash) {
            Map.Entry<Long, String> entry = virtualInvokers.ceilingEntry(hash);
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }

        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        private byte[] md5(String value) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.reset();
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            md5.update(bytes);
            return md5.digest();
        }

    }
}
