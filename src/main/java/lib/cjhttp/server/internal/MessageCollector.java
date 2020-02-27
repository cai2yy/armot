package lib.cjhttp.server.internal;

import com.cai2yy.armot.core.ArmOT;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.concurrent.EventExecutorGroup;
import lib.cjioc.iockids.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

/**
 * 无状态类
 * Sharable：支持并发：可以被多个channel（连接）安全地共享
 */
@Sharable
public class MessageCollector extends ChannelInboundHandlerAdapter {

	ArmOT armOT;

	private EventExecutorGroup executors;
	private IRequestDispatcher dispatcher;

	private final static Logger LOG = LoggerFactory.getLogger(MessageCollector.class);

	public MessageCollector(IRequestDispatcher dispatcher) {


		this.armOT = Injector.getInjector().getInstance(ArmOT.class);
		this.executors = armOT.getExecutors();
		this.dispatcher = dispatcher;
	}

	public void closeGracefully() {
		executors.shutdownGracefully();
	}

	/**
	 * 有新客户端连接连入
	 * @param ctx 绑定的handler上下文
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("connection comes {}", ctx.channel().remoteAddress());
		//TODO 分布式
		// ArmOTContextBus.initNewContext();
	}

	/**
	 * 有客户端断开连接
	 * @param ctx 绑定的handler上下文
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOG.info("connection leaves {}", ctx.channel().remoteAddress());
		ctx.close();
	}

	/**
	 * 读取客户端传入信息，交给线程池处理
	 * @param ctx 绑定的handler上下文
	 * @param msg 客户端传入的信息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 此处目前只支持处理Http协议
		if (msg instanceof FullHttpRequest) {
			var req = (FullHttpRequest) msg;
			this.executors.execute(() -> {
				dispatcher.dispatch(ctx, req);
			});
		}
	}

	/**
	 * 错误拦截器
	 * @param ctx 绑定的handler上下文
	 * @param cause 报错原因
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
