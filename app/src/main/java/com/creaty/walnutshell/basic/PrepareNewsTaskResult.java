package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PrepareNewsTaskResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int newsId;
	public String newsName;
	public ArrayList<Integer> sourceIds;
	public HashMap<Integer, Source> sources;
	
	public PrepareNewsTaskResult(int newsId, String newsName,
			ArrayList<Integer> sourceIds,
			HashMap<Integer, Source> sources) {
		super();
		this.newsId = newsId;
		this.newsName = newsName;
		this.sourceIds = sourceIds;
		this.sources = sources;
	}

	@Override
	public String toString() {
		return "PrepareNewsTaskResult [newsId=" + newsId + ", sourceIds="
				+ sourceIds + ", sources=" + sources + "]";
	}
	
}
