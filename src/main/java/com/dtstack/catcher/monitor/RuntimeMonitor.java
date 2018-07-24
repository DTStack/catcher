package com.dtstack.catcher.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import com.dtstack.catcher.info.RunTime;

public class RuntimeMonitor {

	private static RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

	public static RunTime get() {
		RunTime rt = new RunTime();

		try {
			rt.setBootClassPath(runtimeMXBean.getBootClassPath());
			rt.setName(runtimeMXBean.getName());
			rt.setInputArguments(runtimeMXBean.getInputArguments());
			rt.setBootClassPath(runtimeMXBean.getLibraryPath());
			rt.setSystemProperties(runtimeMXBean.getSystemProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rt;
	}

	public static void main(String[] args) {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
//		System.out.println(runtimeMXBean.getName());
//		System.out.println(runtimeMXBean.getBootClassPath());
//		System.out.println(runtimeMXBean.getInputArguments());
//		System.out.println(runtimeMXBean.getLibraryPath());
//		System.out.println(runtimeMXBean.getSystemProperties());
		System.out.println(System.getProperty("os.name"));
	}
}
