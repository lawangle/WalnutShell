package com.creaty.walnutshell.basic;

import java.io.Serializable;
import java.util.ArrayList;

public class Source implements Serializable {
	
	/**
	 * 
	 */
	public Source() {
		// TODO Auto-generated constructor stub
		page_address = "";
		name = "";
		description = "";
		modified_date = -1;
	}
	private static final long serialVersionUID = 1L;
	
	public static int RSS_SOURCE = 0;		//类型
	public static int SELF_SOURCE = 1;
	public static int UL_STRATEGY = 100;
	public static int TABLE_STRATEGY = 101;
	public static int SECTION_STRATEGY = 102;
	public static int DIV_STRATEGY = 103;
	public static int OTHER_STRATEGY = 104;
	public String page_address, name, description;		//源所在网页地址，名字（没有置为""），描述（没有置为""）
	public int sourceSeq;   										//该源在其所在网页所有源中的序列号，从0开始
	public int type;												//源的类型，自筛选源还是RSS源
	public int strategyType;
	public long modified_date;			//最后修改时间，来源：系统当前时间
	public ArrayList<Entry> entries;		//源中条目集合，pair： 名字/链接
	@Override
	public String toString() {
		return "Source [page_address=" + page_address + ", name=" + name
				+ ", description=" + description + ", sourceSeq=" + sourceSeq
				+ ", type=" + type + ", strategyType=" + strategyType
				+ ", modified_date=" + modified_date + ", entries=" + entries
				+ "]";
	}
}
