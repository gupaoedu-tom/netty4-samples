package com.gupaoedu.vip.netty.io.nio.buffer;
import java.nio.*;
/**
 * 只读缓冲区
 */
public class ReadOnlyBuffer {  
	static public void main( String args[] ) throws Exception {  
		ByteBuffer buffer = ByteBuffer.allocate( 10 );  
	    
		// 缓冲区中的数据0-9  
		for (int i=0; i<buffer.capacity(); ++i) {  
			buffer.put( (byte)i );  
		}  
	
		// 创建只读缓冲区  
		ByteBuffer readonly = buffer.asReadOnlyBuffer();  
	    
		// 改变原缓冲区的内容  
		for (int i=0; i<buffer.capacity(); ++i) {  
			byte b = buffer.get( i );  
			b *= 10;  
			buffer.put( i, b );  
		}  
	    
		readonly.position(0);  
		readonly.limit(buffer.capacity());  
	    
		// 只读缓冲区的内容也随之改变  
		while (readonly.remaining()>0) {  
			System.out.println( readonly.get());  
		}
	}
}