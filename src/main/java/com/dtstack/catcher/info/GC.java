package com.dtstack.catcher.info;

import lombok.Data;

@Data
public class GC {
	
	private Full full = new Full();
	private Young young = new Young();
	
	@Data
	public static class Full {
		private long count;
		private long time;
	}
	
	@Data
	public static class Young {
		private long count;
		private long time;
	}
	

}
