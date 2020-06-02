package cn.norshtein.server;

import cn.norshtein.annotation.RpcService;
import cn.norshtein.common.codec.MyObjectDecoder;
import cn.norshtein.common.codec.MyObjectEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
@Component
public class RpcNettyServer implements ApplicationContextAware {
    private static final Map<String, Object> serviceMap = new HashMap<>(16);

    public void start(int port) throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            ////使用netty自带编解码器
                            /** 入参有5个，分别解释如下
                             maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                             lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                             lengthFieldLength：长度字段的长度。如：长度字段是int型表示，那么这个值就是4（long型就是8）
                             lengthAdjustment：要添加到长度字段值的补偿值
                             initialBytesToStrip：从解码帧中去除的第一个字节数
                             */
//                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                            //自定义协议编码器
//                            pipeline.addLast(new LengthFieldPrepender(4));
//                            //对象参数类型编码器
//                            pipeline.addLast("encoder",new ObjectEncoder());
//                            //对象参数类型解码器
//                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            //使用netty自带编解码器
                            pipeline.addLast("encoder", new MyObjectEncoder());
                            pipeline.addLast("decoder", new MyObjectDecoder());
                            pipeline.addLast(new SimpleServerHandler(serviceMap));
                        }
                    });
            ChannelFuture f = server.bind(port).sync();  //6
            System.out.println("RPC server is listening on port: " + port);
            f.channel().closeFuture().sync();
            System.out.println("RPC server on port[" + port + "] is closed");
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
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