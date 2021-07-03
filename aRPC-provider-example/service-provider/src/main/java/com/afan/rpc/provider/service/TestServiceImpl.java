package com.afan.rpc.provider.service;

import com.afan.rpc.annotation.ARpcService;
import com.afan.rpc.provider.api.TestService;

/**
 * @author AFan
 * @date 2021/7/3 21:51
 */
@ARpcService
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        System.out.println("TestServiceImpl ... ...");
    }
}
