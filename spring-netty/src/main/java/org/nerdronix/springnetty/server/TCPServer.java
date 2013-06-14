package org.nerdronix.springnetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TCPServer {

	@Autowired
	@Qualifier("serverBootstrap")
	private ServerBootstrap b;
	
	@Autowired
	@Qualifier("tcpSocketAddress")
	private InetSocketAddress tcpPort;

	private Channel serverChannel;

	@PostConstruct
	public void start() throws Exception {
		System.out.println("Starting server at " + tcpPort);
		serverChannel = b.bind(tcpPort).sync().channel().closeFuture().sync()
				.channel();
	}

	@PreDestroy
	public void stop() {
		serverChannel.close();
	}

	public ServerBootstrap getB() {
		return b;
	}

	public void setB(ServerBootstrap b) {
		this.b = b;
	}

	public InetSocketAddress getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(InetSocketAddress tcpPort) {
		this.tcpPort = tcpPort;
	}

}
