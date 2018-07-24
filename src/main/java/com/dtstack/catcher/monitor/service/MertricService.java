package com.dtstack.catcher.monitor.service;

import java.util.ArrayList;
import java.util.List;

import com.dtstack.catcher.common.bean.MetricFamily;
import com.dtstack.catcher.info.GC;
import com.dtstack.catcher.info.Memory;
import com.dtstack.catcher.monitor.GCMonitor;
import com.dtstack.catcher.monitor.MemMonitor;
import com.dtstack.catcher.monitor.ProcessMonitor;
import com.dtstack.catcher.monitor.metric.CounterMonitor;
import com.dtstack.catcher.monitor.metric.GaugeMonitor;
import com.google.common.collect.Lists;

public class MertricService {
	
	private List<MetricFamily> metricFamily = new ArrayList<>();
	
	public List<MetricFamily> collect() {
		
		metricFamily.clear();
		
		metricFamily.add(getGCMetricFamily());
		metricFamily.add(getMemoryMetricFamily());
		metricFamily.addAll(getProcessMetricFamily());
		
		List<MetricFamily> gaugeList = getGaugeMetricFamily();
		if(gaugeList.size() > 0) {
			metricFamily.addAll(gaugeList);
		}
		
		List<MetricFamily> counterList = getCounterMetricFamily();
		if(counterList.size() > 0) {
			metricFamily.addAll(counterList);
		}
		
		
		return metricFamily;
	}
	
	public List<MetricFamily> getProcessMetricFamily() {
		List<MetricFamily> list = new ArrayList<>();
		
		MetricFamily fdMetricFamily = new MetricFamily.Gauge("process_fd", "process fd");
		fdMetricFamily.addMetric("process_fd", Lists.newArrayList("stat"), Lists.newArrayList("max"), String.valueOf(ProcessMonitor.getMaxFileDescriptorCount()));
		fdMetricFamily.addMetric("process_fd", Lists.newArrayList("stat"), Lists.newArrayList("used"), String.valueOf(ProcessMonitor.getOpenFileDescriptorCount()));
		
		MetricFamily cpuPercentMetricFamily = new MetricFamily.Gauge("process_cpu_percent", "process cpu percent");
		cpuPercentMetricFamily.addMetric("process_cpu_percent", String.valueOf(ProcessMonitor.getProcessCpuPercent()));
		
		MetricFamily cpuTimeMetricFamily = new MetricFamily.Counter("process_cpu_time", "process cpu time");
		cpuTimeMetricFamily.addMetric("process_cpu_time", String.valueOf(ProcessMonitor.getProcessCpuTotalTime()));
		
		list.add(cpuTimeMetricFamily);
		list.add(cpuPercentMetricFamily);
		list.add(fdMetricFamily);
		
		return list;
	}
	
	public List<MetricFamily> getGaugeMetricFamily() {
		return GaugeMonitor.getAllMetricFamily();
	}
	
	public List<MetricFamily> getCounterMetricFamily() {
		return CounterMonitor.getAllMetricFamily();
	}
	
	public MetricFamily getGCMetricFamily() {
		MetricFamily metricFamily = new MetricFamily.Counter("jvm_gc", "jvm gc");
		GC gc = GCMonitor.get();
		
		metricFamily.addMetric("jvm_gc", Lists.newArrayList("type","stat"), Lists.newArrayList("full","time"), String.valueOf(gc.getFull().getTime()));
		metricFamily.addMetric("jvm_gc", Lists.newArrayList("type","stat"), Lists.newArrayList("full","count"), String.valueOf(gc.getFull().getCount()));
		metricFamily.addMetric("jvm_gc", Lists.newArrayList("type","stat"), Lists.newArrayList("young","time"), String.valueOf(gc.getYoung().getTime()));
		metricFamily.addMetric("jvm_gc", Lists.newArrayList("type","stat"), Lists.newArrayList("young","count"), String.valueOf(gc.getYoung().getCount()));
		
		return metricFamily;
	}
	
	public MetricFamily getMemoryMetricFamily() {
		MetricFamily metricFamily = new MetricFamily.Gauge("jvm_memory", "jvm memory");
		Memory mem = MemMonitor.get();
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heap","init"), String.valueOf(mem.getHeap().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heap","commited"), String.valueOf(mem.getHeap().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heap","max"), String.valueOf(mem.getHeap().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heap","used"), String.valueOf(mem.getHeap().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("nonheap","init"), String.valueOf(mem.getNonHeap().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("nonheap","commited"), String.valueOf(mem.getNonHeap().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("nonheap","max"), String.valueOf(mem.getNonHeap().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("nonheap","used"), String.valueOf(mem.getNonHeap().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("codecache","init"), String.valueOf(mem.getCodeCache().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("codecache","commited"), String.valueOf(mem.getCodeCache().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("codecache","max"), String.valueOf(mem.getCodeCache().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("codecache","used"), String.valueOf(mem.getCodeCache().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("compressedclassspace","init"), String.valueOf(mem.getCompressedClassSpace().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("compressedclassspace","commited"), String.valueOf(mem.getCompressedClassSpace().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("compressedclassspace","max"), String.valueOf(mem.getCompressedClassSpace().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("compressedclassspace","used"), String.valueOf(mem.getCompressedClassSpace().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapedenspace","init"), String.valueOf(mem.getHeapEdenSpace().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapedenspace","commited"), String.valueOf(mem.getHeapEdenSpace().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapedenspace","max"), String.valueOf(mem.getHeapEdenSpace().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapedenspace","used"), String.valueOf(mem.getHeapEdenSpace().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapoldgen","init"), String.valueOf(mem.getHeapOldGen().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapoldgen","commited"), String.valueOf(mem.getHeapOldGen().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapoldgen","max"), String.valueOf(mem.getHeapOldGen().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapoldgen","used"), String.valueOf(mem.getHeapOldGen().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapsurvivorspace","init"), String.valueOf(mem.getHeapSurvivorSpace().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapsurvivorspace","commited"), String.valueOf(mem.getHeapSurvivorSpace().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapsurvivorspace","max"), String.valueOf(mem.getHeapSurvivorSpace().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("heapsurvivorspace","used"), String.valueOf(mem.getHeapSurvivorSpace().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("metaspace","init"), String.valueOf(mem.getMetaspace().getInit()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("metaspace","commited"), String.valueOf(mem.getMetaspace().getCommitted()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("metaspace","max"), String.valueOf(mem.getMetaspace().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("metaspace","used"), String.valueOf(mem.getMetaspace().getUsed()));
		
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("direct","max"), String.valueOf(mem.getDirect().getMax()));
		metricFamily.addMetric("jvm_memory", Lists.newArrayList("type","stat"), Lists.newArrayList("direct","used"), String.valueOf(mem.getDirect().getUsed()));
		
		return metricFamily;
	}
	
	
	
}
