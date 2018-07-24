package com.dtstack.catcher.monitor.service;

import com.dtstack.catcher.info.RunTime;
import com.dtstack.catcher.monitor.RuntimeMonitor;

public class RuntimeService {
	
	public RunTime collect() {
		return RuntimeMonitor.get();
	}
}
