package com.dtstack.catcher.network.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.dtstack.catcher.common.bean.ThreadBean;
import com.dtstack.catcher.monitor.service.ThreadService;
import com.sun.net.httpserver.HttpExchange;

public class ThreadHandler extends AbstractHandler {

	private ThreadService threadService;

	public ThreadHandler(ThreadService threadService) {
		this.threadService = threadService;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {

		ByteArrayOutputStream response = this.response.get();
		response.reset();
		OutputStreamWriter osw = new OutputStreamWriter(response);
		write(osw, threadService.collect());
		osw.flush();
		osw.close();
		response.flush();
		response.close();

		flush(t, response);
	}

	public void write(OutputStreamWriter osw, List<ThreadBean> source) throws IOException {
		for (ThreadBean s : source) {
			osw.write(s.format());
			osw.write('\n');
		}
	}
	
	

}
