package cn.norshtein.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ObjectInputStream;
import java.util.List;

/**
 * @Author: Norshtein
 * @Date: 2020/6/2
 * @Desc:
 */
public class MyObjectDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try(ByteBufInputStream bbin = new ByteBufInputStream(in);
            ObjectInputStream bin = new ObjectInputStream(bbin)){
            Object decoded = bin.readObject();
            if (decoded != null){
                out.add(decoded);
            }
        }
    }
}