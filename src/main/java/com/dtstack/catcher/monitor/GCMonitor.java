package com.dtstack.catcher.monitor;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

import com.dtstack.catcher.info.GC;

public class GCMonitor {

	private static GarbageCollectorMXBean fullGC;
	private static GarbageCollectorMXBean youngGC;

	static {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		List<GarbageCollectorMXBean> list = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean item : list) {
			if ("ConcurrentMarkSweep".equals(item.getName()) || "MarkSweepCompact".equals(item.getName())
					|| "PS MarkSweep".equals(item.getName()) || "G1 Old Generation".equals(item.getName())
					|| "Garbage collection optimized for short pausetimes Old Collector".equals(item.getName())
					|| "Garbage collection optimized for throughput Old Collector".equals(item.getName())
					|| "Garbage collection optimized for deterministic pausetimes Old Collector"
							.equals(item.getName())) {
				fullGC = item;
			} else if ("ParNew".equals(item.getName()) || "Copy".equals(item.getName())
					|| "PS Scavenge".equals(item.getName()) || "G1 Young Generation".equals(item.getName())
					|| "Garbage collection optimized for short pausetimes Young Collector".equals(item.getName())
					|| "Garbage collection optimized for throughput Young Collector".equals(item.getName())
					|| "Garbage collection optimized for deterministic pausetimes Young Collector"
							.equals(item.getName())) {
				youngGC = item;
			}
		}
	}

	public static GC get() {
		GC gc = new GC();
		try {
			gc.getFull().setCount(fullGC.getCollectionCount());
			gc.getFull().setTime(fullGC.getCollectionTime());
			gc.getYoung().setCount(youngGC.getCollectionCount());
			gc.getYoung().setTime(youngGC.getCollectionTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gc;
	}

	public static void main(String[] args) {
		List<GarbageCollectorMXBean> list = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean item : list) {
			if ("ConcurrentMarkSweep".equals(item.getName()) //
					|| "MarkSweepCompact".equals(item.getName()) //
					|| "PS MarkSweep".equals(item.getName()) //
					|| "G1 Old Generation".equals(item.getName()) //
					|| "Garbage collection optimized for short pausetimes Old Collector".equals(item.getName()) //
					|| "Garbage collection optimized for throughput Old Collector".equals(item.getName()) //
					|| "Garbage collection optimized for deterministic pausetimes Old Collector".equals(item.getName()) //
			) {
				fullGC = item;
			} else if ("ParNew".equals(item.getName()) //
					|| "Copy".equals(item.getName()) //
					|| "PS Scavenge".equals(item.getName()) //
					|| "G1 Young Generation".equals(item.getName()) //
					|| "Garbage collection optimized for short pausetimes Young Collector".equals(item.getName()) //
					|| "Garbage collection optimized for throughput Young Collector".equals(item.getName()) //
					|| "Garbage collection optimized for deterministic pausetimes Young Collector"
							.equals(item.getName()) //
			) {
				youngGC = item;
			}
		}

	}
}
