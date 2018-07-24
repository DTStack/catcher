package com.dtstack.catcher.common.bean;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

import lombok.Data;

@Data
public class ThreadBean {
	private long cpuTime;
	private long blockedTime;
	private long waitedTime;
	private ThreadInfo info;
	
	private boolean hasDelta = false;
	
	public ThreadBean(long cpuTime, ThreadInfo info) {
		this.cpuTime = cpuTime;
		this.blockedTime = info.getBlockedTime();
		this.waitedTime = info.getWaitedTime();
		this.info = info;
	}
	
	public void setDelta(long cpuTime, ThreadInfo info) {
		if(hasDelta == true) {
			return;
		}
		
		this.cpuTime = cpuTime - this.cpuTime;
		this.blockedTime = info.getBlockedTime() - this.blockedTime;
		this.waitedTime = info.getWaitedTime() - this.waitedTime;
		this.info = info;
		this.hasDelta = true;
	}
	
	public String format() {
		return getInfo().getThreadName() + "\n- usage:" + getCpuTime() + "ms blocked:" + getBlockedTime() + "ms waited:"
				+ getWaitedTime() + "ms\n- " + info;
	}
	
	public String string(ThreadInfo info) {
        StringBuilder sb = new StringBuilder("\"" + info.getThreadName() + "\"" +
                                             " Id=" + info.getThreadId() + " " +
                                             info.getThreadState());
        if (info.getLockName() != null) {
            sb.append(" on " + info.getLockName());
        }
        if (info.getLockOwnerName() != null) {
            sb.append(" owned by \"" + info.getLockOwnerName() +
                      "\" Id=" + info.getLockOwnerId());
        }
        if (info.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (info.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;
        
        for (; i < info.getStackTrace().length && i < 8; i++) {
            StackTraceElement ste = info.getStackTrace()[i];
            sb.append("\tat " + ste.toString());
            sb.append('\n');
            if (i == 0 && info.getLockInfo() != null) {
                Thread.State ts = info.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + info.getLockInfo());
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + info.getLockInfo());
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + info.getLockInfo());
                        sb.append('\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : info.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked " + mi);
                    sb.append('\n');
                }
            }
       }
       if (i < info.getStackTrace().length) {
           sb.append("\t...");
           sb.append('\n');
       }

       LockInfo[] locks = info.getLockedSynchronizers();
       if (locks.length > 0) {
           sb.append("\n\tNumber of locked synchronizers = " + locks.length);
           sb.append('\n');
           for (LockInfo li : locks) {
               sb.append("\t- " + li);
               sb.append('\n');
           }
       }
       sb.append('\n');
       return sb.toString();
    }
	
	
}