package com.dtstack.catcher.monitor.log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogMonitor {
	private static final int queueCapacity = 10;
	private static BlockingQueue<String> recentLogQueue = new LinkedBlockingQueue<>(queueCapacity);
	private static BlockingQueue<String> errorLogQueue = new LinkedBlockingQueue<>(queueCapacity);
	
	public static void pushToErrorLogQueue(String message) {
		pushToQueue(message, errorLogQueue);
	}

	public static void pushToRecentLogQueue(String message) {
		pushToQueue(message, recentLogQueue);
	}

	public static void pushToQueue(String message, BlockingQueue<String> queue) {
		if (queue.remainingCapacity() < 1) {
			queue.poll();
		}
		queue.offer(message);
	}
	
	public static List<String> recentLog() {
		return listQueueData(recentLogQueue);
	}
	
	public static List<String> errorLog() {
		return listQueueData(errorLogQueue);
	}

	public static List<String> listQueueData(BlockingQueue<String> queue) {
		String[] data = new String[queue.size()];
		queue.toArray(data);
		return Arrays.asList(data);
	}
}
