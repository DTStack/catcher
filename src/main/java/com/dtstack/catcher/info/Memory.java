package com.dtstack.catcher.info;

import lombok.Data;


@Data
public class Memory {
	
	private Heap heap = new Heap();
	private HeapEdenSpace heapEdenSpace = new HeapEdenSpace();
	private HeapOldGen heapOldGen = new HeapOldGen();
	private HeapSurvivorSpace heapSurvivorSpace = new HeapSurvivorSpace(); 
	private Metaspace metaspace = new Metaspace();
	private CodeCache codeCache = new CodeCache();
	private CompressedClassSpace compressedClassSpace = new CompressedClassSpace();
	private Direct direct = new Direct();
	private NonHeap nonHeap = new NonHeap();
	
	@Data
	public static class Heap {
		private long init;
		private long used;
		private long committed;
		private long max;
	}
	
	@Data
	public static class NonHeap {
		private long init;
		private long used;
		private long committed;
		private long max;
	}
	
	@Data
	public static class Metaspace {
		private long init;
		private long used;
		private long committed;
		private long max;
	}
	
	@Data
	public static class HeapOldGen {
		private long init;
		private long used;
		private long committed;
		private long max;	
	}
	
	@Data
	public static class HeapEdenSpace {
		private long init;
		private long used;
		private long committed;
		private long max;	
	}
	
	@Data
	public static class CodeCache {
		private long init;
		private long used;
		private long committed;
		private long max;	
	}
	
	@Data
	public static class CompressedClassSpace {
		private long init;
		private long used;
		private long committed;
		private long max;	
	}
	
	@Data
	public static class HeapSurvivorSpace {
		private long init;
		private long used;
		private long committed;
		private long max;
	}
	
	@Data
	public static class Direct {
		private long used;
		private long max;
	}
	
}
