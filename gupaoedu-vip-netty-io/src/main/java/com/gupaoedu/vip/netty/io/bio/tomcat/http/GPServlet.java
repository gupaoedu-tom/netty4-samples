package com.gupaoedu.vip.netty.io.bio.tomcat.http;

/**
 * Created by Tom on 2019/6/15.
 */
public abstract class GPServlet {
    public void service(GPRequest request, GPResponse response) throws Exception{
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }

    public abstract void doGet(GPRequest request, GPResponse response) throws Exception;
    public abstract void doPost(GPRequest request, GPResponse response) throws Exception;
}
