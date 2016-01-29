package com.creaty.walnutshell.basic;

import java.io.Serializable;
/**源中每一个条目的基本信息
 * @deprecated
 * @author OrangeHIX
 * @version 1
 */
public class SourceDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//source details: content_address, name, description, *created date(long), *modified date(long)
	private String content_address, name, description;
	private long created_date, modified_date;			//创建与最后修改时间，来源：系统当前时间
	
	public SourceDetail(String content_address, String name,
			String description, long created_date, long modified_date) {
		super();
		this.content_address = content_address;
		this.name = name;
		this.description = description;
		this.created_date = created_date;
		this.modified_date = modified_date;
	}
	public SourceDetail(){
		super();
		this.content_address = "";
		this.created_date = 0;
		this.name = "";
		this.description = "";
		this.modified_date = 0;
	}
	public void setContent_address(String content_address){
		this.content_address = content_address;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setCreated_date(long created_date){
		this.created_date = created_date;
	}
	public void setModified_data(long modefied_data){
		this.modified_date = modefied_data;
	}
	public String getContent_address() {
		return content_address;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getCreated_date() {
		return created_date;
	}

	public long getModified_date() {
		return modified_date;
	}
}