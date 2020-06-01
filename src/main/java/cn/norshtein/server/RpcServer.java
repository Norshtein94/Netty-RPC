package cn.norshtein.server;

import cn.norshtein.annotation.RpcService;
import cn.norshtein.common.InvocationProtocol;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
@Component
public class RpcServer implements ApplicationContextAware {
    private static final Map<String, Object> serviceMap = new HashMap<>(16);

    public void start(int port) throws Exception {
        ServerSocket server = new ServerSocket(port);
        System.out.println("RPC server is listening on port: " + port);
        while (true){
            Socket client = server.accept();
            process(client);
        }
    }

    private void process(Socket client) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try (ObjectInputStream is = new ObjectInputStream(client.getInputStream());
             ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream())){
            InvocationProtocol request = (InvocationProtocol) is.readObject();
            String className = request.getClassName();
            Object target = serviceMap.get(className);
            Object result = null;
            if (request.getParams() == null || request.getParams().length == 0){
                Method method = target.getClass().getMethod(request.getMethodName());
                result = method.invoke(target);
            }else {
                Method method = target.getClass().getMethod(request.getMethodName(), request.getParams());
                result = method.invoke(target, request.getValues());
            }
            os.writeObject(result);
            os.flush();
        }finally {
            client.close();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object bean : beans.values()) {
            serviceMap.put(bean.getClass().getInterfaces()[0].getName(), bean);
        }
    }
}