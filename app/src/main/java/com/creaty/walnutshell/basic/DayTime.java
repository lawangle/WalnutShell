package com.creaty.walnutshell.basic;

import java.util.Iterator;
import java.util.StringTokenizer;

public class DayTime {
	public int hours;
	public int minutes;
	public DayTime( int hours, int minutes){
		if( hours >= 24 || hours< 0 ){
			this.hours = 0;
		}else{
			this.hours = hours;
		}
		if( minutes >=60 || minutes< 0){
			this.minutes = 0;
		}else{
			this.minutes = minutes;
		}
		String s =new String("");
	}
	public DayTime(String s){
		s = s.replace(";", "");
		String[] tmp = s.split(":");
		hours = Integer.valueOf(tmp[ 0 ]);
		minutes = Integer.valueOf(tmp[ 1 ]);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minutes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DayTime other = (DayTime) obj;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ""+hours + ":" + minutes + ";";
	}
	public static void main(String args[]){
		DayTime d = new DayTime( new DayTime(8,56).toString() );
		System.out.println(d);
	}
}
