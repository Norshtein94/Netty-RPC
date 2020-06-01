package cn.norshtein.client;

import java.lang.reflect.Proxy;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
public class ProxyFactory {
    public static <T> T create(Class<T> clazz){
        RpcProxy rpcProxy = new RpcProxy(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, rpcProxy);
    }
}