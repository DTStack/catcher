package com.dtstack.catcher.common.bean;

import java.util.List;

import lombok.Data;

@Data
public class Metric {
	
	private String name;
	private List<String> labelNames;
	private List<String> labelValues;
	private String value;
	private long timestamp;
	
	public Metric(String name, List<String> labelNames, List<String> labelValues, String value) {
		this.name = name;
		this.labelNames = labelNames;
		this.labelValues = labelValues;
		this.value = value;
		this.timestamp = System.currentTimeMillis();
	}
}
