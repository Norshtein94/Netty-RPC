package cn.norshtein.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Service;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
@Service
@ChannelHandler.Sharable
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
    private int count = 0;
    private Object response;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(count++);
        response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("client exception is general");
    }

    public Object getResponse() {
        return response;
    }
}