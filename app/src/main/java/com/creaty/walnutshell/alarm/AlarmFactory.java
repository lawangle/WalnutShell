package com.creaty.walnutshell.alarm;

import java.util.Calendar;

import com.creaty.walnutshell.AlarmReceiver;
import com.creaty.walnutshell.UpdateService;
import com.creaty.walnutshell.WelcomeActivity;
import com.creaty.walnutshell.basic.DayTime;
import com.creaty.walnutshell.content_provider.DataBridge;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.AlarmTableMetaData;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class AlarmFactory {

	public static final String ARG_NEWS_ID = "alarmfactory_news_id";
	public static final String ARG_ALARM_TIME = "alarm_time";
	public static void sendMultiAlarm(Context mContext, Calendar[] cal, int[] id) {
		DataBridge db = new DataBridge(mContext);

		int size = id.length;
		for (int i = 0; i < size; i++) {
			// sendAlarm(mContext, cal[i], id[i]);
		}
	}

	// cancel alarm if action, data, type, class, and categories of two intent
	// are the same.
	public static void cancelAlarm(Context mContext, int alarm_id) {
		AlarmManager am = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(mContext.getApplicationContext(),
				UpdateService.class);
		PendingIntent pi = PendingIntent.getService(mContext, alarm_id, intent,
				0);
		am.cancel(pi);
	}

	public static void sendAlarm(Context mContext, int alarm_id, int news_id,
			DayTime dayTime) {
		//Calendar cal = AlarmUtils.getTimeAfterInSecs(10);
		Calendar cal = AlarmUtils.getTodayAt(dayTime.hours, dayTime.minutes);
		if( cal.getTimeInMillis() < System.currentTimeMillis() ){
			cal = AlarmUtils.getTomorrowAt(dayTime.hours, dayTime.minutes );
		}
		// Print to the debug view that we are
		// scheduling at a specific time
		String s = AlarmUtils.getDateTimeString(cal);

		// Get an intent to invoke
		// TestReceiver class
		Intent intent = new Intent(mContext.getApplicationContext(),
				UpdateService.class);
		// intent.putExtra("hours", cal.get(Calendar.HOUR_OF_DAY));
		// intent.putExtra("minutes", cal.get(Calendar.MINUTE));
		intent.putExtra(ARG_NEWS_ID, news_id);
		intent.putExtra(ARG_ALARM_TIME, cal.getTimeInMillis() );
		PendingIntent pi = PendingIntent.getService(mContext, alarm_id, intent,
				0);// PendingIntent.FLAG_ONE_SHOT);

		// Schedule the alarm!
		AlarmManager am = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		Log.d("alarm", "send repeat alarm" );
		am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, pi);
	}

	/** 当设备重启或者打开应用时应该启动所用闹钟，假如之前已经启动，新的闹钟将会取代旧的闹钟 */
	public static void sendAllAlarms(Context mContext) {
		ContentResolver cr = mContext.getContentResolver();
		Cursor c = cr.query(AlarmTableMetaData.CONTENT_URI, null, null, null,
				null);
		//System.out.println("Alarm%%%"+"alarm count"+c.getCount());
		int iid = c.getColumnIndex(AlarmTableMetaData._ID);
		int inews_id = c.getColumnIndex(AlarmTableMetaData.NEWS_ID);
		int iat = c.getColumnIndex(AlarmTableMetaData.ALARM_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//			Log.d("Alarm%%%", ""+ c.getInt(iid) +" "+ c.getInt(inews_id) +" "+ new DayTime(
//					c.getString(iat)).toString());
			sendAlarm(mContext, c.getInt(iid), c.getInt(inews_id), new DayTime(
					c.getString(iat)));
		}
		c.close();
	}

	public static void cancelAllAlarms(Context mContext){
		ContentResolver cr = mContext.getContentResolver();
		Cursor c = cr.query(AlarmTableMetaData.CONTENT_URI, null, null, null,
				null);
		//System.out.println("Alarm%%%"+"alarm count"+c.getCount());
		int iid = c.getColumnIndex(AlarmTableMetaData._ID);
		int inews_id = c.getColumnIndex(AlarmTableMetaData.NEWS_ID);
		int iat = c.getColumnIndex(AlarmTableMetaData.ALARM_TIME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//			Log.d("Alarm%%%", ""+ c.getInt(iid) +" "+ c.getInt(inews_id) +" "+ new DayTime(
//					c.getString(iat)).toString());
			cancelAlarm(mContext, c.getInt(iid));
		}
		c.close();
	}
	public static int getRequestCode(Calendar c, int id) {
		return Integer.parseInt(String.valueOf(c.get(Calendar.HOUR_OF_DAY))
				+ String.valueOf(c.get(Calendar.MINUTE)) + String.valueOf(id));
	}

}
