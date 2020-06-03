package cn.norshtein.client;

import cn.norshtein.common.InvocationProtocol;
import cn.norshtein.common.codec.MyJsonDecoder;
import cn.norshtein.common.codec.MyJsonEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
public class RpcProxy implements InvocationHandler {
    private Class<?> clazz;

    public RpcProxy(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())){
            return method.invoke(proxy, args);
        }
        InvocationProtocol protocol = new InvocationProtocol();
        protocol.setClassName(this.clazz.getName());
        protocol.setMethodName(method.getName());
        protocol.setParams(method.getParameterTypes());
        protocol.setValues(args);
        protocol.setReturnType(method.getReturnType());
        return rpcNettyInvoke(protocol);
    }

    private Object rpcInvoke(InvocationProtocol protocol) throws Exception {
        // 1. 建立连接
        Socket client = new Socket("localhost", 8080);
        Object result = null;
        // 2. 序列化传输
        try (ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(client.getInputStream())){
            os.writeObject(protocol);
            os.flush();
            result = is.readObject();
        }
        return result;
    }

    private Object rpcNettyInvoke(InvocationProtocol protocol) throws Exception {
        final SimpleClientHandler clientHandler = new SimpleClientHandler();
        Bootstrap client = new Bootstrap();
        //第1步 定义线程组，处理读写和链接事件，没有了accept事件
        EventLoopGroup group = new NioEventLoopGroup();
        client.group(group)
            //第2步 绑定客户端通道
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            //第3步 给NIoSocketChannel初始化handler， 处理读写事件
            .handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //使用netty自带编解码器
                /** 入参有5个，分别解释如下
                 maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                 lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                 lengthFieldLength：长度字段的长度：如：长度字段是int型表示，那么这个值就是4（long型就是8）
                 lengthAdjustment：要添加到长度字段值的补偿值
                 initialBytesToStrip：从解码帧中去除的第一个字节数
                 */
//                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                //自定义协议编码器
//                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                //对象参数类型编码器
//                pipeline.addLast("encoder", new ObjectEncoder());
//                //对象参数类型解码器
//                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                // 使用自定义编、解码器
                pipeline.addLast("encoder", new MyJsonEncoder());
                pipeline.addLast("decoder", new MyJsonDecoder(protocol.getReturnType()));
                pipeline.addLast("handler", clientHandler);
            }
        });
        //连接服务器
        ChannelFuture future = client.connect("localhost", 8080).sync();
        future.channel().writeAndFlush(protocol);
        future.channel().closeFuture().sync();
        return clientHandler.getResponse();
    }
}