package com.dtstack.catcher.monitor;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.dtstack.catcher.common.utils.PathUtils;
import com.dtstack.catcher.common.utils.constants.SystemProperty;

public class OsMonitor {

	private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();

    private static final Method getFreePhysicalMemorySize;
    private static final Method getTotalPhysicalMemorySize;
    private static final Method getFreeSwapSpaceSize;
    private static final Method getTotalSwapSpaceSize;
    private static final Method getSystemLoadAverage;
    private static final Method getSystemCpuLoad;

    static {
        getFreePhysicalMemorySize = getMethod("getFreePhysicalMemorySize");
        getTotalPhysicalMemorySize = getMethod("getTotalPhysicalMemorySize");
        getFreeSwapSpaceSize = getMethod("getFreeSwapSpaceSize");
        getTotalSwapSpaceSize = getMethod("getTotalSwapSpaceSize");
        getSystemLoadAverage = getMethod("getSystemLoadAverage");
        getSystemCpuLoad = getMethod("getSystemCpuLoad");
    }
    
    public static long getFreePhysicalMemorySize() {
        if (getFreePhysicalMemorySize == null) {
            return -1;
        }
        try {
            return (long) getFreePhysicalMemorySize.invoke(osMxBean);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public static long getFreeSwapSpaceSize() {
        if (getFreeSwapSpaceSize == null) {
            return -1;
        }
        try {
            return (long) getFreeSwapSpaceSize.invoke(osMxBean);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public static long getTotalSwapSpaceSize() {
        if (getTotalSwapSpaceSize == null) {
            return -1;
        }
        try {
            return (long) getTotalSwapSpaceSize.invoke(osMxBean);
        } catch (Exception e) {
            return -1;
        }
    }
    
    final static double[] getSystemLoadAverage() {
        if (SystemProperty.WINDOWS) {
            return null;
        } else if (SystemProperty.LINUX) {
            try {
                final String procLoadAvg = readProcLoadavg();
                assert procLoadAvg.matches("(\\d+\\.\\d+\\s+){3}\\d+/\\d+\\s+\\d+");
                final String[] fields = procLoadAvg.split("\\s+");
                return new double[]{Double.parseDouble(fields[0]), Double.parseDouble(fields[1]), Double.parseDouble(fields[2])};
            } catch (final IOException e) {
                
                return null;
            }
        } else {
            if (getSystemLoadAverage == null) {
                return null;
            }
            try {
                final double oneMinuteLoadAverage = (double) getSystemLoadAverage.invoke(osMxBean);
                return new double[]{oneMinuteLoadAverage >= 0 ? oneMinuteLoadAverage : -1, -1, -1};
            } catch (IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
    }


    
    public static long getTotalPhysicalMemorySize() {
        if (getTotalPhysicalMemorySize == null) {
            return -1;
        }
        try {
            return (long) getTotalPhysicalMemorySize.invoke(osMxBean);
        } catch (Exception e) {
            return -1;
        }
    }

    
    private static Method getMethod(String methodName) {
        try {
            return Class.forName("com.sun.management.OperatingSystemMXBean").getMethod(methodName);
        } catch (Exception e) {
            // not available
            return null;
        }
    }
    
    public static String readProcLoadavg() throws IOException {
    	
        return readSingleLine(PathUtils.get("/proc/loadavg"));
    }
    
    private static String readSingleLine(final Path path) throws IOException {
        final List<String> lines = Files.readAllLines(path);
        assert lines != null && lines.size() == 1;
        return lines.get(0);
    }
    
    public static short getSystemCpuPercent() {
        return getLoadAndScaleToPercent(getSystemCpuLoad, osMxBean);
    }
    
    private static short getLoadAndScaleToPercent(Method method, OperatingSystemMXBean osMxBean) {
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
    
    public static void main(String[] args) {
		System.out.println(OsMonitor.getFreePhysicalMemorySize());
		System.out.println(OsMonitor.getTotalPhysicalMemorySize());
		System.out.println(OsMonitor.getFreeSwapSpaceSize());
		System.out.println(OsMonitor.getTotalSwapSpaceSize());
		System.out.println(OsMonitor.getSystemCpuPercent());
	}
}
