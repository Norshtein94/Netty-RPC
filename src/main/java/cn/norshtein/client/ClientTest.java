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
        for (int i = 0; i < 10; i++) {
            helloService.sayHello();
            System.out.println(helloService.getName());
            System.out.println(helloService.getUserInfo("Norshtein" + i));
        }
    }
}