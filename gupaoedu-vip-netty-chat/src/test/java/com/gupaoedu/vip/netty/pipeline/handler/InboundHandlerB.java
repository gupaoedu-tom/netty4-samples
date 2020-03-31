package com.gupaoedu.vip.netty.pipeline.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InboundHandlerB extends ChannelInboundHandlerAdapter {
  
    @Override
    // 读取Client发送的信息，并打印出来  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        System.out.println("InboundHandlerB");
        ctx.fireChannelRead(msg);
    }  

  
}   
