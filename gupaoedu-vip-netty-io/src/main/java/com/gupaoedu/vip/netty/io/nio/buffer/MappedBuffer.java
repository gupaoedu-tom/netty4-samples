package com.gupaoedu.vip.netty.io.nio.buffer;
import java.io.*;
import java.nio.*;  
import java.nio.channels.*;

/**
 * IO映射缓冲区
 */
public class MappedBuffer {  
    static private final int start = 0;
    static private final int size = 26;
      
    static public void main( String args[] ) throws Exception {  
        RandomAccessFile raf = new RandomAccessFile( "E://test.txt", "rw" );
        FileChannel fc = raf.getChannel();
        
        //把缓冲区跟文件系统进行一个映射关联
        //只要操作缓冲区里面的内容，文件内容也会跟着改变
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE,start, size );
          
        mbb.put( 0, (byte)97 );  //a
        mbb.put( 25, (byte)122 );   //z

        raf.close();  
    }  
}