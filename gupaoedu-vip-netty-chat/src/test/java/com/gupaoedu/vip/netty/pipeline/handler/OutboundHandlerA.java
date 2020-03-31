package com.gupaoedu.vip.netty.pipeline.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutboundHandlerA extends ChannelOutboundHandlerAdapter {
    @Override
    // 向client发送消息  
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {  
        System.out.println("OutboundHandlerA.write");

        ctx.write(msg,promise);
    }


      
      
}  
