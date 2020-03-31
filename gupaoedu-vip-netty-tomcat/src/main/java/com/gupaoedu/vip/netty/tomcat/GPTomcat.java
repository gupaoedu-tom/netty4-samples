package com.gupaoedu.vip.netty.tomcat;


import com.gupaoedu.vip.netty.tomcat.http.GPRequest;
import com.gupaoedu.vip.netty.tomcat.http.GPResponse;
import com.gupaoedu.vip.netty.tomcat.http.GPServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.FileInputStream;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


//Netty就是一个同时支持多协议的网络通信框架
public class GPTomcat {
	//打开Tomcat源码，全局搜索ServerSocket

	private int port = 8080;

	private Map<String,GPServlet> servletMapping = new HashMap<String,GPServlet>();

	private Properties webxml = new Properties();

	private void init(){

		//加载web.xml文件,同时初始化 ServletMapping对象
		try{
			String WEB_INF = this.getClass().getResource("/").getPath();
			FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");

			webxml.load(fis);

			for (Object k : webxml.keySet()) {

				String key = k.toString();
				if(key.endsWith(".url")){
					String servletName = key.replaceAll("\\.url$", "");
					String url = webxml.getProperty(key);
					String className = webxml.getProperty(servletName + ".className");
					GPServlet obj = (GPServlet)Class.forName(className).newInstance();
					servletMapping.put(url, obj);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void start(){

		init();


		//eventLoop-1-XXX

		//Netty封装了NIO，Reactor模型，Boss，worker
		// Boss线程
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// Worker线程
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {

			//1、创建对象
			// Netty服务
			//ServetBootstrap   ServerSocketChannel
			ServerBootstrap server = new ServerBootstrap();


			//2、配置参数
			// 链路式编程
			server.group(bossGroup, workerGroup)
					// 主线程处理类,看到这样的写法，底层就是用反射
					.channel(NioServerSocketChannel.class)
					// 子线程处理类 , Handler
					.childHandler(new ChannelInitializer<SocketChannel>() {
						// 客户端初始化处理
						protected void initChannel(SocketChannel client) throws Exception {
							// 无锁化串行编程
							//Netty对HTTP协议的封装，顺序有要求
							// HttpResponseEncoder 编码器
							// 责任链模式，双向链表Inbound OutBound
							client.pipeline().addLast(new HttpResponseEncoder());
							// HttpRequestDecoder 解码器
							client.pipeline().addLast(new HttpRequestDecoder());
							// 业务逻辑处理
							client.pipeline().addLast(new GPTomcatHandler());
						}
					})
					// 针对主线程的配置 分配线程最大数量 128
					.option(ChannelOption.SO_BACKLOG, 128)
					// 针对子线程的配置 保持长连接
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			//3、启动服务器
			ChannelFuture f = server.bind(port).sync();
			System.out.println("GP Tomcat 已启动，监听的端口是：" + port);
			f.channel().closeFuture().sync();
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			// 关闭线程池
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public class GPTomcatHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

			if (msg instanceof HttpRequest){
				System.out.println("hello");
				HttpRequest req = (HttpRequest) msg;

				// 转交给我们自己的request实现
				GPRequest request = new GPRequest(ctx,req);
				// 转交给我们自己的response实现
				GPResponse response = new GPResponse(ctx,req);
				// 实际业务处理
				String url = request.getUrl();

				if(servletMapping.containsKey(url)){
					servletMapping.get(url).service(request, response);
				}else{
					response.write("404 - Not Found");
				}

			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		}

	}

	public static void main(String[] args) {
		new GPTomcat().start();
	}

}
