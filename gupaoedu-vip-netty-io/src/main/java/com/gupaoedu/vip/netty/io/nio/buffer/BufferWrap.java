package com.gupaoedu.vip.netty.io.nio.buffer;
import java.nio.ByteBuffer;

/**
 * 手动分配缓冲区
 */
public class BufferWrap {  
    
    public void myMethod() {  
        // 分配指定大小的缓冲区  
        ByteBuffer buffer1 = ByteBuffer.allocate(10);  
          
        // 包装一个现有的数组  
        byte array[] = new byte[10];  
        ByteBuffer buffer2 = ByteBuffer.wrap( array );
    } 
}