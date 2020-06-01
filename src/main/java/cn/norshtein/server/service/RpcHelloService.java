package cn.norshtein.server.service;

import cn.norshtein.annotation.RpcService;
import cn.norshtein.common.IRpcHelloService;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
@RpcService
public class RpcHelloService implements IRpcHelloService {

    @Override
    public void sayHello() {
        System.out.println("Hello, RPC !!!");
    }

    @Override
    public String getName() {
        return "Toma.H.Norshtein!";
    }
}