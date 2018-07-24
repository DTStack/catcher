package com.dtstack.catcher.network.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class AbstractHandler implements HttpHandler {
	
	public void flush(HttpExchange t, ByteArrayOutputStream response) throws IOException {
		t.getResponseHeaders().set("Content-Type", "text/plain; version=0.0.4; charset=utf-8");
		t.getResponseHeaders().set("Content-Length", String.valueOf(response.size()));
		if (shouldUseCompression(t)) {
			t.getResponseHeaders().set("Content-Encoding", "gzip");
			t.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			final GZIPOutputStream os = new GZIPOutputStream(t.getResponseBody());
			response.writeTo(os);
			os.finish();
		} else {
			t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.size());
			response.writeTo(t.getResponseBody());
		}
		t.close();
	}
	
	protected final LocalByteArray response = new LocalByteArray();

	protected static boolean shouldUseCompression(HttpExchange exchange) {
		List<String> encodingHeaders = exchange.getRequestHeaders().get("Accept-Encoding");
		if (encodingHeaders == null)
			return false;

		for (String encodingHeader : encodingHeaders) {
			String[] encodings = encodingHeader.split(",");
			for (String encoding : encodings) {
				if (encoding.trim().toLowerCase().equals("gzip")) {
					return true;
				}
			}
		}
		return false;
	}

	public static class LocalByteArray extends ThreadLocal<ByteArrayOutputStream> {
		protected ByteArrayOutputStream initialValue() {
			return new ByteArrayOutputStream(1 << 20);
		}
	}
}
