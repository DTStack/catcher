package com.dtstack.catcher.monitor.metric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.dtstack.catcher.common.bean.MetricFamily;

public class GaugeMonitor {
	
	private static Map<String, List<String>> cache = new ConcurrentHashMap<>();
	private static Map<String, Map<String, Map<String, AtomicLong>>> gaugePool = new ConcurrentHashMap<>();

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
		for (Map.Entry<String, Map<String, Map<String, AtomicLong>>> entry : gaugePool.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}

			list.add(getMetricFamily(entry.getKey()));
		}

		return list;
	}

	public static MetricFamily getMetricFamily(String name) {
		MetricFamily counter = new MetricFamily.Gauge(name, "");

		Map<String, Map<String, AtomicLong>> values = gaugePool.get(name);
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

	public static void set(String name, long value) {
		set(name, "", "", value);
	}

	public static void set(String name, String labelNames, String labelValues, long value) {
		try {
			if (!gaugePool.containsKey(name)) {
				synchronized (GaugeMonitor.class) {
					if (!gaugePool.containsKey(name)) {
						gaugePool.put(name, new ConcurrentHashMap<String, Map<String, AtomicLong>>());
					}
				}
			}

			if (!gaugePool.get(name).containsKey(labelNames)) {
				synchronized (GaugeMonitor.class) {
					if (!gaugePool.get(name).containsKey(labelNames)) {
						gaugePool.get(name).put(labelNames, new ConcurrentHashMap<String, AtomicLong>());
						
					}
				}
			}

			if (!gaugePool.get(name).get(labelNames).containsKey(labelValues)) {
				synchronized (GaugeMonitor.class) {
					if (!gaugePool.get(name).get(labelNames).containsKey(labelValues)) {
						gaugePool.get(name).get(labelNames).put(labelValues, new AtomicLong(0));
					}
					
				}
			}

			gaugePool.get(name).get(labelNames).get(labelValues).set(value);
		} catch (Exception e) {
			System.out.println("input=" + name+labelNames+labelValues+value + " gaugePool="+gaugePool);
			e.printStackTrace();
		}
	}

}
