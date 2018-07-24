package com.dtstack.catcher.monitor.log;

import com.dtstack.catcher.common.utils.TimeUtils;
import com.dtstack.catcher.monitor.metric.CounterMonitor;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogFilter extends Filter<ILoggingEvent> {

	public void increaseLineCount() {
		CounterMonitor.increase("logback", "type", "total_line");
	}

	public void increaseBytesCount(String message) {
		CounterMonitor.increase("logback", "type", "total_bytes", message.getBytes().length);
	}

	public void increaseLevelLineCount(Level level) {
		CounterMonitor.increase("logback_level", "type", level.levelStr);
	}

	public void increaseErrorLineCount(String exceptionClass) {
		CounterMonitor.increase("logback_error", "class", exceptionClass);
	}
	
	public StringBuilder formatLog(ILoggingEvent event) {
		
		StringBuilder logBuilder = new StringBuilder("");
		logBuilder.append(TimeUtils.local(event.getTimeStamp())).append(" ").append(event.getLevel())
				.append(" ").append(event.getFormattedMessage());
		
		if (Level.ERROR.equals(event.getLevel()) && event.getThrowableProxy() != null) {
			
			logBuilder.append(" ").append(event.getThrowableProxy().getClassName()).append(" ")
					.append(event.getThrowableProxy().getMessage());
			
			if (event.getThrowableProxy().getStackTraceElementProxyArray() != null) {
				for (StackTraceElementProxy p : event.getThrowableProxy().getStackTraceElementProxyArray()) {
					logBuilder.append("\n	").append(p.getStackTraceElement().getClassName());
				}
			}

		}
		
		return logBuilder;
	}

	@Override
	public FilterReply decide(ILoggingEvent event) {
		try {
			increaseLineCount();

			increaseLevelLineCount(event.getLevel());

			if (Level.ERROR.equals(event.getLevel())) {
				String className = event.getThrowableProxy() == null ? "null"
						: event.getThrowableProxy().getClassName();

				increaseErrorLineCount(className);
			}
			
			
			StringBuilder logBuilder = formatLog(event);
			
			if(Level.ERROR.equals(event.getLevel())) {
				LogMonitor.pushToErrorLogQueue(logBuilder.toString());
			}

			LogMonitor.pushToRecentLogQueue(logBuilder.toString());
			
			increaseBytesCount(logBuilder.toString());

		} catch (Throwable e) {
			e.printStackTrace();
		}

		return FilterReply.ACCEPT;
	}

}