package com.dtstack.catcher.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

public class ProcessMonitor {

	private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

	private static final Method getMaxFileDescriptorCountField;
	private static final Method getOpenFileDescriptorCountField;
	private static final Method getProcessCpuLoad;
	private static final Method getProcessCpuTime;
	private static final Method getCommittedVirtualMemorySize;

	static {
		getMaxFileDescriptorCountField = getUnixMethod("getMaxFileDescriptorCount");
		getOpenFileDescriptorCountField = getUnixMethod("getOpenFileDescriptorCount");
		getProcessCpuLoad = getMethod("getProcessCpuLoad");
		getProcessCpuTime = getMethod("getProcessCpuTime");
		getCommittedVirtualMemorySize = getMethod("getCommittedVirtualMemorySize");
	}

	private static Method getUnixMethod(String methodName) {
		try {
			return Class.forName("com.sun.management.UnixOperatingSystemMXBean").getMethod(methodName);
		} catch (Exception t) {
			// not available
			return null;
		}
	}

	private static Method getMethod(String methodName) {
		try {
			return Class.forName("com.sun.management.OperatingSystemMXBean").getMethod(methodName);
		} catch (Exception t) {
			// not available
			return null;
		}
	}

	public static long getMaxFileDescriptorCount() {
		if (getMaxFileDescriptorCountField == null) {
			return -1;
		}
		try {
			return (Long) getMaxFileDescriptorCountField.invoke(osMxBean);
		} catch (Exception t) {
			return -1;
		}
	}

	public static long getOpenFileDescriptorCount() {
		if (getOpenFileDescriptorCountField == null) {
			return -1;
		}
		try {
			return (Long) getOpenFileDescriptorCountField.invoke(osMxBean);
		} catch (Exception t) {
			return -1;
		}
	}

	public static long getProcessCpuTotalTime() {
		if (getProcessCpuTime != null) {
			try {
				long time = (long) getProcessCpuTime.invoke(osMxBean);
				if (time >= 0) {
					return (time / 1_000_000L);
				}
			} catch (Exception t) {
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Returns the process CPU usage in percent
	 */
	public static short getProcessCpuPercent() {
		return getLoadAndScaleToPercent(getProcessCpuLoad, osMxBean);
	}

	public static long getTotalVirtualMemorySize() {
		if (getCommittedVirtualMemorySize != null) {
			try {
				long virtual = (long) getCommittedVirtualMemorySize.invoke(osMxBean);
				if (virtual >= 0) {
					return virtual;
				}
			} catch (Exception t) {
				return -1;
			}
		}
		return -1;
	}

	public static short getLoadAndScaleToPercent(Method method, OperatingSystemMXBean osMxBean) {
		if (method != null) {
			try {
				double load = (double) method.invoke(osMxBean);
				if (load >= 0) {
					return (short) (load * 100);
				}
			} catch (Exception e) {
				return -1;
			}
		}
		return -1;
	}

}
