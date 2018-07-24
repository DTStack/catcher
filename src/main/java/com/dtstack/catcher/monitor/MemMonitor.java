package com.dtstack.catcher.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import com.dtstack.catcher.info.Memory;

public class MemMonitor {

	private static MemoryPoolMXBean metaSpaceMxBean = null;
	private static MemoryPoolMXBean oldGenMxBean = null;
	private static MemoryPoolMXBean edenSpaceMxBean = null;
	private static MemoryPoolMXBean pSSurvivorSpaceMxBean = null;
	private static MemoryPoolMXBean codeCache = null;
	private static MemoryPoolMXBean compressedClassSpace = null;

	private static MemoryMXBean memoryMXBean = null;

	static {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() {

		memoryMXBean = ManagementFactory.getMemoryMXBean();

		List<MemoryPoolMXBean> list = ManagementFactory.getMemoryPoolMXBeans();
		for (MemoryPoolMXBean item : list) {
			if ("CMS Perm Gen".equals(item.getName()) || "Perm Gen".equals(item.getName())
					|| "PS Perm Gen".equals(item.getName()) || "G1 Perm Gen".equals(item.getName())
					|| "Metaspace".equals(item.getName())) {
				metaSpaceMxBean = item;
			} else if ("CMS Old Gen".equals(item.getName()) || "Tenured Gen".equals(item.getName())
					|| "PS Old Gen".equals(item.getName()) || "G1 Old Gen".equals(item.getName())) {
				oldGenMxBean = item;
			} else if ("Par Eden Space".equals(item.getName()) || "Eden Space".equals(item.getName())
					|| "PS Eden Space".equals(item.getName()) || "G1 Eden Space".equals(item.getName())) {
				edenSpaceMxBean = item;
			} else if ("Par Survivor Space".equals(item.getName()) || "Survivor Space".equals(item.getName())
					|| "PS Survivor Space".equals(item.getName()) || "G1 Survivor Space".equals(item.getName())) {
				pSSurvivorSpaceMxBean = item;
			} else if ("Code Cache".equals(item.getName())) {
				codeCache = item;
			} else if ("Compressed Class Space".equals(item.getName())) {
				compressedClassSpace = item;
			}
		}
	}

	public static Memory get() {
		Memory memory = new Memory();

		try {

			MemoryUsage heapMemUsage = memoryMXBean.getHeapMemoryUsage();
			memory.getHeap().setCommitted(heapMemUsage.getCommitted());
			memory.getHeap().setUsed(heapMemUsage.getUsed());
			memory.getHeap().setMax(heapMemUsage.getMax());
			memory.getHeap().setInit(heapMemUsage.getInit());

			MemoryUsage nonHeapMemUsage = memoryMXBean.getNonHeapMemoryUsage();
			memory.getNonHeap().setCommitted(nonHeapMemUsage.getCommitted());
			memory.getNonHeap().setInit(nonHeapMemUsage.getInit());
			memory.getNonHeap().setUsed(nonHeapMemUsage.getUsed());
			memory.getNonHeap().setMax(nonHeapMemUsage.getMax());

			try {
				Class<?> vmClass = Class.forName("sun.misc.VM");
				long directMemoryMax = (Long) vmClass.getMethod("maxDirectMemory").invoke(null);
				memory.getDirect().setMax(directMemoryMax);

				// 仅仅统计到ByteBuffer.allocateDirect()方式分配的内存
				long directUsed = sun.misc.SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed();
				memory.getDirect().setUsed(directUsed);
				// Class<?> vmClass = Class.forName("sun.misc.VM");
			} catch (Exception t) {
				t.printStackTrace();
			}

			if(edenSpaceMxBean != null) {
				memory.getHeapEdenSpace().setCommitted(edenSpaceMxBean.getUsage().getCommitted());
				memory.getHeapEdenSpace().setInit(edenSpaceMxBean.getUsage().getInit());
				memory.getHeapEdenSpace().setUsed(edenSpaceMxBean.getUsage().getUsed());
				memory.getHeapEdenSpace().setMax(edenSpaceMxBean.getUsage().getMax());
			}
			
			if(oldGenMxBean != null) {
				memory.getHeapOldGen().setCommitted(oldGenMxBean.getUsage().getCommitted());
				memory.getHeapOldGen().setInit(oldGenMxBean.getUsage().getInit());
				memory.getHeapOldGen().setUsed(oldGenMxBean.getUsage().getUsed());
				memory.getHeapOldGen().setMax(oldGenMxBean.getUsage().getMax());
			}
			
			if(metaSpaceMxBean != null) {
				memory.getMetaspace().setCommitted(metaSpaceMxBean.getUsage().getCommitted());
				memory.getMetaspace().setInit(metaSpaceMxBean.getUsage().getInit());
				memory.getMetaspace().setMax(metaSpaceMxBean.getUsage().getMax());
				memory.getMetaspace().setUsed(metaSpaceMxBean.getUsage().getUsed());
			}
			
			if(pSSurvivorSpaceMxBean != null) {
				memory.getHeapSurvivorSpace().setCommitted(pSSurvivorSpaceMxBean.getUsage().getCommitted());
				memory.getHeapSurvivorSpace().setInit(pSSurvivorSpaceMxBean.getUsage().getInit());
				memory.getHeapSurvivorSpace().setMax(pSSurvivorSpaceMxBean.getUsage().getMax());
				memory.getHeapSurvivorSpace().setUsed(pSSurvivorSpaceMxBean.getUsage().getUsed());
			}
			
			if(codeCache != null) {
				memory.getCodeCache().setCommitted(codeCache.getUsage().getCommitted());
				memory.getCodeCache().setInit(codeCache.getUsage().getInit());
				memory.getCodeCache().setMax(codeCache.getUsage().getMax());
				memory.getCodeCache().setUsed(codeCache.getUsage().getUsed());
			}
			
			if(compressedClassSpace != null) {
				memory.getCompressedClassSpace().setCommitted(compressedClassSpace.getUsage().getCommitted());
				memory.getCompressedClassSpace().setInit(compressedClassSpace.getUsage().getInit());
				memory.getCompressedClassSpace().setMax(compressedClassSpace.getUsage().getMax());
				memory.getCompressedClassSpace().setUsed(compressedClassSpace.getUsage().getUsed());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return memory;
	}

	public static void main(String[] args) {

		Memory fmem = MemMonitor.get();
		ByteBuffer b = ByteBuffer.allocateDirect(1000);
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < 10000; i++) {
			// l.add("333"+ System.currentTimeMillis());
		}
		Memory smem = MemMonitor.get();
		System.out.println(smem.getDirect().getUsed());
		System.out.println(smem.getHeap().getUsed() - fmem.getHeap().getUsed());

	}

}
