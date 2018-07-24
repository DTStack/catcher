package com.dtstack.catcher.monitor.service;

import java.util.List;

import com.dtstack.catcher.common.bean.ThreadBean;
import com.dtstack.catcher.monitor.ThreadMonitor;

public class ThreadService {
	
	public List<ThreadBean> collect() {
		return ThreadMonitor.get();
	}
}
