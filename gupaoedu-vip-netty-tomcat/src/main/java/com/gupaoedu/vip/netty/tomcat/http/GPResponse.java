package com.gupaoedu.vip.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;


public class GPResponse {

	//SocketChannel的封装
	private ChannelHandlerContext ctx;

	private HttpRequest req;

	public GPResponse(ChannelHandlerContext ctx, HttpRequest req) {
		this.ctx = ctx;
		this.req = req;
	}

	public void write(String out) throws Exception {
		try {
			if (out == null || out.length() == 0) {
				return;
			}
			// 设置 http协议及请求头信息
			FullHttpResponse response = new DefaultFullHttpResponse(
				// 设置http版本为1.1
				HttpVersion.HTTP_1_1,
				// 设置响应状态码
				HttpResponseStatus.OK,
				// 将输出值写出 编码为UTF-8
				Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

			response.headers().set("Content-Type", "text/html;");

			ctx.write(response);
		} finally {
			ctx.flush();
			ctx.close();
		}
	}
}
