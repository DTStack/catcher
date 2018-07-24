package com.dtstack.catcher.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.dtstack.catcher.monitor.service.LogService;
import com.dtstack.catcher.monitor.service.MertricService;
import com.dtstack.catcher.monitor.service.RuntimeService;
import com.dtstack.catcher.monitor.service.ThreadService;
import com.dtstack.catcher.network.handler.LogHandler;
import com.dtstack.catcher.network.handler.MetricHandler;
import com.dtstack.catcher.network.handler.RuntimeHandler;
import com.dtstack.catcher.network.handler.ThreadHandler;
import com.sun.net.httpserver.HttpServer;

public class NetServer {

	protected final ExecutorService executorService;
	protected final HttpServer server;

	private MertricService mertricService = new MertricService();
	private LogService logService = new LogService();
	private ThreadService threadService = new ThreadService();
	private RuntimeService runtimeService = new RuntimeService(); 

	public NetServer(String address) throws IOException {
		String[] addrs = address.split(":");
		server = HttpServer.create(new InetSocketAddress(addrs[0], Integer.valueOf(addrs[1])), 3);
		MetricHandler mHandler = new MetricHandler(mertricService);
		LogHandler logHandler = new LogHandler(logService);
		ThreadHandler threadHandler = new ThreadHandler(threadService);
		RuntimeHandler runtimeHandler = new RuntimeHandler(runtimeService); 
		
		server.createContext("/", mHandler);
		server.createContext("/metrics", mHandler);
		server.createContext("/logs", logHandler);
		server.createContext("/threads", threadHandler);
		server.createContext("/runtime", runtimeHandler);
		
		executorService = Executors.newFixedThreadPool(5, new NamedThreadFactory("netserver", true));
		server.setExecutor(executorService);
	}
	
	public void start() {
		server.start();
	}
	
	public void stop() {
		server.stop(0);
	}

	static class NamedThreadFactory implements ThreadFactory {
		
		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		final AtomicInteger threadNumber = new AtomicInteger(1);
		final ThreadGroup group;
		final String namePrefix;
		final boolean isDaemon;

		public NamedThreadFactory() {
			this("pool");
		}

		public NamedThreadFactory(String name) {
			this(name, false);
		}

		public NamedThreadFactory(String preffix, boolean daemon) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = preffix + "-" + poolNumber.getAndIncrement() + "-thread-";
			isDaemon = daemon;
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			t.setDaemon(isDaemon);
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
	}

	public static void main(String[] args) throws IOException {
	}
}
