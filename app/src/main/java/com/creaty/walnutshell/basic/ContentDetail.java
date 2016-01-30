package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class ContentDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Entry basicInfor;		//条目基本信息
	public String author;		//作者/来源
	public ArrayList<StringAndImg>	content;			//正文（分段落存取）
	
	//图片位置和图片链接（位置举例：假如段落0与段落1之间有图片，图片位置为1）
	public Map<Integer,String> images;		
	
	//最后修改时间，（创建时间从页面获取，没有为-1）（修改时间为系统当前时间）
	public long modified_date;
	public ContentDetail() {
		// TODO Auto-generated constructor stub
		this.author = "";
		content = new ArrayList<StringAndImg>();
		basicInfor = new Entry();
	}
	@Override
	public String toString() {
		return "ContentDetail [basicInfor=" + basicInfor + ", author=" + author
				+ ", content=" + content + ", images=" + images
				+ ", modified_date=" + modified_date + "]";
	}	
	
	
}
