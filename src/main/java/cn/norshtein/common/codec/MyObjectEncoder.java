package cn.norshtein.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @Author: Norshtein
 * @Date: 2020/6/2
 * @Desc:
 */
public class MyObjectEncoder extends MessageToByteEncoder<Serializable> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        try (ByteBufOutputStream bbout = new ByteBufOutputStream(out);
             ObjectOutputStream oout = new ObjectOutputStream(bbout)){
            oout.writeObject(msg);
            oout.flush();
        }
    }
}