package com.creaty.walnutshell.UI;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

	public static String getDateTimeString(long time)
	{
		Date d = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		df.setLenient(false);
		String s = df.format(d);
		return s;
	}
	public static void main( String[] args[]){
		System.out.println(""+getDateTimeString(System.currentTimeMillis()));
	}
}
