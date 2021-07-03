package com.afan.rpc;

import com.afan.rpc.extension.ExtensionLoader;
import com.afan.rpc.loadbalance.LoadBalance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AFan
 * @date 2021/7/3 14:56
 */
public class SpiTest {
    @Test
    public void spiTest() {
        LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("randomLoadBalance");
        System.out.println(loadBalance);
        List<String> list = new ArrayList<>();
        list.add("192.168.1.13:8080");
        list.add("192.168.1.14:8080");
        list.add("192.168.1.15:8080");
        list.add("192.168.1.16:8080");
        list.add("192.168.1.17:8080");
        System.out.println(loadBalance.select(list, null));
    }
}
