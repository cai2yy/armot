package lib.cjhttp.server.internal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpServer {
	private final static Logger LOG = LoggerFactory.getLogger(HttpServer.class);

	public String UUID;
	private String ip;
	private int port;
	private int ioThreads;
	private IRequestDispatcher dispatcher;

	public HttpServer(String ip, int port, int ioThreads, IRequestDispatcher dispatcher) {
		this.UUID = java.util.UUID.randomUUID().toString();
		this.ip = ip;
		this.port = port;
		this.ioThreads = ioThreads;
		// TODO 用户定义的dispatcher
		this.dispatcher = dispatcher;
	}

	private ServerBootstrap bootstrap;
	private EventLoopGroup group;
	private Channel serverChannel;
	private MessageCollector collector;

	public void start() {
		bootstrap = new ServerBootstrap();

		var threadFactory = new ThreadFactory() {
			AtomicInteger seq = new AtomicInteger();

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("CJHttp-" + seq.getAndIncrement());
				return t;
			}
		};
		group = new NioEventLoopGroup(ioThreads, threadFactory);

		bootstrap.group(group);
		// dispatcher -> collector
		collector = new MessageCollector(dispatcher);

		bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				var pipe = ch.pipeline();
				//设置连接超时（长连接）
				pipe.addLast(new ReadTimeoutHandler(10));
				pipe.addLast(new HttpServerCodec());
				pipe.addLast(new HttpObjectAggregator(1 << 30)); // max_size = 1g
				pipe.addLast(new ChunkedWriteHandler());
				// TODO dispatcher -> collector
				pipe.addLast(collector);
			}
		});
		bootstrap.option(ChannelOption.SO_BACKLOG, 100).option(ChannelOption.SO_REUSEADDR, true)
				.option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);

		serverChannel = bootstrap.bind(this.ip, this.port).channel();

		LOG.info("http服务启动成功 @ {}:{}, UUID:{}", ip, port, UUID);
	}

	/**
	 * 配合netty的优雅停机函数
	 */
	public void stop() {
		// 先关闭服务端套件字
		serverChannel.close();
		// 再斩断消息来源，停止io线程池
		group.shutdownGracefully();
		// 停止业务线程池
		collector.closeGracefully();
	}

}
