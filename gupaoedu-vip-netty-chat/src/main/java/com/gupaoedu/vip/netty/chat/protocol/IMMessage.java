package com.gupaoedu.vip.netty.chat.protocol;

import lombok.Data;
import org.msgpack.annotation.Message;



/**
 * 自定义消息实体类
 *
 */
@Message
@Data
public class IMMessage{
	
	private String addr;		//IP地址及端口
	private String cmd;		//命令类型[LOGIN]或者[SYSTEM]或者[LOGOUT]
	private long time;		//命令发送时间
	private int online;		//当前在线人数
	private String sender;  //发送人
	private String receiver;	//接收人
	private String content;		//消息内容
	private String terminal; 	//终端
	
	public IMMessage(){}
	
	public IMMessage(String cmd,long time,int online,String content){
		this.cmd = cmd;
		this.time = time;
		this.online = online;
		this.content = content;
		this.terminal = terminal;
	}

	public IMMessage(String cmd,String terminal,long time,String sender){
		this.cmd = cmd;
		this.time = time;
		this.sender = sender;
		this.terminal = terminal;
	}


	public IMMessage(String cmd,long time,String sender,String content){
		this.cmd = cmd;
		this.time = time;
		this.sender = sender;
		this.content = content;
		this.terminal = terminal;
	}

	@Override
	public String toString() {
		return "IMMessage{" +
				"addr='" + addr + '\'' +
				", cmd='" + cmd + '\'' +
				", time=" + time +
				", online=" + online +
				", sender='" + sender + '\'' +
				", receiver='" + receiver + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
