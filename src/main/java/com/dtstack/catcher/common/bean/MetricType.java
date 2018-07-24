package com.dtstack.catcher.common.bean;

public enum MetricType {
	COUNTER("counter"), GAUGE("gauge"), SUMMARY("summary"), HISTOGRAM("histogram"), UNTYPED("untyped");

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	MetricType(String name) {
		this.name = name;
	}

}
