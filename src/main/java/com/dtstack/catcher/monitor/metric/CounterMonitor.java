package com.dtstack.catcher.monitor.metric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.dtstack.catcher.common.bean.MetricFamily;

public class CounterMonitor {
	
	private static Map<String, List<String>> cache = new ConcurrentHashMap<>();
	private static Map<String, Map<String, Map<String, AtomicLong>>> counterPool = new ConcurrentHashMap<>();

	public static List<String> getListFormat(String source) {
		if (source == null) {
			source = "";
		}

		if (!cache.containsKey(source)) {
			List<String> target = Arrays.asList(source.split(","));

			cache.put(source, target);
		}

		return cache.get(source);
	}

	public static List<MetricFamily> getAllMetricFamily() {
		List<MetricFamily> list = new ArrayList<>();
		for (Map.Entry<String, Map<String, Map<String, AtomicLong>>> entry : counterPool.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}

			list.add(getMetricFamily(entry.getKey()));
		}

		return list;
	}

	public static MetricFamily getMetricFamily(String name) {
		MetricFamily counter = new MetricFamily.Counter(name, "");

		Map<String, Map<String, AtomicLong>> values = counterPool.get(name);
		if (values == null) {
			return counter;
		}

		for (Map.Entry<String, Map<String, AtomicLong>> labelNameEntry : values.entrySet()) {
			String labelName = labelNameEntry.getKey();
			if (labelNameEntry.getValue() == null) {
				continue;
			}

			for (Map.Entry<String, AtomicLong> labelValueEntry : labelNameEntry.getValue().entrySet()) {
				String labelValue = labelValueEntry.getKey();

				List<String> labelNameList = getListFormat(labelName);
				List<String> labelValueList = getListFormat(labelValue);

				counter.addMetric(name, labelNameList, labelValueList,
						String.valueOf(labelValueEntry.getValue().get()));
			}
		}
		return counter;
	}

	public static void increase(String name, String labelNames, String labelValues) {
		increase(name, labelNames, labelValues, 1);
	}

	public static void increase(String name) {
		increase(name, "", "");
	}

	public static void increase(String name, long delta) {
		increase(name, "", "", delta);
	}

	public static void increase(String name, String labelNames, String labelValues, long delta) {
		try {
			if (!counterPool.containsKey(name)) {
				synchronized (CounterMonitor.class) {
					if (!counterPool.containsKey(name)) {
						counterPool.put(name, new ConcurrentHashMap<String, Map<String, AtomicLong>>());
					}
				}
			}

			if (!counterPool.get(name).containsKey(labelNames)) {
				synchronized (CounterMonitor.class) {
					if (!counterPool.get(name).containsKey(labelNames)) {
						counterPool.get(name).put(labelNames, new ConcurrentHashMap<String, AtomicLong>());
					}
				}
			}

			if (!counterPool.get(name).get(labelNames).containsKey(labelValues)) {
				synchronized (CounterMonitor.class) {
					if (!counterPool.get(name).get(labelNames).containsKey(labelValues)) {
						counterPool.get(name).get(labelNames).put(labelValues, new AtomicLong(0));
					}
				}
			}

			counterPool.get(name).get(labelNames).get(labelValues).addAndGet(delta);
		} catch (Exception e) {
			System.out.println("input=" + name+labelNames+labelValues+delta + " counterPool=" + counterPool);
			e.printStackTrace();
		}
	}

}
