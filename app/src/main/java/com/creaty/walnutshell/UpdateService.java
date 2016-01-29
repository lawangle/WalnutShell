package com.creaty.walnutshell;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;


import com.creaty.walnutshell.alarm.AlarmFactory;
import com.creaty.walnutshell.alarm.AlarmUtils;
import com.creaty.walnutshell.basic.Source;
import com.creaty.walnutshell.content_provider.DataProviderMetaData.SourceTableMetaData;
import com.creaty.walnutshell.fang.InterfaceImplement;
import com.creaty.walnutshell.fang.InternetInterface2;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends Service {
	static String tag = "UpdateService";
	int mStartMode; // indicates how to behave if the service is killed

	@Override
	public void onCreate() {
		// The service is being created
		mStartMode = Service.START_NOT_STICKY;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// The service is starting, due to a call to startService()
		Toast.makeText(getApplicationContext(), "Hello Service",
				Toast.LENGTH_SHORT).show();
		// Log.d(tag, "onstartCommand");
		try {
			AlarmFactory.sendAlarm(getApplicationContext(), getNewTime(intent),
					intent.getExtras().getInt("id"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mStartMode;
	}

	private Calendar getNewTime(Intent intent) throws Exception {
		int hours = intent.getIntExtra("hours", -1);
		int minutes = intent.getIntExtra("minutes", -1);
		if (hours < 0 || minutes < 0) {
			throw new Exception("illegal arguments when getLastTime() in "
					+ tag);
		}
		return AlarmUtils.getTomorrowAt(hours, minutes);
	}

	public class UpdateSourceTask extends AsyncTask<Integer, Integer, Integer> {
		public static final int NET_ERROR = -1;
		int netErrorCode;
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO �Զ����ɵķ������
			InternetInterface2 ii = new InterfaceImplement();
			ContentResolver cr = getContentResolver();
			Source s = null;
			
			Cursor c = cr.query(Uri.withAppendedPath(SourceTableMetaData.CONTENT_URI,
					String.valueOf(params[0])), null, null, null, null);
			c.moveToFirst();
			int ipage_address = c.getColumnIndex(SourceTableMetaData.PAGE_ADDRESS);
			int itype = c.getColumnIndex(SourceTableMetaData.SOURCE_TYPE);
			if( c.getInt(itype) == Source.RSS_SOURCE ){
				try {
					s= ii.getRssSource(new URL(c.getString(ipage_address)));
				} catch (IOException e) {
					netErrorCode = NET_ERROR;
					e.printStackTrace();
				}
			}else{
				int iseq =  c.getColumnIndex(SourceTableMetaData.SOURCE_SEQ);
				int istra = c.getColumnIndex(SourceTableMetaData.SOURCE_STRATEGY);
				try {
					s= ii.getSelfSource(new URL(c.getString(ipage_address)),
							c.getInt(iseq), c.getInt(istra));
				} catch (IOException e) {
					netErrorCode = NET_ERROR;
					e.printStackTrace();
				}
			}
			c.close();
			return itype;
		}

	}

	@Override
	public void onDestroy() {
		// The service is no longer used and is being destroyed
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
