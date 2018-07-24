package com.dtstack.catcher.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
	
	private static SimpleDateFormat utc;
	private static SimpleDateFormat local;
	
	static {
		utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utc.setTimeZone(TimeZone.getTimeZone("GMT"));
		local = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	public static String utc(Date date) {
		return utc.format(date);
	}
	
	public static String utc(long timestamp) {
		return utc.format(new Timestamp(timestamp));
	}
	
	public static String local(Date date) {
		return local.format(date);
	}
	
	public static String local(long timestamp) {
		return local.format(new Timestamp(timestamp));
	}
	
	public static void main(String[] args) {
		System.out.println(TimeUtils.local(System.currentTimeMillis()));
	}
}
