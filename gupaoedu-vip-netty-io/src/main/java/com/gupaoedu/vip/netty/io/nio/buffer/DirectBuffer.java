package com.gupaoedu.vip.netty.io.nio.buffer;
import java.io.*;
import java.nio.*;  
import java.nio.channels.*;


/**
 * 直接缓冲区
 * Zero Copy 减少了一个拷贝的过程
  */
public class DirectBuffer {  
    static public void main( String args[] ) throws Exception {  

        //在Java里面存的只是缓冲区的引用地址
        //管理效率

    	//首先我们从磁盘上读取刚才我们写出的文件内容
        String infile = "E://test.txt";
        FileInputStream fin = new FileInputStream( infile );  
        FileChannel fcin = fin.getChannel();

        //把刚刚读取的内容写入到一个新的文件中
        String outfile = String.format("E://testcopy.txt");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();  
          
        // 使用allocateDirect，而不是allocate
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);  
          
        while (true) {  
            buffer.clear();  
              
            int r = fcin.read(buffer);  
              
            if (r==-1) {  
                break;  
            }  
              
            buffer.flip();  
              
            fcout.write(buffer);  
        }
	}  
}