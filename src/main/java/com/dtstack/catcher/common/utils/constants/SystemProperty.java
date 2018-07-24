package com.dtstack.catcher.common.utils.constants;

public class SystemProperty {

	public static final String OS_NAME = System.getProperty("os.name");
	public static final boolean LINUX = OS_NAME.startsWith("Linux");
	public static final boolean WINDOWS = OS_NAME.startsWith("Windows");
	public static final boolean SUN_OS = OS_NAME.startsWith("SunOS");
	public static final boolean MAC_OS_X = OS_NAME.startsWith("Mac OS X");
	public static final boolean FREE_BSD = OS_NAME.startsWith("FreeBSD");
}
