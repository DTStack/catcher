package com.dtstack.catcher.common.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MetricFamily {

	private String name;
	private String help;
	private MetricType type;
	private List<Metric> metrics = new ArrayList<Metric>();
	
	public MetricFamily(String name, String help) {
		this.name = name;
		this.help = help;
	}
	
	public void addMetric(Metric metric) {
		metrics.add(metric);
	}
	
	public void addMetric(String name, List<String> labelNames, List<String> labelValues, String value) {
		Metric metric = new Metric(name, labelNames, labelValues, value);
		metrics.add(metric);
	}
	
	public void addMetric(String name, String value) {
		Metric metric = new Metric(name, new ArrayList<String>(), new ArrayList<String>(), value);
		metrics.add(metric);
	}
	
	public static class Gauge extends MetricFamily {

		public Gauge(String name, String help) {
			super(name, help);
			setType(MetricType.GAUGE);
		}
		
	}
	
	public static class Counter extends MetricFamily {

		public Counter(String name, String help) {
			super(name, help);
			setType(MetricType.COUNTER);
		}
		
	}

}
