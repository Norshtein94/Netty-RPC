package cn.norshtein.client;

import cn.norshtein.common.IRpcHelloService;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
public class ClientTest {
    public static void main(String[] args){
        IRpcHelloService helloService = ProxyFactory.create(IRpcHelloService.class);
        helloService.sayHello();
        System.out.println(helloService.getName());
        System.out.println(helloService.getUserInfo("Norshtein"));
    }
}