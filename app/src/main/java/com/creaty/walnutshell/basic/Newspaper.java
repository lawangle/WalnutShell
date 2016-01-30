package com.creaty.walnutshell.basic;

import java.util.ArrayList;
import java.util.Calendar;

public class Newspaper {

	public String name;
	//public int frequence;
	public ArrayList<DayTime> alarmTime;
	public long createdDate;
	
	public Newspaper(){
		alarmTime = new ArrayList<DayTime>();
	}
	public Newspaper(String name, //int frequence,
			ArrayList<DayTime> alarmTime,
			long createdDate) {
		super();
		this.name = name;
		//this.frequence = frequence;
		this.alarmTime = alarmTime;
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "Newspaper [name=" + name //+ ", frequence=" + frequence
				+ ", alarmTime=" + alarmTime + ", createdDate=" + createdDate
				+ "]";
	}

	
}
