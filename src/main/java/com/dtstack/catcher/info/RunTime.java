package com.dtstack.catcher.info;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class RunTime {
	
	private String name;
	private String bootClassPath;
	private List<String> inputArguments;
	private String libraryPath;
	private Map<String, String> systemProperties;
	
	public static void main(String[] args) {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		System.out.println(runtimeMXBean.getName());
		System.out.println(runtimeMXBean.getBootClassPath());
		System.out.println(runtimeMXBean.getInputArguments());
		System.out.println(runtimeMXBean.getLibraryPath());
		System.out.println(runtimeMXBean.getSystemProperties());
	}

}
