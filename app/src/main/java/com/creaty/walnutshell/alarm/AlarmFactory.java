package com.creaty.walnutshell.alarm;

import java.util.Calendar;

import com.creaty.walnutshell.UpdateService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmFactory {

	/*
	 * An alarm can invoke a broadcast request
	 * at a specified time.
	 * The name of the broadcast receiver is explicitly
	 * specified in the intent.
	 */
    public static void sendAlarm( Context mContext, Calendar cal, int id )
    {
    	
    	//If you want to point to 11:00 hours today.
    	cal = AlarmUtils.getTimeAfterInSecs(10);
    	//Print to the debug view that we are 
    	//scheduling at a specific time
    	String s = AlarmUtils.getDateTimeString(cal);
    	
    	//Get an intent to invoke
    	//TestReceiver class
    	Intent intent = 
    		new Intent(mContext, UpdateService.class);
    	intent.putExtra("hours", cal.get(Calendar.HOUR_OF_DAY));
    	intent.putExtra("minutes", cal.get(Calendar.MINUTE));
    	intent.putExtra("id", id);
    	PendingIntent pi = 
    		PendingIntent.getService(
    				mContext,
    				id,
    				intent,
    				PendingIntent.FLAG_ONE_SHOT);
    	
    	// Schedule the alarm!
    	AlarmManager am = 
    		(AlarmManager)
    		     mContext.getSystemService(Context.ALARM_SERVICE);
    	
    	am.set(AlarmManager.RTC_WAKEUP, 
    			cal.getTimeInMillis(), 
    			pi);
    }
    
}
