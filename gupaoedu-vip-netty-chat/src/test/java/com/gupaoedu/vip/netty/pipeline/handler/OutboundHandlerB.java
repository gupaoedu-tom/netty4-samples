package com.gupaoedu.vip.netty.pipeline.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.concurrent.TimeUnit;

public class OutboundHandlerB extends ChannelOutboundHandlerAdapter {
      
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {  
        System.out.println("OutboundHandlerB.write");
        // 执行下一个OutboundHandler
        ctx.write(msg,promise);
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        ctx.executor().schedule(new Runnable() {
            public void run() {
                ctx.channel().write("say hello");
            }
        },3, TimeUnit.SECONDS);
    }
}
