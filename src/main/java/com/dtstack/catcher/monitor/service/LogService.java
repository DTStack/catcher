package com.dtstack.catcher.monitor.service;

import java.util.List;

import com.dtstack.catcher.monitor.log.LogMonitor;

public class LogService {
	
	public List<String> collectErrorLog() {
		return LogMonitor.errorLog();
	}
	
	public List<String> collectRecentLog() {
		return LogMonitor.recentLog();
	}
}
