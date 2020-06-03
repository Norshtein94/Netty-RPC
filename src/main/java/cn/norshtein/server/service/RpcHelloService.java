package cn.norshtein.server.service;

import cn.norshtein.annotation.RpcService;
import cn.norshtein.common.IRpcHelloService;
import cn.norshtein.common.UserInfo;

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

    @Override
    public UserInfo getUserInfo(String userName) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(userName);
        userInfo.setAge(18);
        userInfo.setAddress("三里屯");
        return userInfo;
    }
}