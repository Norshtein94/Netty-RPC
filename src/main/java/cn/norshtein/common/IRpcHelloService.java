package cn.norshtein.common;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
public interface IRpcHelloService {
    void sayHello();

    String getName();

    UserInfo getUserInfo(String userName);
}