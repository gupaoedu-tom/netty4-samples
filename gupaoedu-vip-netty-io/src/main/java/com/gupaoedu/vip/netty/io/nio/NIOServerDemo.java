package com.gupaoedu.vip.netty.io.nio;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO的操作过于繁琐，于是才有了Netty
 * Netty就是对这一系列非常繁琐的操作进行了封装
 *
 * Created by Tom.
 */
public class NIOServerDemo {

    private int port = 8080;

    //准备两个东西
    //轮询器 Selector 大堂经理
    private Selector selector;
    //缓冲区 Buffer 等候区
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    //初始化完毕
    public NIOServerDemo(int port){
        //初始化大堂经理，开门营业
        try {
            this.port = port;
            ServerSocketChannel server = ServerSocketChannel.open();


            //大堂经理准备就绪，接客
            selector = Selector.open();

            //在门口翻牌子，正在营业
            server.register(selector, SelectionKey.OP_ACCEPT);

            //BIO 升级版本 NIO，为了兼容BIO，NIO模型默认是采用阻塞式
            server.configureBlocking(false);
            //我得告诉地址
            //IP/Port
            server.bind(new InetSocketAddress(this.port));







        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen(){
        System.out.println("listen on " + this.port + ".");
        try {
            //轮询主线程

            while (true){
                //大堂经理再叫号
                selector.select();
                //每次都拿到所有的号子
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                //不断地迭代，就叫轮询
                //同步体现在这里，因为每次只能拿一个key，每次只能处理一种状态
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    iter.remove();
                    //每一个key代表一种状态
                    //没一个号对应一个业务
                    //数据就绪、数据可读、数据可写 等等等等
                    process(key);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //具体办业务的方法，坐班柜员
    //每一次轮询就是调用一次process方法，而每一次调用，只能干一件事
    //在同一时间点，只能干一件事
    private void process(SelectionKey key) throws IOException {
        //针对于每一种状态给一个反应
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel)key.channel();
            //这个方法体现非阻塞，不管你数据有没有准备好
            //你给我一个状态和反馈
            SocketChannel channel = server.accept();
            //一定一定要记得设置为非阻塞
            channel.configureBlocking(false);
            //当数据准备就绪的时候，将状态改为可读
            key = channel.register(selector,SelectionKey.OP_READ);
        }
        else if(key.isReadable()){
            //key.channel 从多路复用器中拿到客户端的引用
            SocketChannel channel = (SocketChannel)key.channel();
            int len = channel.read(buffer);
            if(len > 0){
                buffer.flip();
                String content = new String(buffer.array(),0,len);
                key = channel.register(selector,SelectionKey.OP_WRITE);
                //在key上携带一个附件，一会再写出去
                key.attach(content);
                System.out.println("读取内容：" + content);
            }
        }
        else if(key.isWritable()){
            SocketChannel channel = (SocketChannel)key.channel();

            String content = (String)key.attachment();
            channel.write(ByteBuffer.wrap(("输出：" + content).getBytes()));

            channel.close();
        }
    }

    public static void main(String[] args) {
        new NIOServerDemo(8080).listen();
    }




}
