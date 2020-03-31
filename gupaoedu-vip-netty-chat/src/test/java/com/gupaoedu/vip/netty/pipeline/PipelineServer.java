package com.gupaoedu.vip.netty.pipeline;

import com.gupaoedu.vip.netty.pipeline.handler.*;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class PipelineServer {  
    public void start(int port) throws Exception {  
        EventLoopGroup bossGroup = new NioEventLoopGroup();   
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap b = new ServerBootstrap();   
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)   
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override  
                                public void initChannel(SocketChannel ch) throws Exception {  

                                    // 注册两个InboundHandler，执行顺序为注册顺序，所以应该是A->B->C
                                    ch.pipeline().addLast(new OutboundHandlerC());
                                    ch.pipeline().addLast(new InboundHandlerB());
                                    ch.pipeline().addLast(new OutboundHandlerA());
                                    ch.pipeline().addLast(new InboundHandlerA());

                                    ch.pipeline().addLast(new InboundHandlerC());

                                    // 注册两个OutboundHandler，执行顺序为注册顺序的逆序，所以应该是C->B-A

                                    ch.pipeline().addLast(new OutboundHandlerB());


                                }  
                            }).option(ChannelOption.SO_BACKLOG, 128)   
                    .childOption(ChannelOption.SO_KEEPALIVE, true);   
  
            ChannelFuture f = b.bind(port).sync();   
  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }  
  
    public static void main(String[] args) throws Exception {  
    	PipelineServer server = new PipelineServer();  
        server.start(8000);  
    }  
} 
