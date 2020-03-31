package com.gupaoedu.vip.netty.pipeline.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientIntHandler extends ChannelInboundHandlerAdapter {  
    @Override
    // 读取服务端的信息  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        System.out.println("ClientIntHandler.channelRead");  
        ByteBuf result = (ByteBuf) msg;  
        byte[] result1 = new byte[result.readableBytes()];  
        result.readBytes(result1);  
        result.release();  
        ctx.close();  
        System.out.println("Server said:" + new String(result1));  
    }  
    @Override
    // 当连接建立的时候向服务端发送消息 ，channelActive 事件当连接建立的时候会触发  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
    	System.out.println("ClientIntHandler.channelActive");  
        String msg = "Are you ok?";  
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());  
        encoded.writeBytes(msg.getBytes());  
        ctx.write(encoded);  
        ctx.flush();  
    }  
} 
