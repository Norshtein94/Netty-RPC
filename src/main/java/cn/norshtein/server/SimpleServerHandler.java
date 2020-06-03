package cn.norshtein.server;

import cn.norshtein.common.InvocationProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Object> serviceMap = new HashMap<>(16);

    public SimpleServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        process(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        System.out.println("occur exception");
    }

    private void process(ChannelHandlerContext ctx, Object msg) throws  Exception {
        try {
            InvocationProtocol request = (InvocationProtocol) msg;
            String className = request.getClassName();
            Object result = new Object();
            if (serviceMap.containsKey(className)){
                Object target = serviceMap.get(className);
                Method method = target.getClass().getMethod(request.getMethodName(), request.getParams());
                result = method.invoke(target, request.getValues());
            }
            if (result != null){
                ctx.writeAndFlush(result);
            }
        }finally {
            ctx.close();
        }
    }
}