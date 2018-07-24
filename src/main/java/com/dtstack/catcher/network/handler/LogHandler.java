package com.dtstack.catcher.network.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.dtstack.catcher.monitor.service.LogService;
import com.sun.net.httpserver.HttpExchange;

public class LogHandler extends AbstractHandler {
	
	private LogService logService;
	
	public LogHandler(LogService logService) {
		this.logService = logService; 
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		
		ByteArrayOutputStream response = this.response.get();
		response.reset();
		OutputStreamWriter osw = new OutputStreamWriter(response);
		List<String> logs = null;
		if(t.getRequestURI().getPath().equals("/logs/recent")) {
			logs = logService.collectRecentLog();
		} else {
			logs = logService.collectErrorLog();
		}
		write(osw, logs);
		osw.flush();
		osw.close();
		response.flush();
		response.close();
		
		flush(t, response);
	}
	
	public void write(OutputStreamWriter osw, List<String> source) throws IOException {
		if(source == null) {
			return;
		}
		for(String s : source) {
			osw.write(s);
			osw.write('\n');
		}
		
	}
	
	

}
