package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;

/**网页页面基本信息和其包含的全部源
 * 
 */
public class PageDetail implements Serializable{
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	public String pageName;
	public String description;
	public long lastModifiedTime;	//最后修改时间，来源：系统当前时间
	public String logoAddress;			//logo图片链接
	
	public ArrayList<Source> ourSource;		//自筛选源
	public ArrayList<Source> rssSource;		//RSS源
	@Override
	public String toString() {
		return "PageDetail [pageName=" + pageName + ", description="
				+ description + ", lastModifiedTime=" + lastModifiedTime + "]";
	}
	
}
