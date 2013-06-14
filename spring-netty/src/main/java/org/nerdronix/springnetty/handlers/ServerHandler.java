package org.nerdronix.springnetty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("serverHandler")
public class ServerHandler extends ChannelInboundMessageHandlerAdapter<String> {

	@Override
	public void messageReceived(ChannelHandlerContext arg0, String msg)
			throws Exception {
		System.out.println("Incomng ->" + msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Channel is active");
		super.channelActive(ctx);
	}
}
