package com.creaty.walnutshell.basic;

import java.io.Serializable;

public class Entry implements Serializable{

	public long id;
	public String name;		//����
	public String href;		//��������
	public String description;		//������û����Ϊ""
	public Entry( long id, String name, String href, String description) {
		super();
		this.id = id;
		this.name = name;
		this.href = href;
		this.description = description;
	}
	public Entry() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Entry [id=" + id + ", name=" + name + ", href=" + href
				+ ", description=" + description + "]\n";
	}
	
}
