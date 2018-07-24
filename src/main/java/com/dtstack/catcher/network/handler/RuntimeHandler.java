package com.dtstack.catcher.network.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import com.alibaba.fastjson.JSON;
import com.dtstack.catcher.info.RunTime;
import com.dtstack.catcher.monitor.service.RuntimeService;
import com.sun.net.httpserver.HttpExchange;

public class RuntimeHandler extends AbstractHandler {

	private RuntimeService runtimeService;

	public RuntimeHandler(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {

		ByteArrayOutputStream response = this.response.get();
		response.reset();
		OutputStreamWriter osw = new OutputStreamWriter(response);
		write(osw, runtimeService.collect());
		osw.flush();
		osw.close();
		response.flush();
		response.close();

		flush(t, response);
	}

	public void write(OutputStreamWriter osw, RunTime source) throws IOException {
			osw.write(JSON.toJSONString(source));
	}
	
	


}
