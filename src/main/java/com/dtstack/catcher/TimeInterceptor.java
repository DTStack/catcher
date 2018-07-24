package com.dtstack.catcher;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class TimeInterceptor {
	// @RuntimeType
	// public static Object intercept(@Origin Method method,
	// @SuperCall Callable<?> callable) throws Exception {
	// long start = System.currentTimeMillis();
	// try {
	// // 原有函数执行
	// return callable.call();
	// } finally {
	// System.out.println(method + ": took " + (System.currentTimeMillis() - start)
	// + "ms");
	// }
	// }

	public static String intercept(String name) {
		return "Hello " + name + "!";
	}

	public static String intercept(int i) {
		return Integer.toString(i);
	}

	public static String intercept(Object o) {
		return o.toString();
	}

	public static String intercept() {
		return "here";
	}

}