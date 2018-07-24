package com.dtstack.catcher.network.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import com.dtstack.catcher.common.bean.Metric;
import com.dtstack.catcher.common.bean.MetricFamily;
import com.dtstack.catcher.monitor.service.MertricService;
import com.sun.net.httpserver.HttpExchange;

public class MetricHandler extends AbstractHandler {

	private MertricService mertricService;

	public MetricHandler(MertricService mertricService) {
		this.mertricService = mertricService;
	}

	@Override
	public void handle(HttpExchange t) {
		
		try {
			ByteArrayOutputStream response = this.response.get();
			response.reset();
			OutputStreamWriter osw = new OutputStreamWriter(response);
			write(osw, mertricService.collect());
			osw.flush();
			osw.close();
			response.flush();
			response.close();

			flush(t, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public static void write(Writer writer, List<MetricFamily> mfs) throws IOException {
		
		for (MetricFamily metricFamily : mfs) {

			writer.write("# HELP ");
			writer.write(metricFamily.getName());
			writer.write(' ');
			writeEscapedHelp(writer, metricFamily.getHelp());
			writer.write('\n');

			writer.write("# TYPE ");
			writer.write(metricFamily.getName());
			writer.write(' ');
			writer.write(metricFamily.getType().getName());
			writer.write('\n');

			for (Metric metric : metricFamily.getMetrics()) {
				writer.write(metric.getName());
				if (metric.getLabelNames().size() > 0) {
					writer.write('{');
					for (int i = 0; i < metric.getLabelNames().size(); ++i) {
						writer.write(metric.getLabelNames().get(i));
						writer.write("=\"");
						writeEscapedLabelValue(writer, metric.getLabelValues().get(i));
						writer.write("\",");
					}
					writer.write('}');
				}
				writer.write(' ');
				writer.write(metric.getValue());
				if (metric.getTimestamp() > 0) {
					writer.write(' ');
					writer.write(String.valueOf(metric.getTimestamp()));
				}
				writer.write('\n');
			}
		}
	}

	private static void writeEscapedHelp(Writer writer, String s) throws IOException {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				writer.append("\\\\");
				break;
			case '\n':
				writer.append("\\n");
				break;
			default:
				writer.append(c);
			}
		}
	}

	private static void writeEscapedLabelValue(Writer writer, String s) throws IOException {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				writer.append("\\\\");
				break;
			case '\"':
				writer.append("\\\"");
				break;
			case '\n':
				writer.append("\\n");
				break;
			default:
				writer.append(c);
			}
		}
	}

}
