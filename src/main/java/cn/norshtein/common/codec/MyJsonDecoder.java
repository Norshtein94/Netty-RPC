package cn.norshtein.common.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author: Norshtein
 * @Date: 2020/6/2
 * @Desc:
 */
public class MyJsonDecoder extends ByteToMessageDecoder {

    private Class clazz;

    public MyJsonDecoder(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 0){
            ByteBuf byteBuf = in.readBytes(in.readableBytes());
            String json = byteBuf.toString(Charset.defaultCharset());
            Object decoded = JSONObject.parseObject(json, clazz);
            if (decoded != null){
                out.add(decoded);
            }
        }
    }
}