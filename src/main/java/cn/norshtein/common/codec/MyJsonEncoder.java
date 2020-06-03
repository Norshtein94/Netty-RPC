package cn.norshtein.common.codec;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @Author: Norshtein
 * @Date: 2020/6/2
 * @Desc:
 */
public class MyJsonEncoder extends MessageToByteEncoder<Serializable> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        if (msg != null){
            try (ByteBufOutputStream bbout = new ByteBufOutputStream(out)){
                bbout.write(JSONObject.toJSONString(msg).getBytes(Charset.defaultCharset()));
                bbout.flush();
            }
        }
    }
}