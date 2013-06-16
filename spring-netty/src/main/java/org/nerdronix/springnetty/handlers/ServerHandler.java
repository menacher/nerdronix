package org.nerdronix.springnetty.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.MessageList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("serverHandler")
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
		public void messageReceived(ChannelHandlerContext ctx,
				MessageList<Object> msgs) throws Exception
	{
		Channel channel = ctx.channel();
		MessageList<String> vals = msgs.cast();
		for(String str : vals){
			System.out.print(str);
			channel.write(str);
		}
		vals.releaseAll();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel is active\n");
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		System.out.println("\nChannel is disconnected");
		super.channelInactive(ctx);
	}
}
