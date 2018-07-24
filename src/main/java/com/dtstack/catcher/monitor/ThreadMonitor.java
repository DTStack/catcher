package com.dtstack.catcher.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dtstack.catcher.common.bean.ThreadBean;

public class ThreadMonitor {

	private static long interval = 1000l;

	private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

	static {
		try {
			threadMXBean.setThreadContentionMonitoringEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<ThreadBean> get() {
		
		List<ThreadBean> threadList = new ArrayList<>();
		
		try {
			
			Map<Long, ThreadBean> threadInfos = new HashMap<>();
			for (long id : threadMXBean.getAllThreadIds()) {
				long cpuTime = threadMXBean.getThreadCpuTime(id) / 1000000;
				ThreadInfo info = threadMXBean.getThreadInfo(id);
				threadInfos.put(id, new ThreadBean(cpuTime, info));
			}

			Thread.sleep(interval);

			for (long id : threadMXBean.getAllThreadIds()) {
				long cpuTime = threadMXBean.getThreadCpuTime(id) / 1000000;
				ThreadInfo info = threadMXBean.getThreadInfo(id, 100);
				threadInfos.get(id).setDelta(cpuTime, info);
			}

			threadList.addAll(threadInfos.values());

			Collections.sort(threadList, new Comparator<ThreadBean>() {

				@Override
				public int compare(ThreadBean o1, ThreadBean o2) {
					long score1 = 100 * o1.getCpuTime() + 50 * o1.getBlockedTime();
					long score2 = 100 * o2.getCpuTime() + 50 * o2.getBlockedTime();

					if (score1 >= score2) {
						return -1;
					} else {
						return 1;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return threadList;
	}

	public static void main(String[] args) throws InterruptedException {
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		Map<Long, ThreadBean> threadInfos = new HashMap<>();
		for (long id : threadMXBean.getAllThreadIds()) {
			long cpuTime = threadMXBean.getThreadCpuTime(id);
			ThreadInfo info = threadMXBean.getThreadInfo(id);
			threadInfos.put(id, new ThreadBean(cpuTime, info));
		}
		System.out.println(threadInfos);
		Thread.sleep(1000l);
		for (long id : threadMXBean.getAllThreadIds()) {
			long cpuTime = threadMXBean.getThreadCpuTime(id);
			ThreadInfo info = threadMXBean.getThreadInfo(id);
			threadInfos.get(id).setDelta(cpuTime, info);
		}
		System.out.println("dd:" + threadInfos);

		List<ThreadBean> threadList = new ArrayList<>(threadInfos.values());
		Collections.sort(threadList, new Comparator<ThreadBean>() {

			@Override
			public int compare(ThreadBean o1, ThreadBean o2) {
				long score1 = 100 * o1.getCpuTime() + 50 * o1.getBlockedTime();
				long score2 = 100 * o2.getCpuTime() + 50 * o2.getBlockedTime();

				if (score1 >= score2) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		for (ThreadBean b : threadList) {
			System.out.println(b.getInfo().getThreadName() + ":" + b.getCpuTime() + ":" + b.getBlockedTime() + ":"
					+ b.getWaitedTime());

		}

	}
}
