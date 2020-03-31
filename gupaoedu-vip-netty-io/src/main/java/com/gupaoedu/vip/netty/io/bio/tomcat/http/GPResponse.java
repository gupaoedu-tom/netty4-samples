package com.gupaoedu.vip.netty.io.bio.tomcat.http;

import java.io.OutputStream;

/**
 * Created by Tom on 2019/6/15.
 */
public class GPResponse {
    private OutputStream out;
    public GPResponse(OutputStream out){
        this.out = out;
    }

    public void write(String s) throws Exception {
        //用的是HTTP协议，输出也要遵循HTTP协议
        //给到一个状态码 200
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(s);
        out.write(sb.toString().getBytes());
    }
}
